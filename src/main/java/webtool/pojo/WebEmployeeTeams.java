package webtool.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import webtool.utils.CoreDAO;

/**
 * Represents the table data for an employye / view
 * @author ian
 *
 */
public class WebEmployeeTeams {
	
	String view;
	String inum;// unique index
	List<Long> cids = new ArrayList<Long>();
	
	
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getInum() {
		return inum;
	}
	public void setInum(String inum) {
		this.inum = inum;
	}
	public List<Long> getCids() {
		return cids;
	}
	public void setCids(List<Long> cids) {
		this.cids = cids;
	}
	

	
}
