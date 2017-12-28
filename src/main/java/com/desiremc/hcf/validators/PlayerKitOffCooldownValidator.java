package com.desiremc.hcf.validators;

import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.HKit;

public class PlayerKitOffCooldownValidator implements Validator<HKit>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, HKit kit)
    {
        FSession session = FSessionHandler.getGeneralFSession(sender.getUniqueId());
        long cooldown = session.getKitCooldown(kit);

        if (session.hasKitCooldown(kit))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "kits.has_cooldown", true, false,
                    "{kit}", kit.getName(),
                    "{time}", DateUtils.formatDateDiff(System.currentTimeMillis() + cooldown));
            return false;
        }

        return true;
    }

}
