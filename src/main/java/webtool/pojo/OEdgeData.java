package webtool.pojo;

public class OEdgeData {

	String  id;
	String source;
	String target;
	
	
	public OEdgeData() {
		super();
	}
	public OEdgeData(String id, String source, String target) {
		super();
		this.id = id;
		this.source = source;
		this.target = target;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

}
