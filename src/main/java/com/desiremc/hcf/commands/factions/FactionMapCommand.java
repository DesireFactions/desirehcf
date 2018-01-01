package com.desiremc.hcf.commands.factions;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.tasks.FactionMapTask;

public class FactionMapCommand extends FactionValidCommand
{

    private static HashMap<UUID, FactionMapTask> tasks = new HashMap<>();

    public FactionMapCommand()
    {
        super("map", "View the edges of the nearby factions.");
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> arguments)
    {
        if (tasks.containsKey(sender.getUniqueId()))
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.map.cancel.early", true, false);
            tasks.remove(sender.getUniqueId()).cancel();
        }
        else
        {
            DesireHCF.getLangHandler().sendRenderMessage(sender, "factions.map.start", true, false);
            FactionMapTask task = new FactionMapTask(sender);
            task.run();
            tasks.put(sender.getUniqueId(), task);
        }
    }

    /**
     * If this method is removed, or the task.cancel() is removed, you must also cancel it from the FactionMapTask.
     * 
     * @param uuid the map session to stop.
     */
    public static void stopMap(UUID uuid)
    {
        FactionMapTask task = tasks.remove(uuid);
        if (task != null)
        {
            task.cancel();
        }

    }

}
