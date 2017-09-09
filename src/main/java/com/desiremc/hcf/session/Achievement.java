package com.desiremc.hcf.session;

public class Achievement {

    private String id;
    private String name;
    private String desc;
    private int reward;

    public Achievement(String id, String name, String desc, int reward) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.reward = reward;
    }

    /*
     * Getters
     */

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getReward() {
        return this.reward;
    }

}
