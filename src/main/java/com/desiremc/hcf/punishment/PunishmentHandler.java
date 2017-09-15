package com.desiremc.hcf.punishment;

import java.util.UUID;

import com.desiremc.hcf.api.LangHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.mongodb.morphia.dao.BasicDAO;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.punishment.Punishment.Type;

public class PunishmentHandler extends BasicDAO<Punishment, Integer>
{

    private static PunishmentHandler instance;

    public PunishmentHandler()
    {
        super(Punishment.class, DesireCore.getInstance().getMongoWrapper().getDatastore());
    }

    public static void initialize()
    {
        instance = new PunishmentHandler();
    }

    public void issuePunishment(Type type, UUID punished, UUID issuer, long time, String reason)
    {
        Punishment punishment = new Punishment();
        punishment.setIssued(System.currentTimeMillis());
        punishment.setType(type);
        punishment.setPunished(punished);
        punishment.setIssuer(issuer);
        punishment.setExpirationTime(time + System.currentTimeMillis());
        punishment.setReason(reason);
        save(punishment);

        if (Bukkit.getPlayer(punished) != null)
        {
            if (type == Punishment.Type.BAN)
            {
                Bukkit.getPlayer(punished).kickPlayer(DesireCore.getLangHandler().renderMessage("punishment.ban"));
            } else if (type == Punishment.Type.MUTE)
            {
                Bukkit.getPlayer(punished).sendMessage(DesireCore.getLangHandler().renderMessage("punishment.mute"));
            }
        }
    }

    public Punishment getPunishment(UUID uuid)
    {
        Punishment punishment = findOne("uuid", uuid);
        if (punishment != null)
        {
            System.out.println(DesireCore.getLangHandler().renderMessage("punishment.found", "{player}", uuid + ""));
            System.out.println(DesireCore.getLangHandler().renderMessage("punishment.type", "{reason}", punishment.getReason()));
            return punishment;
        }

        System.out.println(DesireCore.getLangHandler().renderMessage("punishment.notfound", "{player}", uuid + ""));
        return null;
    }

    public static PunishmentHandler getInstance()
    {
        return instance;
    }

}
