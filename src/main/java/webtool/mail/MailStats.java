package webtool.mail;

import java.util.Date;

public class MailStats {

	Date lastRun;
	long lastCount;
	long totalSent;
	long pending;
	
	public Date getLastRun() {
		return lastRun;
	}
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}
	public long getLastCount() {
		return lastCount;
	}
	public void setLastCount(long lastCount) {
		this.lastCount = lastCount;
		this.totalSent += lastCount;
	}
	public long getTotalSent() {
		return totalSent;
	}
	public void setTotalSent(long totalSent) {
		this.totalSent = totalSent;
	}
	public long getPending() {
		return pending;
	}
	public void setPending(long pending) {
		this.pending = pending;
	}
	
	
	
}
