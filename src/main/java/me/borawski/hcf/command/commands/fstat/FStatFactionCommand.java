package me.borawski.hcf.command.commands.fstat;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.FactionSessionParser;
import me.borawski.hcf.session.FactionSession;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class FStatFactionCommand extends ValidCommand {

    public FStatFactionCommand() {
        super("faction", "shows faction stats", Rank.MODERATOR, new String[] { "faction" });
        addParser(new FactionSessionParser(), "faction");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        FactionSession session = (FactionSession) args[0];
        LANG.sendRenderMessage(sender, "trophy_points", "{points}", Integer.toString(session.getTrophies()));
        LANG.sendRenderMessage(sender, "koth_wins", "{koth_wins}", Integer.toString(session.getKoth()));
        LANG.sendRenderMessage(sender, "faction", "{faction}", session.getName());
    }

}
