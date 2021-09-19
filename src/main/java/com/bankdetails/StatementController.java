package com.bankdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/** Controller 
 * @author Deeksha Gupta(deeksha.gupta2@gmail.com)
 *
 */
@Controller
@RequestMapping("/myAccount")
public class StatementController {	
	
	public static Logger log = LoggerFactory.getLogger(StatementController.class);
	/**
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param valuesRequest HashMap<String,Object> with details for generation of statement using @RequestParam
	 * keys- userName,accountID(required) startDate,endDate,fromAmount,toAmount (optional)
	 * @return ResponseEntity<>
	 * if HTTP error comes the <"string",HttpStatus>
	 * else <mapWithDetails,HttpStatus>
	 */
	@RequestMapping(value="/statementDetails", method=RequestMethod.POST)
	//Spring will bind the return value to outgoing HTTP response body when you add @ResponseBody annotation. Hence used ResponseBody assuming data in given through ajax
	@ResponseBody
	public ResponseEntity<?> returnDetails(HttpServletRequest request, HttpServletResponse response,@RequestParam HashMap<String,Object> valuesRequest) {	
		try(ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("ApplicationContext.xml")){
			//get values from request params
			log.info("Input Parameters.."+valuesRequest);
			String username=Objects.toString(valuesRequest.get("userName"),"");
			String accountId=Objects.toString(valuesRequest.get("accountID"),"");
			String startdate=Objects.toString(valuesRequest.get("startDate"),"");
			String enddate=Objects.toString(valuesRequest.get("endDate"),"");
			String amount=Objects.toString(valuesRequest.get("fromAmount"),"");
			String toamount=Objects.toString(valuesRequest.get("toAmount"),"");
			// validate user authorization, if not authorized send response 401 error
			// checking for a particular case in which the ‘user’ can only do a request without parameters
			if("user".equalsIgnoreCase(username) && (!"".equalsIgnoreCase(startdate)||!"".equalsIgnoreCase(enddate)||!"".equalsIgnoreCase(amount)||!"".equalsIgnoreCase(toamount))) {
				log.error("Uauthorized User");
				return new ResponseEntity<>("Unauthorized",HttpStatus.UNAUTHORIZED);				
			}
			//validations for correct values input; if not the appropriate message send
			LinkedHashMap <String,Object> responseDetails=new LinkedHashMap <String,Object> ();
			List<String> validationFails=new ArrayList<String>();
			if(!"".equalsIgnoreCase(startdate)) {
				if(!Utils.validateDate(startdate)) {
					validationFails.add("Incorrect Start Date");
				}
			}
			if(!"".equalsIgnoreCase(enddate)) {
				if( !Utils.validateDate(enddate)) {
					validationFails.add("Incorrect End Date");
				}
			}
			if(!Utils.validateNumber(accountId)) {
				validationFails.add("Incorrect AccountID");
			}
			if("".equalsIgnoreCase(amount)) {
				if(!Utils.validateNumber(amount)) {
					validationFails.add("Incorrect Amount");
				}
			}
			if("".equalsIgnoreCase(toamount)) {
				if(!Utils.validateNumber(toamount)) {
					validationFails.add("Incorrect To Amount");
				}
			}
			//if validation failed then status ok but Message "Validation Failed" with map of validationFails send
			log.info("Validation Result.."+validationFails.size());
			if(validationFails.size()>0) {
				responseDetails.put("Mesage","Validation Failed");
				responseDetails.put("Errors",validationFails);
				log.error("Validation Failed");
				return new ResponseEntity<>(responseDetails,HttpStatus.OK);
			}		
			// if cleared all validations the return the statement generated. In this map "Message" with status "Success" OR "No Records Found" send
			responseDetails=StatementDetails.returnStatements(valuesRequest);		
			return new ResponseEntity<>(responseDetails,HttpStatus.OK);
		} catch (BeansException e) {
			log.error("Exception in returnDetails>>>"+e);
		}
		// if unable to send any of the able response the BAD REQUEST status send
		return new ResponseEntity<>("Bad Request",HttpStatus.BAD_REQUEST);
	}
}
