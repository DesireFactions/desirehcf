package me.borawski.hcf.command.commands.region;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;

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
