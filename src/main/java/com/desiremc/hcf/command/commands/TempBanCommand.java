package com.desiremc.hcf.command.commands;

import org.bukkit.command.CommandSender;

import com.desiremc.hcf.Core;
import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.parser.PlayerSessionParser;
import com.desiremc.hcf.parser.StringParser;
import com.desiremc.hcf.parser.TimeParser;
import com.desiremc.hcf.punishment.Punishment;
import com.desiremc.hcf.punishment.PunishmentHandler;
import com.desiremc.hcf.punishment.Punishment.Type;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.util.DateUtils;
import com.desiremc.hcf.validator.PlayerValidator;
import com.desiremc.hcf.validator.SenderOutranksValidator;

public class TempBanCommand extends ValidCommand
{

    public TempBanCommand()
    {
        super("tempban", "Temporarily ban a user from the server.", Rank.MODERATOR, ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "target", "time", "reason" });
        addParser(new PlayerSessionParser(), "target");
        addParser(new TimeParser(), "time");
        addParser(new StringParser(), "reason");
        addValidator(new PlayerValidator());
        addValidator(new SenderOutranksValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];
        StringBuilder sb = new StringBuilder();

        if (args.length >= 3)
        {
            for (int i = 2; i < args.length; i++)
            {
                sb.append(args[i] + " ");
            }
        }

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(time);
        punishment.setReason(sb.toString().trim());
        punishment.setIssuer(session != null ? session.getUniqueId() : Core.getConsoleUUID());
        punishment.setType(Type.BAN);
        PunishmentHandler.getInstance().save(punishment);

        LANG.sendRenderMessage(sender, "ban.tempban_message",
                "{duration}", DateUtils.formatDateDiff(time),
                "{player}", target.getName(),
                "{reason}", punishment.getReason());
    }

}
