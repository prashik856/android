package com.prashik.scorer.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Team implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    private String name = "";
    private String captainName = "";
    private String commonName = "";
    ArrayList<MatchPlayer> teamPlayers = new ArrayList<>();
    ArrayList<String> playerNames = new ArrayList<>();

    int runs;
    int wickets;
    int maxWickets;
    ArrayList<Over> overs = new ArrayList<>();

    int teamSize;

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

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public ArrayList<Over> getOvers() {
        return overs;
    }

    // Initialize overs
    public void setOvers(int overs) {
        ArrayList<Over> newOvers = new ArrayList<>(overs);
        for(int i=0; i<newOvers.size(); i++) {
            Over over = new Over(i);
            newOvers.set(i, over);
        }
        this.overs = newOvers;
    }

    public int getMaxWickets() {
        return maxWickets;
    }

    public void setMaxWickets(int maxWickets) {
        this.maxWickets = maxWickets;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }
}
