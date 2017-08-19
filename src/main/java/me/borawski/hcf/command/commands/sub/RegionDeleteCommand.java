package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;

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
        Region r = RegionHandler.getInstance().getRegion(name.toLowerCase());
        if (r == null) {
            sender.sendMessage(l.getString("delete.not-found"));
            return;
        }

        RegionHandler.getInstance().delete(r);
        sender.sendMessage(l.getString("delete.success").replace("{name}", name));
    }

}
