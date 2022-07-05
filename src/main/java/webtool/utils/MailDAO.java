package webtool.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import webtool.mail.EmailSettings;
import webtool.mail.MailStats;
import webtool.mail.MailStatus;
import webtool.pojo.EmailMsg;
import webtool.pojo.RespStatus;
import webtool.pojo.SystemConfig;
import webtool.pojo.UserAndRole;
import webtool.repository.EmailRepository;

@Component
@Scope("singleton")
public class MailDAO {

	@Autowired
	SystemConfig sysConfig;

	public enum EMAIL_TYPE {
		EMAIL_NEWUSER("template_newuser", "New User Signup"), // welcome note after validation
		EMAIL_FORGOT_PWD("template_forgotpwd", "Forgot password"), // Your password was reset
		EMAIL_PWD_CHANGED("template_pwdchanged", "Password changed"), // Your password changed
		EMAIL_VALIDATE_EMAIL("template_validate", "Validate Email"), // Your password was reset
		EMAIL_NEWOFFER("template_newoffer", "New Offer"), 
		EMAIL_OFFER_EXPIRED("template_expiredoffer", "Offer Expired"),
		EMAIL_UPDATED_ORDER("template_updated_order", "Offer updated"),
		EMAIL_OFFERACPT("template_offeraccepted", "Offer Accepted"),
		EMAIL_TRADEREFUND("template_traderefund", "Trade Refunding"), // buyer did not send funds and did not cancel
		EMAIL_OFFERCANCEL("template_offercancel", "Offer Cancel"), 
		EMAIL_DEPOSIT("template_deposit", "Deposit Made"),
		EMAIL_ERROR_DEPOSIT("template_errordeposit", "Deposit Error"),
		EMAIL_BANKSENT("template_banksent", "Bank funds sent"),
		EMAIL_BANKREC("template_bankrec", "Bank funds received"),
		EMAIL_TFRCOMPLETE("template_tfrcomplete", "Trade complete"),
		EMAIL_HEADER("template_header", "-template-header-"), 
		EMAIL_FOOTER("template_footer", "-template-footer-"),
		EMAIL_ARBITRATE("template_arbitrate", "Trade Arbitration needed"),
		EMAIL_ARBITRATE_COMPLETE("template_arb_complete", "Arbitration Complete"),
		EMAIL_WARN_ACC_CANCEL("template_warn_acc_cancel", "Notice, Please send funds or Accept cancel request"),
		EMAIL_WARN_BANKSEND("template_warn_banksend", "Notice, Please send funds"),
		EMAIL_WARN_BANKREC("template_warn_bankrec", "Notice Please check your account"),
		EMAIL_INVITE_USER("template_invite_user", "Invitation to trade"),
		/****  STAFF EMAILS *****/
		EMAIL_STAFF_NEWUSER("template_staff_user", "Notice New User signup"),
		EMAIL_STAFF_ARB("template_staff_arb", "Notice New Arbitration Case");

		public String fileName;
		public String subject;

		EMAIL_TYPE(String fname, String subj) {
			fileName = fname;
			subject = subj;
		}
	}

	public static final String TEMPLATE_EXT = ".html";
	public static final String TEMP_FOLDER = "temp";
	public static final String TEMPLATE_FOLDER = "emailTemplates";

	public static final String WILDCARD = "%";
	static Logger log = Logger.getLogger(MailDAO.class);

	/**
	 * USING MONGO for quick start
	 */

	final SimpleDateFormat ordIdFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	final static List<Integer> PENDING = Arrays.asList(MailStatus.READY_TO_SEND.ordinal(), MailStatus.RETRY.ordinal());
	final static List<Integer> HELD = Arrays.asList(MailStatus.HOLD.ordinal());
	final static List<Integer> ERROR = Arrays.asList(MailStatus.ERROR.ordinal());
	final static List<Integer> READY = Arrays.asList(MailStatus.READY_TO_SEND.ordinal());
	final static List<Integer> SENT = Arrays.asList(MailStatus.SENT.ordinal());

	TimerTask timerTask = null;
	Timer timer;
	Session session;

	boolean shutdown = false;

	MailStats mailStats = new MailStats();

