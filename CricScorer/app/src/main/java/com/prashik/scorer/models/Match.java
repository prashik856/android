package com.prashik.scorer.models;

import java.io.Serializable;

public class Match implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    Team teamA;
    Team teamB;

    public Match() {
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
}
