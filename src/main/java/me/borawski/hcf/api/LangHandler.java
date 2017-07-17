package me.borawski.hcf.api;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Michael Ziluck
 *
 */
public class LangHandler extends FileHandler {

    private String prefix;
    private boolean usePrefix;

    /**
     * Create a new {@link LangHandler} based on the {@link FileHandler}. Also
     * loads the prefix.
     * 
     * @param file
     */
    public LangHandler(FileConfiguration file) {
        super(file);
        prefix = super.getString("prefix");
    }

    /**
     * Gets a formatted string from the config file. Replaces any color place
     * holders as well. If the string does not exist in the config, returns
     * null.
     * 
     * @param string
     * @return the formatted string.
     */
    public String getString(String string) {
        return prefix + " §r" + super.getString(string);
    }

    /**
     * Shorthand to send getString to {@link CommandSender}
     * 
     * @param sender
     * @param string
     */
    public void sendString(CommandSender sender, String string) {
        sender.sendMessage(getString(string));
    }

    /**
     * Render a message using the format rendered in lang.yml
     * 
     * @param string
     * @param args
     * @return
     */
    public String renderMessage(String string, String... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("Message rendering requires arguments of an even number. " + Arrays.toString(args) + " given.");
        }

        String message = getString(string);
        for (int i = 0; i < args.length; i += 2) {
            message = message.replace(args[i], args[i + 1]);
        }

        return message;
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     * 
     * @param sender
     * @param string
     * @param args
     */
    public void sendRenderMessage(CommandSender sender, String string, String... args) {
        sender.sendMessage(renderMessage(string, args));
    }

    /**
     * Render a usage message using the format specified in lang.yml
     * 
     * @param args
     * @return
     */
    public String usageMessage(String label, String... args) {
        String argsString = "/" + label;

        for (String arg : args) {
            argsString += " [" + arg + "]";
        }

        return renderMessage("usage-message", "{usage}", argsString);
    }

    /**
     * Shorthand to send a usage message to a {@link CommandSender}
     * 
     * @param sender
     * @param usage
     */
    public void sendUsageMessage(CommandSender sender, String label, String... args) {
        sender.sendMessage(usageMessage(label, args));
    }

}