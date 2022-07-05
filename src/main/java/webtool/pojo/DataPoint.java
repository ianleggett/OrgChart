package webtool.pojo;

import java.time.Instant;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "DataPoint")
public class DataPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dp_generator")
	@SequenceGenerator(name = "dp_generator", sequenceName = "dp_seq", initialValue = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;
	
	@Column(columnDefinition = "DATETIME(6)")
	//@CreationTimestamp	
	Instant tmstamp;
	
	@OneToOne
    @JoinColumn(name = "data_id", referencedColumnName = "id")
	DataName dataName; // e,g bitfinex, cumvolume
	
	Double doubleVal;
	Long longVal;

	public DataPoint() {
		super();
	}

	public DataPoint(DataName dataName, long value) {
		super();
		this.dataName = dataName;
		this.longVal = value;
	}
	
	public DataPoint(DataName dataName, double value) {
		super();
		this.dataName = dataName;
		this.doubleVal = value;
	}	

	public DataPoint(Instant tmstamp, DataName dataName, Double doubleVal) {
		super();
		this.tmstamp = tmstamp;
		this.dataName = dataName;
		this.doubleVal = doubleVal;
	}

	public DataName getDataName() {
		return dataName;
	}

	public void setDataName(DataName dataName) {
		this.dataName = dataName;
	}

	public Instant getTmstamp() {
		return tmstamp;
	}

	public void setTmstamp(Instant tmstamp) {
		this.tmstamp = tmstamp;
	}

	public Double getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(Double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public Long getLongVal() {
		return longVal;
	}

	public void setLongVal(Long longVal) {
		this.longVal = longVal;
	}

	@Override
	public String toString() {
		return "DataPoint [tmstamp=" + tmstamp + ", dataName=" + dataName + ", doubleVal=" + doubleVal + ", longVal="
				+ longVal + "]";
	}

	
	
}
