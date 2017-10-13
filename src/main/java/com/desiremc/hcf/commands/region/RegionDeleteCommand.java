package com.desiremc.hcf.commands.region;

import com.desiremc.hcf.HCFCore;
import com.desiremc.hcf.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.parser.RegionParser;
import com.desiremc.hcf.session.Region;
import com.desiremc.hcf.session.RegionHandler;

public class RegionDeleteCommand extends ValidCommand
{

    private static final LangHandler LANG = HCFCore.getLangHandler();

    public RegionDeleteCommand()
    {
        super("delete", "Delete a protected region.", Rank.ADMIN, new String[]{"name"}, "remove");

        addParser(new RegionParser(), "name");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Region r = (Region) args[0];
        RegionHandler.getInstance().delete(r);

        sender.sendMessage(LANG.getString("region.delete").replace("{name}", r.getName()));
    }

}
