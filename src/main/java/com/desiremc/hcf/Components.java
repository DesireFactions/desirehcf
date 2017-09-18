package com.desiremc.hcf;

import org.bukkit.Bukkit;

import com.desiremc.hcf.handler.BrewingSpeedHandler;
import com.desiremc.hcf.handler.EnchantmentLimiterHandler;
import com.desiremc.hcf.handler.EnderchestHandler;
import com.desiremc.hcf.handler.EnderpearlHandler;
import com.desiremc.hcf.handler.FurnaceSpeedHandler;
import com.desiremc.hcf.handler.LootingBuffHandler;
import com.desiremc.hcf.handler.MobStackHandler;
import com.desiremc.hcf.handler.PotionLimiterHandler;

public class Components
{

    private static Components instance;

    public void onEnable()
    {
        instance = this;
        registerListeners();
    }

    public void registerListeners()
    {
        HCFCore instance = HCFCore.getInstance();
        Bukkit.getPluginManager().registerEvents(new MobStackHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new FurnaceSpeedHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new EnderpearlHandler(), instance);
        // got to here
        Bukkit.getPluginManager().registerEvents(new EnderchestHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new BrewingSpeedHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new LootingBuffHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new EnchantmentLimiterHandler(), instance);
        Bukkit.getPluginManager().registerEvents(new PotionLimiterHandler(), instance);
    }

    public static Components getInstance()
    {
        return instance;
    }

}
