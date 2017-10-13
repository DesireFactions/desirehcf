package com.desiremc.hcf.api;

import com.desiremc.core.session.HCFSession;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.Arrays;

public class LangHandler extends FileHandler
{

    private String prefix;

    /**
     * Create a new {@link LangHandler} based on the {@link FileHandler}. Also
     * loads the prefix.
     *
     * @param file
     */
    public LangHandler(File file)
    {
        super(file);
        boolean usePrefix = super.getBoolean("prefix.use");
        if (usePrefix)
        {
            prefix = super.getString("prefix.text");
        }

    }

    /**
     * Gets a formatted string from the config file. Replaces any color place
     * holders as well. If the string does not exist in the config, returns
     * null.
     *
     * @param string
     * @return the formatted string.
     */
    public String getString(String string)
    {
        String str = super.getString(string);
        if (str == null)
        {
            str = "==ERROR==";
        }
        return (prefix != null && !str.startsWith("`") ? prefix + " " : "") + "§r" + (!str.startsWith("`") ? str : str.substring(1, str.length()));
    }

    /**
     * Shorthand to send getString to {@link CommandSender}
     *
     * @param sender
     * @param string
     */
    public void sendString(CommandSender sender, String string)
    {
        sender.sendMessage(getString(string));
    }

    /**
     * Render a message using the format rendered in lang.yml
     *
     * @param string
     * @param args
     * @return
     */
    public String renderMessage(String string, String... args)
    {
        if (args.length % 2 != 0)
        {
            throw new IllegalArgumentException("Message rendering requires arguments of an even number. " + Arrays.toString(args) + " given.");
        }

        String message = getString(string);
        for (int i = 0; i < args.length; i += 2)
        {
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
    public void sendRenderMessage(CommandSender sender, String string, String... args)
    {
        sender.sendMessage(renderMessage(string, args));
    }

    public void sendRenderMessage(HCFSession s, String string, String... args)
    {
        CommandSender sender = Bukkit.getPlayer(s.getUniqueId());
        sendRenderMessage(sender, string, args);
    }

    public void sendRenderMessage(Session s, String string, String... args)
    {
        CommandSender sender = Bukkit.getPlayer(s.getUniqueId());
        sendRenderMessage(sender, string, args);
    }

    public void sendRenderMessage(CommandSender sender, String string, boolean center, String... args)
    {
        if (center)
        {
            ChatUtils.sendCenteredMessage(sender, renderMessage(string, args));
        }
        else
        {
            sender.sendMessage(renderMessage(string, args));
        }
    }

    public void sendRenderMessage(HCFSession s, String string, boolean center, String... args)
    {
        CommandSender sender = Bukkit.getPlayer(s.getUniqueId());
        if (center)
        {
            ChatUtils.sendCenteredMessage(sender, renderMessage(string, args));
        }
        else
        {
            sender.sendMessage(renderMessage(string, args));
        }
    }

    /**
     * Render a usage message using the format specified in lang.yml
     *
     * @param args
     * @return
     */
    public String usageMessage(String label, String... args)
    {
        String argsString = "/" + label;

        for (String arg : args)
        {
            argsString += " [" + arg + "]";
        }

        return renderMessage("usage-message", "{usage}", argsString);
    }

    /**
     * Shorthand to send a usage message to a {@link CommandSender}
     *
     * @param sender
     */
    public void sendUsageMessage(CommandSender sender, String label, String... args)
    {
        sender.sendMessage(usageMessage(label, args));
    }

    public String getPrefix()
    {
        return prefix;
    }

}