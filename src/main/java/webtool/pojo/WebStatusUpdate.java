package webtool.pojo;

public class WebStatusUpdate {

	String webtext = "not set";
	boolean complete;
	
	public WebStatusUpdate() {
		super();
	}
	public WebStatusUpdate(String webtext, boolean complete) {
		super();
		this.webtext = webtext;
		this.complete = complete;
	}
	public String getWebtext() {
		return webtext;
	}
	public void setWebtext(String webtext) {
		this.webtext = webtext;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
}
