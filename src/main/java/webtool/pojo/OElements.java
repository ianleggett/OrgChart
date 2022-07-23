package webtool.pojo;

import java.util.List;

public class OElements {

	List<ONode> nodes;
	List<OEdge> edges;
	
	public OElements() {
		super();
	}
	public OElements(List<ONode> nodes, List<OEdge> edges) {
		super();
		this.nodes = nodes;
		this.edges = edges;
	}
	public List<ONode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ONode> nodes) {
		this.nodes = nodes;
	}
	public List<OEdge> getEdges() {
		return edges;
	}
	public void setEdges(List<OEdge> edges) {
		this.edges = edges;
	}
		
}
