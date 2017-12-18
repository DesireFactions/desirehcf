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
import com.desiremc.hcf.validators.PlayerHasEnoughLivesValidator;
import org.bukkit.entity.Player;

import java.util.List;

public class LivesSendCommand extends ValidCommand
{
    public LivesSendCommand()
    {
        super("send", "send lives", Rank.GUEST, new String[] {"target", "amount"});

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("amount")
                .setParser(new PositiveIntegerParser())
                .addValidator(new PlayerHasEnoughLivesValidator())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = (Player) args.get(0).getValue();
        int amount = (Integer) args.get(1).getValue();

        FSession target = FSessionHandler.getFSession(player.getUniqueId());
        FSession source = FSessionHandler.getFSession(sender.getUniqueId());
        source.takeLives(amount);
        target.addLives(amount);
        source.save();
        target.save();

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.send", "{amount}", String.valueOf(amount), "{player}", target.getName());
        DesireHCF.getLangHandler().sendRenderMessage(player, "lives.send_target", "{amount}", String.valueOf(amount), "{player}", sender.getName());
    }
}
