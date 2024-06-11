package webtool.pojo;

public class GoJSLinkData {

	 //{ from: 1, to: 2, color: "blue" },
	long from;
	long to;
	String color;
	
	public GoJSLinkData() {
		super();
	}
	public GoJSLinkData(long from, long to, String color) {
		super();
		this.from = from;
		this.to = to;
		this.color = color;
	}
	public long getFrom() {
		return from;
	}
	public void setFrom(long from) {
		this.from = from;
	}
	public long getTo() {
		return to;
	}
	public void setTo(long to) {
		this.to = to;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
