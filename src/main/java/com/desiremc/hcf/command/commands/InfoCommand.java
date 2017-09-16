package com.desiremc.hcf.command.commands;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.util.DateUtils;
import com.desiremc.hcf.validator.PlayerValidator;
import org.bukkit.command.CommandSender;

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
        LANG.sendRenderMessage(target, "info.header-footer");
        LANG.sendRenderMessage(target, "info.name", true, "{name}", target.getName());
        LANG.sendRenderMessage(target, "info.uuid", true, "{uuid}", target.getUniqueId().toString());
        LANG.sendRenderMessage(target, "info.ip", true, "{ip}", target.getPlayer().getAddress().getAddress().toString());
        LANG.sendRenderMessage(target, "info.tokens", true, "{tokens}", target.getTokens() + "");
        LANG.sendRenderMessage(target, "info.status", true, "{status}", getStatus(target));
        LANG.sendRenderMessage(target, "info.header-footer");
    }

    private String getStatus(Session player)
    {
        if (player.isBanned() != null)
        {
            return LANG.renderMessage("info.banned", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }

        if (player.isMuted() != null)
        {
            return LANG.renderMessage("info.muted", "{time}", longToTime(player.isBanned().getExpirationTime()));
        }
        return LANG.renderMessage("info.normal");
    }

    private String longToTime(long time)
    {
        return DateUtils.formatDateDiff(time);
    }
}
