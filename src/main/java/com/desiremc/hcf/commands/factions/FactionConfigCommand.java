package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionSettingParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionSetting;

public class FactionConfigCommand extends FactionValidCommand
{

    public FactionConfigCommand()
    {
        super("config", "Manage your faction settings.", true, new String[] { "setting", "settings" });

        addArgument(CommandArgumentBuilder.createBuilder(FactionSetting.class)
                .setName("setting")
                .setParser(new FactionSettingParser())
                .setOptional()
                .setVariableLength()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (arguments.get(0).hasValue())
        {
            FactionSetting factionSetting = (FactionSetting) arguments.get(0).getValue();
            if (sender.hasSetting(factionSetting))
            {
                sender.removeSetting(factionSetting);
                DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.config.remove",
                        "{setting}", factionSetting.getName());
            }
            else
            {
                sender.addSetting(factionSetting);
                DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.config.add",
                        "{setting}", factionSetting.getName());
            }
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.config.header");
            for (FactionSetting factionSetting : FactionSetting.values())
            {
                DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.config.list",
                        "{setting}", factionSetting.getName(),
                        "{description}", factionSetting.getDescription());
            }
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.config.header");
        }
    }

}
