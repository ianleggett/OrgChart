package webtool.mail;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplates {

	// use MailDAO.EMAIL_TYPE
	
	private Map<String,String> bodyMap = new HashMap<String,String>();
	
	public EmailTemplates() {
		super();
	}
	
	public void clear() {
		bodyMap.clear();
	}
	
	public void setBody(String name,String body) {
		bodyMap.put(name, body);
	}

	public String getBody(String name) {
		return bodyMap.get(name);
	}
	
}
