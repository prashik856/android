package com.prashik.scorer.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MatchStats implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private String playerId;
    private int matchesPlayed = 0;
    private int catches = 0;

    private int runOuts = 0;

    public MatchStats(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getCatches() {
        return catches;
    }

    public void setCatches(int catches) {
        this.catches = catches;
    }

    @SuppressLint("DefaultLocale")
    public String toJson() {
        return "{\"playerId\":\"" + playerId + "\",\"matchesPlayed\":" + matchesPlayed + ",\"catches\":" + catches + "}";
    }


    public int getRunOuts() {
        return runOuts;
    }

    public void setRunOuts(int runOuts) {
        this.runOuts = runOuts;
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchStats{" +
                "playerId='" + playerId + '\'' +
                ", matchesPlayed=" + matchesPlayed +
                ", catches=" + catches +
                ", runOuts=" + runOuts +
                '}';
    }
}
