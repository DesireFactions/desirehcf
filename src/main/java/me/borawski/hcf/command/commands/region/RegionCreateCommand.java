package me.borawski.hcf.command.commands.region;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionBlocks;
import me.borawski.hcf.session.RegionHandler;
import me.borawski.hcf.util.ItemNames;

public class RegionCreateCommand extends ValidCommand {

    public RegionCreateCommand() {
        super("create", "Create a new region.", Rank.ADMIN, new String[] { "name", "material" }, "new");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Player p = (Player) sender;
        Selection s = Core.getWorldEdit().getSelection(p);
        if (s == null) {
            sender.sendMessage(LANG.getString("create.no-selection"));
            return;
        }

        String name = (String) args[0];
        if (name.length() > Core.getConfigHandler().getInteger("regions.max-name")) {
            sender.sendMessage(LANG.getString("create.too-long"));
            return;
        }

        Region r = RegionHandler.getInstance().getRegion(name.toLowerCase());
        if (r != null) {
            sender.sendMessage(LANG.getString("create.name-taken"));
            return;
        }

        // TODO CHECK FOR BAD NAME
        ItemStack is = Core.getItemHandler().get((String) args[1]);
        if (is == null) {
            sender.sendMessage(LANG.getString("create.invalid-item"));
            return;
        }
        if (is.getType().getId() > 217) {
            sender.sendMessage(LANG.getString("create.not-block"));
            return;
        }

        r = new Region(name, p.getWorld().getName(), new RegionBlocks(s.getMaximumPoint(), s.getMinimumPoint()), is.getData(), Core.getConfigHandler().getInteger("barrier.view-distance"));
        RegionHandler.getInstance().save(r, true);
        sender.sendMessage(LANG.getString("create.success").replace("{name}", name).replace("{material}", ItemNames.lookup(is)));
    }

}