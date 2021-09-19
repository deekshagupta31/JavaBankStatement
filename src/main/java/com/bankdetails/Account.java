package com.bankdetails;

import org.springframework.stereotype.Component;

/**
 *Bean class for Account 
 * @author Deeksha Gupta(deeksha.gupta2@gmail.com)
 *  getter-setter for variables account_type,account_number present
 */ 
@Component("account")
public class Account {
	
	private String account_type;
	private String account_number;
	
	public Account() {
		super();
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public String getAccount_number() {
		return account_number;
	}
	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}
	

}
