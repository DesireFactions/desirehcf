package com.desiremc.hcf.command.commands.region;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionDeleteCommand extends ValidCommand {

    public RegionDeleteCommand() {
        super("delete", "Delete a protected region.", Rank.ADMIN, new String[] { "name" }, "remove");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String name = (String) args[0];
        
        Region r = RegionHandler.getInstance().getRegion(name.toLowerCase());
        
        if (r == null) {
            sender.sendMessage(LANG.getString("delete.not-found"));
            return;
        }

        RegionHandler.getInstance().delete(r);
        sender.sendMessage(LANG.getString("delete.success").replace("{name}", name));
    }

}
