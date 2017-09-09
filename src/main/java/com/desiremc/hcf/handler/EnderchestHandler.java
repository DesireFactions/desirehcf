package com.desiremc.hcf.handler;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.desiremc.hcf.MscAchievements;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.Utils;

public class EnderchestHandler implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                if (Utils.enderchestDisabled == true) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("EnderChest are disabled!");
                } else {
                    Session s = SessionHandler.getSession(event.getPlayer());
                    if (!s.hasAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN.getId())) {
                        s.awardAchievement(MscAchievements.FIRST_ENDERCHEST_OPEN, true);
                    }

                    event.setCancelled(false);
                }
            }
        }
    }
}
