package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomCommand;
import me.borawski.hcf.punishment.Punishment;
import me.borawski.hcf.punishment.Punishment.Type;
import me.borawski.hcf.punishment.PunishmentHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;
import me.borawski.hcf.util.DateUtils;
import me.borawski.hcf.util.PlayerUtils;

/**
 * Created by Ethan on 3/8/2017.
 */
public class BanCommand extends CustomCommand {

    public BanCommand() {
        super("ban", "Ban a user from the server.", Rank.MODERATOR);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {

        if (args.length < 3) {
            LANG.sendUsageMessage(sender, label, "target");
            return;
        }

        if (!PlayerUtils.hasPlayed(args[0])) {
            LANG.sendString(sender, "never_logged_in");
            return;
        }

        Session user = sender instanceof Player ? SessionHandler.getSession((Player) sender) : null;
        Session target = SessionHandler.getSession(PlayerUtils.getUUIDFromName(args[0]));

        if (user == null || target.getRank().getId() >= user.getRank().getId()) {
            LANG.sendString(sender, "ban.rank_to_low_error");
            return;
        }

        long time;
        try {
            time = DateUtils.parseDateDiff(args[1], true);
        } catch (Exception ex) {
            LANG.sendString(sender, "ban.invalid_ban_time_error");
            return;
        }

        StringBuilder reason = new StringBuilder(args[3]);
        for (int i = 3; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        long finalTime = System.currentTimeMillis() + time;

        Punishment punishment = new Punishment();
        punishment.setPunished(target.getUniqueId());
        punishment.setIssued(System.currentTimeMillis());
        punishment.setExpirationTime(finalTime);
        punishment.setReason(reason.toString().trim());
        punishment.setIssuer(user != null ? user.getUniqueId() : Core.getConsoleUUID());
        punishment.setType(Type.BAN);
        PunishmentHandler.getInstance().save(punishment);

        LANG.sendRenderMessage(sender, "ban.ban_message",
                "{duration}", (finalTime == -1 ? "permanently" : "temporarily"),
                "{player}", args[0],
                "{reason}", reason.toString());
    }

}
