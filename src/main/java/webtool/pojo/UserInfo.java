package webtool.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Very brief user data for public trade use
 * 
 * @author ian
 *
 */
@Entity
@Table(name = "userinfo")
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uo_generator")
	@SequenceGenerator(name = "uo_generator", sequenceName = "uo_seq", initialValue = 10000000)
	@Column(name = "cid", updatable = false, nullable = false)
	long cid;

	@Column(unique = true, nullable = false)
	String username;
	int feedback; // rating 1..5

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Column(nullable = false, columnDefinition = "TIMESTAMP (6)")
	LocalDateTime lastseen;
	
	@ColumnDefault(value = "0")
	long tradeVolume;
	@ColumnDefault(value = "0")
	long tradeCount; // total trades completed
	@ColumnDefault(value = "0")
	long tradeCancelCount;  // number of time this user invoked cancel
	@ColumnDefault(value = "0")
	long ratedCount;
	@ColumnDefault(value = "0")
	long ratedScoreTotal; // score / ratedcount = (feedback)
	
	String aveTradeTime;
	@Column(nullable = false)
	String countryISO;
	
	@Override
	public boolean equals(Object obj) {
		   if (obj == this) return true;
      if (!(obj instanceof UserInfo)) return false;
      final UserInfo other = (UserInfo) obj;
      return (other.cid == this.cid);     
	}

	/**
	 * Add 0..5 ratings
	 * 
	 * @param rating
	 */
	public void addRating(int rating) {
		ratedScoreTotal += rating;
		ratedCount++;
		feedback = (int)(ratedScoreTotal / ratedCount);
	}
	public void unsetRating(int rating) {
		ratedScoreTotal -= rating;
		ratedCount--;
		feedback = ratedCount == 0 ? 0 : (int)(ratedScoreTotal / ratedCount);
	}
	/**
	 * Automated increament, rating may not be given
	 */
	public void addTradeCount(long usdtVol) {
		tradeVolume += usdtVol;
		tradeCount++;
	}
	
	public void addCancelCount() {		
		tradeCancelCount++;
	}
	
	public UserInfo() {
		super();
	}

	public UserInfo(String username, String countryISO) {
		super();
		this.username = username;
		this.feedback = 0;
		this.lastseen = LocalDateTime.now();
		this.tradeCount = 0;
		this.countryISO = countryISO;
	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCountryISO() {
		return countryISO;
	}

	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

	public int getFeedback() {
		return feedback;
	}

	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	public long getTradecount() {
		return tradeCount;
	}

	public void setTradecount(long tradecount) {
		this.tradeCount = tradecount;
	}

	public LocalDateTime getLastseen() {
		return lastseen;
	}

	public void setLastseen(LocalDateTime lastseen) {
		this.lastseen = lastseen;
	}

	public long getTradeVolume() {
		return tradeVolume;
	}
	public void setTradeVolume(long tradeVolume) {
		this.tradeVolume = tradeVolume;
	}
	public long getTradeCount() {
		return tradeCount;
	}
	public void setTradeCount(long tradeCount) {
		this.tradeCount = tradeCount;
	}
	public long getTradeCancelCount() {
		return tradeCancelCount;
	}
	public void setTradeCancelCount(long tradeCancelCount) {
		this.tradeCancelCount = tradeCancelCount;
	}
	public long getRatedCount() {
		return ratedCount;
	}
	public void setRatedCount(long ratedCount) {
		this.ratedCount = ratedCount;
	}
	public long getRatedScoreTotal() {
		return ratedScoreTotal;
	}
	public void setRatedScoreTotal(long ratedScoreTotal) {
		this.ratedScoreTotal = ratedScoreTotal;
	}
	public String getAveTradeTime() {
		return aveTradeTime;
	}
	public void setAveTradeTime(String aveTradeTime) {
		this.aveTradeTime = aveTradeTime;
	}
	@Override
	public String toString() {
		return "UserInfo [cid=" + cid + ", username=" + username + ", feedback=" + feedback + ", lastseen=" + lastseen
				+ ", tradeVolume=" + tradeVolume + ", tradecount=" + tradeCount + ", tradecancelcount="
				+ tradeCancelCount + ", ratedcount=" + ratedCount + ", ratedscoretotal=" + ratedScoreTotal
				+ ", countryISO=" + countryISO + "]";
	}

}