	private ThreadedQueueProcessor<Boolean> qProc;
	EmailSettings emailSettings;
	int mins;
	boolean fakeSend;
	
	
	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	ApplicationContext appContext;

	@Autowired
	SettingsDAO settingsDAO;

	@PostConstruct
	public void init() {		
		initProc();
		reloadSettings();
	}

	@PreDestroy
	public void destroy() {
		log.info("Mail shutting down");
		qProc.stop();
		if (timerTask != null) {
			timerTask.cancel();
			if (timer != null)
				timer.cancel();
		}
		shutdown = true;
	}
	
	public void reloadSettings() {		
		
		if ((session == null) && (!settingsDAO.getSettings().getEmailSettings().isEmpty())) {
			try {
				if (session != null)
					session.getTransport().close();
			} catch (MessagingException e) {
				log.error(e);
			}
			session = settingsDAO.createMailSession();
		}

		emailSettings = settingsDAO.getSettings().getEmailSettings();
		mins = emailSettings.getEmailCheckMins();
		fakeSend = emailSettings.isSimulateSending();
		
		scheduleCheck();
	}

	private void initProc() {
				
		qProc = new ThreadedQueueProcessor<Boolean>(1,"emailSender") {

			@Override
			public boolean processMessage(Boolean trigger) {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.error(e);
				} // wait 1 sec
				
				if (shutdown)
					return false;
//				log.info("Checking Emails to send");
				
				mailStats.setLastRun(new Date());
				// only send a max of 12 per min
				int sent = 0;
				EmailMsg toSend = emailRepository.getOneReadyToSend();
				while ((toSend != null) && (sent < (12 * mins))) {
					try {
						if ((toSend.getToAddr() != null) && (!toSend.getToAddr().isBlank())) {
							log.info("Sending Message " + toSend.getSubject() + " to [" + toSend.getToAddr() + "]");

							if (!fakeSend) {
								doSend(toSend);
							} else {
								// simulate send
								toSend.setStatus(MailStatus.SENT);
							}
							Thread.sleep(500);
						} else {
							// error email blank or null to address
							log.error("Trying to send email with no customer Address :" + toSend.getSubject());
							toSend.setStatus(MailStatus.ERROR);
						}

					} catch (InterruptedException e1) {
						log.error(e1);
						toSend.setStatus(MailStatus.ERROR);
					}
					toSend.setLastSent(new Date());
					emailRepository.save(toSend);

					toSend = emailRepository.getOneReadyToSend();
					sent++;
				}
				mailStats.setLastCount(sent);
				mailStats.setPending(emailRepository.getMailQueueCount(PENDING));

				return true;
			}

			@Override
			public void shutdown() {
				log.info("emailSender stopping ");
			}
		};
	}

	public boolean validEmail(String email) {
		String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public RespStatus setEmailState(long id, String nextState) {

		EmailMsg msg = emailRepository.findById(id);
		if (msg != null) {
			msg.setStatus(MailStatus.valueOf(nextState));
			emailRepository.save(msg);
			return RespStatus.OK;
		}
		return new RespStatus(99, "Could not find email id=" + id);
	}

	public void scheduleCheck() {

		if (this.shutdown) {
			return;
		}

		if (timer != null) {
			timer.cancel();
		}

		if (((session != null) || (fakeSend)) && (mins > 0)) {

			timerTask = new TimerTask() {
				@Override
				public void run() {
					qProc.addCmd(Boolean.TRUE);
				}
			};
			// running timer task as daemon thread
			timer = new Timer(true);
			timer.scheduleAtFixedRate(timerTask, 60 * 1000, mins * 60 * 1000);
		}

	}

	/**
	 * Get template from file directory
	 * 
	 * @param eType
	 * @return
	 */
	private File getTemplate(EMAIL_TYPE eType) {

		String rootPath = System.getProperty("user.dir");
		File serverFile = new File(
				rootPath + File.separator + TEMPLATE_FOLDER + File.separator + eType.fileName + TEMPLATE_EXT);
		if (!serverFile.exists()) {
			log.error(
					String.format("Error getting email template %s at folder %s", eType, serverFile.getAbsoluteFile()));
		}
		return serverFile;
	}

	public String getEmailBody(EMAIL_TYPE eType) {
		// Resource resource = new ClassPathResource("template.html");
		// File file = resource.getFile();
		File file = getTemplate(eType);
		BufferedReader b;
		try {
			if (file != null) {
				return FileUtils.readFileToString(file, "UTF-8");
			} else {
				log.error("Error Getting :" + eType + " email header template");
			}
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}

	public String createHTML(Properties extraParams, String body) {

		StringBuffer sb = new StringBuffer();
		sb.append(getEmailBody(EMAIL_TYPE.EMAIL_HEADER));
		sb.append(body);
		sb.append(getEmailBody(EMAIL_TYPE.EMAIL_FOOTER));
		sb.append("</body></html>");

		VelocityContext context = new VelocityContext();

		context.put("numberTool", new NumberTool());
		context.put("dateTool", new DateTool());
		context.put("logo", "logo");
		context.put("datenow", UtilsWeb.humanDateFormat.format(new Date()));

		// write extra props if any
		if (extraParams != null) {
			for (Entry<Object, Object> item : extraParams.entrySet()) {
				context.put(item.getKey().toString(), item.getValue());
			}
		}

		// String template = "Hello $name. Please find attached invoice" + "
		// $invoiceNumber which is due on $dueDate.";
		StringWriter writer = new StringWriter();
		try {
			Velocity.evaluate(context, writer, "TemplateName", sb.toString());
		} catch (Exception e) {
			log.error("**** Velocity error! - email not sent ");
			log.info(sb.toString());
		}
		return writer.toString();
	}

//	public void createEmail(Properties xtraParams, RetailSale order, EMAIL_TYPE eType, String targetEmail) {
//
//		if ((targetEmail == null) || targetEmail.isEmpty() || (!targetEmail.contains("@"))) {
//			// email is invalid
//			log.error("Factory email is invalid ");
//		} else {
//			MessageHeader mgsHeader = new MessageHeader(new Date(), MailStatus.READY_TO_SEND, targetEmail,
//					order.getOrderId());
//			//Properties xtraParams = new Properties();
//			//xtraParams.setProperty("serverip", ipaddr);
//			mgsHeader.setMsg(createHTML(xtraParams, order, getEmailBody(eType), "fact_ordertable"));
//			// create message body
//
//			emailRepository.save(mgsHeader);
//		}
//	}

	public List<String> getStaffEmailAddresses() {
		List<String> emails = new ArrayList<String>();
		Settings settings = settingsDAO.getSettings();
		EmailSettings emailSettings = settings.getEmailSettings();
		if (emailSettings.isNotifyStaff()) {
			String staffList = emailSettings.getStaffList();
			if (staffList.contains("@")) {
				String[] tok = staffList.split(",");
				for (String item : tok) {
					if (validEmail(item)) {
						emails.add(item);
					}
				}
			}
		}
		return emails;
	}

	public EmailMsg createSingleAdminEmail(String targetEmail, EMAIL_TYPE eType, Properties extraParams) {
		if ((targetEmail == null) || targetEmail.isEmpty() || (!targetEmail.contains("@"))) {
			// email is invalid
			log.error("Factory email [" + targetEmail + "] is invalid ");
		} else {
			EmailMsg emailMsg = new EmailMsg(new Date(), MailStatus.READY_TO_SEND, targetEmail, eType.subject);
			// prepare user object for email details
			extraParams.put("servername", sysConfig.getMachineurl());
			emailMsg.setMsg(createHTML(extraParams, getEmailBody(eType)));
			// create message body
			emailMsg = emailRepository.save(emailMsg);
			return emailMsg;
		}
		return null;
	}

	public void createAdminEmail(EMAIL_TYPE eType, Properties extraParams) {
		final Properties eParams = (extraParams != null) ? extraParams : new Properties();
		// send email to multiple staff users
		final List<String> targetEmailList = getStaffEmailAddresses();
		List<EmailMsg> msgs = new ArrayList<EmailMsg>();
		targetEmailList.forEach((targetEmail) -> {
			createSingleAdminEmail(targetEmail, eType, eParams);
		});		
	}


	/**
	 * NEW USER welcome or password reset / validation
	 * 
	 * @param uar
	 * @param eType
	 */
	private EmailMsg createCustomerEmail(UserAndRole uar, EMAIL_TYPE eType, Properties extraParams) {
		if (extraParams == null)
			extraParams = new Properties();
		EmailMsg mgsHeader = new EmailMsg(new Date(), MailStatus.READY_TO_SEND, uar.getEmail(), eType.subject);
		// prepare user object for email details
		extraParams.put("username", uar.getUsername());
		// need validation code, user name
		extraParams.put("vcode", (uar.getValidationCode() != null) ? uar.getValidationCode() : "no validation code");
		extraParams.put("servername", sysConfig.getMachineurl());
		mgsHeader.setMsg(createHTML(extraParams, getEmailBody(eType)));
		return mgsHeader;
	}
	/**
	 * New user invite only
	 * @param newUser
	 * @param eType
	 * @param extraParams
	 * @return
	 */
	private EmailMsg createCustomerEmail(String newUser, EMAIL_TYPE eType, Properties extraParams) {
		if (extraParams == null)
			extraParams = new Properties();
		EmailMsg mgsHeader = new EmailMsg(new Date(), MailStatus.READY_TO_SEND, newUser, eType.subject);
		// prepare user object for email details		
		extraParams.put("servername", sysConfig.getMachineurl());
		mgsHeader.setMsg(createHTML(extraParams, getEmailBody(eType)));
		return mgsHeader;
	}
	
	public EmailMsg createCustomerEmail(UserAndRole uar, EMAIL_TYPE eType) {
		Properties extraParams = new Properties();
		extraParams.put("mydetails", uar.getUserDetails());
		EmailMsg msg = createCustomerEmail(uar, eType, extraParams);
		msg = emailRepository.save(msg);
		qProc.addCmd(Boolean.TRUE);
		return msg;
	}

	


	public EmailMsg getEmail(long id) {
		EmailMsg emaildata = emailRepository.findById(id);
		if (emaildata != null) {
			return emaildata;
		}
		emaildata = new EmailMsg();
		emaildata.setMsg("email not found !!");
		return emaildata;
	}

	public List<EmailMsg> getReadyToSend() {
		return emailRepository.getMailQueue(READY);
	}

	public List<EmailMsg> getPending() {
		return emailRepository.getMailQueue(PENDING);
	}

	public List<EmailMsg> getSent() {
		return emailRepository.getMailQueue(SENT);
	}

	public List<EmailMsg> getHeld() {
		return emailRepository.getMailQueue(HELD);
	}

	public List<EmailMsg> getError() {
		return emailRepository.getMailQueue(ERROR);
	}

	public boolean doSend(EmailMsg msgH) {

		try {
			MimeMessage email = new MimeMessage(session);
			email.addRecipient(RecipientType.TO, new InternetAddress(msgH.getToAddr()));
			email.setSubject(msgH.getSubject());

			MimeMultipart multipart = new MimeMultipart("related");
			BodyPart text = new MimeBodyPart();
			text.setContent(msgH.getMsg(), "text/html; charset=utf-8");
			multipart.addBodyPart(text);

			BodyPart imagelogo = new MimeBodyPart();
			// javax.activation.DataSource fds = new FileDataSource("http://www/image.jpg");
			Resource resource = new ClassPathResource("logo.png");
			FileDataSource fds = new FileDataSource(resource.getFile());
			imagelogo.setDataHandler(new DataHandler(fds));
			imagelogo.addHeader("Content-ID", "<logo>");
			multipart.addBodyPart(imagelogo);
			email.setContent(multipart, "text/html; charset=utf-8");
			email.saveChanges();
			EmailSettings eSet = settingsDAO.getSettings().getEmailSettings();
			if (!settingsDAO.getSettings().getEmailSettings().getFromEmail().isEmpty()) {
				email.setFrom(new InternetAddress(eSet.getFromEmail()));
			}
			// sends the e-mail
			// mailSender.send(email);
			Transport.send(email);
			msgH.setLastSent(new Date());
			msgH.setStatus(MailStatus.SENT);
			emailRepository.save(msgH);
			return true;
		} catch (Exception e) {
			log.error(e);
			msgH.setLastSent(new Date());
			msgH.setStatus(MailStatus.ERROR);
			emailRepository.save(msgH);
		}
		return false;
	}

	public MailStats getMailStats() {
		return mailStats;
	}

	public void setMailStats(MailStats mailStats) {
		this.mailStats = mailStats;
	}

}
