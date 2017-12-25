package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FactionChannelParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.faction.FactionChannel;
import com.desiremc.hcf.validators.FactionChannelValidator;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class FactionChatCommand extends FactionValidCommand
{
    public FactionChatCommand()
    {
        super("chat", "Switch your chat channel", true, new String[] {"c"});

        addSenderValidator(new SenderHasFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(FactionChannel.class)
                .setName("channel")
                .setParser(new FactionChannelParser())
                .addValidator(new FactionChannelValidator())
                .setOptional()
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (!arguments.get(0).hasValue())
        {
            FactionChannel channel = FactionChannel.getNext(sender.getChannel());
            sender.setChannel(channel);

            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.chat.switch", true, false, "{channel}", StringUtils.capitalize(channel.name().toLowerCase()));
        }
        else
        {
            FactionChannel channel = (FactionChannel) arguments.get(0).getValue();
            sender.setChannel(channel);

            DesireHCF.getLangHandler().sendRenderMessage(sender.getSender(), "factions.chat.switch", true, false, "{channel}", StringUtils.capitalize(channel.name().toLowerCase()));
        }
    }
}
