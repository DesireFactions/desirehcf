package me.borawski.hcf.command.commands.sub;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;

public class RegionListCommand extends CustomCommand {

    public RegionListCommand() {
        super("list", "List all the regions created.", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Collection<Region> regions = RegionHandler.getInstance().getRegions();

        if (regions.size() >= 1) {
            String message = Core.getLangHandler().getString("list.format");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Region r : regions) {
                sb.append(r.getName());
                if (i != regions.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }
            message = message.replace("{regions}", sb.toString());
            sender.sendMessage(message);
        } else {
            sender.sendMessage(Core.getLangHandler().getString("list.no-regions"));
        }
    }

}
