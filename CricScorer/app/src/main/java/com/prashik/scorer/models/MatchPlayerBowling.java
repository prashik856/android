package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchPlayerBowling implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    // Bowling
    private boolean bowled = false;
    private int wicketsTaken = 0;
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
    private int extrasConceded = 0;
    private int runsConceded = 0;
    private int legalDeliveriesBowled = 0;
    private int deliveriesBowled = 0;
    ArrayList<String> bowledPlayers = new ArrayList<>();
    ArrayList<String> wicketsTakenPlayers = new ArrayList<>();

    public MatchPlayerBowling() {

    }

    public boolean isBowled() {
        return bowled;
    }

    public void setBowled(boolean bowled) {
        this.bowled = bowled;
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
    public void incrementTwoFer() {
        this.twoFer = this.twoFer + 1;
    }
    public void decrementTwoFer() {
        if(this.twoFer > 0) {
            this.twoFer = this.twoFer - 1;
        }
    }

    public void setTwoFer(int twoFer) {
        this.twoFer = twoFer;
    }

    public int getThreeFer() {
        return threeFer;
    }
    public void incrementThreeFer() {
        this.threeFer = this.threeFer + 1;
    }
    public void decrementThreeFer() {
        if(this.threeFer > 0) {
            this.threeFer = this.threeFer - 1;
        }
    }

    public void setThreeFer(int threeFer) {
        this.threeFer = threeFer;
    }

    public int getFiveFer() {
        return fiveFer;
    }
    public void incrementFiveFer() {
        this.fiveFer = this.fiveFer + 1;
    }

    public void setFiveFer(int fiveFer) {
        this.fiveFer = fiveFer;
    }

    public int getFoursConceded() {
        return foursConceded;
    }
    public void incrementFoursConceded() {
        this.foursConceded = this.foursConceded + 1;
    }

    public void setFoursConceded(int foursConceded) {
        this.foursConceded = foursConceded;
    }

    public int getDotsConceded() {
        return dotsConceded;
    }
    public void incrementDotsConceded() {
        this.dotsConceded = this.dotsConceded + 1;
    }

    public void setDotsConceded(int dotsConceded) {
        this.dotsConceded = dotsConceded;
    }

    public int getSixesConceded() {
        return sixesConceded;
    }
    public void incrementSixesConceded() {
        this.sixesConceded = this.sixesConceded + 1;
    }

    public void setSixesConceded(int sixesConceded) {
        this.sixesConceded = sixesConceded;
    }

    public int getWideBalls() {
        return wideBalls;
    }
    public void incrementWides() {
        this.wideBalls = this.wideBalls + 1;
    }

    public void setWideBalls(int wideBalls) {
        this.wideBalls = wideBalls;
    }

    public int getNoBalls() {
        return noBalls;
    }
    public void incrementNoBalls() {
        this.noBalls = this.noBalls + 1;
    }

    public void setNoBalls(int noBalls) {
        this.noBalls = noBalls;
    }

    public int getNoOfOvers() {
        return noOfOvers;
    }
    public void incrementNoOfOvers() {
        this.noOfOvers = this.noOfOvers + 1;
    }

    public void setNoOfOvers(int noOfOvers) {
        this.noOfOvers = noOfOvers;
    }

    public int getRunsConceded() {
        return runsConceded;
    }

    public void setRunsConceded(int runsConceded) {
        this.runsConceded = runsConceded;
    }

    public int getWicketsTaken() {
        return wicketsTaken;
    }
    public void incrementWicketsTaken() {
        this.wicketsTaken = this.wicketsTaken + 1;
    }

    public void setWicketsTaken(int wicketsTaken) {
        this.wicketsTaken = wicketsTaken;
    }

    public int getExtrasConceded() {
        return extrasConceded;
    }

    public void setExtrasConceded(int extrasConceded) {
        this.extrasConceded = extrasConceded;
    }

    public int getLegalDeliveriesBowled() {
        return legalDeliveriesBowled;
    }

    public void incrementLegalDeliveriesBowled() {
        this.legalDeliveriesBowled = this.legalDeliveriesBowled + 1;
    }

    public void setLegalDeliveriesBowled(int legalDeliveriesBowled) {
        this.legalDeliveriesBowled = legalDeliveriesBowled;
    }

    public int getDeliveriesBowled() {
        return deliveriesBowled;
    }

    public void incrementDeliveriesBowled() {
        this.deliveriesBowled = this.deliveriesBowled + 1;
    }

    public void setDeliveriesBowled(int deliveriesBowled) {
        this.deliveriesBowled = deliveriesBowled;
    }

    public ArrayList<String> getBowledPlayers() {
        return bowledPlayers;
    }

    public void setBowledPlayers(ArrayList<String> bowledPlayers) {
        this.bowledPlayers = bowledPlayers;
    }

    public ArrayList<String> getWicketsTakenPlayers() {
        return wicketsTakenPlayers;
    }

    public void setWicketsTakenPlayers(ArrayList<String> wicketsTakenPlayers) {
        this.wicketsTakenPlayers = wicketsTakenPlayers;
    }

    public void updateBowlingEconomy() {
        if(!(this.legalDeliveriesBowled == 0)) {
            this.noOfOvers = this.legalDeliveriesBowled / 6;
            int ballsInNewOver = this.legalDeliveriesBowled % 6;
            double denominator = this.noOfOvers +  ((double)ballsInNewOver/6);
            this.economy = this.runsConceded/denominator;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchPlayerBowling{" +
                "bowled=" + bowled +
                ", wicketsTaken=" + wicketsTaken +
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
                ", extrasConceded=" + extrasConceded +
                ", runsConceded=" + runsConceded +
                ", legalDeliveriesBowled=" + legalDeliveriesBowled +
                ", deliveriesBowled=" + deliveriesBowled +
                ", bowledPlayers=" + bowledPlayers +
                ", wicketsTakenPlayers=" + wicketsTakenPlayers +
                '}';
    }
}
