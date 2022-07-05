package webtool.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "audit")
public class AuditAction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_generator")
    @SequenceGenerator(name="aud_generator", sequenceName = "aud_seq",initialValue = 1)
    @Column(name = "id", updatable = false, nullable = false)   
    private long id;
	String staffName;
	Date whenDt;	
	@Lob 
	String action;
		
	public AuditAction(String staffName, Date whenDt, String action) {
		super();
		this.staffName = staffName;
		this.whenDt = whenDt;
		this.action = action;
	}
	public AuditAction() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public Date getWhen() {
		return whenDt;
	}
	public void setWhen(Date when) {
		this.whenDt = when;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	
}
