package com.desiremc.hcf.commands.spawn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

public class SpawnCommand extends ValidCommand
{

    private Location spawn;
    private static SpawnCommand instance;

    public SpawnCommand()
    {
        super("spawn", "Teleport to the server spawn.", Rank.GUEST, true);

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .setOptional()
                .setRequiredRank(Rank.HELPER)
                .build());

        instance = this;
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = args.get(0).hasValue() ? (Player) args.get(0).getValue() : sender.getPlayer();

        player.teleport(getSpawn());

        if (args.get(0).hasValue())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.target", true, false,
                    "{target}", player.getName());
            DesireHCF.getLangHandler().sendRenderMessage(player, "spawn.force", true, false);
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.confirm", true, false);
        }
    }

    public static SpawnCommand getInstance()
    {
        return instance;
    }

    public Location getSpawn()
    {
        if (spawn == null)
        {
            FileHandler config = DesireHCF.getConfigHandler();
            new Location(Bukkit.getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.z"),
                    config.getDouble("spawn.yaw").floatValue(),
                    config.getDouble("spawn.pitch").floatValue());
        }
        return spawn;
    }

    public void setSpawn(Location spawn)
    {
        getInstance().spawn = spawn;
    }
}