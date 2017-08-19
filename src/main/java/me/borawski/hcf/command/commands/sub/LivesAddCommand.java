package me.borawski.hcf.command.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.borawski.hcf.api.LivesAPI;
import me.borawski.hcf.command.ValidCommand;
import me.borawski.hcf.parser.IntegerParser;
import me.borawski.hcf.parser.PlayerParser;
import me.borawski.hcf.session.Rank;

public class LivesAddCommand extends ValidCommand {

    public LivesAddCommand() {
        super("add", "add lives", Rank.MODERATOR, new String[] { "target", "amount" }, "give");
        addParser(new PlayerParser(), "target");
        addParser(new IntegerParser(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args) {
        Player target = (Player) args[0];
        Integer amount = (Integer) args[1];

        LivesAPI.addLives(sender, target, amount);
    }

}