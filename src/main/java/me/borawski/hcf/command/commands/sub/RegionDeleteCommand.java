package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;

public class RegionDeleteCommand extends CustomCommand {

    public RegionDeleteCommand() {
        super("delete", "Delete a protected region.", Rank.ADMIN, "remove");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        LangHandler l = Core.getLangHandler();

        if (args.length != 1) {
            sender.sendMessage(l.getString("usage-message").replace("{usage}", "/region " + label + " [name]"));
            return;
        }

        String name = args[0];
        Region r = Core.getRegionHandler().getRegion(name.toLowerCase());
        if (r == null) {
            sender.sendMessage(l.getString("delete.not-found"));
            return;
        }

        Core.getRegionHandler().delete(r);
        sender.sendMessage(l.getString("delete.success").replace("{name}", name));
    }

}
