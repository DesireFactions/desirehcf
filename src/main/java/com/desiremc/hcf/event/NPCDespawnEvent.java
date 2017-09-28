package com.desiremc.hcf.event;

import com.desiremc.hcf.npc.NPC;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCDespawnEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final NPC NPC;
    private final NPCDespawnReason reason;

    public NPCDespawnEvent(NPC NPC, NPCDespawnReason reason) {
        this.NPC = NPC;
        this.reason = reason;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public NPC getNPC() {
        return NPC;
    }

    public NPCDespawnReason getDespawnReason(){
        return reason;
    }
}
