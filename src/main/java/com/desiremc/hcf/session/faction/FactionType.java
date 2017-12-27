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
        switch (this)
        {
            case SAFEZONE:
                return "§aNon-Deathban";
            case PLAYER:
            case WARZONE:
            case WILDERNESS:
            default:
                return "§cDeathban";
        }
    }

    public static FactionType getFactionType(String raw)
    {
        raw = raw.toLowerCase();
        if (StringUtils.containsAny(raw, "play", "player", "normal"))
        {
            return PLAYER;
        }
        else if (StringUtils.containsAny(raw, "war", "warzone", "pvp"))
        {
            return WARZONE;
        }
        else if (StringUtils.containsAny(raw, "wild", "wilderness"))
        {
            return WILDERNESS;
        }
        else if (StringUtils.containsAny(raw, "safe", "safezone"))
        {
            return FactionType.SAFEZONE;
        }
        return null;
    }
}
