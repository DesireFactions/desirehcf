package com.desiremc.hcf.handler;

import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.tasks.SOTWTask;

public class SOTWHandler
{

    private static boolean sotw;
    private static SOTWHandler instance;

    public static SOTWHandler getInstance()
    {
        return instance;
    }

    public static boolean getSOTW()
    {
        return sotw;
    }

    public static void setSOTW(boolean sotw)
    {
        SOTWHandler.sotw = sotw;
    }

    public void startSOTWTimer()
    {
        sotw = true;
        new SOTWTask().runTaskTimer(DesireHCF.getInstance(), 0L, 20L);
    }

    public static void initialize()
    {
        instance = new SOTWHandler();
        sotw = false;
    }
}
