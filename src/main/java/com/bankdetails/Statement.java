package com.bankdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Bean class for Statement 
 * @author Deeksha Gupta(deeksha.gupta2@gmail.com)
 * having private variables- account_id,datefield,amount and Object of class Account along with their getter-setter
 */
@Component("statement")
public class Statement {
	// Getter Setter for the following fields declared below
	private int account_id;
	private String datefield;
	private String amount;
	private Account myaccount;
	//Constructor
	public Statement() {
		
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public String getDatefield() {
		return datefield;
	}
	public void setDatefield(String datefield) {
		this.datefield = datefield;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}	
	public Account getMyaccount() {
		return myaccount;
	}
	@Autowired
	public void setMyaccount(Account myaccount) {
		this.myaccount = myaccount;
	}
}
