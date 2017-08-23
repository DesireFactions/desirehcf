package me.borawski.hcf.command.commands.fstat;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.FactionSessionHandler;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.FactionsUtils;
import me.borawski.hcf.validator.PlayerHasFactionValidator;

public class FStatShowCommand extends ValidCommand {

    public FStatShowCommand() {
        super("show", "show my fstats", Rank.GUEST, new String[] {});
        addValidator(new PlayerHasFactionValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        String faction = FactionsUtils.getFaction((Player) sender).getTag();
        FactionSession session = FactionSessionHandler.getFactionSession(faction);
        LANG.sendRenderMessage(sender, "faction", "{faction}", faction);
        LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
        LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));

    }

}
