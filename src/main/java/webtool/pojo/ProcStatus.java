package webtool.pojo;

public enum ProcStatus {
	UNKNOWN,
	EDIT, // job is being edited
	CREATED,//("Order has been placed"),
	CANCELLED,//("Order was cancelled");	
	IN_PROGRESS,//("Order is in progress"),			
	COMPLETE,//("Order is ready for collection"),	
	EXPIRED,
	DELETED;
	//HOLD;// IN store order all taken away

}
