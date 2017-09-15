package com.desiremc.hcf.api;

import java.util.UUID;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;

public class EconomyAPI
{

    public static int getTokens(UUID uuid)
    {
        Session session = SessionHandler.getSession(uuid);
        return session.getTokens();
    }

    public static void removeTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getSession(uuid);
        setTokens(uuid, session.getTokens() - amount);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.remove", "{tokens}", amount + "");
        }
    }

    public static void giveTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getSession(uuid);
        setTokens(uuid, session.getTokens() + amount);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.give", "{tokens}", amount + "");
        }
    }

    public static void setTokens(UUID uuid, int amount)
    {
        Session session = SessionHandler.getSession(uuid);
        session.setTokens(amount);
        SessionHandler.getInstance().save(session);
    }

    public static void setTokens(UUID uuid, int amount, boolean inform)
    {
        Session session = SessionHandler.getSession(uuid);
        session.setTokens(amount);
        SessionHandler.getInstance().save(session);
        if (inform)
        {
            DesireCore.getLangHandler().sendRenderMessage(session, "tokens.set", "{tokens}", amount + "");
        }
    }

}
