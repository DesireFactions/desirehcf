package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MPlayer;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class FStatShowCommand extends ValidCommand {

    public FStatShowCommand() {
        super("show", "show my fstats", Rank.GUEST, new String[] {});
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String faction = MPlayer.get(sender).getFactionName().replace(Factions.NAME_NONE_DEFAULT, "None");
        FactionSession session = FactionSessionHandler.getFactionSession(faction);
        LANG.sendRenderMessage(sender, "faction", "{faction}", faction);
        LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
        LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));

    }

}
