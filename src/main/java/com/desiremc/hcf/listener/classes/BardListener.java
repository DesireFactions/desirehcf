package com.desiremc.hcf.listener.classes;

import com.desiremc.core.session.PVPClass;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BardListener implements DesireClass
{
    private int range;
    public static HashMap<UUID, Integer> energy;

    public BardListener()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        energy = new HashMap<>();
        range = DesireHCF.getConfigHandler().getInteger("classes.bard.range");

        ClassListener.energyRunnable(energy);
    }

    @EventHandler
    public void onRightClickAbility(PlayerInteractEvent event)
    {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            return;
        }

        Player p = event.getPlayer();

        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.BARD.equals(session.getPvpClass()))
        {
            return;
        }

        ItemStack item = p.getItemInHand();

        if (!ClassListener.isClassItem(p.getItemInHand(), PVPClass.BARD))
        {
            return;
        }

        ConfigurationSection section = DesireHCF.getConfigHandler().getConfigurationSection("classes.bard.effects");
        PotionEffectType effect = PotionEffectType.getByName(section.getString(item.getType().name() + ".click"));

        if (section.get(item.getType().name() + ".click") == null)
        {
            return;
        }

        section = DesireHCF.getConfigHandler().getConfigurationSection("classes.bard.effects." + item.getType().name() + ".click");
        int energyNeeded = section.getInt("energy");

        if (energy.get(p.getUniqueId()) < energyNeeded)
        {
            DesireHCF.getLangHandler().sendRenderMessage(p, "classes.not-enough-energy");
            return;
        }

        energy.replace(p.getUniqueId(), energy.get(p.getUniqueId()) - energyNeeded);

        ClassListener.applyEffect(p, item.getType(), "click", PVPClass.BARD, section.getInt("duration"), range,
                section.getBoolean("faction"), section.getBoolean("self"),
                section.getBoolean("allies"),
                section.getBoolean("other"));
    }

    @EventHandler
    public void playerItemHeldEvent(PlayerItemHeldEvent event)
    {
        Player p = event.getPlayer();
        HCFSession session = HCFSessionHandler.getHCFSession(p.getUniqueId());

        if (!PVPClass.BARD.equals(session.getPvpClass()))
        {
            return;
        }

        ItemStack item = p.getInventory().getItem(event.getNewSlot());

        if (item == null || item.getType().equals(Material.AIR))
        {
            return;
        }

        if (!ClassListener.isClassItem(item, PVPClass.BARD))
        {
            return;
        }

        ConfigurationSection section = DesireHCF.getConfigHandler().getConfigurationSection("classes.bard.effects");

        if (!section.isSet(item.getType().name() + ".hold.amplifier"))
        {
            return;
        }

        section = DesireHCF.getConfigHandler().getConfigurationSection("classes.bard.effects." + item.getType().name() + ".hold");

        ClassListener.applyEffect(p, item.getType(), "hold", PVPClass.BARD, section.getInt("duration"), range,
                section.getBoolean("faction"), section.getBoolean("self"),
                section.getBoolean("allies"),
                section.getBoolean("other"));
    }

    private void showAllRogues(Player source)
    {
        List<Player> players = FactionsUtils.getFactionMembersInRange(source, range);

        for (Player target : players)
        {
            HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());

            if (PVPClass.ROGUE.equals(session.getPvpClass()))
            {
                RogueListener.invisCooldown.put(target.getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}