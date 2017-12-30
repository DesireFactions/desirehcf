package com.desiremc.hcf.listener;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

import net.minecraft.util.com.google.common.primitives.Doubles;

public class SignListener implements Listener
{

    @EventHandler
    public void onSignPlace(SignChangeEvent event)
    {
        String title = event.getLine(0);
        if (!title.equalsIgnoreCase("[DesireHCF]"))
        {
            return;
        }

        FSession session = FSessionHandler.getFSession(event.getPlayer());

        if (!session.getRank().isManager())
        {
            return;
        }

        String type = event.getLine(1);

        if (!type.equalsIgnoreCase("Buy") && !type.equalsIgnoreCase("Sell"))
        {
            return;
        }

        String price = event.getLine(3).replace("$", "");

        if (Doubles.tryParse(price) == null)
        {
            return;
        }

        event.setLine(0, ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "DesireHCF" + ChatColor.DARK_GRAY + "]");
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        Block clicked = event.getClickedBlock();

        if (clicked.getType() != Material.WALL_SIGN)
        {
            return;
        }

        Sign sign = (Sign) clicked.getState();

        if (!sign.getLine(0).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&8[&bDesireHCF&8]")))
        {
            return;
        }

        Player player = event.getPlayer();
        FSession session = FSessionHandler.getFSession(player);

        double price = Doubles.tryParse(sign.getLine(3).replace("$", ""));
        ItemStack item = new ItemStack(Material.matchMaterial(sign.getLine(2)));
        String name = StringUtils.capitalize(item.getType().name().replace("_", " ").toLowerCase());

        if (sign.getLine(1).equalsIgnoreCase("Buy"))
        {
            if (session.getBalance() < price)
            {
                DesireHCF.getLangHandler().sendRenderMessage(session, "signs.not_enough_money", true, false);
                return;
            }

            if (player.getInventory().firstEmpty() == -1)
            {
                DesireHCF.getLangHandler().sendRenderMessage(session, "signs.full_inventory", true, false);
                return;
            }

            player.getInventory().addItem(item);

            session.withdrawBalance(price);
            session.save();
            DesireHCF.getLangHandler().sendRenderMessage(session, "signs.buy_success", true, false, "{item}", name, "{price}", price);
        }
        else
        {
            if (!player.getInventory().contains(item.getType()))
            {
                DesireHCF.getLangHandler().sendRenderMessage(session, "signs.no_item", true, false);
                return;
            }

            consumeItem(player, 1, item.getType());

            session.depositBalance(price);
            session.save();
            DesireHCF.getLangHandler().sendRenderMessage(session, "signs.sell_success", true, false, "{item}", name, "{price}", price);
        }
    }

    public boolean consumeItem(Player player, int count, Material mat)
    {
        Map<Integer, ? extends ItemStack> ammo = player.getInventory().all(mat);

        int found = 0;
        for (ItemStack stack : ammo.values())
        {
            found += stack.getAmount();
        }
        if (count > found)
        {
            return false;
        }

        for (Integer index : ammo.keySet())
        {
            ItemStack stack = ammo.get(index);

            int removed = Math.min(count, stack.getAmount());
            count -= removed;

            if (stack.getAmount() == removed)
            {
                player.getInventory().setItem(index, null);
            }
            else
            {
                stack.setAmount(stack.getAmount() - removed);
            }

            if (count <= 0)
            {
                break;
            }
        }

        player.updateInventory();
        return true;
    }
}