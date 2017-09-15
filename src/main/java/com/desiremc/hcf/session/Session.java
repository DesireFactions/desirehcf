package com.desiremc.hcf.session;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.punishment.Punishment;
import com.desiremc.hcf.punishment.Punishment.Type;
import com.desiremc.hcf.util.ChatUtils;

@Entity(value = "players", noClassnameStored = true)
public class Session
{

    @Id
    private UUID uuid;

    @Indexed
    private String name;

    private Rank rank;

    private int tokens;

    private int level;

    private int exp;

    @Property("first_login")
    private long firstLogin;

    @Property("last_login")
    private long lastLogin;

    @Property("total_played")
    private long totalPlayed;

    @Property("safe_timer")
    private int safeTimer;

    private int lives;

    @Indexed
    private String ip;

    private List<String> achievements;

    private List<UUID> friends;

    private List<UUID> incomingFriendRequests;

    private List<UUID> outgoingFriendRequests;

    private Map<String, String> settings;

    @Transient
    private List<Punishment> activePunishments;

    @Transient
    private PVPTimer pvpTimer;

    public Session()
    {
        pvpTimer = new PVPTimer();
        achievements = new LinkedList<>();
        friends = new LinkedList<>();
        incomingFriendRequests = new LinkedList<>();
        outgoingFriendRequests = new LinkedList<>();
        activePunishments = new LinkedList<>();
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public void setUniqueId(UUID uuid)
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    public int getTokens()
    {
        return tokens;
    }

    public void addTokens(int tokens, boolean notify)
    {
        this.tokens += tokens;
        if (notify)
        {
            DesireCore.getLangHandler().sendRenderMessage(getPlayer(), "tokens.add",
                    "{tokens}", tokens + "");
        }
        SessionHandler.getInstance().save(this);
    }

    public void setTokens(int tokens)
    {
        this.tokens = tokens;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getExp()
    {
        return exp;
    }

    public void setExp(int exp)
    {
        this.exp = exp;
    }

    public long getFirstLogin()
    {
        return firstLogin;
    }

    public void setFirstLogin(long firstLogin)
    {
        this.firstLogin = firstLogin;
    }

    public long getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public long getTotalPlayed()
    {
        return totalPlayed;
    }

    public void setTotalPlayed(long totalPlayed)
    {
        this.totalPlayed = totalPlayed;
    }

    public int getSafeTimeLeft()
    {
        return safeTimer;
    }

    public void setSafeTimeLeft(int safeTimer)
    {
        this.safeTimer = safeTimer;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public List<String> getAchievements()
    {
        return achievements;
    }

    public void setAchievements(List<String> achievements)
    {
        this.achievements = achievements;
    }

    public List<UUID> getFriends()
    {
        return friends;
    }

    public void setFriends(List<UUID> friends)
    {
        this.friends = friends;
    }

    public List<UUID> getIncomingFriendRequests()
    {
        return incomingFriendRequests;
    }

    public void setIncomingFriendRequests(List<UUID> incomingFriendRequests)
    {
        this.incomingFriendRequests = incomingFriendRequests;
    }

    public List<UUID> getOutgoingFriendRequests()
    {
        return outgoingFriendRequests;
    }

    public void setOutgoingFriendRequests(List<UUID> outgoingFriendRequests)
    {
        this.outgoingFriendRequests = outgoingFriendRequests;
    }

    public Map<String, String> getSettings()
    {
        return settings;
    }

    public void setSettings(Map<String, String> settings)
    {
        this.settings = settings;
    }

    public List<Punishment> getActivePunishments()
    {
        return activePunishments;
    }

    public void setActivePunishments(List<Punishment> activePunishments)
    {
        this.activePunishments = activePunishments;
    }

    public Punishment isBanned()
    {
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.BAN)
            {
                if (!p.isRepealed())
                {
                    return p;
                }
            }
        }
        return null;
    }

    public Punishment isMuted()
    {
        for (Punishment p : activePunishments)
        {
            if (p.getType() == Type.MUTE)
            {
                return p;
            }
        }
        return null;
    }

    public void sendMessage(String message)
    {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null)
        {
            p.sendMessage(message);
        }
    }

    public boolean hasAchievement(String string)
    {
        for (String achievement : achievements)
        {
            if (achievement.equalsIgnoreCase(string))
            {
                return true;
            }
        }
        return false;
    }

    public void awardAchievement(Achievement achievement, boolean inform)
    {
        if (hasAchievement(achievement.getId())) return;

        getAchievements().add(achievement.getId());

        if (inform)
        {
            Player player = DesireCore.getInstance().getServer().getPlayer(uuid);
            Session session = SessionHandler.getSession(player);
            DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.header");
            DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.title", true);
            DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.name", true);
            DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.desc", true);
            if (achievement.getReward() > 0)
            {
                DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.reward", true);
            }
            DesireCore.getLangHandler().sendRenderMessage(session, "achievement.award.header");
        }
        if (achievement.getReward() > 0)
        {
            tokens += achievement.getReward();
        }
        SessionHandler.getInstance().save(this);
    }

    public PVPTimer getTimer()
    {
        return pvpTimer;
    }

    public class PVPTimer implements Runnable
    {

        private boolean pause;

        @Override
        public void run()
        {
            if (!pause && safeTimer > 0)
            {
                Bukkit.getScheduler().runTaskLater(DesireCore.getInstance(), this, 20);
            }
            safeTimer--;
        }

        public void pause()
        {
            pause = true;
        }

        public void resume()
        {
            pause = false;
            run();
        }

    }

}
