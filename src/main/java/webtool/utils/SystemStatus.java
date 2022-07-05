package webtool.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SystemStatus{
	@JsonFormat
	boolean working = false;
	
	public boolean isOK() {
		return working;
	}
	
	public void copyFrom(SystemStatus stat) {
		this.working = stat.working;
	}

	public boolean isWorking() {
		return working;
	}

	public void setWorking(boolean working) {
		this.working = working;
	}


}
