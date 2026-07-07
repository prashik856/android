package com.prashik.scorer.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class MatchPlayer implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;
    private Player player;
    private String playerName;

    private MatchPlayerBatting matchPlayerBatting;
    private MatchPlayerBowling matchPlayerBowling;
    private MatchPlayerFielding matchPlayerFielding;

    public MatchPlayer(Player player) {
        this.player = player;
        this.playerName = this.player.getFullName();
        this.matchPlayerBatting = new MatchPlayerBatting();
        this.matchPlayerBowling = new MatchPlayerBowling();
        this.matchPlayerFielding = new MatchPlayerFielding();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public MatchPlayerBatting getMatchPlayerBatting() {
        return matchPlayerBatting;
    }

    public void setMatchPlayerBatting(MatchPlayerBatting matchPlayerBatting) {
        this.matchPlayerBatting = matchPlayerBatting;
    }

    public MatchPlayerBowling getMatchPlayerBowling() {
        return matchPlayerBowling;
    }

    public void setMatchPlayerBowling(MatchPlayerBowling matchPlayerBowling) {
        this.matchPlayerBowling = matchPlayerBowling;
    }

    public MatchPlayerFielding getMatchPlayerFielding() {
        return matchPlayerFielding;
    }

    public void setMatchPlayerFielding(MatchPlayerFielding matchPlayerFielding) {
        this.matchPlayerFielding = matchPlayerFielding;
    }

    @NonNull
    @Override
    public String toString() {
        return "MatchPlayer{" +
                "player=" + player +
                ", playerName='" + playerName + '\'' +
                ", matchPlayerBatting=" + matchPlayerBatting +
                ", matchPlayerBowling=" + matchPlayerBowling +
                ", matchPlayerFielding=" + matchPlayerFielding +
                '}';
    }
}
