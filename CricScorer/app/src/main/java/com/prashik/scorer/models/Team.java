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
    private int currentOverBowling = 0;
    private int currentOverBatting = 0;
    private int teamSize;
    private double runRate = 0;
    private int legalDeliveriesPlayed = 0;
    private int maxLegalDeliveries;

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
    public void incrementRuns() {
        this.runs = this.runs + 1;
    }
    public void addTwoToRuns() {
        this.runs = this.runs + 2;
    }
    public void addThreeToRuns() {
        this.runs = this.runs + 3;
    }
    public void addFourToRuns() {
        this.runs = this.runs + 4;
    }
    public void addFiveToRuns() {
        this.runs = this.runs + 5;
    }
    public void addSixToRuns() {
        this.runs = this.runs + 6;
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
        this.maxLegalDeliveries = maxOvers * 6;
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

    public double getRunRate() {
        return runRate;
    }

    public void setRunRate(double runRate) {
        this.runRate = runRate;
    }

    public void updateRunRate() {
        if(!(this.legalDeliveriesPlayed == 0)) {
            int oversBowled = this.legalDeliveriesPlayed / 6;
            int ballsInNewOver = this.legalDeliveriesPlayed % 6;
            double denominator = oversBowled +  ((double)ballsInNewOver/6);
            this.runRate = this.runs/denominator;
        }
    }

    public int getLegalDeliveriesPlayed() {
        return legalDeliveriesPlayed;
    }

    public void incrementLegalDeliveriesPlayed() {
        this.legalDeliveriesPlayed = this.legalDeliveriesPlayed + 1;
    }

    public void setLegalDeliveriesPlayed(int legalDeliveriesPlayed) {
        this.legalDeliveriesPlayed = legalDeliveriesPlayed;
    }

    public int getMaxLegalDeliveries() {
        return maxLegalDeliveries;
    }

    public void setMaxLegalDeliveries(int maxLegalDeliveries) {
        this.maxLegalDeliveries = maxLegalDeliveries;
    }

    public int getCurrentOverBowling() {
        return currentOverBowling;
    }

    public void incrementCurrentOverBowling() {
        this.currentOverBowling = this.currentOverBowling + 1;
    }

    public void setCurrentOverBowling(int currentOverBowling) {
        this.currentOverBowling = currentOverBowling;
    }

    public int getCurrentOverBatting() {
        return currentOverBatting;
    }

    public void incrementCurrentOverBatting() {
        this.currentOverBatting = this.currentOverBatting + 1;
    }

    public void setCurrentOverBatting(int currentOverBatting) {
        this.currentOverBatting = currentOverBatting;
    }

    public MatchPlayer getMatchPlayerFromName(String playerName) {
        for(MatchPlayer matchPlayer : this.teamPlayers) {
            if(matchPlayer.getPlayerName().equals(playerName)) {
                return matchPlayer;
            }
        }
        return null;
    }

    public int getMatchPlayerIndex(String playerName) {
        for(int i=0; i<this.teamPlayers.size(); i++) {
            if(this.teamPlayers.get(i).getPlayerName().equals(playerName)) {
                return i;
            }
        }
        return -1;
    }

    public Over getCurrentOverObject() {
        return this.overs.get(this.getCurrentOverBowling());
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
                ", currentOverBowling=" + currentOverBowling +
                ", currentOverBatting=" + currentOverBatting +
                ", teamSize=" + teamSize +
                ", runRate=" + runRate +
                ", legalDeliveriesPlayed=" + legalDeliveriesPlayed +
                ", maxLegalDeliveries=" + maxLegalDeliveries +
                '}';
    }
}
