package com.desiremc.hcf.commands.spawn;

import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends ValidCommand
{

    private FileHandler config = DesireHCF.getConfigHandler();

    public SpawnCommand()
    {
        super("spawn", "Teleport to the server spawn.", Rank.GUEST, new String[] {});

        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        Location loc = new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"),
                config.getDouble("spawn.z"), (float) config.getDouble("spawn.yaw").doubleValue(), (float) config.getDouble("spawn.pitch").doubleValue());

        p.teleport(loc);
        DesireHCF.getLangHandler().sendRenderMessage(sender, "spawn");
    }
}
