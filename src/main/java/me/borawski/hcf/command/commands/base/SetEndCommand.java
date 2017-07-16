package me.borawski.hcf.command.commands.base;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.command.CustomBaseCommand;
import me.borawski.hcf.command.commands.sub.SetEndExitCommand;
import me.borawski.hcf.command.commands.sub.SetEndSpawnCommand;
import me.borawski.hcf.session.Rank;

public class SetEndCommand extends CustomBaseCommand {

    public SetEndCommand() {
        super("setendspawn", "sets end spawn", Rank.ADMIN);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        addSubCommand(new SetEndSpawnCommand());
        addSubCommand(new SetEndExitCommand());
    }

}
