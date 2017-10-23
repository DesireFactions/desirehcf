package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.PVPClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BardListener implements Listener
{
    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if(!session.getPvpClass().equals(PVPClass.BARD)) return;

        switch (p.getInventory().getItemInMainHand().getType())
        {
            case BLAZE_ROD:

                break;
            case GHAST_TEAR:

                break;
            case MAGMA_CREAM:

                break;
            case SUGAR:

                break;
        }
    }
}
