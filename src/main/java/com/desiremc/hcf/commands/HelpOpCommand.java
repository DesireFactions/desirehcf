package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.hcf.DesireHCF;

import java.util.List;

public class HelpOpCommand extends ValidCommand
{

    public HelpOpCommand()
    {
        super("helpop", "Request help from online staff.", true);

        addArgument(CommandArgumentBuilder.createBuilder(String.class)
                .setName("message")
                .setParser(new StringParser())
                .setVariableLength()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        String message = DesireHCF.getLangHandler().renderMessage("helpop.target", false, false, "{player}", sender.getName(), "{message}", arguments.get(0).getValue());

        for (Session staff : SessionHandler.getOnlineStaff())
        {
            staff.sendMessage(message);
        }

        DesireHCF.getLangHandler().sendRenderMessage(sender, "helpop.self", true, false);
    }
}
