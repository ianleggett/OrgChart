package webtool.pojo;

public class ONode {

	// { data: { id: 'a', label:'billy',parent: 'b' }},
	ONodeData data;
              
    public ONode() {
		super();
	}
    public ONode( String id,String label,String parent) {
    	data = new ONodeData(id,label,parent);
    }
    public ONode( String id,String label) {
    	data = new ONodeData(id,label);
    }
    
	public ONode(ONodeData data) {
		super();
		this.data = data;
	}
	public ONodeData getData() {
		return data;
	}
	public void setData(ONodeData data) {
		this.data = data;
	}
	
}
