package com.prashik.scorer.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Match implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    Team teamA;
    Team teamB;

    String date;

    ArrayList<Player> playingPlayers;

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
    public String getDate() {
        return date;
    }
    public void setDate() {
        Date temp = new Date();
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(temp);
        dateString = dateString + "-" + String.valueOf( new Random().nextInt(1001));
        this.date = dateString;
    }
}
