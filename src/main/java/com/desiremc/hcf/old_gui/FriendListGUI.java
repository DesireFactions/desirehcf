package com.desiremc.hcf.old_gui;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.ChatUtils;

public class FriendListGUI extends ItemGUI {

    public FriendListGUI(Core instance, Player p) {
        super(null, p, 54);
    }

    @Override
    public String getName() {
        return "Friend List";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        Session session = SessionHandler.getSession(getPlayer());
        int i = 0;
        for (UUID uuid : session.getFriends()) {
            String name = ChatUtils.getNameWithRankColor(uuid, true);
            String status = Core.getInstance().getServer().getPlayer(uuid) == null ? ChatColor.RED + "[OFFLINE]" : ChatColor.GREEN + "[ONLINE]";
            set(i, new MenuItem(new CustomIS().setMaterial(Material.SKULL)
                    .setName(name)
                    .addLore(status), new Runnable() {
                        @Override
                        public void run() {

                        }
                    }));
            i++;
        }
    }
}
