package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;
import me.borawski.hcf.util.ItemNames;

public class RegionModifyCommand extends ValidCommand {

    public RegionModifyCommand() {
        super("modify", "Modify an existing region.", Rank.ADMIN, new String[] { "region", "name/material/distance" }, "edit");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String name = (String) args[0];

        Region r = RegionHandler.getInstance().getRegion(name.toLowerCase());
        if (r == null) {
            sender.sendMessage(LANG.getString("modify.not-found"));
            return;
        }

        if (((String) args[1]).equalsIgnoreCase("name")) {
            if (args.length != 3) {
                sender.sendMessage(LANG.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [new name]"));
                return;
            }
            String newName = (String) args[2];
            if (newName.length() > Core.getConfigHandler().getInteger("regions.max-name")) {
                sender.sendMessage(LANG.getString("modify.too-long"));
                return;
            }
            Region check = RegionHandler.getInstance().getRegion(newName);
            if (check == r) {
                sender.sendMessage(LANG.getString("modify.same-name"));
                return;
            }
            if (check != null) {
                sender.sendMessage(LANG.getString("modify.name-taken"));
                return;
            }

            r.setName(newName);
            RegionHandler.getInstance().save(r);
            RegionHandler.getInstance().remove(((String) args[0]).toLowerCase());
            sender.sendMessage(LANG.getString("modify.changed-name").replace("{old}", (CharSequence) args[0]).replace("{new}", newName));
        } else if (((String) args[1]).equalsIgnoreCase("material")) {
            if (args.length != 3) {
                sender.sendMessage(LANG.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [new material]"));
                return;
            }
            ItemStack is = Core.getItemHandler().get((String) args[2]);
            if (is == null) {
                sender.sendMessage(LANG.getString("modify.invalid-item"));
                return;
            }
            if (is.getType().getId() > 217) {
                sender.sendMessage(LANG.getString("modify.not-block"));
                return;
            }
            ItemStack old = new ItemStack(r.getBarrierMaterial(), 1, r.getBarrierMaterialData());
            r.setBarrierMaterial(is.getType());
            r.setBarrierMaterialData(is.getData().getData());
            RegionHandler.getInstance().save(r);
            sender.sendMessage(LANG.getString("modify.changed-material").replace("{old}", ItemNames.lookup(old)).replace("{new}", ItemNames.lookup(is)));
        } else if (((String) args[1]).equalsIgnoreCase("distance")) {
            if (args.length != 3) {
                sender.sendMessage(LANG.getString("usage-message").replace("{usage}", "/region " + label + " " + args[0] + " [distance]"));
                return;
            }
            try {
                int distance = Integer.parseInt((String) args[2]);
                if (distance > Core.getConfigHandler().getInteger("barrier.max-distance")) {
                    sender.sendMessage(LANG.getString("modify.max-distance"));
                    return;
                }
                int oldDistance = r.getViewDistance();
                r.setViewDistance(distance);
                RegionHandler.getInstance().save(r);
                sender.sendMessage(LANG.getString("modify.changed-distance").replace("{old}", oldDistance + "").replace("{new}", distance + ""));
            } catch (NumberFormatException ex) {
                sender.sendMessage(LANG.getString("modify.not-a-number"));
                return;
            }
        } else {
            sender.sendMessage(LANG.getString("usage-message").replace("{usage}", "/protection " + label + " [name/material]"));
            return;
        }

    }

}
