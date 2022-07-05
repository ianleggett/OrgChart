package webtool.pojo;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "ccy", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class Ccy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;

	String name; // AUD
	String description;
	boolean enable;
	CcyType ccyType; // marks crypto or Fiat currency
	String tokenCtrAddress;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") 
	@UpdateTimestamp
	@Column(nullable = false,columnDefinition = "TIMESTAMP (6)")
	LocalDateTime updated;

	public Ccy() {
		super();
	}

	public Ccy(String name, String description,CcyType ccyType, String tokenCtrAddr,boolean enable) {
		super();
		this.name = name;
		this.description = description;
		this.ccyType = ccyType;
		this.enable = enable;
		this.tokenCtrAddress = tokenCtrAddr;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}	

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public CcyType getCcyType() {
		return ccyType;
	}

	public void setCcyType(CcyType ccyType) {
		this.ccyType = ccyType;
	}

	public String getTokenCtrAddress() {
		return tokenCtrAddress;
	}

	public void setTokenCtrAddress(String tokenCtrAddress) {
		this.tokenCtrAddress = tokenCtrAddress;
	}

	@Override
	public String toString() {
		return "Ccy [id=" + id + ", name=" + name + ", description=" + description + ", enable=" + enable + ", ccyType="
				+ ccyType + ", tokenCtrAddress=" + tokenCtrAddress + ", updated=" + updated + "]";
	}

	
}
