package com.desiremc.hcf.command.commands;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.validator.PlayerValidator;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Christian Tooley
 */

public class InfoCommand extends ValidCommand
{
    public InfoCommand()
    {
        super("info", "Get information about a player.", Rank.MODERATOR, new String[]{"target"});
        addParser(new PlayerSessionParser(), "target");
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];
        DesireCore.getLangHandler().sendRenderMessage(target, "info.header-footer");
        DesireCore.getLangHandler().sendRenderMessage(target, "info.name", true, "{name}", target.getName());
        DesireCore.getLangHandler().sendRenderMessage(target, "info.uuid", true, "{uuid}", target.getUniqueId().toString());
        DesireCore.getLangHandler().sendRenderMessage(target, "info.ip", true, "{ip}", target.getPlayer().getAddress().getAddress().toString());
        DesireCore.getLangHandler().sendRenderMessage(target, "info.tokens", true, "{tokens}", target.getTokens() + "");
        DesireCore.getLangHandler().sendRenderMessage(target, "info.status", true, "{status}", getStatus(target));
        DesireCore.getLangHandler().sendRenderMessage(target, "info.header-footer");
    }

    private String getStatus(Session player)
    {
        if (player.isBanned() != null)
        {
            return DesireCore.getLangHandler().renderMessage("info.banned", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }

        if (player.isMuted() != null)
        {
            return DesireCore.getLangHandler().renderMessage("info.muted", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        return DesireCore.getLangHandler().renderMessage("info.normal");
    }

    private String longToTime(long time)
    {
        Date date = new Date(time);
        DateFormat formatter = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        return formatter.format(date);
    }
}
