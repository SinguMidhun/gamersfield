package com.gmf.gamersfieldesports.Pubg;

public class k_w_model {
    public k_w_model(){
        //empty constructor needed
    }
    private String name;
    private String position;
    private String kills;
    private String winning;

    public k_w_model(String name, String position, String kills, String winning) {
        this.name = name;
        this.position = position;
        this.kills = kills;
        this.winning = winning;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getKills() {
        return kills;
    }

    public String getWinning() {
        return winning;
    }
}
