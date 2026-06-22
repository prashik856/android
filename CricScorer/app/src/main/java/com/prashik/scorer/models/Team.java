package com.prashik.scorer.models;

import java.io.Serializable;

public class Team implements Serializable {
    private static final long serialVersionUID = 2462471862401256640L;

    private String name = "";
    private String captainName = "";
    private String commonName = "";

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
}
