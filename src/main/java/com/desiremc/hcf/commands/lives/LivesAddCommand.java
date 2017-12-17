package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PlayerParser;
import com.desiremc.core.newparsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import org.bukkit.entity.Player;

import java.util.List;

public class LivesAddCommand extends ValidCommand
{

    public LivesAddCommand()
    {
        super("add", "add lives", Rank.MODERATOR, false, new String[] {"give"});

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new PositiveIntegerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player target = (Player) args.get(0).getValue();
        int amount = (Integer) args.get(1).getValue();

        HCFSession session = HCFSessionHandler.getHCFSession(target.getUniqueId());
        session.addLives(amount);
        session.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.add", "{amount}", String.valueOf(amount), "{player}", target.getName());
        DesireHCF.getLangHandler().sendRenderMessage(target, "lives.recieved", "{amount}", String.valueOf(amount));
    }

}