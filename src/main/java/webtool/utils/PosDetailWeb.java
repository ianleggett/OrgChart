package webtool.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PosDetailWeb {

	String inst;
	String exch;
	String ordType; //LONG SHORT
	double qty;
	double position;
			
	public PosDetailWeb(@JsonProperty("inst") String inst,@JsonProperty("exch") String exch, @JsonProperty("ordType") String ordType,@JsonProperty("qty") double qty,@JsonProperty("position") double position) {
		super();
		this.inst = inst;
		this.exch = exch;
		this.ordType = ordType;
		this.qty = qty;
		this.position = position;
	}
	public String getExch() {
		return exch;
	}
	public void setExch(String exch) {
		this.exch = exch;
	}
	public double getQty() {
		return qty;
	}
	public void setQty(double qty) {
		this.qty = qty;
	}
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public String getInst() {
		return inst;
	}
	public void setInst(String inst) {
		this.inst = inst;
	}
	public String getOrdType() {
		return ordType;
	}
	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}
	@Override
	public String toString() {
		return "PosDetailWeb [inst=" + inst + ", exch=" + exch + ", ordType=" + ordType + ", qty=" + qty + ", position="
				+ position + "]";
	}	
	
}
