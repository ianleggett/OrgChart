package webtool;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class InetConfig {

	String hostname;
		
	public InetConfig(){
		try {
			this.setHostname(
			  InetAddress.getLocalHost().getHostName()
					);
		} catch (UnknownHostException e) {
			hostname="unknown";
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
	
}
