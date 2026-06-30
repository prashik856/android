package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchPlayerFielding implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    // Just for stats that we can view
    private int noOfCatches = 0;
    private int noOfRunOuts = 0;

    ArrayList<String> coughtPlayers = new ArrayList<>();
    ArrayList<String> runOutPlayers = new ArrayList<>();

    public MatchPlayerFielding() {}

    public int getNoOfCatches() {
        return noOfCatches;
    }
    public void incrementNoOfCatches() {
        this.noOfCatches = this.noOfCatches + 1;
    }

    public void setNoOfCatches(int noOfCatches) {
        this.noOfCatches = noOfCatches;
    }

    public int getNoOfRunOuts() {
        return noOfRunOuts;
    }
    public void incrementNoOfRunOuts() {
        this.noOfRunOuts = this.noOfRunOuts + 1;
    }

    public void setNoOfRunOuts(int noOfRunOuts) {
        this.noOfRunOuts = noOfRunOuts;
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchPlayerFielding{" +
                "noOfCatches=" + noOfCatches +
                ", noOfRunOuts=" + noOfRunOuts +
                ", coughtPlayers=" + coughtPlayers +
                ", runOutPlayers=" + runOutPlayers +
                '}';
    }
}
