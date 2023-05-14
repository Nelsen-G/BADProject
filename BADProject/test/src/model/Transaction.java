package model;

public class Transaction {
	private int transactionID;
	private String transactionDate;
	
	public Transaction(int transactionID, String transactionDate) {
		super();
		this.transactionID = transactionID;
		this.transactionDate = transactionDate;
	}
	
	public int getTransactionID() {
		return transactionID;
	}
	
	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

}
