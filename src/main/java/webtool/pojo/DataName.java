package webtool.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "DataName", uniqueConstraints = { @UniqueConstraint(columnNames = { "source","prefix", "name" }) })
public class DataName implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "n_generator")
	@SequenceGenerator(name = "n_generator", sequenceName = "n_seq", initialValue = 1)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	@Column(nullable = false)
	private String source;
	
	@Column(nullable = false)
	private String prefix;	
	
	@Column(nullable = false)
	private String name;
	
	public DataName() {
		super();
	}

	public DataName(String source,String prefix, String name) {
		super();
		this.source = source;
		this.prefix = prefix;
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "DataName [id=" + id + ", source=" + source + ", prefix=" + prefix + ", name=" + name + "]";
	}

}
