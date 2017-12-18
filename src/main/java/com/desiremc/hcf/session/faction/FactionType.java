package com.desiremc.hcf.session.faction;

import com.desiremc.core.utils.StringUtils;

public enum FactionType
{
    PLAYER,
    WARZONE,
    WILDERNESS,
    SAFEZONE;

    /**
     * @return {@code true} if it is not a {@link #PLAYER} faction.
     */
    public boolean isSystem()
    {
        return this != PLAYER;
    }

    @Override
    public String toString()
    {
        return StringUtils.capitalize(name().toLowerCase());
    }
}
