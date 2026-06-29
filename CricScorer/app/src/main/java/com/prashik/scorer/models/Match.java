package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import com.prashik.scorer.util.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Match implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String id;
    private Team teamA;
    private Team teamB;
    private String date;
    private boolean teamAToss = false;
    private boolean teamABatFirst = false;
    private boolean teamBToss = false;
    private boolean teamBBatFirst = false;
    private String tossDecision = "";
    private ArrayList<String> matchPlayers = new ArrayList<>();
    private int maxOvers;
    private String strikerBatsman = "";
    private String nonStrikeBatsman = "";
    private String currentBowler = "";
    private boolean completed = false;

    public Match(Team team1, Team team2) {
        this.teamA = team1;
        this.teamB = team2;
        this.id = UUID.randomUUID().toString();
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
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(temp);
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

    public String getCurrentBowler() {
        return currentBowler;
    }

    public void setCurrentBowler(String currentBowler) {
        this.currentBowler = currentBowler;
    }

    public String getStrikerBatsman() {
        return strikerBatsman;
    }

    public void setStrikerBatsman(String strikerBatsman) {
        this.strikerBatsman = strikerBatsman;
    }

    public String getNonStrikeBatsman() {
        return nonStrikeBatsman;
    }

    public void setNonStrikeBatsman(String nonStrikeBatsman) {
        this.nonStrikeBatsman = nonStrikeBatsman;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDataFileName(String dataFileName) {
        return  dataFileName + "/" + "match_" + this.date
                + "_" + this.id
                + ".dat";
    }

    public boolean isEqual(Match match2) {
        return this.date.equals(match2.getDate())
                && Utils.equateArrayList(this.matchPlayers, match2.getMatchPlayers())
                && this.teamA.getCaptainName().equals(match2.getTeamA().getCaptainName())
                && this.teamB.getCaptainName().equals(match2.getTeamB().getCaptainName())
                && Utils.equateArrayList(this.teamA.getPlayerNames(), match2.getTeamA().getPlayerNames())
                && Utils.equateArrayList(this.teamB.getPlayerNames(), match2.getTeamB().getPlayerNames())
                && this.maxOvers == match2.maxOvers;
    }

    @NonNull
    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", teamA=" + teamA +
                ", teamB=" + teamB +
                ", date='" + date + '\'' +
                ", teamAToss=" + teamAToss +
                ", teamABatFirst=" + teamABatFirst +
                ", teamBToss=" + teamBToss +
                ", teamBBatFirst=" + teamBBatFirst +
                ", tossDecision='" + tossDecision + '\'' +
                ", matchPlayers=" + matchPlayers +
                ", maxOvers=" + maxOvers +
                ", strikerBatsman='" + strikerBatsman + '\'' +
                ", nonStrikeBatsman='" + nonStrikeBatsman + '\'' +
                ", currentBowler='" + currentBowler + '\'' +
                ", completed=" + completed +
                '}';
    }
}
