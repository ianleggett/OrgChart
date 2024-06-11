package webtool.pojo;

import java.util.ArrayList;
import java.util.List;

public class GoJSData {

	List<GoJSNodeData> nodedata = new ArrayList<GoJSNodeData>();
	List<GoJSLinkData> linkdata = new ArrayList<GoJSLinkData>();
	
	public List<GoJSNodeData> getNodedata() {
		return nodedata;
	}
	public void setNodedata(List<GoJSNodeData> nodedata) {
		this.nodedata = nodedata;
	}
	public List<GoJSLinkData> getLinkdata() {
		return linkdata;
	}
	public void setLinkdata(List<GoJSLinkData> linkdata) {
		this.linkdata = linkdata;
	}
	
	
	
}
