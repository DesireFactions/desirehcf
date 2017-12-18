package com.desiremc.hcf.session.faction;

import org.bukkit.ChatColor;

import com.desiremc.core.utils.StringUtils;

public enum FactionRelationship
{

    ALLY(ChatColor.LIGHT_PURPLE),
    NEUTRAL(ChatColor.YELLOW),
    ENEMY(ChatColor.RED),
    MEMBER(ChatColor.GREEN);

    private ChatColor chatColor;

    private FactionRelationship(ChatColor chatColor)
    {
        this.chatColor = chatColor;
    }

    /**
     * @return the color representation of this relationship.
     */
    public ChatColor getChatColor()
    {
        return chatColor;
    }

    /**
     * @return {@code true} if the relationship is {@link #NEUTRAL} or {@link #ENEMY}.
     */
    public boolean canAttack()
    {
        return this == ENEMY || this == NEUTRAL;
    }
    
    public boolean canUseRedstone()
    {
        return this == MEMBER || this == ALLY;
    }
    
    public boolean canUseChests()
    {
        return this == MEMBER;
    }
    
    public boolean canBuild()
    {
        return this == MEMBER;
    }

    @Override
    public String toString()
    {
        return StringUtils.capitalize(name().toLowerCase());
    }
    
}
