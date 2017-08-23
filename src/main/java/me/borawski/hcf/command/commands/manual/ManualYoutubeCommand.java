package me.borawski.hcf.command.commands.manual;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.util.ManualUtil;
import me.borawski.hcf.validator.PlayerSenderValidator;

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
