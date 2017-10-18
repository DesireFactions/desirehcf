package com.desiremc.hcf.commands;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.core.validators.PlayerValidator;
import com.desiremc.hcf.old_gui.CustomIS;
import com.desiremc.hcf.old_gui.ItemGUI;
import com.desiremc.hcf.old_gui.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class AchievementCommand extends ValidCommand
{

    public AchievementCommand()
    {
        super("achievement", "List all aquired achievements.", Rank.GUEST, new String[]{}, "achieve");
        addValidator(new PlayerValidator());
    }

    public ItemGUI getAchievements(UUID uuid)
    {
        return new ItemGUI(null, Bukkit.getPlayer(uuid), 54)
        {
            @Override
            public String getName()
            {
                return "Achievements";
            }

            @Override
            public boolean isCloseOnClick()
            {
                return false;
            }

            @SuppressWarnings("deprecation")
            @Override
            public void registerItems()
            {
                List<Achievement> achievements = SessionHandler.getSession(uuid).getAchievements();

                if (achievements.size() == 0)
                {
                    for (int i = 0; i < 54; i++)
                    {
                        set(i, new MenuItem(new CustomIS().setMaterial(Material.STAINED_GLASS_PANE).setData(DyeColor.RED.getWoolData()).setName(ChatColor.RED + "You haven't unlocked any achievements!").addLore(ChatColor.RED + "FeelsBadMan"), new Runnable()
                        {
                            @Override
                            public void run()
                            {

                            }
                        }));
                    }
                    return;
                }

                String title = ChatUtils.getNameWithRankColor(uuid, true);
                int i = 0;
                for (Achievement s : achievements)
                {
                    set(i, new MenuItem(new CustomIS().setMaterial(Material.PAPER).setName(ChatColor.YELLOW + "Achievement #" + (++i)).addLore(ChatColor.DARK_GRAY + "---------------------------").addLore(ChatColor.GRAY + "Name: " + ChatColor.YELLOW + s.getName()).addLore(ChatColor.GRAY + "Desc: " + ChatColor.YELLOW + s.getDescription())
                            .addLore(ChatColor.GRAY + "Reward: " + ChatColor.YELLOW + s.getReward() + " Tokens").addLore(ChatColor.DARK_GRAY + "---------------------------").addLore(ChatColor.GRAY + "This achievement was earned by: " + title), new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Link in chat. //
                        }
                    }));
                    i++;
                }
            }
        };
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        getAchievements(player.getUniqueId()).show();
    }

}
