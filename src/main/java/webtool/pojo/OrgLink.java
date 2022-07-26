package webtool.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

//@Entity
//@Table(name = "olink")
//public class OrgLink {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "l_generator")
//	@SequenceGenerator(name = "l_generator", sequenceName = "l_seq", initialValue = 1)
//	@Column(name = "id", updatable = false, nullable = false)
//	private long id;
//
//	@OneToOne(fetch = FetchType.EAGER)	
//	Employee iFrom;
//	@OneToOne(fetch = FetchType.EAGER)	
//	Employee iTo;
//	String reportType;
//	
//	public String getIdHash() {
//		return iFrom.getInum()+"_"+iTo.getInum();
//	}
//	
//	public OrgLink() {
//		super();
//	}
//
//	public OrgLink(Employee iFrom, Employee iTo, String reportType) {
//		super();
//		this.iFrom = iFrom;
//		this.iTo = iTo;
//		this.reportType = reportType;
//	}
//
//	public Employee getiFrom() {
//		return iFrom;
//	}
//	public void setiFrom(Employee iFrom) {
//		this.iFrom = iFrom;
//	}
//	public Employee getiTo() {
//		return iTo;
//	}
//	public void setiTo(Employee iTo) {
//		this.iTo = iTo;
//	}
//	public String getReportType() {
//		return reportType;
//	}
//	public void setReportType(String reportType) {
//		this.reportType = reportType;
//	}
//
//}
