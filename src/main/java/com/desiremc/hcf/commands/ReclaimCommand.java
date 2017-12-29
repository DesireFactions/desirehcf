package com.desiremc.hcf.commands;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.validators.SenderDonorValidator;
import com.desiremc.core.validators.SenderHasFreeSlotValidator;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.SenderHasNotReclaimedValidator;

public class ReclaimCommand extends FactionValidCommand
{

    public ReclaimCommand(String name, String description)
    {
        super("reclaim", "Claim your rank items.");

        addSenderValidator(new SenderDonorValidator());
        addSenderValidator(new SenderHasNotReclaimedValidator());
        addSenderValidator(new SenderHasFreeSlotValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        sender.addLives(sender.getUnclaimedLives());

        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.lives", true, false,
                "{lives}", sender.getUnclaimedLives());

        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("reclaim.lives_broadcast", false, false));

        sender.setUnclaimedLives(0);
        sender.save();

    }

}
