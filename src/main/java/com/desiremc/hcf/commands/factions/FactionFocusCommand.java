package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.api.commands.FactionValidCommand;
import com.desiremc.hcf.parsers.FSessionParser;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.validators.SenderHasFactionValidator;
import com.desiremc.hcf.validators.TargetNotSameFactionValidator;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import java.util.List;

public class FactionFocusCommand extends FactionValidCommand
{
    public FactionFocusCommand()
    {
        super("focus", "Focus your faction on a player", Rank.GUEST, true, new String[] {});

        addSenderValidator(new SenderHasFactionValidator());

        addArgument(CommandArgumentBuilder.createBuilder(FSession.class)
                .setName("target")
                .setParser(new FSessionParser())
                .addValidator(new TargetNotSameFactionValidator())
                .build());
    }

    @Override
    public void validFactionRun(FSession sender, String[] label, List<CommandArgument<?>> args)
    {
        FSession target = (FSession) arguments.get(0).getValue();

        EntityPlayer entityPlayer = ((CraftPlayer) target.getPlayer()).getHandle();
        entityPlayer.displayName = ChatColor.RED + entityPlayer.getName();

        for (FSession session : sender.getFaction().getOnlineMembers())
        {
            DesireHCF.getLangHandler().sendRenderMessage(session.getPlayer(), "factions.focus", true, false, "{player}", target.getName());

            ((CraftPlayer) session.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
        }
    }
}