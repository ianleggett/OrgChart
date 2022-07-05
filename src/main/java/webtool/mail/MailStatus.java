package webtool.mail;

public enum MailStatus {
	HOLD,         // will hold until operator sets it to Ready-to-send
	CANCELLED,
	READY_TO_SEND,
	SENT,
	RETRY,
	ERROR;
}
