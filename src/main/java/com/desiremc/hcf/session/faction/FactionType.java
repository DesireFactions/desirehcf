package com.desiremc.hcf.session.faction;

public enum FactionType
{
    PLAYER,
    WARZONE,
    SAFEZONE;

    /**
     * @return {@code true} if it is not a {@link #PLAYER} faction.
     */
    public boolean isSystem()
    {
        return this != PLAYER;
    }
}
