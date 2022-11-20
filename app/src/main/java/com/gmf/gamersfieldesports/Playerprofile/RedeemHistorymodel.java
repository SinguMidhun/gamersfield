package com.gmf.gamersfieldesports.Playerprofile;

public class RedeemHistorymodel {

    private String Status;
    private String date;
    private String redeemItem;
    private int transactionNo;

    public RedeemHistorymodel(){

    }

    public RedeemHistorymodel(String status, String date, String redeemItem, int transactionNo) {
        Status = status;
        this.date = date;
        this.redeemItem = redeemItem;
        this.transactionNo = transactionNo;
    }

    public String getStatus() {
        return Status;
    }

    public String getDate() {
        return date;
    }

    public String getRedeemItem() {
        return redeemItem;
    }

    public int getTransactionNo() {
        return transactionNo;
    }
}
