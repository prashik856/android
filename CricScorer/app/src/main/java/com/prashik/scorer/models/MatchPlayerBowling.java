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
    private boolean twoFer = false;
    private boolean threeFer = false;
    private boolean fiveFer = false;
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
    private int maidenOverBowled = 0;
    ArrayList<Integer> overBowled = new ArrayList<>();
    ArrayList<String> bowledPlayers = new ArrayList<>();
    ArrayList<String> wicketsTakenPlayers = new ArrayList<>();
    ArrayList<String> maidenBowledTo = new ArrayList<>();
    ArrayList<String> bowlingDetails = new ArrayList<>();

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

    public boolean isTwoFer() {
        return twoFer;
    }

    public void setTwoFer(boolean twoFer) {
        this.twoFer = twoFer;
    }

    public boolean isThreeFer() {
        return threeFer;
    }

    public void setThreeFer(boolean threeFer) {
        this.threeFer = threeFer;
    }

    public boolean isFiveFer() {
        return fiveFer;
    }

    public void setFiveFer(boolean fiveFer) {
        this.fiveFer = fiveFer;
    }

    public int getFoursConceded() {
        return foursConceded;
    }
    public void incrementFoursConceded() {
        this.foursConceded = this.foursConceded + 1;
    }

    public void decrementFoursConceded() {
        this.foursConceded = this.foursConceded - 1;
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

    public void decrementDotsConceded() {
        this.dotsConceded = this.dotsConceded - 1;
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
    public void decrementSixesConceded() {
        this.sixesConceded = this.sixesConceded - 1;
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
    public void incrementRunsConceded() {
        this.runsConceded = this.runsConceded + 1;
    }
    public void decrementRunsConceded() {
        this.runsConceded = this.runsConceded - 1;
    }
    public void addTwoToRunsConceded() {
        this.runsConceded = this.runsConceded + 2;
    }
    public void decrementTwoFromRunsConceded() {
        this.runsConceded = this.runsConceded - 2;
    }
    public void addThreeToRunsConceded() {
        this.runsConceded = this.runsConceded + 3;
    }
    public void decrementThreeFromRunsConceded() {
        this.runsConceded = this.runsConceded - 3;
    }
    public void addFourToRunsConceded() {
        this.runsConceded = this.runsConceded + 4;
    }
    public void decrementFourFromRunsConceded() {
        this.runsConceded = this.runsConceded - 4;
    }
    public void addFiveToRunsConceded() {
        this.runsConceded = this.runsConceded + 5;
    }
    public void decrementFiveFromRunsConceded() {
        this.runsConceded = this.runsConceded - 5;
    }
    public void addSixToRunsConceded() {
        this.runsConceded = this.runsConceded + 6;
    }
    public void decrementSixFromRunsConceded() {
        this.runsConceded = this.runsConceded - 6;
    }
    public void addSevenToRunsConceded() {
        // no ball + 6
        this.runsConceded = this.runsConceded + 7;
    }
    public void decrementSevenFromRunsConceded() {
        this.runsConceded = this.runsConceded - 7;
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

    public void decrementWicketsTaken() {
        this.wicketsTaken = this.wicketsTaken - 1;
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

    public void decrementLegalDeliveriesBowled() {
        this.legalDeliveriesBowled = this.legalDeliveriesBowled - 1;
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

    public void decrementDeliveriesBowled() {
        this.deliveriesBowled = this.deliveriesBowled - 1;
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

    public void addToBowledPlayers(String bowledPlayer) {
        this.bowledPlayers.add(bowledPlayer);
    }

    public void removeFromBowledPlayers() {
        int size = this.bowledPlayers.size();
        this.bowledPlayers.remove(size - 1);
    }

    public ArrayList<String> getWicketsTakenPlayers() {
        return wicketsTakenPlayers;
    }

    public void setWicketsTakenPlayers(ArrayList<String> wicketsTakenPlayers) {
        this.wicketsTakenPlayers = wicketsTakenPlayers;
    }

    public void addToWicketsTakenPlayers(String wicketTakenPlayer) {
        this.wicketsTakenPlayers.add(wicketTakenPlayer);
    }

    public void removeFromWicketsTakenPlayers() {
        int size = this.wicketsTakenPlayers.size();
        this.wicketsTakenPlayers.remove(size - 1);
    }

    public int getMaidenOverBowled() {
        return maidenOverBowled;
    }

    public void incrementMaidenOverBowled() {
        this.maidenOverBowled = this.maidenOverBowled + 1;
    }

    public void setMaidenOverBowled(int maidenOverBowled) {
        this.maidenOverBowled = maidenOverBowled;
    }

    public ArrayList<String> getMaidenBowledTo() {
        return maidenBowledTo;
    }

    public void setMaidenBowledTo(ArrayList<String> arr) {
        this.maidenBowledTo = arr;
    }

    public void addToMaidenOverBowledTo(String playerName) {
        this.maidenBowledTo.add(playerName);
    }

    public ArrayList<Integer> getOverBowled() {
        return overBowled;
    }

    public void addToOverBowled(int index) {
        this.overBowled.add(index);
    }

    public void setOverBowled(ArrayList<Integer> overBowled) {
        this.overBowled = overBowled;
    }

    public ArrayList<String> getBowlingDetails() {
        return bowlingDetails;
    }

    public void setBowlingDetails(ArrayList<String> bowlingDetails) {
        this.bowlingDetails = bowlingDetails;
    }

    public void addToBowlingDetails(String bowlDetails) {
        this.bowlingDetails.add(bowlDetails);
    }

    public void removeLastElementOfBowlingDetails() {
        int size = this.bowlingDetails.size();
        this.bowlingDetails.remove(size - 1);
    }

    public void updateBowlingEconomy() {
        if(!(this.legalDeliveriesBowled == 0)) {
            this.noOfOvers = this.legalDeliveriesBowled / 6;
            int ballsInNewOver = this.legalDeliveriesBowled % 6;
            double denominator = this.noOfOvers +  ((double)ballsInNewOver/6);
            this.economy = this.runsConceded/denominator;
        }
    }

    public void updateRecords() {
        if(this.wicketsTaken == 2) {
            this.twoFer = true;
        }

        if(this.wicketsTaken == 3) {
            this.twoFer = false;
            this.threeFer = true;
        }

        if(this.wicketsTaken >= 5) {
            this.fiveFer = true;
            this.twoFer = false;
            this.threeFer = false;
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
