package webtool.utils;

public class FailedTransactionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String hash;
	
	public  FailedTransactionException(String msg, String hash) {
		super(msg);
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}
	
	
}
