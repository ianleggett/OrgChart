package webtool.pojo;

public class ONodeData {

	 String id;
	 String label;
	 String parent;
	 
	public ONodeData() {
		super();
	}
	public ONodeData(String id, String label) {
		super();
		this.id = id;
		this.label = label;
		this.parent = null;
	}
	public ONodeData(String id, String label, String parent) {
		super();
		this.id = id;
		this.label = label;
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	 
}
