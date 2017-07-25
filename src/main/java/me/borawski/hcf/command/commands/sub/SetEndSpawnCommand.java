package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.api.SetEndAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.PlayerSenderValidator;

public class SetEndSpawnCommand extends ValidCommand {

    public SetEndSpawnCommand() {
        super("spawn", "set end spawn", Rank.ADMIN, new String[] {});
        addValidator(new PlayerSenderValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        SetEndAPI.setEndSpawn(sender, "endspawn", "set_end.spawn");
    }

}
