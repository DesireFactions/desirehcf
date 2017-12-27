package com.desiremc.hcf.commands.factions;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.utils.StringUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.Faction;

public class FactionShowCommand extends FactionValidCommand
{

    public FactionShowCommand()
    {
        super("show", "Show information about your faction.", true, new String[] {"who", "info"});

        addArgument(CommandArgumentBuilder.createBuilder(Faction.class)
                .setName("faction")
                .setParser(new FactionParser())
                .setOptional()
                .setAllowsConsole()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (!arguments.get(0).hasValue() && !sender.hasFaction())
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender.getSession(), "factions.no_faction", true, false);
            return;
        }

        Faction faction = arguments.get(0).hasValue() ? (Faction) arguments.get(0).getValue() : sender.getFaction();

        List<String> values = DesireHCF.getLangHandler().getStringList("factions.who");

        for (String value : values)
        {
            if (value.contains("{description}") && faction.getDescription() == null)
            {
                continue;
            }
            sender.sendMessage(processPlaceholders(value, faction));
        }
    }

    private String processPlaceholders(String value, Faction faction)
    {
        value = DesireHCF.getLangHandler().renderString(value,
                "{faction}", faction.getName(),
                "{online}", faction.getOnlineMembers().size(),
                "{max}", faction.getMemberSize(),
                "{description}", faction.getDescription(),
                "{leader}", faction.getLeader().getName(),
                "{leader_kills}", faction.getLeader().getTotalKills(),
                "{kills}", faction.getTotalKills(),
                "{balance}", StringUtils.doubleFormat(faction.getBalance()),
                "{dtr}", StringUtils.doubleFormat(faction.getDTR()));

        StringBuilder sb = new StringBuilder();

        for (FSession member : faction.getMembers())
        {
            if (member.isOnline())
            {
                sb.append("§a");
            }
            else
            {
                sb.append("§c");
            }
            sb.append(member.getFactionRank().getPrefix());
            sb.append(member.getName());
            sb.append("§e, ");
        }
        sb.setLength(sb.length() - 2);

        value = value.replace("{members}", sb.toString());

        return value;
    }

}
