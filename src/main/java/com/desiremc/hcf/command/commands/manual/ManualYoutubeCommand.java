package com.desiremc.hcf.command.commands.manual;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.hcf.command.ValidCommand;
import com.desiremc.hcf.session.Rank;
import com.desiremc.hcf.util.ManualUtil;
import com.desiremc.hcf.validator.PlayerSenderValidator;

public class ManualYoutubeCommand extends ValidCommand {

    public ManualYoutubeCommand() {
        super("yt", "Opens youtuber manual", Rank.YOUTUBER, new String[] {}, "youtube", "youtuber");
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        ManualUtil.openManual(Rank.YOUTUBER, (Player) sender);
    }

}
