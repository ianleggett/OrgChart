package webtool.pojo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "updatelog")
public class UpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "upd_generator")
    @SequenceGenerator(name="upd_generator", sequenceName = "upd_seq", initialValue = 1)
    @Column(name = "id", updatable = false, nullable = false)          
    private long id;
    
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") 
	@Column(name = "timestamp", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	Date timestamp;
	
	UpdateType updateType; // import staff, import group, user move, aspect name, aspect change
	
	String refId; // user id
	
	@Lob
	String notes;
	
	public UpdateLog() {
		super();
	}

	public UpdateLog(UpdateType updateType, String refId, String notes) {
		super();
		this.updateType = updateType;
		this.refId = refId;
		this.notes = notes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	

}
