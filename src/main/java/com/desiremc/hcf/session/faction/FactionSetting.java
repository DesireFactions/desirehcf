package com.desiremc.hcf.session.faction;

public enum FactionSetting
{

    CONNECTION_MESSAGES("");
    
    private String description;
    
    private FactionSetting(String description)
    {
        this.description = description;
    }
    
    public String getName()
    {
        return name().replace("_", " ");
    }
    
    public String getDescription()
    {
        return description;
    }

    public static FactionSetting getFactionSetting(String name)
    {
        for (FactionSetting factionSetting : values())
        {
            if (factionSetting.name().equalsIgnoreCase(name) || factionSetting.name().replace("_", " ").equalsIgnoreCase(name))
            {
                return factionSetting;
            }
        }
        return null;
    }

}
