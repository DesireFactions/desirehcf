package com.desiremc.hcf.npc;

import org.bukkit.entity.Player;

public class NPC
{
    private final NPCIdentity identity;

    private final Player entity;

    public NPC(Player entity)
    {
        this.identity = NPCPlayerHelper.getIdentity(entity);
        this.entity = entity;
    }

    public NPCIdentity getIdentity()
    {
        return identity;
    }

    public Player getEntity()
    {
        return entity;
    }
}
