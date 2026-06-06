package com.prashik.scorer.models;

import java.io.Serializable;

public class Team implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    private String name = "";

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
