package me.borawski.hcf.thread.staff;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.borawski.hcf.session.StaffHandler;

public class ClicksPerSecondThread extends Thread {

    StaffHandler staffHandlerInstance;
    Player playerTesting;

    public ClicksPerSecondThread(StaffHandler staffHandlerInstance, Player playerTesting) {
        this.staffHandlerInstance = staffHandlerInstance;
        this.playerTesting = playerTesting;
    }

    @Override
    public void run() {
        HashMap<UUID, Integer> cps = staffHandlerInstance.getCPS();
        UUID id = playerTesting.getUniqueId();

        playerTesting.sendMessage("Starting clicks per second test. Result in 10 seconds.");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int perSecond = cps.get(id) / 5;

        playerTesting.sendMessage("Clicked " + perSecond + " per second over 5 seconds.");
        cps.remove(id);
        staffHandlerInstance.decreaseNumCPSTests();
    }

}
