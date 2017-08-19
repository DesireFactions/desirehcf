package me.borawski.hcf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import me.borawski.hcf.Core;
import me.borawski.hcf.manual.Manual;
import me.borawski.hcf.manual.ManualPage;
import me.borawski.hcf.manual.YouTuberManual;
import me.borawski.hcf.session.Rank;

/**
 * Created by Ethan on 5/16/2017.
 */
public class ManualUtil {

    public static void initializeManuals(Map<Rank, Manual> map) {
        map.put(Rank.YOUTUBER, new YouTuberManual());
    }

    public static ItemStack newBook(Rank rank) {
        BookMeta meta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.WRITTEN_BOOK);
        meta.setTitle(rank.getMain() + rank.getDisplayName() + " Manual");
        meta.setAuthor("DesireHCF");

        List<String> stringList = new ArrayList<>();
        // stringList.add(getCover(rank).getContents().toString().replace("[",
        // "").replace("]", "").replace(",", ""));
        for (ManualPage s : Core.getInstance().getManualManager().getManualMap().get(rank).getPages()) {
            String page = s.getContents().toString().replace("[", "").replace("]", "").replace(",", "");
            stringList.add(page);
        }

        meta.setPages(stringList);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(meta);
        return book;
    }



    public static ManualPage getCover(Rank rank) {
        return new ManualPage("cover")
                .addString("&7Welcome to the")
                .addString("\n" + rank.getPrefix() + " Manual")
                .addString("")
                .addString("\n")
                .addString("\n" + "&4&lYOUR RANK:")
                .addString("\n")
                .addString("\n" + "&7Name: " + rank.getMain() + rank.getDisplayName().toUpperCase() + "")
                .addString("\n" + "&7Perm: &a" + rank.getId() + "")
                .addString("\n" + "&7Tag: " + rank.getPrefix() + "")
                .addString("\n");
    }

    public static void openManual(Rank youtuber, Player sender) {
    }

}
