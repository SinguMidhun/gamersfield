package com.gmf.gamersfieldesports.Pubg;

public class Model {

    private Model() {
        //empty constructor
    }

    private String matchnum;
    private String datetime;
    private String prizepool;
    private String killprize;
    private String entryFee;
    private String type;
    private String version;
    private String map;
    private String joined;
    private String total;
    private String cTimer;
    private String tName;
    private String tNotify;

    public String gettName() {
        return tName;
    }

    public String gettNotify() {
        return tNotify;
    }

    public String getcTimer() { return cTimer; }

    public String getJoined() {
        return joined;
    }

    public String getTotal() {
        return total;
    }

    public String getType() {
        return type;
    }

    public String getMatchnum() {
        return matchnum;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPrizepool() {
        return prizepool;
    }

    public String getKillprize() {
        return killprize;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public String getVersion() {
        return version;
    }

    public String getMap() {
        return map;
    }


    public Model(String matchnum, String datetime, String prizepool, String killprize, String entryFee, String type, String version, String map, String joined, String total, String cTimer,String tName, String tNotify) {
        this.matchnum = matchnum;
        this.datetime = datetime;
        this.prizepool = prizepool;
        this.killprize = killprize;
        this.entryFee = entryFee;
        this.type = type;
        this.version = version;
        this.map = map;
        this.joined = joined;
        this.total = total;
        this.cTimer = cTimer;
        this.tName = tName;
        this.tNotify = tNotify;
    }
}
