package webtool.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestParam;

@Entity
@Table(name = "mediaevidence",uniqueConstraints = { @UniqueConstraint( name="guidAndSlot", columnNames = { "caseguid", "slotnum" }) } )
public class FileDB {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  String caseguid;
  
  int slotnum;

  private String type;   // pdf, jpg,png

  @Lob
  private byte[] data;

  public FileDB() {
  }

  public FileDB(String caseguid,int slot, String type, byte[] data) {
    this.caseguid = caseguid;
    this.slotnum = slot;
    this.type = type;
    this.data = data;
  }

  public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getCaseguid() {
	return caseguid;
}

public void setCaseguid(String caseguid) {
	this.caseguid = caseguid;
}

public int getSlotnum() {
	return slotnum;
}

public void setSlotnum(int slotnum) {
	this.slotnum = slotnum;
}

public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}