package com.prashik.scorer.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    private String name = "";
    private String captainName = "";
    private String commonName = "";
    ArrayList<MatchPlayer> teamPlayers = new ArrayList<>();
    ArrayList<String> playerNames = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public ArrayList<MatchPlayer> getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(ArrayList<MatchPlayer> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }
}
