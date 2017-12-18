package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PlayerParser;
import com.desiremc.core.newparsers.PositiveIntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import org.bukkit.entity.Player;

import java.util.List;

public class LivesRemoveCommand extends ValidCommand
{

    public LivesRemoveCommand()
    {
        super("remove", "remove lives", Rank.MODERATOR, new String[] {"take"});

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

        FSession session = FSessionHandler.getFSession(target.getUniqueId());
        session.takeLives(amount);
        session.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.remove", "{amount}", String.valueOf(amount), "{player}", target.getName());
    }

}