package com.desiremc.hcf.session.faction;

public enum FactionChannel
{
    GENERAL,
    ALLY,
    FACTION;

    public static FactionChannel getNext(FactionChannel channel)
    {
        if (channel == FactionChannel.GENERAL)
        {
            return FactionChannel.ALLY;
        }
        else if (channel == FactionChannel.ALLY)
        {
            return FactionChannel.FACTION;
        }
        else
        {
            return FactionChannel.GENERAL;
        }
    }
}
