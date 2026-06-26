package com.prashik.scorer.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Match implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    Team teamA;
    Team teamB;
    String date;
    boolean teamAToss = false;
    boolean teamABatFirst = false;
    boolean teamBToss = false;
    boolean teamBBatFirst = false;
    String tossDecision = "";
    ArrayList<String> matchPlayers = new ArrayList<>();
    int maxOvers;

    public Match(Team team1, Team team2) {
        this.teamA = team1;
        this.teamB = team2;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }
    public String getDate() {
        return date;
    }
    public void setDate() {
        Date temp = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(temp);
        dateString = dateString + "-" + String.valueOf( new Random().nextInt(1001));
        this.date = dateString;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTeamAToss() {
        return teamAToss;
    }

    public void setTeamAToss(boolean teamAToss) {
        this.teamAToss = teamAToss;
    }

    public boolean isTeamBToss() {
        return teamBToss;
    }

    public void setTeamBToss(boolean teamBToss) {
        this.teamBToss = teamBToss;
    }

    public String getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(String tossDecision) {
        this.tossDecision = tossDecision;
    }

    public boolean isTeamABatFirst() {
        return teamABatFirst;
    }

    public void setTeamABatFirst(boolean teamABatFirst) {
        this.teamABatFirst = teamABatFirst;
    }

    public boolean isTeamBBatFirst() {
        return teamBBatFirst;
    }

    public void setTeamBBatFirst(boolean teamBBatFirst) {
        this.teamBBatFirst = teamBBatFirst;
    }

    public ArrayList<String> getMatchPlayers() {
        return matchPlayers;
    }

    public void setMatchPlayers(ArrayList<String> matchPlayers) {
        this.matchPlayers = matchPlayers;
    }

    public int getMaxOvers() {
        return maxOvers;
    }

    public void setMaxOvers(int maxOvers) {
        this.maxOvers = maxOvers;
    }
}
