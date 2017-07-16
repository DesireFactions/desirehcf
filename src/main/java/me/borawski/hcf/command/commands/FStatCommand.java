package me.borawski.hcf.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MPlayer;

import me.borawski.hcf.Core;
import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.session.Session;
import me.borawski.hcf.session.SessionHandler;

/**
 * Created by Ethan on 5/7/2017.
 */
public class FStatCommand extends CustomBaseCommand {

    public FStatCommand() {
        super("fstat", "View your player stats", Rank.GUEST, "fstats");
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                LANG.sendString(sender, "only-playes");
                return;
            }
            String faction = MPlayer.get(sender).getFactionName().replace(Factions.NAME_NONE_DEFAULT, "None");
            FactionSession session = FactionSessionHandler.getFactionSession(faction);
            LANG.sendRenderMessage(sender, "faction", "{faction}", faction);
            LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
            LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));
        } else {
            Session s = SessionHandler.getSession((Player) sender);
            if (s.getRank().getId() >= Rank.MODERATOR.getId()) {
                String faction = args[0];
                try {
                    FactionSession session = FactionSessionHandler.getFactionSession(faction);
                    LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
                    LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));
                    LANG.sendRenderMessage(sender, "faction", "{faction}", session.getName());
                } catch (Exception e) {
                    LANG.sendRenderMessage(sender, "no_season_data", "{faction}", faction);
                }
            } else {
                LANG.sendString(sender, "no-permissions");
            }
        }
    }

}
