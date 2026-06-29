package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    private String name = "";
    private String captainName = "";
    private String commonName = "";
    private ArrayList<MatchPlayer> teamPlayers = new ArrayList<>();
    private ArrayList<String> playerNames = new ArrayList<>();
    private int runs = 0;
    private int wickets = 0;
    private int maxWickets;
    private ArrayList<Over> overs = new ArrayList<>();
    private int currentOver = 0;
    private int teamSize;

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
    public void setOvers(int maxOvers) {
        System.out.println("Setting max overs.");
        ArrayList<Over> newOvers = new ArrayList<>();
        for(int i=0; i<maxOvers; i++) {
            Over over = new Over(i);
            newOvers.add(over);
        }
        this.overs = newOvers;
        System.out.println("Overs array after set: " + this.overs);
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

    public void setOvers(ArrayList<Over> overs) {
        this.overs = overs;
    }

    public int getCurrentOver() {
        return currentOver;
    }

    public void setCurrentOver(int currentOver) {
        this.currentOver = currentOver;
    }

    public MatchPlayer getMatchPlayerFromName(String playerName) {
        for(MatchPlayer matchPlayer : this.teamPlayers) {
            if(matchPlayer.getPlayerName().equals(playerName)) {
                return matchPlayer;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                ", captainName='" + captainName + '\'' +
                ", commonName='" + commonName + '\'' +
                ", teamPlayers=" + teamPlayers +
                ", playerNames=" + playerNames +
                ", runs=" + runs +
                ", wickets=" + wickets +
                ", maxWickets=" + maxWickets +
                ", overs=" + overs +
                ", currentOver=" + currentOver +
                ", teamSize=" + teamSize +
                '}';
    }
}
