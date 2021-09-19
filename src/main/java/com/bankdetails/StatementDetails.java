package com.bankdetails;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** 
 * @author Deeksha Gupta(deeksha.gupta2@gmail.com)
 *  	Method- returnStatements(HashMap<String,Object> data) to generate Statement details of the given Account ID and date and amount rang if present.
 */
@Component("statementDetails")
public class StatementDetails {
public static Logger log = LoggerFactory.getLogger(StatementDetails.class);
	/** Method used to generate statement from the given data entry 
	 * @param data HashMap<String,Object> contains details send from the request facilitating in filtering the statement to be extracted.
	 * Keys are - accountID,startDate,endDate,toAmount,fromAmount
	 * @return LinkedHashMap <String,Object> returning having the statement/ or an appropriate .
	 * key "Message" can have either of the  2 values- "Success"/ "No Records Found".
	 * if Success- the map has key value pair for statement object where key is sequence-"1","2"... and value is statement object
	 */
	public static LinkedHashMap <String,Object> returnStatements(HashMap<String,Object> data){
		LinkedHashMap <String,Object> returnStatementMap=new LinkedHashMap <String,Object>();
		try {
			// get values from the map passed
			log.info("values passed.."+data);
			int account_id=Integer.parseInt(Objects.toString(data.get("accountID"),"0"));
			String startDateString=Objects.toString(data.get("startDate"),"");
			String endDateString=Objects.toString(data.get("endDate"),"");
			String amount=Objects.toString(data.get("fromAmount"),"");
			String toAmount=Objects.toString(data.get("toAmount"),"");
			// convert string date to Local Date and if start date not mentioned then set to current value
			LocalDate startDate="".equalsIgnoreCase(startDateString)?LocalDate.now():LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			log.info("startDate.."+startDate);
			// convert string date to Local Date and if end date not mentioned then set it to start date-3 months
			LocalDate endDate="".equalsIgnoreCase(endDateString)?startDate.minusMonths(3):LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			log.info("endDate.."+endDate);
			// Creating of the Query to be executed
			// For left join between Account and Statement account_number from Account table Converted to un-sign integer
			// For date range all date parameters(startdate, enddate and the column date) converted to date of format yyyy-MM-dd
			String query="SELECT (@row_number:=@row_number + 1) AS Sequence,stm.account_id AccountId,stm.datefield AS Date,stm.amount AS Amount,acc.account_type AS AccountType FROM Statement stm LEFT JOIN Account acc ON(stm.account_id=CONVERT(NULLIF(acc.account_number, ''),UNSIGNED INT)) WHERE account_id="+account_id+" AND STR_TO_DATE(`Date`,'%Y-%m-%d') BETWEEN STR_TO_DATE('+"+startDate+"','%Y/%m/%d') AND STR_TO_DATE('"+endDate+"', '%Y/%m/%d') ";
			// if start amount present then add in where clause after converting to UN-signed integer for > clause
			if(!"".equalsIgnoreCase(amount)) {
				query=query+" AND CONVERT(Amount, UNSIGNED INTEGER) >= CONVERT("+amount+", SIGNED INTEGER)";
			}
			// if to amount present then add in where clause
			if(!"".equalsIgnoreCase(toAmount)) {
				query=query+" AND CONVERT(Amount, UNSIGNED INTEGER) <= CONVERT("+toAmount+", SIGNED INTEGER) ";
			}
			// order the result set by date; row_number is to provide the sequence
			query=query+" ORDER BY datefield desc,@row_number = 0";
			log.info("Final Query.."+query);
			//  connection with MS Access	
			//url to Data Source -(ASSUMING) mydsn
			//TODO replace the correct Data Source name here 		
			 String url="jdbc:odbc:mydsn";
			 // loading the jdbc odbc driver   
		   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); 
		   // making the connection with the url; ASSUMING the user and password for the database 
		   //TODO replace with correct user and password 
		   Connection c=DriverManager.getConnection(url,"admin","password");  
		   // PreparedStatement to execute query
		   PreparedStatement ptmt=c.prepareStatement(query);  
		   //resultset 
		   ResultSet rs=ptmt.executeQuery();  
		   // check if there is any rows in resultset
		   if(rs.isBeforeFirst()) {
			   //iterate resultset to get statement data
			   while(rs.next()) {
				   Statement resultSetStatement=new Statement();
				   //Hashing of Account Id Done Using DigestUtils
				   resultSetStatement.setAccount_id(Integer.parseInt(DigestUtils.sha256Hex(rs.getString("AccountId"))));
				   resultSetStatement.setAmount(rs.getString("Amount"));
				   resultSetStatement.setDatefield(rs.getString("Date"));
				   Account resultSetAccount = new Account();
				   resultSetAccount.setAccount_type(rs.getString("AccountType"));
				   resultSetStatement.setMyaccount(resultSetAccount);
				   returnStatementMap.put(rs.getString("Sequence"), resultSetStatement);					   
			   }
			   returnStatementMap.put("Message","Success");			
		   }
		   //if no rows present in resultset then return appropriate message
		   else {
			   returnStatementMap.put("Message", "No Records Found");
		   }	
		}catch(Exception e) {
			log.error("Exception in returnStatements>>"+e);
		}
		// map returned
		return returnStatementMap;		
	}

}
