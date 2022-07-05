package webtool.pojo;

public class RespStatus {

	public static RespStatus OK = new RespStatus(0,"OK");
	
	int statusCode;
	String msg;
	
	public RespStatus() {
		super();
	}
	
	public RespStatus(int value) {
		super();
		this.statusCode = value;
	}

	public RespStatus(int statusCode, String msg) {
		super();
		this.statusCode = statusCode;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "RespStatus [statusCode=" + statusCode + ", msg=" + msg + "]";
	}
	
}
