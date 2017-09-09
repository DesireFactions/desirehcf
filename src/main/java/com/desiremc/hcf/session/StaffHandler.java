package com.desiremc.hcf.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.api.FileHandler;
import com.desiremc.hcf.api.LangHandler;
import com.desiremc.hcf.thread.staff.ClicksPerSecondThread;
import com.desiremc.hcf.thread.staff.MovementFreezeThread;

public class StaffHandler {

    private static StaffHandler instance;
    private HashMap<UUID, ItemStack[]> inventories;
    private HashMap<UUID, Integer> cpsTests;
    private HashMap<UUID, Boolean> frozenPlayers;
    private List<UUID> hiddenPlayers;
    private int numCPSTests;

    private static final LangHandler LANG = Core.getLangHandler();

    public StaffHandler() {
        inventories = new HashMap<UUID, ItemStack[]>();
        cpsTests = new HashMap<UUID, Integer>();
        hiddenPlayers = new LinkedList<UUID>();
        frozenPlayers = new HashMap<UUID, Boolean>();
        numCPSTests = 0;
    }

    public static void initialize() {
        instance = new StaffHandler();
    }

    public static StaffHandler getInstance() {
        return instance;
    }

    public boolean inStaffMode(Player p) {
        return inventories.containsKey(p.getUniqueId());
    }

    public boolean runningCPSTests() {
        return numCPSTests > 0;
    }

    public HashMap<UUID, Integer> getCPS() {
        return cpsTests;
    }

    public void handleCPSTest(PlayerInteractEvent e) {
        CPSTest(e.getPlayer());
    }

    public boolean CPSTest(Player p) {
        Integer cps = cpsTests.get(p.getUniqueId());

        if (cps != null) {
            cpsTests.put(p.getUniqueId(), Integer.valueOf(cps.intValue() + 1));
            return true;
        }

        return false;
    }

    public void decreaseNumCPSTests() {
        numCPSTests--;
    }

    public void enableStaffMode(Player p) {
        inventories.put(p.getUniqueId(), p.getInventory().getContents());
        p.getInventory().clear();
        p.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
        p.getInventory().addItem(new ItemStack(Material.EYE_OF_ENDER, 1));
        p.getInventory().addItem(new ItemStack(Material.CLAY, 1));
        p.getInventory().addItem(new ItemStack(Material.PAPER, 1));
        p.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
        p.getInventory().addItem(new ItemStack(Material.WATCH, 1));
        p.getInventory().addItem(new ItemStack(Material.CHEST, 1));
        p.getInventory().addItem(new ItemStack(Material.LEASH, 1));
        LANG.sendString(p, "staff.in_staff_mode");

    }

    public void disableStaffMode(Player p) {
        if (inStaffMode(p)) {
            p.getInventory().setContents(inventories.get(p.getUniqueId()));
            inventories.remove(p.getUniqueId());
            LANG.sendString(p, "staff.not_in_staff_mode");
        }
    }

    public boolean toggleStaffMode(Player p) {
        if (inStaffMode(p)) {
            disableStaffMode(p);
            return false;
        }

        enableStaffMode(p);
        return true;
    }

    public boolean isFrozen(Player p) {
        Boolean frozen = frozenPlayers.get(p.getUniqueId());

        if (frozen == null) return false;

        return frozen.booleanValue();
    }

    public void removeIsFrozenObject(Player p) {
        frozenPlayers.remove(p.getUniqueId());
    }

    public void unfreezePlayer(Player player) {
        if (isFrozen(player)) {
            frozenPlayers.put(player.getUniqueId(), Boolean.valueOf(false));
        }
    }

    public boolean toggleFreeze(Player p) {
        if (isFrozen(p)) {
            frozenPlayers.put(p.getUniqueId(), Boolean.valueOf(false));
            return false;
        }

        frozenPlayers.put(p.getUniqueId(), Boolean.valueOf(true));
        MovementFreezeThread mft = new MovementFreezeThread(this, p);
        mft.start();

        return true;
    }

    public void playerInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
        if (e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                Material type = e.getPlayer().getInventory().getItemInMainHand().getType();
                switch (type) {
                case WATCH:
                    useClicksPerSecond(e);
                    break;
                case LEASH:
                    useMount(e);
                    break;
                case BLAZE_ROD:
                    useFreeze(e);
                    break;
                case CHEST:
                    useOpenInventory(e);
                    break;
                default:
                    return;
                }
            }
        }
    }

    public void playerInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
        if (e.getItem() != null) {
            Material type = e.getItem().getType();
            switch (type) {
            case COMPASS:
                useLaunch(e);
                break;
            case EYE_OF_ENDER:
                useTeleport(e);
                break;
            case CLAY:
                useInvisibility(e);
                break;
            default:
                return;
            }
        }
    }

    public void useLaunch(PlayerInteractEvent e) {
        Player staffPlayer = e.getPlayer();
        FileHandler c = Core.getConfigHandler();
        double launchVelocity = c.getDouble("staff.launch_velocity");
        System.out.println(launchVelocity);
        Vector cameraVector = staffPlayer.getLocation().getDirection().normalize();

        staffPlayer.setVelocity(cameraVector.multiply(launchVelocity));
    }

    public void useTeleport(PlayerInteractEvent e) {
        Player staffPlayer = e.getPlayer();
        Collection<? extends Player> playerCollection = Core.getInstance().getServer().getOnlinePlayers();
        List<Player> players = new ArrayList<Player>(playerCollection);

        // remove staff player to avoid teleporting to yourself
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == staffPlayer) {
                players.remove(i);
                break;
            }
        }

        if (players.size() == 0) {
            return;
        }

        Player randomPlayer = players.get(new Random().nextInt(players.size()));
        staffPlayer.teleport(randomPlayer);
    }

    public void useInvisibility(PlayerInteractEvent e) {
        toggleInvisibility(e.getPlayer());
    }

    public void toggleInvisibility(Player player) {
        int index = hiddenPlayers.indexOf(player.getUniqueId());

        if (index == -1) {
            hidePlayer(player);
            hiddenPlayers.add(player.getUniqueId());
            LANG.sendString(player, "staff.set-invisible");
        } else {
            showPlayer(player);
            hiddenPlayers.remove(index);
            LANG.sendString(player, "staff.set-visible");
        }
    }

    public void useClicksPerSecond(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            startCPSTestForPlayer((Player) e.getRightClicked());
        }
    }

    public void startCPSTestForPlayer(Player player) {
        UUID playerID = player.getUniqueId();

        if (cpsTests.containsKey(playerID))
            return;

        cpsTests.put(playerID, Integer.valueOf(0));
        ClicksPerSecondThread newThread = new ClicksPerSecondThread(this, player);
        numCPSTests++;
        newThread.start();
    }

    public void useFreeze(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            Player p = (Player) e.getRightClicked();
            toggleFreeze(p);
        }
    }

    public void useMount(PlayerInteractEntityEvent e) {
        mount(e.getPlayer(), (Player) e.getRightClicked());
    }

    public void mount(Player passenger, Player target) {
        if (!target.isDead()) {
            target.addPassenger(passenger);
        }
    }

    private void useOpenInventory(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            e.getPlayer().openInventory(((Player) e).getInventory());
        }
    }

    private void showPlayer(Player p) {
        Server server = Core.getInstance().getServer();
        for (Player player : server.getOnlinePlayers()) {
            player.showPlayer(p);
        }
    }

    private void hidePlayer(Player p) {
        Server server = Core.getInstance().getServer();
        for (Player player : server.getOnlinePlayers()) {
            player.hidePlayer(p);
        }
    }

}