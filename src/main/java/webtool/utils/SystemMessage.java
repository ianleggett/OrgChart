package webtool.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SystemMessage{
	@JsonFormat
	int msgid;
	@JsonFormat
	String msg;
	public int getMsgid() {
		return msgid;
	}
	public void setMsgid(int msgid) {
		this.msgid = msgid;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}		
}
