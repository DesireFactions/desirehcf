package com.desiremc.hcf.validators;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionSenderValidator;
import com.desiremc.hcf.session.FSession;
import org.bukkit.Location;
import org.bukkit.World;

public class SenderCanFactionStuck extends FactionSenderValidator {
    @Override
    public boolean factionsValidate(FSession sender) {
        Location loc = sender.getLocation();
        if (loc.getWorld().getEnvironment() == World.Environment.THE_END) {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.stuck.invalid", true, false);
            return false;
        }
        return true;
    }
}
