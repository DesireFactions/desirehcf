package com.desiremc.hcf.api.commands;

import java.util.List;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;

public abstract class FactionValidCommand extends ValidCommand
{
    /**
     * Constructs a new command with the rank of {@link Rank#GUEST}.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param blocksConsole if this command is unusable by the console.
     * @param aliases the aliases of the command.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, boolean blocksConsole, String[] aliases)
    {
        super(name, description, blocksConsole, aliases);
    }

    /**
     * Constructs a new command without any aliases and the rank of {@link Rank#GUEST}.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param blocksConsole if this command is unusable by the console.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, boolean blocksConsole)
    {
        super(name, description, blocksConsole);
    }

    /**
     * Constructs a new command with the given arguments. This will initialize the arguments list as well as the values
     * table. Additionally, it will automatically convert all aliases to lowercase.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param requiredRank the required rank for the command.
     * @param blocksConsole if this command is unusable by the console.
     * @param aliases the aliases of the command.
     */
    public FactionValidCommand(String name, String description, Rank requiredRank, boolean blocksConsole, String[] aliases)
    {
        super(name, description, requiredRank, blocksConsole, aliases);
    }

    /**
     * Constructs a new command without any aliases.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param requiredRank the required rank for the command.
     * @param blocksConsole if this command is unusable by the console.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, Rank requiredRank, boolean blocksConsole)
    {
        super(name, description, requiredRank, blocksConsole);
    }

    /**
     * Constructs a new command that is usable by the console.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param requiredRank the required rank for the command.
     * @param aliases the aliases of the command.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, Rank requiredRank, String[] aliases)
    {
        super(name, description, requiredRank, aliases);
    }

    /**
     * Constructs a new command without any aliases and is usable by the console.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param requiredRank the required rank for the command.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, Rank requiredRank)
    {
        super(name, description, requiredRank);
    }

    /**
     * Constructs a new command with the rank of {@link Rank#GUEST} and is usable by the console.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @param aliases the aliases of the command.
     * @see #ValidCommand(String, String, Rank, boolean, String[])
     */
    public FactionValidCommand(String name, String description, String[] aliases)
    {
        super(name, description, aliases);
    }

    /**
     * Constructs a new command without any aliases, the rank of {@link Rank#GUEST}, and is usable by the console.
     * 
     * @param name the name of the command.
     * @param description the description of the command.
     * @see #ValidCommand(String, String, String[])
     */
    public FactionValidCommand(String name, String description)
    {
        super(name, description);
    }

    @Override
    public final void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        FSession hcfSender = FSessionHandler.getOnlineFSession(sender.getUniqueId());

        this.validFactionRun(hcfSender, label, arguments);
    }

    /**
     * A convenience method to use instead of validRun. FactionValidCommand will automatically convert Session to
     * HCFSession and then call this method.
     * 
     * @param sender the sender of the command.
     * @param label the command label used.
     * @param arguments the arguments of the command.
     */
    public abstract void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments);

}
