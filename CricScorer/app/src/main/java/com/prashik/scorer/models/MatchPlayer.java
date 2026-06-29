package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MatchPlayer implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private Player player;
    private String playerName;
    // Batting
    private boolean batted = false;
    private int runs = 0;
    private double strikeRate = 0;
    private int fours = 0;
    private int sixes = 0;
    private int dots = 0;
    private boolean twenty = false;
    private boolean thirty = false;
    private boolean fifty = false;

    // Bowling
    private boolean bowled = false;
    private int wickets = 0;
    private double economy = 0;
    private int twoFer = 0;
    private int threeFer = 0;
    private int fiveFer = 0;
    private int foursConceded = 0;
    private int dotsConceded = 0;
    private int sixesConceded = 0;
    private int wideBalls = 0;
    private int noBalls = 0;
    private int noOfOvers = 0;
    // Just for stats that we can view
    private int noOfCatches = 0;
    private int noOfRunOuts = 0;
    private boolean out = false;

    public MatchPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isBatted() {
        return batted;
    }

    public void setBatted(boolean batted) {
        this.batted = batted;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public void setStrikeRate(double strikeRate) {
        this.strikeRate = strikeRate;
    }

    public int getFours() {
        return fours;
    }

    public void setFours(int fours) {
        this.fours = fours;
    }

    public int getSixes() {
        return sixes;
    }

    public void setSixes(int sixes) {
        this.sixes = sixes;
    }

    public int getDots() {
        return dots;
    }

    public void setDots(int dots) {
        this.dots = dots;
    }

    public boolean isTwenty() {
        return twenty;
    }

    public void setTwenty(boolean twenty) {
        this.twenty = twenty;
    }

    public boolean isThirty() {
        return thirty;
    }

    public void setThirty(boolean thirty) {
        this.thirty = thirty;
    }

    public boolean isFifty() {
        return fifty;
    }

    public void setFifty(boolean fifty) {
        this.fifty = fifty;
    }

    public boolean isBowled() {
        return bowled;
    }

    public void setBowled(boolean bowled) {
        this.bowled = bowled;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public double getEconomy() {
        return economy;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    public int getTwoFer() {
        return twoFer;
    }

    public void setTwoFer(int twoFer) {
        this.twoFer = twoFer;
    }

    public int getThreeFer() {
        return threeFer;
    }

    public void setThreeFer(int threeFer) {
        this.threeFer = threeFer;
    }

    public int getFiveFer() {
        return fiveFer;
    }

    public void setFiveFer(int fiveFer) {
        this.fiveFer = fiveFer;
    }

    public int getFoursConceded() {
        return foursConceded;
    }

    public void setFoursConceded(int foursConceded) {
        this.foursConceded = foursConceded;
    }

    public int getDotsConceded() {
        return dotsConceded;
    }

    public void setDotsConceded(int dotsConceded) {
        this.dotsConceded = dotsConceded;
    }

    public int getSixesConceded() {
        return sixesConceded;
    }

    public void setSixesConceded(int sixesConceded) {
        this.sixesConceded = sixesConceded;
    }

    public int getWideBalls() {
        return wideBalls;
    }

    public void setWideBalls(int wideBalls) {
        this.wideBalls = wideBalls;
    }

    public int getNoBalls() {
        return noBalls;
    }

    public void setNoBalls(int noBalls) {
        this.noBalls = noBalls;
    }

    public int getNoOfOvers() {
        return noOfOvers;
    }

    public void setNoOfOvers(int noOfOvers) {
        this.noOfOvers = noOfOvers;
    }

    public int getNoOfCatches() {
        return noOfCatches;
    }

    public void setNoOfCatches(int noOfCatches) {
        this.noOfCatches = noOfCatches;
    }

    public int getNoOfRunOuts() {
        return noOfRunOuts;
    }

    public void setNoOfRunOuts(int noOfRunOuts) {
        this.noOfRunOuts = noOfRunOuts;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchPlayer{" +
                "player=" + player +
                ", playerName='" + playerName + '\'' +
                ", batted=" + batted +
                ", runs=" + runs +
                ", strikeRate=" + strikeRate +
                ", fours=" + fours +
                ", sixes=" + sixes +
                ", dots=" + dots +
                ", twenty=" + twenty +
                ", thirty=" + thirty +
                ", fifty=" + fifty +
                ", bowled=" + bowled +
                ", wickets=" + wickets +
                ", economy=" + economy +
                ", twoFer=" + twoFer +
                ", threeFer=" + threeFer +
                ", fiveFer=" + fiveFer +
                ", foursConceded=" + foursConceded +
                ", dotsConceded=" + dotsConceded +
                ", sixesConceded=" + sixesConceded +
                ", wideBalls=" + wideBalls +
                ", noBalls=" + noBalls +
                ", noOfOvers=" + noOfOvers +
                ", noOfCatches=" + noOfCatches +
                ", noOfRunOuts=" + noOfRunOuts +
                ", out=" + out +
                '}';
    }
}
