package com.desiremc.hcf.npc;

import org.bukkit.entity.Player;

import java.util.UUID;

public class NPCIdentity
{

    private final UUID id;

    private final String name;

    public NPCIdentity(UUID id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public NPCIdentity(Player player)
    {
        this(player.getUniqueId(), player.getName());
    }

    public UUID getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

}
