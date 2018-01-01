package com.desiremc.hcf.commands.spawn;

import java.util.List;

import org.bukkit.entity.Player;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.SpawnHandler;

public class SpawnCommand extends ValidCommand
{

    public SpawnCommand()
    {
        super("spawn", "Teleport to the server spawn.", Rank.GUEST, true);

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .setOptional()
                .setRequiredRank(Rank.HELPER)
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = args.get(0).hasValue() ? (Player) args.get(0).getValue() : sender.getPlayer();

        player.teleport(SpawnHandler.getInstance().getSpawn());

        if (args.get(0).hasValue())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.target", true, false,
                    "{player}", player.getName());
            DesireHCF.getLangHandler().sendRenderMessage(player, "spawn.force", true, false);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.confirm", true, false);
        }
    }
}