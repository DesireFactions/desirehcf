package com.desiremc.hcf.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.validator.PlayerHasSafeTimeLeft;

public class PVPCommand extends ValidCommand
{

    public PVPCommand()
    {
        super("pvp", "Disable your PVP timer.", Rank.GUEST, new String[] {});

        addValidator(new PlayerValidator());
        addValidator(new PlayerHasSafeTimeLeft());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(((Player) sender).getUniqueId());

        session.setSafeTimeLeft(0);
        session.save();
        DesireHCF.getLangHandler().sendString(sender, "pvp.disabled");
        EntryRegistry.getInstance().removeValue(session.getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
    }

}
