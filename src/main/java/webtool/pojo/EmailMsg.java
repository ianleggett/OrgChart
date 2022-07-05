package webtool.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import webtool.mail.MailStatus;

@Entity
@Table(name = "email")
public class EmailMsg {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    private long id;
	int retry = 0;
	Date issueDt;
	Date lastSent = new Date(0);
	
	@Enumerated(EnumType.ORDINAL)
	MailStatus status;
	
	String toAddr;
	String contentType; 
	String subject;
	@Lob
	@Column
	String msg;
	
	public EmailMsg() {
		super();
	}

	public EmailMsg(Date issueDt, MailStatus status, String toAddr, String subject) {
		super();
		this.subject = subject;
		this.issueDt = issueDt;
		this.status = status;
		this.toAddr = toAddr;		
	}
	
	public void shallowCopy(EmailMsg ref) {
		ref.id = this.id;
		ref.issueDt = this.issueDt;
		ref.lastSent = this.lastSent;					
		ref.subject = this.subject;
		ref.retry = this.retry;
		ref.status = this.status;
		ref.toAddr = this.toAddr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public Date getIssueDt() {
		return issueDt;
	}

	public void setIssueDt(Date issueDt) {
		this.issueDt = issueDt;
	}

	public Date getLastSent() {
		return lastSent;
	}

	public void setLastSent(Date lastSent) {
		this.lastSent = lastSent;
	}

	public MailStatus getStatus() {
		return status;
	}

	public void setStatus(MailStatus status) {
		this.status = status;
	}

	public String getToAddr() {
		return toAddr;
	}

	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	
	
}
