//package test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.io.IOException;
//
//import javax.ws.rs.BeanParam;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.apache.log4j.Logger;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import si.mazi.rescu.RestProxyFactory;
//import test.dto.GetterObject;
//import test.dto.TodoTest;
//
//class TestAPICall {
//	
//	Logger log = Logger.getLogger(TestAPICall.class);
//	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
//	@BeforeEach
//	void setUp() throws Exception {
//	}
//
//	@AfterEach
//	void tearDown() throws Exception {
//	}	 
//	
//	@Test
//	void test() throws IOException {
//		GetterObject service = RestProxyFactory.createProxy(GetterObject.class, "https://jsonplaceholder.typicode.com");
//		TodoTest tdt = service.getTodo();
//		log.info(tdt);
//		assertEquals(1,tdt.getId());
//	}
//
//}
