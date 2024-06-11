package webtool.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class UtilsWeb {
	
	public static String NOT_SET = "-not-set-";	
	public static String PARKING_BIN = "0";
	
	public static final List<String> FLOATING = Arrays.asList(NOT_SET,PARKING_BIN);
	
	public static LocalDate FIRST_DATE = LocalDate.of(2000, 1, 1);
	
	public static SimpleDateFormat humanDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	public static String COMMA = ",";
	public static String CART = "CART";
	public static String USER_PREF = "USER_PREF";
	
	static public List<String> enumSetToStr(EnumSet<?> en){
		List<String> enList = new ArrayList<String>();
		en.forEach( (iter) ->{ enList.add( iter.toString() );});			
		return enList;
	}
	
	public enum TUNITS {
			minute(TimeUnit.MINUTES,1),
			hour(TimeUnit.HOURS,1),
			day(TimeUnit.HOURS,24);
		public TimeUnit tbench;
		public int mult;
		TUNITS(TimeUnit t,int mul){
			tbench = t;
			mult = mul;
		}
	}
	
	static Logger log = Logger.getLogger(UtilsWeb.class);

	
	public String getUserPrefs(HttpSession session) {		
		Object obj = session.getAttribute(USER_PREF);				 
		if (!(obj instanceof String)) {
			return null;
		}		
		return obj.toString();	
	}
	
	public void setUserPrefs(HttpSession session,String prefs) {		
		session.setAttribute(USER_PREF,prefs);				 		
	}

	public static double round(double val, int dp) {
		final double powder = Math.pow(10,2);
		return  Math.round(val * powder) / powder;
	}
	
	public static String hideKey(String str,int peekLastN) {
		if (str==null) str = "";
		int pos = (peekLastN > str.length()) ? 0 : str.length()-peekLastN ;
		return StringUtils.repeat('*', pos)+str.substring(pos);		
	}
	
	public static LocalDateTime parseISODate(String dtStr) {
		try {
			return LocalDateTime.parse(dtStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		}catch(DateTimeParseException e) {
			return null;
		}
	}

	public static long getExpiryMins(String val,String units) {
		 TimeUnit timeMins = TimeUnit.MINUTES;	
		 // first parse val
		 long value = Long.parseLong(val);
		 TUNITS tunit = TUNITS.valueOf(units);
		 return timeMins.convert(value * tunit.mult,tunit.tbench);
	}
	
}
