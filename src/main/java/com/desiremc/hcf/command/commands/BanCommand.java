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
import com.desiremc.hcf.validator.PlayerSenderValidator;
import com.desiremc.hcf.validator.SenderOutranksValidator;

public class BanCommand extends ValidCommand {

    public BanCommand() {
        super("ban", "Ban a user from the server.", Rank.GUEST,
                ValidCommand.ARITY_REQUIRED_VARIADIC, new String[] { "target", "time", "reason" });
        addParser(new PlayerSessionParser(), "target");
        addParser(new TimeParser(), "time");
        addParser(new StringParser(), "reason");
        addValidator(new PlayerSenderValidator());
        addValidator(new SenderOutranksValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Session session = SessionHandler.getSession(sender);
        Session target = (Session) args[0];
        long time = (long) args[1];
        String reason = "";

        if (args.length >= 2) {
            for (int i = 2; i < args.length; i++) {
                reason += " " + args[i];
            }
        }

        long finalTime = System.currentTimeMillis() + time;

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(finalTime);
        punishment.setReason(reason.toString().trim());
        punishment.setIssuer(session != null ? session.getUniqueId() : Core.getConsoleUUID());
        punishment.setType(Type.BAN);
        PunishmentHandler.getInstance().save(punishment);

        LANG.sendRenderMessage(sender, "ban.ban_message",
                "{duration}", (finalTime == -1 ? "permanently" : "temporarily"),
                "{player}", target.getName(),
                "{reason}", reason);
    }

}
