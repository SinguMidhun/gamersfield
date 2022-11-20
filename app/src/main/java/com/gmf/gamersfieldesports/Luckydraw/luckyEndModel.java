package com.gmf.gamersfieldesports.Luckydraw;

public class luckyEndModel {

    private String date;
    private String name;

    public luckyEndModel(){
        //empty constructor required
    }

    public luckyEndModel(String date, String name) {
        this.date = date;
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
}
