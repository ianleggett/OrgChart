package webtool.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import webtool.mail.EmailSettings;
import webtool.mail.EmailTemplates;



public class Settings {

	String _id; // mongo
	Date updated;
	String siteTitle;
	String siteVersion;

	// email prefs
	// auto scan 
	// scan send 0= off, 1min..1h
		
	Date lastCatalogUpdate;
	int catItems;
	
	Date lastorderbookUpdate;
	int orderbookItems;
	
	Date lastbarcodeupdate;
	int barcodeitems;
	
	PrivateKeys privateKeys = new PrivateKeys();
	
	// notify customer when est date might be exceeded.
	
	EmailSettings emailSettings = new EmailSettings();
	
	// audit trail who did what and when
	
	public void setDefault() {
		setSiteTitle("Site-Title");
		setSiteVersion("Version-0.1");
		setUpdated(new Date());		
		//staffRoles = new ArrayList<UserAndRole>();	
	}

	public PrivateKeys getPrivateKeys() {
		return privateKeys;
	}

	public void setPrivateKeys(PrivateKeys privateKeys) {
		this.privateKeys = privateKeys;
	}

	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getSiteTitle() {
		return siteTitle;
	}
	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}
	public String getSiteVersion() {
		return siteVersion;
	}
	public void setSiteVersion(String siteVersion) {
		this.siteVersion = siteVersion;
	}

	public EmailSettings getEmailSettings() {
		return emailSettings;
	}

	public void setEmailSettings(EmailSettings emailSettings) {
		this.emailSettings = emailSettings;
	}

	public Date getLastCatalogUpdate() {
		return lastCatalogUpdate;
	}

	public void setLastCatalogUpdate(Date lastCatalogUpdate) {
		this.lastCatalogUpdate = lastCatalogUpdate;
	}


	public int getCatItems() {
		return catItems;
	}


	public void setCatItems(int catItems) {
		this.catItems = catItems;
	}


	public Date getLastorderbookUpdate() {
		return lastorderbookUpdate;
	}

	public void setLastorderbookUpdate(Date lastorderbookUpdate) {
		this.lastorderbookUpdate = lastorderbookUpdate;
	}

	public int getOrderbookItems() {
		return orderbookItems;
	}

	public void setOrderbookItems(int orderbookItems) {
		this.orderbookItems = orderbookItems;
	}

	public Date getLastbarcodeupdate() {
		return lastbarcodeupdate;
	}


	public void setLastbarcodeupdate(Date lastbarcodeupdate) {
		this.lastbarcodeupdate = lastbarcodeupdate;
	}


	public int getBarcodeitems() {
		return barcodeitems;
	}


	public void setBarcodeitems(int barcodeitems) {
		this.barcodeitems = barcodeitems;
	}

}
