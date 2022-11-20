package com.gmf.gamersfieldesports.Playerprofile;

public class transactionModel {
    //strings
    private String type;
    private String winDiamonds;
    private int transactionNo;

    public transactionModel(){
        //empty constructor needed
    }

    public transactionModel(String type, String winDiamonds, int transactionNo) {
        this.type = type;
        this.winDiamonds = winDiamonds;
        this.transactionNo = transactionNo;
    }

    public String getType() {
        return type;
    }

    public String getWinDiamonds() {
        return winDiamonds;
    }

    public int getTransactionNo() {
        return transactionNo;
    }
}
