package me.borawski.hcf.old_gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Session;

public class PlayerInfoGUI extends ItemGUI {

    public static Map<UUID, Session> crossTarget = new HashMap<>();

    public PlayerInfoGUI(Player player) {
        super(null, player, 9);
    }

    @Override
    public String getName() {
        return "Player Info";
    }

    @Override
    public boolean isCloseOnClick() {
        return false;
    }

    @Override
    public void registerItems() {
        Session s1 = crossTarget.get(getPlayer().getUniqueId());
        set(0, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "UUID: " + s1.getUniqueId()), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(1, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "NAME: " + s1.getName()), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(2, new MenuItem(new CustomIS().setMaterial(Material.NAME_TAG).setName(ChatColor.GRAY + "IP: 10.0.0.1 (N/A)"), new Runnable() {
            @Override
            public void run() {

            }
        }));
        set(4, new MenuItem(new CustomIS().setMaterial(Material.FIREWORK_CHARGE).setName(ChatColor.GRAY + "RANK: " + s1.getRank().getPrefix()).addLore(ChatColor.GRAY + "(Click to edit rank)"), new Runnable() {
            @Override
            public void run() {
                new RankChangeGUI(getPlayer(), s1).show();
            }
        }));
        set(5, new MenuItem(new CustomIS().setMaterial(Material.ANVIL).setName(ChatColor.GRAY + "PUNISHMENTS").addLore(ChatColor.GRAY + "(Click to manage their punishments)").addLore(ChatColor.GRAY + "(Coming Soon)"), new Runnable() {
            @Override
            public void run() {
                new PlayerPunishmentsGUI(getPlayer(), s1).show();
            }
        }));
    }
}
