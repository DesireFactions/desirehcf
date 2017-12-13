package com.desiremc.hcf.session.faction;

public enum FactionRelationship
{

    ALLY,
    NEUTRAL,
    ENEMY,
    MEMBER;

    /**
     * @return {@code true} if the relationship is {@link #NEUTRAL} or {@link #ENEMY}.
     */
    public boolean canDamage()
    {
        return this == ENEMY || this == NEUTRAL;
    }

}
