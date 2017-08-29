package me.borawski.hcf.thread.staff;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.StaffHandler;

public class MovementFreezeThread extends Thread {

    private StaffHandler staffHandlerInstance;
    private Player frozenPlayer;
    private Location anchorPoint;

    public MovementFreezeThread(StaffHandler sh, Player fp) {
        staffHandlerInstance = sh;
        frozenPlayer = fp;
        anchorPoint = fp.getLocation();
    }

    public void run() {
        frozenPlayer.sendMessage("You have been frozen. You cannot move until a staff member unfreezes you.");
        int checkFrozenCounter = 0;
        boolean frozen = true;

        while (frozen) {
            checkFrozenCounter++;
            if (checkFrozenCounter == 5) {
                frozen = staffHandlerInstance.isFrozen(frozenPlayer);
                checkFrozenCounter = 0;
                frozenPlayer.sendMessage("You have been unfrozen.");
            }

            frozenPlayer.teleport(anchorPoint);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        staffHandlerInstance.removeIsFrozenObject(frozenPlayer);
    }

}
