package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.ManualYoutubeCommand;
import me.borawski.hcf.session.Rank;

public class ManualCommand extends ValidBaseCommand {

    public ManualCommand() {
        super("Manual", "Opens a manual", Rank.GUEST);
        addSubCommand(new ManualYoutubeCommand());
    }

}
