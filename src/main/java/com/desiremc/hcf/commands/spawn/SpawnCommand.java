package com.desiremc.hcf.commands.spawn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

public class SpawnCommand extends ValidCommand
{

    protected static Location spawnLocation;

    public SpawnCommand()
    {
        super("spawn", "Teleport to the server spawn.", Rank.GUEST, true);

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .setOptional()
                .setRequiredRank(Rank.MODERATOR)
                .build());

        FileHandler config = DesireHCF.getConfigHandler();

        spawnLocation = new Location(Bukkit.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                config.getDouble("spawn.yaw").floatValue(),
                config.getDouble("spawn.pitch").floatValue());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = args.get(0).hasValue() ? (Player) args.get(0) : sender.getPlayer();

        player.teleport(spawnLocation);

        if (args.get(0).hasValue())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.target",
                    "{target}", player.getName());
            DesireHCF.getLangHandler().sendRenderMessage(player, "spawn.force");
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.confirm");
        }
    }
}
