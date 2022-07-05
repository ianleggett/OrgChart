package test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import webtool.pojo.UserInfo;
import webtool.utils.ThreadedQueueProcessor;
import webtool.utils.UtilsWeb;

class TestStrPair {

	Logger log = Logger.getLogger(TestStrPair.class);
	
	private ThreadedQueueProcessor<String> qProc;		
		
//	Comparator<ContractMatch> byName = new Comparator<ContractMatch>() {
//		@Override
//		public int compare(ContractMatch o1, ContractMatch o2) {			
//			return o1.getStatus().displayOrder.compareTo(o2.getStatus().displayOrder);
//		}
//	};
//	
//	ContractMatch createCtr(ContractStatus stat) {		
//		ContractMatch ctrm = new ContractMatch();
//		ctrm.setStatus( stat );
//		return ctrm;
//	}
//	
//	@Test
//	void testBySort() {
//		
//		List<ContractMatch> str1 = List.of( createCtr(ContractStatus.CREATED), createCtr(ContractStatus.DEPOSIT) , createCtr(ContractStatus.DEPOSITING));
//							
//		List<ContractMatch> str2 = str1.stream().sorted(byName).collect(Collectors.toList());
//		str2.forEach( (l)->{
//			log.info(l.getStatus());
//		});
//		
//	}
//	
	
	
//	@Test
//	void testQueue() throws InterruptedException {
//		qProc = new ThreadedQueueProcessor<String>(10,"testtradeFlow") {
//			@Override
//			public boolean processMessage(String str) {
//				try {					
//					log.info("Processing " + str);
//					LocalDateTime now = LocalDateTime.now();
//					Thread.sleep((long)(Math.random() *10000));
//					log.info("cmd " + str);
//					return true;
//				} catch (Exception e) {
//					log.error(String.format("Error exception %s", e));
//				}
//				return false;
//			}
//			@Override
//			public void shutdown() {
//			}
//		};
//	
//		for(int x=0;x<500;x++) {
//			qProc.addCmd("Msg_"+x);
//		}		
//		
//		Thread.sleep(60000);
//		
//	}
	
//	Single<String> testObserver(){
//		return Single.create(emitter -> {
//			Thread.sleep(4000);
//		    emitter.onSuccess("Success !!");
//		    Thread.sleep(4000);
//		    log.info("emitter disposed:" + emitter.isDisposed());
//		   // emitter.onError(new IllegalAccessError("Whoops"));
//		             //emitter.onError(e);
//		 });
//		
//	}
//	
//	
//	@Test
//	void TestRxJava() throws InterruptedException {
//		
//		testObserver().subscribeWith( new DisposableSingleObserver<String>() {
//			@Override
//			public void onSuccess(String t) {
//				log.info("Succ: "+t);
//				
//			}
//
//			@Override
//			public void onError(Throwable e) {
//				log.error(e);				
//			}
//				
//			});
//					
//					
//		Thread.sleep(15000);
//							
//	}
	
	
	@Test
	void TestUserInfo() {
		UserInfo u1 = new UserInfo();
		u1.setCid(1234);
		u1.setUsername("user1234");
		
		UserInfo u2 = new UserInfo();
		u2.setCid(1235);
		u2.setUsername("user1234");
		assertThat(!u1.equals(u2));
		assertThat(u1 != u2);
		
		UserInfo u3 = new UserInfo();
		u3.setCid(1234);
		u3.setUsername("user9999");
		
		assertThat(u1.equals(u3));
		
		if (u1 == u3) {
			System.out.println("Equal");
		}
		
		assertThat(u1 == u3);
	}
	
	@Test
	void testUnits() {
		assertThat(UtilsWeb.getExpiryMins("1", "minute")).isEqualTo(1);
		assertThat(UtilsWeb.getExpiryMins("10", "minute")).isEqualTo(10);
		assertThat(UtilsWeb.getExpiryMins("100", "minute")).isEqualTo(100);
		assertThat(UtilsWeb.getExpiryMins("1", "hour")).isEqualTo(60);
		assertThat(UtilsWeb.getExpiryMins("10", "hour")).isEqualTo(600);
		assertThat(UtilsWeb.getExpiryMins("1", "day")).isEqualTo(1440);
	}	

	@Test
	void testHideKey() {
		String str = UtilsWeb.hideKey("1234567890",3);
		assertThat(str).isEqualTo("*******890");
		
		str = UtilsWeb.hideKey("1234567890",20);
		assertThat(str).isEqualTo("1234567890");
		
		str = UtilsWeb.hideKey("",3);
		assertThat(str).isEqualTo("");		
	}
	
	
	@Test
	void testStringDate() {
		LocalDateTime tm = LocalDateTime.parse("2021-02-01T12:30",DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));			
	}
	
	@Test
	void numberCheck() {		
		log.info( BigInteger.valueOf( Long.parseLong( "400e17a35f04081" , 16) ));
	}
	
	@Test
	void uuidCheck() throws UnsupportedEncodingException {
		String str = UUID.randomUUID().toString();
		str = str.replaceAll("-", "");
		log.info( str );		
		BigInteger bint = new BigInteger(str, 16);
		log.info(bint);
	}
	
	@Test
	void phoneRegEx() {
		final String reg = "\\+{0,1}[0-9]{2,4}\\s{0,1}[0-9]{10,20}";		
		assertThat("+441234567890".matches(reg)).isTrue();
		assertThat("+44 1234567890".matches(reg)).isTrue();
		assertThat("+44  1234567890".matches(reg)).isFalse();
		assertThat("+442 1234567890".matches(reg)).isTrue();
		assertThat("+4424 1234567890".matches(reg)).isTrue();
		assertThat("+44244 1234567890".matches(reg)).isFalse();
		assertThat("+44 123456789".matches(reg)).isFalse();
		
	}
	
}
