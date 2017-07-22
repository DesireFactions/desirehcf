package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.util.ItemNames;

public class RegionModifyCommand extends CustomCommand {

    public RegionModifyCommand() {
        super("modify", "Modify an existing region.", Rank.ADMIN, "edit");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run(CommandSender sender, String label, String[] args) {
        LangHandler l = Core.getLangHandler();

        if (args.length <= 1) {
            sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " [region] [name/material/distance]"));
            return;
        }

        String name = args[0];

        Region r = Core.getRegionHandler().getRegion(name.toLowerCase());
        if (r == null) {
            sender.sendMessage(l.getString("modify.not-found"));
            return;
        }

        if (args[1].equalsIgnoreCase("name")) {
            if (args.length != 3) {
                sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [new name]"));
                return;
            }
            String newName = args[2];
            if (newName.length() > Core.getConfigHandler().getInteger("regions.max-name")) {
                sender.sendMessage(l.getString("modify.too-long"));
                return;
            }
            Region check = Core.getRegionHandler().getRegion(newName);
            if (check == r) {
                sender.sendMessage(l.getString("modify.same-name"));
                return;
            }
            if (check != null) {
                sender.sendMessage(l.getString("modify.name-taken"));
                return;
            }

            r.setName(newName);
            Core.getRegionHandler().save(r);
            Core.getRegionHandler().remove(args[0].toLowerCase());
            sender.sendMessage(l.getString("modify.changed-name").replace("{old}", args[0]).replace("{new}", newName));
        } else if (args[1].equalsIgnoreCase("material")) {
            if (args.length != 3) {
                sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [new material]"));
                return;
            }
            ItemStack is = Core.getItemHandler().get(args[2]);
            if (is == null) {
                sender.sendMessage(l.getString("modify.invalid-item"));
                return;
            }
            if (is.getType().getId() > 217) {
                sender.sendMessage(l.getString("modify.not-block"));
                return;
            }
            ItemStack old = new ItemStack(r.getBarrierMaterial(), 1, r.getBarrierMaterialData());
            r.setBarrierMaterial(is.getType());
            r.setBarrierMaterialData(is.getData().getData());
            Core.getRegionHandler().save(r);
            sender.sendMessage(l.getString("modify.changed-material").replace("{old}", ItemNames.lookup(old)).replace("{new}", ItemNames.lookup(is)));
        } else if (args[1].equalsIgnoreCase("distance")) {
            if (args.length != 3) {
                sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [distance]"));
                return;
            }
            try {
                int distance = Integer.parseInt(args[2]);
                if (distance > Core.getConfigHandler().getInteger("barrier.max-distance")) {
                    sender.sendMessage(l.getString("modify.max-distance"));
                    return;
                }
                int oldDistance = r.getViewDistance();
                r.setViewDistance(distance);
                Core.getRegionHandler().save(r);
                sender.sendMessage(l.getString("modify.changed-distance").replace("{old}", oldDistance + "").replace("{new}", distance + ""));
            } catch (NumberFormatException ex) {
                sender.sendMessage(l.getString("modify.not-a-number"));
                return;
            }
        } else {
            sender.sendMessage(l.getString("usage-message").replace("{usage}", "/protection " + label + " [name/material]"));
            return;
        }

    }

}
