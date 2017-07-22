package me.borawski.hcf.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.Core;
import me.borawski.hcf.api.LangHandler;
import me.borawski.hcf.parser.ArgumentParser;
import me.borawski.hcf.session.Rank;
import me.borawski.hcf.validator.CommandValidator;

/**
 * @author Ryan Radomski
 *
 */
public abstract class ValidCommand {

    protected List<ValidCommand> subCommands;

    protected String name;

    protected String description;

    protected Rank requiredRank;

    protected String[] aliases;

    protected String[] args;

    private List<CommandValidator> validators;

    private ArgumentParser[] parsers;

    private Map<String, Integer> argsMap;

    protected static final LangHandler LANG = Core.getLangHandler();

    /**
     * @param name
     * @param description
     * @param permission
     * @param aliases
     */
    public ValidCommand(String name, String description, Rank requiredRank, String args[], String... aliases) {
        this.name = name;
        this.description = description;
        this.requiredRank = requiredRank;
        this.aliases = aliases;
        this.subCommands = new ArrayList<>();
        this.validators = new LinkedList<CommandValidator>();
        this.argsMap = new HashMap<String, Integer>();
        this.args = args;

        for (int i = 0; i < args.length; i++) {
            argsMap.put(args[i], i);
        }

        parsers = new ArgumentParser[args.length];
    }

    /**
     * Execute the command with the proper parameter.
     * 
     * @param sender
     * @param label
     * @param args
     */
    public void run(CommandSender sender, String label, String[] args) {
        // all commands have a psuedo validator that returns the usage message
        // if an improper amount
        // of arguments have been sent
        if (args.length != this.args.length) {
            LANG.sendUsageMessage(sender, label, this.args);
            return;
        }

        Object[] validArgs = new Object[args.length];

        //only run the command if all parsers pass and all arguments have a parser
        for (int i = 0; i < parsers.length; i++) {
            if (parsers[i] == null) {
                throw new IllegalStateException("No parser has been set for argument " + args[i] + ".");
            } else {
                Object parsed = parsers[i].parseArgument(sender, label, args[i]);

                if (parsed == null) {
                    return;
                }

                validArgs[i] = parsed;
            }
        }
        
        // if any validators fail, don't run the command
        for (CommandValidator v : validators) {
            if (!v.validate(sender, label, validArgs)) {
                return;
            }
        }

        this.validRun(sender, label, (Object[]) args);
    }

    public void addValidator(CommandValidator validator, String... argsToValidate) {
        validator.setArgsToValidate(argsMap, argsToValidate);
        validators.add(validator);
    }

    public void addParser(ArgumentParser parser, String... argsToParse) {
        for (String arg : argsToParse) {
            Integer index = argsMap.get(arg);

            if (index == null) {
                throw new IllegalArgumentException("Argument " + arg + " not found.");
            }

            parsers[argsMap.get(arg)] = parser;
        }
    }

    public abstract void validRun(CommandSender sender, String label, Object... args);

    /**
     * @return the name of the command.
     */
    public String getName() {
        return name;
    }

    /**
     * @return a description of the command.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the permission required to use the command.
     */
    protected Rank getRequiredRank() {
        return requiredRank;
    }

    /**
     * @return the aliases of the command.
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Checks whether the passed in command string matches this particular
     * custom command.
     * 
     * @param command
     * @return whether the parameter matches the command.
     */
    public boolean matches(String command) {
        if (command == null) {
            return false;
        }
        if (command.equalsIgnoreCase(name)) {
            return true;
        }
        for (String alias : aliases) {
            if (command.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a new subcommand.
     * 
     * @param subCommand
     */
    public void addSubCommand(ValidCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    /**
     * @param cmd
     * @return the subcommand of the given name. Null if it does not exist.
     */
    public ValidCommand getSubCommand(String cmd) {
        for (ValidCommand command : this.subCommands) {
            if (command.matches(cmd)) {
                return command;
            }
        }
        return null;
    }

    protected String getSubCommandString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < subCommands.size(); i++) {
            sb.append(subCommands.get(i).getName());
            if (i != subCommands.size() - 1) {
                sb.append('/');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    public String[] getArgs() {
        return args;
    }

}