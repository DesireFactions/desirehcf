package com.desiremc.hcf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.Bukkit;

import com.desiremc.hcf.handler.BrewingSpeedHandler;
import com.desiremc.hcf.handler.DeathBanHandler;
import com.desiremc.hcf.handler.EnchantmentLimiterHandler;
import com.desiremc.hcf.handler.EnderchestHandler;
import com.desiremc.hcf.handler.EnderpearlHandler;
import com.desiremc.hcf.handler.FurnaceSpeedHandler;
import com.desiremc.hcf.handler.GappleHandler;
import com.desiremc.hcf.handler.LootingBuffHandler;
import com.desiremc.hcf.handler.MobStackHandler;
import com.desiremc.hcf.handler.PotionLimiterHandler;
import com.desiremc.hcf.handler.SellSignHandler;
import com.desiremc.hcf.util.Cooldown;

public class Components {

    private static Components instance;

    public final static String GAPPLE = "GAPPLE";
    public final static String CBTLOG = "CBTLOG";
    public final static String ENDERP = "ENDERP";
    public final static String PVPT = "PVPT";
    public final static String DEATHBAN = "DEATHBAN";

    private Map<String, Cooldown> cooldowns = new HashMap<>();

    private DeathBanHandler playerDeath;

    public void onEnable() {
        instance = this;
        cooldowns.clear();
        checkDependencies();
        registerCooldowns();
        registerListeners();

    }

    public void onDisable() {
        instance = null;
        saveLives();
    }

    public void registerListeners() {
        DesireCore instance = DesireCore.getInstance();
        Bukkit.getPluginManager().registerEvents(new MobStackHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new FurnaceSpeedHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new GappleHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new EnderpearlHandler(), instance);
        // got to here
        Bukkit.getPluginManager().registerEvents(new EnderchestHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new BrewingSpeedHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new LootingBuffHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new SellSignHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new EnchantmentLimiterHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new PotionLimiterHandler(), instance);
        //Bukkit.getPluginManager().registerEvents(new PvPTimer(), instance);
        playerDeath = new DeathBanHandler();
    }

    public static Components getInstance() {
        return instance;
    }

    public void registerCooldowns() {
        File cooldownFolder = new File(DesireCore.getInstance().getDataFolder() + File.separator + "cooldowns");
        if (!cooldownFolder.exists())
            cooldownFolder.mkdirs();
        File gapFile = new File(cooldownFolder, "gapple.cooldown");
        cooldowns.put(GAPPLE, new Cooldown(gapFile));
        File cbtLogFile = new File(cooldownFolder, "cbtlog.cooldown");
        cooldowns.put(CBTLOG, new Cooldown(cbtLogFile));
        File pvpTFile = new File(cooldownFolder, "pvpt.cooldown");
        cooldowns.put(PVPT, new Cooldown(pvpTFile));
        File enderPFile = new File(cooldownFolder, "enderp.cooldown");
        cooldowns.put(ENDERP, new Cooldown(enderPFile));
        File dbanFile = new File(cooldownFolder, "dban.cooldown");
        cooldowns.put(DEATHBAN, new Cooldown(dbanFile));
        cooldowns.values().forEach(cooldown -> cooldown.startRunning());
    }

    public void checkDependencies() {
        try {
            Scanner scan = new Scanner(new File(DesireCore.getInstance().getDataFolder(), "items.csv"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] values = line.split(",");
                if (!line.startsWith("#") && values.length == 3) {
                    SellSignHandler.namesToIds.put(values[0].toLowerCase(), values[1] + ":" + values[2]);
                }
            }
            Bukkit.getLogger().info("Successfully loaded items.csv!");
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveLives() {
        cooldowns.values().forEach(cooldown -> {
            try {
                cooldown.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        playerDeath.saveLives();
    }

    public Cooldown getCooldown(String s) {
        return cooldowns.get(s);
    }
}
