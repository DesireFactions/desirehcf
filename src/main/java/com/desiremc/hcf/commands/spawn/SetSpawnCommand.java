package com.desiremc.hcf.commands.spawn;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends ValidCommand
{

    private FileHandler config = DesireHCF.getConfigHandler();

    public SetSpawnCommand()
    {
        super("setspawn", "Set the server spawn.", Rank.ADMIN, new String[] {});

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        Location loc = p.getLocation();

        config.setDouble("spawn.x", loc.getX());
        config.setDouble("spawn.y", loc.getY());
        config.setDouble("spawn.z", loc.getZ());
        config.setDouble("spawn.yaw", loc.getYaw());
        config.setDouble("spawn.pitch", loc.getPitch());
        config.setString("spawn.world", loc.getWorld().getName());

        DesireHCF.getLangHandler().sendRenderMessage(sender, "set-spawn");
    }
}
