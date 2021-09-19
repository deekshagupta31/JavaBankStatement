package com.bankdetails;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Deeksha Gupta(deeksha.gupta2@gmail.com)
 * Class for generic use methods
 */
public class Utils {
	
	/**
	 * @param input a String value to checked for number
	 * @return true or false (if value is integer then true else false)
	 */
	public static Logger log = LoggerFactory.getLogger(Utils.class);
	public static boolean validateNumber(String input) {
		try {
			String regex = "[0-9]+";
			return(Pattern.matches(regex,input));

		}catch(Exception e) {
			log.error("Exception in Util.validateNumber>>>",e);
		}		
		return false;
	}
	/**
	 * @param input a String value to checked for date format- "yyyy-MM-dd"
	 * @return true or false (if value is date of correct format then true else false)
	 */
	public static boolean validateDate(String input) {
		try {
			String regex="[0-9]{4}[-|\\\\/]{1}[0-1]{1}[1-9]{1}[-|\\\\/]{1}[0-3]{1}[1-9]{1}";
			return(Pattern.matches(regex,input));
			
		}catch(Exception e) {
			log.error("Exception in Util.validateDate>>>",e);
		}
		return false;
	}
	
}
