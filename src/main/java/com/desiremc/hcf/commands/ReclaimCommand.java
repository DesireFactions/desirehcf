package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.SenderHasFreeSlotValidator;
import com.desiremc.crates.data.CrateHandler;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.SenderCanReclaim;
import com.desiremc.hcf.validators.SenderHasNotReclaimedValidator;
import org.bukkit.Bukkit;

import java.util.List;

public class ReclaimCommand extends FactionValidCommand
{

    public ReclaimCommand()
    {
        super("reclaim", "Claim your rank items.");

        addSenderValidator(new SenderCanReclaim());
        addSenderValidator(new SenderHasNotReclaimedValidator());
        addSenderValidator(new SenderHasFreeSlotValidator());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        int lives;
        int keys;

        if (sender.getRank() == Rank.COMMODORE || sender.getRank() == Rank.YOUTUBER)
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
        else if (sender.getRank() == Rank.BETA || sender.getRank() == Rank.PARTNER)
        {
            lives = 20;
            keys = 15;
        }
        else
        {
            return;
        }

        sender.addLives(lives);

        String rank = sender.getRank().getDisplayName();

        if (sender.getRank() == Rank.BETA || sender.getRank() == Rank.PARTNER) {
            rank = "Grandmaster";
        } else if (sender.getRank() == Rank.YOUTUBER) {
            rank = "Commodore";
        }

        CrateHandler.getCrate(rank).addPendingKeys(sender.getUniqueId(), keys);

        Bukkit.broadcastMessage(DesireHCF.getLangHandler().renderMessage("reclaim.broadcast", false, false, "{player}", sender.getName(), "{rank}", sender.getRank().getDisplayName()));

        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.lives", true, false,
                "{lives}", lives);
        DesireHCF.getLangHandler().sendRenderMessage(sender, "reclaim.keys", true, false,
                "{amount}", keys, "{type}", rank);

        sender.setClaimedRank(true);
        sender.save();
    }

}
