package com.desiremc.hcf.session.faction;

public enum FactionRank
{

    MEMBER,
    OFFICER,
    LEADER;

    /**
     * @return {@code true} if the player is Officer or Leader.
     */
    public boolean isOfficer()
    {
        return this == FactionRank.OFFICER || this == FactionRank.LEADER;
    }

    /**
     * @return {@code true} if the player is Leader.
     */
    public boolean isLeader()
    {
        return this == FactionRank.LEADER;
    }

}
