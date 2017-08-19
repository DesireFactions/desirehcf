package me.borawski.hcf.command.commands.base;

import me.borawski.hcf.command.ValidBaseCommand;
import me.borawski.hcf.command.commands.sub.ManualYoutubeCommand;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 5/17/2017.
 */
public class ManualCommand extends ValidBaseCommand {

    public ManualCommand() {
        super("Manual", "Opens a manual", Rank.GUEST);
        addSubCommand(new ManualYoutubeCommand());
    }

}
