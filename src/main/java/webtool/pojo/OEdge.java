package webtool.pojo;

public class OEdge {
	
    //{ data: { id: 'ad', source: 'a', target: 'd' } },
	OEdgeData data;

	public OEdge() {
		super();
	}
	public OEdge(String id, String source, String target) {
		super();
		this.data = new OEdgeData(id,source,target);
	}
	public OEdge(OEdgeData data) {
		super();
		this.data = data;
	}

	public OEdgeData getData() {
		return data;
	}

	public void setData(OEdgeData data) {
		this.data = data;
	}

	
}
