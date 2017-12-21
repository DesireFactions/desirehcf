package com.desiremc.hcf.session.faction;

public enum FactionRank
{

    MEMBER,
    OFFICER,
    COLEADER,
    LEADER;

    /**
     * @return {@code true} if the player is Officer or Leader.
     */
    public boolean isOfficer()
    {
        return this == FactionRank.OFFICER || this == FactionRank.LEADER || this == FactionRank.COLEADER;
    }

    /**
     * @return {@code true} if the player is Coleader.
     */
    public boolean isColeader()
    {
        return this == FactionRank.COLEADER;
    }

    /**
     * @return {@code true} if the player is Leader.
     */
    public boolean isLeader()
    {
        return this == FactionRank.LEADER;
    }

    public static FactionRank getNextRank(FactionRank rank)
    {
        if (rank == FactionRank.MEMBER)
        {
            return FactionRank.OFFICER;
        }
        else if (rank == FactionRank.OFFICER)
        {
            return FactionRank.COLEADER;
        }
        else if (rank == FactionRank.COLEADER)
        {
            return FactionRank.LEADER;
        }
        else
        {
            return null;
        }
    }

}
