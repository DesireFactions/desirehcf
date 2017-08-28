package me.borawski.hcf.session;

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

import me.borawski.hcf.Core;
import me.borawski.hcf.api.FileHandler;
import me.borawski.hcf.api.LangHandler;

public class StaffHandler {
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

    public boolean inStaffMode(Player p) {
        return inventories.containsKey(p.getUniqueId());
    }

    public boolean runningCPSTests() {
        return numCPSTests > 0;
    }

    public void removeIsFrozenObject(Player p) {
        frozenPlayers.remove(p.getUniqueId());
    }

    public HashMap<UUID, Integer> getCPS() {
        return cpsTests;
    }

    public void handleCPSTest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Integer cps = cpsTests.get(p.getUniqueId());
        if (cps != null) {
            cpsTests.put(p.getUniqueId(), Integer.valueOf(cps.intValue() + 1));
        }
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

    public void forceUnfreeze(Player p) {
        if (!isFrozen(p)) return;
        frozenPlayers.put(p.getUniqueId(), Boolean.valueOf(false));
    }

    public void disableStaffMode(Player p) {
        p.getInventory().setContents(inventories.get(p.getUniqueId()));
        inventories.remove(p.getUniqueId());
        LANG.sendString(p, "staff.not_in_staff_mode");
    }

    public void toggleStaffMode(Player p) {
        if (inStaffMode(p)) {
            disableStaffMode(p);
        } else {
            enableStaffMode(p);
        }
    }

    public void playerInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
        if (e.getHand().equals(EquipmentSlot.HAND)) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                Material type = e.getPlayer().getInventory().getItemInMainHand().getType();
                switch (type) {
                // case Material.WATCH:
                // useClicksPerSecond(e);
                // break;
                case LEASH:
                    useMount(e);
                    break;
                // case Material.BLAZE_ROD:
                // useFreeze(e);
                // break;
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
            if (type == Material.COMPASS)
                useLaunch(e);
            else if (type == Material.EYE_OF_ENDER)
                useTeleport(e);
            else if (type == Material.CLAY)
                useInvisibility(e);
            // else if (type == Material.PAPER)
            // useReportGUI(e);
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
        LangHandler l = Core.getLangHandler();
        Player p = e.getPlayer();
        int index = hiddenPlayers.indexOf(p.getUniqueId());

        if (index == -1) {
            hidePlayer(p);
            hiddenPlayers.add(p.getUniqueId());
            p.sendMessage(l.getString("staff.set-invisible"));
        } else {
            showPlayer(p);
            hiddenPlayers.remove(index);
            p.sendMessage(l.getString("staff.set-visible"));
        }
    }

    // public void useReportGUI(PlayerInteractEvent e) {
    // Inventory reportMenu = Bukkit.createInventory(null, 27, "Reports");
    // Collection<Report> reports = Core.getReportHandler().getReports();
    //
    // int index = 0;
    // for (Iterator i = reports.iterator(); i.hasNext();) {
    // reportMenu.setItem(index, ((Report) i.next()).asItem());
    // index++;
    // }
    //
    // e.getPlayer().openInventory(reportMenu);
    // }

    private void useOpenInventory(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            e.getPlayer().openInventory(((Player) e).getInventory());
        }
    }

    // public void useClicksPerSecond(PlayerInteractEntityEvent e) {
    // if (e.getRightClicked() instanceof Player) {
    // Player p = (Player) e.getRightClicked();
    // UUID playerID = p.getUniqueId();
    //
    // if (cpsTests.containsKey(playerID))
    // return;
    //
    // cpsTests.put(playerID, Integer.valueOf(0));
    // ClicksPerSecondThread newThread = new ClicksPerSecondThread(this, p);
    // numCPSTests++;
    // newThread.start();
    // }
    // }

    // public void useFreeze(PlayerInteractEntityEvent e) {
    //
    // if (e.getRightClicked() instanceof Player) {
    // Player p = (Player) e.getRightClicked();
    //
    // // condition to unfreeze the player putting a value of false will
    // // let
    // // the Thread automatically unfreeze without causing any errors
    // if (isFrozen(p)) {
    // frozenPlayers.put(p.getUniqueId(), Boolean.valueOf(false));
    // return;
    // }
    //
    // frozenPlayers.put(p.getUniqueId(), Boolean.valueOf(true));
    // MovementFreezeThread mft = new MovementFreezeThread(this, p);
    // mft.start();
    // }
    // }

    public void useMount(PlayerInteractEntityEvent e) {
        if (!e.getRightClicked().isDead())
            e.getRightClicked().addPassenger(e.getPlayer());
    }

    public boolean isFrozen(Player p) {
        Boolean frozen = frozenPlayers.get(p.getUniqueId());

        if (frozen == null) return false;

        return frozen.booleanValue();
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