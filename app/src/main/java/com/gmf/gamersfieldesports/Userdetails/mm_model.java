package com.gmf.gamersfieldesports.Userdetails;

public class mm_model {

    private String MatchNumber;
    private String Type;
    private String Map;
    private String Date;
    private String BType;

    private mm_model(){
        //empty constructor
    }

    public mm_model(String matchNumber, String type, String map, String date, String BType) {
        MatchNumber = matchNumber;
        Type = type;
        Map = map;
        Date = date;
        this.BType = BType;
    }

    public String getMatchNumber() {
        return MatchNumber;
    }

    public String getType() {
        return Type;
    }

    public String getMap() {
        return Map;
    }

    public String getDate() {
        return Date;
    }

    public String getBType() {
        return BType;
    }
}
