package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.PlayerSessionParser;
import me.borawski.hcf.parser.StringParser;
import me.borawski.hcf.parser.TimeParser;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.punishment.Punishment.Type;
import me.borawski.hcf.punishment.PunishmentHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.validator.PlayerSenderValidator;
import me.borawski.hcf.validator.SenderOutranksValidator;

/**
 * Created by Ethan on 3/8/2017.
 */
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
