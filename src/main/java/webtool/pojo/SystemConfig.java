package webtool.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sys")
public class SystemConfig {

	boolean writeBooksdb;	
	boolean useaccounting;
	boolean usebook;
	String machineurl;
	String ethGasApi;
	boolean usemq;
	
	public boolean isUsemq() {
		return usemq;
	}
	public void setUsemq(boolean usemq) {
		this.usemq = usemq;
	}
	public String getMachineurl() {
		return machineurl;
	}
	public void setMachineurl(String machineurl) {
		this.machineurl = machineurl;
	}
	public boolean isWriteBooksdb() {
		return writeBooksdb;
	}
	public void setWriteBooksdb(boolean writeBooksdb) {
		this.writeBooksdb = writeBooksdb;
	}
	public boolean isUseaccounting() {
		return useaccounting;
	}
	public void setUseaccounting(boolean useaccounting) {
		this.useaccounting = useaccounting;
	}
	public boolean isUsebook() {
		return usebook;
	}
	public void setUsebook(boolean usebook) {
		this.usebook = usebook;
	}
	public String getEthGasApi() {
		return ethGasApi;
	}
	public void setEthGasApi(String ethGasApi) {
		this.ethGasApi = ethGasApi;
	}
	@Override
	public String toString() {
		return "SystemConfig [writeBooksdb=" + writeBooksdb + ", useaccounting=" + useaccounting + ", usebook="
				+ usebook + ", machineurl=" + machineurl + ", ethGasApi=" + ethGasApi + ", usemq=" + usemq + "]";
	}
		
}
