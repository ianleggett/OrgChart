package webtool.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoJSNodeData {

	//{ key: 1, text: "Alpha", color: "lightblue", isGroup: true , group: 6
	long key;
	String text;
	String title;
	String color;
	String city;
	Boolean isGroup;
	Long group;
	
	public GoJSNodeData() {
		super();
	}
	
	public GoJSNodeData(long key, String text, String title,String city, String color, Boolean isGroup, Long group) {
		super();
		this.key = key;
		this.text = text;
		this.title = title;
		this.color = color;
		this.isGroup = isGroup;
		this.group = group;
		this.city = city;
	}

	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}
	public Long getGroup() {
		return group;
	}
	public void setGroup(Long group) {
		this.group = group;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
}
