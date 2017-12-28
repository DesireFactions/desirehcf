package com.desiremc.hcf.handler;

import com.desiremc.hcf.tasks.SOTWTask;

public class SOTWHandler
{

    private static boolean sotw;
    private static SOTWHandler instance;

    public SOTWHandler()
    {
        sotw = false;
        instance = this;
    }

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
        SOTWHandler.setSOTW(true);
        new SOTWTask();
    }
}
