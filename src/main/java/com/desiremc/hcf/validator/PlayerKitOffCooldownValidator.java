package com.desiremc.hcf.validator;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.HKit;

public class PlayerKitOffCooldownValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        HCFSession session = HCFSessionHandler.getHCFSession(sender);
        HKit kit = (HKit) arg;
        long cooldown = session.getKitCooldown(kit);

        if (session.hasKitCooldown(kit))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.has_cooldown",
                    "{kit}", kit.getName(),
                    "{time}", DateUtils.formatDateDiff(System.currentTimeMillis() + cooldown));
            return false;
        }

        return true;
    }

}
