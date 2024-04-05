package test0326;

import java.sql.Timestamp;

public class Currency {
	
	private int rowNum;
	private String currency;
	private Double cashBuy;
	private Double cashSell;
	private Double spotBuy;
	private Double spotSell;
	private Timestamp recordTime;
	
	
	public Currency() {
	}


	public int getRowNum() {
		return rowNum;
	}


	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public Double getCashBuy() {
		return cashBuy;
	}


	public void setCashBuy(Double cashBuy) {
		this.cashBuy = cashBuy;
	}


	public Double getCashSell() {
		return cashSell;
	}


	public void setCashSell(Double cashSell) {
		this.cashSell = cashSell;
	}


	public Double getSpotBuy() {
		return spotBuy;
	}


	public void setSpotBuy(Double spotBuy) {
		this.spotBuy = spotBuy;
	}


	public Double getSpotSell() {
		return spotSell;
	}


	public void setSpotSell(Double spotSell) {
		this.spotSell = spotSell;
	}




	public Timestamp getRecordTime() {
		return recordTime;
	}


	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}


	@Override
	public String toString() {
		return "Currency [rowNum=" + rowNum + ", currency=" + currency + ", cashBuy=" + cashBuy + ", cashSell="
				+ cashSell + ", spotBuy=" + spotBuy + ", spotSell=" + spotSell + ", recordTime=" + recordTime + "]";
	}
	
	
	
	
}
