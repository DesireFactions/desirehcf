package com.desiremc.hcf.commands;

import java.util.List;

import org.bukkit.Bukkit;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.SenderDonorValidator;
import com.desiremc.core.validators.SenderHasFreeSlotValidator;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.SenderHasNotReclaimedValidator;

public class ReclaimCommand extends FactionValidCommand
{

    public ReclaimCommand()
    {
        super("reclaim", "Claim your rank items.");

        addSenderValidator(new SenderDonorValidator());
        addSenderValidator(new SenderHasNotReclaimedValidator());
        addSenderValidator(new SenderHasFreeSlotValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        int lives;
        int keys;

        if (sender.getRank() == Rank.COMMODORE)
        {
            lives = 5;
            keys = 8;
        }
        else if (sender.getRank() == Rank.PREMIER)
        {
            lives = 10;
            keys = 8;
        }
        else if (sender.getRank() == Rank.GRANDMASTER)
        {
            lives = 15;
            keys = 8;
        }
        else if (sender.getRank() == Rank.BETA)
        {
            lives = 20;
            keys = 15;
        }
        else
        {
            return;
        }
        sender.addLives(lives);

        CrateHandler.getCrate(sender.getRank().getDisplayName()).addPendingKeys(sender.getUniqueId(), keys);

        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("reclaim.broadcast", false, false, "{player}", sender.getName(), "{rank}", sender.getRank().getDisplayName()));

        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.lives", true, false,
                "{lives}", lives);
        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.keys", true, false,
                "{lives}", lives, "{type}", sender.getRank().getDisplayName());

        sender.setClaimedRank(true);
        sender.save();
    }

}
