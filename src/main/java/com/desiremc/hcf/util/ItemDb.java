package com.desiremc.hcf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.desiremc.hcf.DesireCore;

@SuppressWarnings("deprecation")
public class ItemDb {
    
    private final File file;
    private final transient Map<String, Integer> items = new HashMap<String, Integer>();
    private final transient Map<ItemData, List<String>> names = new HashMap<ItemData, List<String>>();
    private final transient Map<ItemData, String> primaryName = new HashMap<ItemData, String>();
    private final transient Map<String, Short> durabilities = new HashMap<String, Short>();
    private final transient Pattern splitPattern = Pattern.compile("((.*)[:+',;.](\\d+))");

    public ItemDb() {
        file = new File(DesireCore.getInstance().getDataFolder(), "items.csv");
        reloadConfig();
    }

    public void reloadConfig() {
        final List<String> lines = getLines(file);
        if (lines.isEmpty()) {
            return;
        }
        durabilities.clear();
        items.clear();
        names.clear();
        primaryName.clear();
        for (String line : lines) {
            line = line.trim().toLowerCase(Locale.ENGLISH);
            if (line.length() > 0 && line.charAt(0) == '#') {
                continue;
            }
            final String[] parts = line.split("[^a-z0-9]");
            if (parts.length < 2) {
                continue;
            }
            final int numeric = Integer.parseInt(parts[1]);
            final short data = parts.length > 2 && !parts[2].equals("0") ? Short.parseShort(parts[2]) : 0;
            String itemName = parts[0].toLowerCase(Locale.ENGLISH);
            durabilities.put(itemName, data);
            items.put(itemName, numeric);
            ItemData itemData = new ItemData(numeric, data);
            if (names.containsKey(itemData)) {
                List<String> nameList = names.get(itemData);
                nameList.add(itemName);
                Collections.sort(nameList, new LengthCompare());
            } else {
                List<String> nameList = new ArrayList<String>();
                nameList.add(itemName);
                names.put(itemData, nameList);
                primaryName.put(itemData, itemName);
            }
        }
    }

    public ItemStack get(final String id, final int quantity) {
        final ItemStack retval = get(id.toLowerCase(Locale.ENGLISH));
        retval.setAmount(quantity);
        return retval;
    }

    public ItemStack get(final String id) {
        int itemid = 0;
        String itemname = null;
        short metaData = 0;
        Matcher parts = splitPattern.matcher(id);
        if (parts.matches()) {
            itemname = parts.group(2);
            metaData = Short.parseShort(parts.group(3));
        } else {
            itemname = id;
        }
        if (NumberUtils.isDigits(itemname)) {
            itemid = Integer.parseInt(itemname);
        } else if (NumberUtils.isDigits(id)) {
            itemid = Integer.parseInt(id);
        } else {
            itemname = itemname.toLowerCase(Locale.ENGLISH);
        }
        if (itemid < 1) {
            if (items.containsKey(itemname)) {
                itemid = items.get(itemname);
                if (durabilities.containsKey(itemname) && metaData == 0) {
                    metaData = durabilities.get(itemname);
                }
            } else if (Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH)) != null) {
                Material bMaterial = Material.getMaterial(itemname.toUpperCase(Locale.ENGLISH));
                itemid = bMaterial.getId();
            } else {
                try {
                    Material bMaterial = Bukkit.getUnsafe().getMaterialFromInternalName(itemname.toLowerCase(Locale.ENGLISH));
                    itemid = bMaterial.getId();
                } catch (Throwable throwable) {
                    return null;
                }
            }
        }
        if (itemid < 1) {
            return null;
        }
        final Material mat = Material.getMaterial(itemid);
        if (mat == null) {
            return null;
        }
        final ItemStack retval = new ItemStack(mat);
        retval.setAmount(mat.getMaxStackSize());
        retval.setDurability(metaData);
        return retval;
    }

    private List<String> getLines(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                return lines;
            } finally {
                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    static class ItemData {
        final private int itemNo;
        final private short itemData;

        ItemData(final int itemNo, final short itemData) {
            this.itemNo = itemNo;
            this.itemData = itemData;
        }

        public int getItemNo() {
            return itemNo;
        }

        public short getItemData() {
            return itemData;
        }

        @Override
        public int hashCode() {
            return (31 * itemNo) ^ itemData;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof ItemData)) {
                return false;
            }
            ItemData pairo = (ItemData) o;
            return this.itemNo == pairo.getItemNo() && this.itemData == pairo.getItemData();
        }
    }

    class LengthCompare implements java.util.Comparator<String> {
        public LengthCompare() {
            super();
        }

        @Override
        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }
}
