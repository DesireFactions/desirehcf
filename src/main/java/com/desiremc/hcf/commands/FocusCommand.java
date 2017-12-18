package com.desiremc.hcf.commands;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.newparsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import java.util.List;

public class FocusCommand extends FactionValidCommand
{
    public FocusCommand()
    {
        super("focus", "Focus your faction on a player", Rank.GUEST);

        addSenderValidator(new SenderHasFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) arguments.get(0).getValue();

        EntityPlayer entityPlayer = ((CraftPlayer) target.getPlayer()).getHandle();
        entityPlayer.displayName = ChatColor.RED + "newName";

        for (FSession session : sender.getFaction().getMembers())
        {
            DesireHCF.getLangHandler().sendRenderMessage(session.getPlayer(), "region.delete", "{target}", target.getName());

            ((CraftPlayer) session.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
        }
    }
}
