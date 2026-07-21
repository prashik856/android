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
    private long startTime;
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
    private String result = "";

    private ArrayList<String> activities = new ArrayList<>();

    public Match(Team team1, Team team2) {
        this.teamA = team1;
        this.teamB = team2;
        this.id = UUID.randomUUID().toString();
        setCurrentDate();
        setCurrentTime();
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
    public void setCurrentDate() {
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

    public void addToMatchPlayers(String matchPlayer) {
        if(!this.matchPlayers.contains(matchPlayer)) {
            System.out.println("New player " + matchPlayer + " not present in existing match players.");
            this.matchPlayers.add(matchPlayer);
        } else {
            System.out.println("New player " + matchPlayer + " already exists in the list of match players.");
        }
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setCurrentTime() {
        this.setStartTime(new Date().getTime());
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

    public void addToActivities(String activity) {
        this.activities.add(activity);
    }
    public void removeLastElementFromActivities() {
        int size = this.activities.size();
        this.activities.remove(size - 1);
    }

    public void updateResult() {
        if(this.isCompleted()) {
            // tied
            if(this.teamA.getRuns() == this.teamB.getRuns()) {
                this.result = "Match Tied";
            }

            // team a bat first
            if(this.teamABatFirst) {
                if(this.teamA.getRuns() > this.teamB.getRuns()) {
                    // team a won
                    int runDiff = this.teamA.getRuns() - this.teamB.getRuns();
                    this.result = String.format("%s with captain %s won by %d runs.",
                            this.teamA.getName(), this.teamA.getCaptainName(), runDiff);
                }
                else {
                    // team b won
                    int wicketsDiff = this.teamB.getMaxWickets() - this.teamB.getWickets();
                    this.result = String.format("%s with captain %s won by %d wickets.",
                            this.teamB.getName(), this.teamB.getCaptainName(), wicketsDiff);
                }
            } else {
                // team b bat first
                if(this.teamB.getRuns() > this.teamA.getRuns()) {
                    // team b won
                    int runDiff = this.teamB.getRuns() - this.teamA.getRuns();
                    this.result = String.format("%s with captain %s won by %d runs.",
                            this.teamB.getName(), this.teamB.getCaptainName(), runDiff);
                } else {
                    // team a won
                    int wicketsDiff = this.teamA.getMaxWickets() - this.teamA.getWickets();
                    this.result = String.format("%s with captain %s won by %d wickets.",
                            this.teamA.getName(), this.teamA.getCaptainName(), wicketsDiff);
                }
            }
        }
    }

    public void setBattingAndBowlingTeamNames() {
        if(this.getInnings() == 1) {
            // first innings
            System.out.println("This is first innings.");
            if(this.isTeamABatFirst()) {
                // team A is batting
                System.out.println("Team A batting and Team B bowling.");
                this.battingTeamName = this.teamA.getName();
                this.bowlingTeamName = this.teamB.getName();
            } else {
                // team B is batting
                System.out.println("Team B batting and Team A bowling.");
                this.battingTeamName = this.teamB.getName();
                this.bowlingTeamName = this.teamA.getName();
            }
        } else {
            // second innings
            System.out.println("This is second innings.");
            if(this.isTeamABatFirst()) {
                // team B is batting
                System.out.println("Team B batting and Team A bowling.");
                this.battingTeamName = this.teamB.getName();
                this.bowlingTeamName = this.teamA.getName();
            } else {
                // team A is batting
                System.out.println("Team A batting and Team B bowling.");
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

    public void updateMatchCompleted() {
        this.completed = false;
        if(this.innings == 2) {
            // runs scored
            if(this.getBattingTeam().getRuns() > this.getBowlingTeam().getRuns()) {
                // match is completed
                this.completed = true;
            }

            // if overs are completed
            if(this.getBattingTeam().getCurrentOverBatting() == this.getBattingTeam().getMaxOvers()) {
                this.completed = true;
            }

            // batting team all out
            if(this.getBattingTeam().isBattingInningsCompleted()) {
                this.completed = true;
            }
        }
    }

    public MatchPlayer getMatchPlayerObject(String playerName) {
        int index1 = this.getBattingTeam().getMatchPlayerIndex(playerName);
        int index2 = this.getBowlingTeam().getMatchPlayerIndex(playerName);
        if(index1 != -1) {
            // batting team player
            return this.getBattingTeam().getTeamPlayers().get(index1);
        }
        if(index2 != -1) {
            // bowling team player
            return this.getBowlingTeam().getTeamPlayers().get(index2);
        }
        return null;
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
