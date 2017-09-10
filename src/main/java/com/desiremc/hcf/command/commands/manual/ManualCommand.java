package com.desiremc.hcf.command.commands.manual;

import com.desiremc.hcf.command.ValidBaseCommand;
import com.desiremc.hcf.session.Rank;

public class ManualCommand extends ValidBaseCommand {

    public ManualCommand() {
        super("Manual", "Opens a manual", Rank.GUEST);
        addSubCommand(new ManualYoutubeCommand());
    }

}
