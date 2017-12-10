package com.desiremc.hcf.commands.spawn;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;

public class SetSpawnCommand extends ValidCommand
{

    public SetSpawnCommand()
    {
        super("setspawn", "Set the server spawn.", Rank.ADMIN, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player p = sender.getPlayer();
        FileHandler config = DesireHCF.getConfigHandler();
        Location loc = p.getLocation();

        loc.setX(Math.round(loc.getX() * 2) / 2);
        loc.setY(Math.round(loc.getY() * 2) / 2);
        loc.setZ(Math.round(loc.getZ() * 2) / 2);
        loc.setPitch(Math.round(loc.getPitch() * 2) / 2);
        loc.setYaw(Math.round(loc.getYaw() * 2) / 2);

        config.setDouble("spawn.x", loc.getX());
        config.setDouble("spawn.y", loc.getY());
        config.setDouble("spawn.z", loc.getZ());
        config.setDouble("spawn.pitch", loc.getPitch());
        config.setDouble("spawn.yaw", loc.getYaw());
        config.setString("spawn.world", loc.getWorld().getName());
        
        SpawnCommand.spawnLocation = loc;

        DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn.set");
    }
}
