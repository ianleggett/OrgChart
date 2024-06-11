package webtool.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter user requests for Offers and trades by src and dest ccy id s etc
 * @author ian
 *
 */
public class WebFilterObject {

	public  boolean buy;
	public  boolean sell;	// can be both true and false
	public  long fromccyid;	
	public  long toccyid;
	public  List<Long> payTypes = new ArrayList<Long>(); //: [ pay ids,.. ], <empty or not present is all>
	public  List<String> status = new ArrayList<String>(); // empty or not present is all
	public  double fromamt; // how much crypto min filter
	public String wildcardId; 
	
	public static WebFilterObject clean(WebFilterObject webFiltIn) {		
		boolean buy = (!webFiltIn.buy && !webFiltIn.sell) ? true : webFiltIn.buy;
		boolean sell = (!webFiltIn.buy && !webFiltIn.sell) ? true : webFiltIn.sell;		
		return new WebFilterObject(buy,sell,webFiltIn.fromccyid,webFiltIn.toccyid,webFiltIn.fromamt,webFiltIn.payTypes,webFiltIn.status,webFiltIn.wildcardId);
	}
	
	public boolean buySellLogic(boolean buy,boolean sell) {
		return (!this.buy && !this.sell) || (this.buy && buy) || (this.sell && sell); 
	}
		
	public WebFilterObject(boolean buy, boolean sell, long fromccyid, long toccyid, double fromamt, List<Long> pTypes,List<String> stats) {
		super();
		this.buy = buy;
		this.sell = sell;
		this.fromccyid = fromccyid;
		this.toccyid = toccyid;
		this.fromamt = fromamt;
		this.payTypes = pTypes!=null ? pTypes : new ArrayList<Long>();
		this.status = stats!=null ? stats : new ArrayList<String>();
	}
	
	public WebFilterObject(boolean buy, boolean sell, long fromccyid, long toccyid, double fromamt, List<Long> pTypes,List<String> stats,String wildcard) {
		this(buy, sell, fromccyid, toccyid, fromamt, pTypes, stats);
		this.wildcardId = (wildcard != null) ? wildcard : "";
	}

	public WebFilterObject() {
		super();
	}

	public boolean isBuy() {
		return buy;
	}

	public boolean isSell() {
		return sell;
	}

	public long getFromccyid() {
		return fromccyid;
	}

	public long getToccyid() {
		return toccyid;
	}

	public List<Long> getPayTypes() {
		return payTypes;
	}

	public List<String> getStatus() {
		return status;
	}

	public double getFromamt() {
		return fromamt;
	}

	public String getWildcardId() {
		return wildcardId;
	}

	public void setWildcardId(String wildcardId) {
		this.wildcardId = wildcardId;
	}

	@Override
	public String toString() {
		return "WebFilterObject [buy=" + buy + ", sell=" + sell + ", fromccyid=" + fromccyid + ", toccyid=" + toccyid
				+ ", payTypes=" + payTypes + ", status=" + status + ", fromamt=" + fromamt + ", wildcardId="
				+ wildcardId + "]";
	}

	
	
}
