package me.borawski.hcf.parser;

import org.bukkit.command.CommandSender;

import me.borawski.hcf.util.DateUtils;

public class SecondsParser implements ArgumentParser {

    @Override
    public Object parseArgument(CommandSender sender, String label, String arg) {
        long time;
        try {
            time = DateUtils.parseDateDiff(arg, true);
        } catch (Exception e) {
            LANG.sendString(sender, "not_time");
            return null;
        }
        return time / 1000;
    }

}
