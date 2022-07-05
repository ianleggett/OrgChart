package webtool.pojo;

public class EmailBodyJson {

	String type;
	String body;
		
	public EmailBodyJson() {
		super();
	}

	public EmailBodyJson(String type, String body) {
		super();
		this.type = type;
		this.body = body;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	
}
