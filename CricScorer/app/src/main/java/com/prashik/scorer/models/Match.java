package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import com.prashik.scorer.util.Utils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
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
    private int strikerBatsmanIndex = -1;
    private String nonStrikeBatsman = "";
    private int nonStrikerBatsmanIndex = -1;
    private String currentBowler = "";
    private int currentBowlerIndex = -1;
    private boolean completed = false;
    private int innings = -1;
    private String battingTeamName = "";
    private String bowlingTeamName = "";

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

    public int getStrikerBatsmanIndex() {
        return strikerBatsmanIndex;
    }

    public void setStrikerBatsmanIndex(int strikerBatsmanIndex) {
        this.strikerBatsmanIndex = strikerBatsmanIndex;
    }

    public int getNonStrikerBatsmanIndex() {
        return nonStrikerBatsmanIndex;
    }

    public void setNonStrikerBatsmanIndex(int nonStrikerBatsmanIndex) {
        this.nonStrikerBatsmanIndex = nonStrikerBatsmanIndex;
    }

    public int getCurrentBowlerIndex() {
        return currentBowlerIndex;
    }

    public void setCurrentBowlerIndex(int currentBowlerIndex) {
        this.currentBowlerIndex = currentBowlerIndex;
    }

    public int getInnings() {
        return innings;
    }

    public void setInnings(int innings) {
        this.innings = innings;
    }

    public String getBattingTeamName() {
        return battingTeamName;
    }

    public void setBattingTeamName(String battingTeamName) {
        this.battingTeamName = battingTeamName;
    }

    public String getBowlingTeamName() {
        return bowlingTeamName;
    }

    public void setBowlingTeamName(String bowlingTeamName) {
        this.bowlingTeamName = bowlingTeamName;
    }

    public void setBattingAndBowlingTeamNames() {
        if(this.getInnings() == 1) {
            // first innings
            if(this.isTeamABatFirst()) {
                // team A is batting
                this.battingTeamName = this.teamA.getName();
                this.bowlingTeamName = this.teamB.getName();
            } else {
                // team B is batting
                this.battingTeamName = this.teamB.getName();
                this.bowlingTeamName = this.teamA.getName();
            }
        } else {
            // second innings
            if(this.isTeamABatFirst()) {
                // team B is batting
                this.battingTeamName = this.teamB.getName();
                this.bowlingTeamName = this.teamA.getName();
            } else {
                // team A is batting
                this.battingTeamName = this.teamA.getName();
                this.bowlingTeamName = this.teamB.getName();
            }
        }
    }

    public ArrayList<Team> getBattingAndBowlingTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        if(this.teamA.getName().equals(this.battingTeamName)) {
            // team a is batting
            teams.add(this.teamA);
            teams.add(this.teamB);
        } else {
            // team b is batting
            teams.add(this.teamB);
            teams.add(this.teamA);
        }
        return teams;
    }

    public Team getBattingTeam() {
        return getBattingAndBowlingTeams().get(0);
    }

    public Team getBowlingTeam() {
        return getBattingAndBowlingTeams().get(1);
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
                ", strikerBatsmanIndex=" + strikerBatsmanIndex +
                ", nonStrikeBatsman='" + nonStrikeBatsman + '\'' +
                ", nonStrikerBatsmanIndex=" + nonStrikerBatsmanIndex +
                ", currentBowler='" + currentBowler + '\'' +
                ", currentBowlerIndex=" + currentBowlerIndex +
                ", completed=" + completed +
                ", innings=" + innings +
                '}';
    }
}
