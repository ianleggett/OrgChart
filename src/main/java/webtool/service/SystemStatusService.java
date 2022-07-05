package webtool.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;

import webtool.utils.SystemMessage;
import webtool.utils.SystemStatus;

@Service("systemStatusService")
public class SystemStatusService {

	final String WS_SYSTEM_STATUS = "/system/status";
	final String WS_SYSTEM_MSG = "/system/message";
		
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;
	final SystemStatus systemStatus  = new SystemStatus();
	final SystemMessage systemMessage  = new SystemMessage();
	int msgId = 0;
	
	@Scheduled(fixedRate = 30000)
	public void sendStatus() {
		simpMessagingTemplate.convertAndSend(WS_SYSTEM_STATUS,
				systemStatus );
		simpMessagingTemplate.convertAndSend(WS_SYSTEM_MSG,
				systemMessage );	
	}
	
	
	public void setStatus(final SystemStatus stats) {
		systemStatus.copyFrom(stats);
	}
	
	public void setAll(boolean tf,String newmsg) {
		setWorking(tf);		
		setNewMessage(newmsg);
	}
	
	public boolean isHealthy() {
		return this.systemStatus.isOK();
	}
	
	public SystemStatus getSystemStatus() {
		return systemStatus;
	}

	public void setWorking(boolean tf) {
		systemStatus.setWorking(tf); 
	}
	
	
	public SystemMessage getMessage() {
		return systemMessage;
	}

	public void setNewMessage(String str) {
		msgId++;
		systemMessage.setMsg(str);		
		systemMessage.setMsgid(msgId);
	}
	
	public void setMessageUpdate(String str) {
		systemMessage.setMsg(str);
		systemMessage.setMsgid(msgId);
	}
	
}
