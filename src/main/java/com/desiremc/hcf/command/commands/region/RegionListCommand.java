package com.desiremc.hcf.command.commands.region;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionListCommand extends ValidCommand {

    public RegionListCommand() {
        super("list", "List all the regions created.", Rank.ADMIN, new String[] {});
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
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
