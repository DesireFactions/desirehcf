package com.desiremc.hcf.listener.classes;

import java.util.UUID;

import org.bukkit.event.Listener;

import com.desiremc.core.utils.cache.Cache;

public abstract class DesireClass implements Listener
{

    protected Cache<UUID, Long> cooldown;

    protected abstract void initialize();

    public Cache<UUID, Long> getCooldown()
    {
        return cooldown;
    }

    public void setCooldown(Cache<UUID, Long> cooldown)
    {
        this.cooldown = cooldown;
    }

}
