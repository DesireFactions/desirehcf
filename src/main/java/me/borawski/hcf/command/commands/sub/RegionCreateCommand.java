package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionBlocks;
import me.borawski.hcf.util.ItemNames;

public class RegionCreateCommand extends CustomCommand {

    public RegionCreateCommand() {
        super("create", "Create a new region.", Rank.ADMIN, "new");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CommandSender sender, String label, String[] args) {

        LangHandler l = Core.getLangHandler();

        if (args.length != 2) {
            sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " [name] [material]"));
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getString("only-players"));
            return;
        }

        Player p = (Player) sender;
        Selection s = Core.getWorldEdit().getSelection(p);
        if (s == null) {
            sender.sendMessage(l.getString("create.no-selection"));
            return;
        }

        String name = args[0];
        if (name.length() > Core.getConfigHandler().getInteger("regions.max-name")) {
            sender.sendMessage(l.getString("create.too-long"));
            return;
        }

        Region r = Core.getRegionHandler().getRegion(name.toLowerCase());
        if (r != null) {
            sender.sendMessage(l.getString("create.name-taken"));
            return;
        }
        // TODO CHECK FOR BAD NAME
        ItemStack is = Core.getItemHandler().get(args[1]);
        if (is == null) {
            sender.sendMessage(l.getString("create.invalid-item"));
            return;
        }
        if (is.getType().getId() > 217) {
            sender.sendMessage(l.getString("create.not-block"));
            return;
        }

        r = new Region(name, p.getWorld().getName(), new RegionBlocks(s.getMaximumPoint(), s.getMinimumPoint()), is.getData(), Core.getConfigHandler().getInteger("barrier.view-distance"));
        Core.getRegionHandler().save(r, true);
        sender.sendMessage(l.getString("create.success").replace("{name}", name).replace("{material}", ItemNames.lookup(is)));
    }

}
