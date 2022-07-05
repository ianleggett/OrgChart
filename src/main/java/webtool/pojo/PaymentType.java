package webtool.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
	
@Entity
@Table(name = "paymenttype", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class PaymentType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	long id;
	String name;
	boolean enabled;
	String field1name;
	String field2name;
	String field3name;
	String field4name;
	String field5name;

	public PaymentType() {
		super();		
	}
	
	public PaymentType(String name) {
		super();
		id = 0;		
		this.name = name;
	}
	
	public PaymentType(String name, String field1name, String field2name, String field3name, String field4name,
			String field5name,boolean enable) {		
		this(name);
		this.field1name = field1name;
		this.field2name = field2name;
		this.field3name = field3name;
		this.field4name = field4name;
		this.field5name = field5name;
		this.enabled = enable;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getField1name() {
		return field1name;
	}

	public void setField1name(String field1name) {
		this.field1name = field1name;
	}

	public String getField2name() {
		return field2name;
	}

	public void setField2name(String field2name) {
		this.field2name = field2name;
	}

	public String getField3name() {
		return field3name;
	}

	public void setField3name(String field3name) {
		this.field3name = field3name;
	}

	public String getField4name() {
		return field4name;
	}

	public void setField4name(String field4name) {
		this.field4name = field4name;
	}

	public String getField5name() {
		return field5name;
	}

	public void setField5name(String field5name) {
		this.field5name = field5name;
	}

	@Override
	public String toString() {
		return "PaymentType [id=" + id + ", name=" + name + ", enabled=" + enabled + ", field1name=" + field1name
				+ ", field2name=" + field2name + ", field3name=" + field3name + ", field4name=" + field4name
				+ ", field5name=" + field5name + "]";
	}
	

}
