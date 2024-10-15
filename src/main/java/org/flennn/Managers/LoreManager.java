package org.flennn.Managers;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.flennn.Enchants.Enchants;
import org.flennn.Utils.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.flennn.Enchants.Enchants.stylizedRoman;

public class LoreManager {

    public static ItemMeta updateEnchantFromLore(ItemStack item, Map<Enchants, Object> enchants) {
        if (item == null || item.getType().isAir()) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();

        if (lore == null) lore = new ArrayList<>();

        if (lore.isEmpty()) {
            lore.add(Components.mm("<bold><aqua>ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs ></aqua></bold> "));
        }

        for (Map.Entry<Enchants, Object> enchantEntry : enchants.entrySet()) {
            Enchants enchant = enchantEntry.getKey();
            int level = (int) enchantEntry.getValue();

            String levelRoman = romanNumeral(level);
            String description = enchant.getDescription();

            lore.add(Components.mm("<gray>- " + Enchants.formatName(enchant.getName()) + "</gray> " + levelRoman));
            lore.add(Components.mm("<dark_aqua>  <italic>" + Enchants.formatName(description) + "</italic></dark_aqua>"));
        }

       // lore.add(Components.mm("<gray></gray>"));

        meta.lore(lore);
        return meta;
    }

    public static ItemMeta removeEnchantFromLore(ItemStack item, Enchants enchant) {
        if (item == null || item.getType().isAir()) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        List<Component> lore = meta.lore();
        if (lore == null || lore.isEmpty()) return meta;

        for (int i = 0; i < lore.size(); i++) {
            Component line = lore.get(i);
            String lineText = line.toString();

            if (lineText.contains(Enchants.formatName(enchant.getName()))) {
                lore.remove(i);
                if (i < lore.size()) {
                    lore.remove(i);
                }
                break;
            }
        }

        boolean hasEnchantments = false;
        for (Component line : lore) {
            if (line.toString().contains("ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs")) {
                hasEnchantments = true;
                break;
            }
        }

        if (!hasEnchantments) {
            lore.removeIf(line -> line.toString().contains("ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs"));
        }

        meta.lore(lore);
        return meta;
    }

    public static ItemMeta clearLoreEnchantments(ItemStack item) {
        if (item == null || item.getType().isAir()) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        List<Component> lore = new ArrayList<>();

        if (meta.hasLore()) {
            lore = meta.lore();
            int enchantmentsIndex = -1;
            for (int i = 0; i < Objects.requireNonNull(lore).size(); i++) {
                if (lore.get(i).toString().contains("ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛs")) {
                    enchantmentsIndex = i;
                    break;
                }
            }
            if (enchantmentsIndex != -1) {
                lore.subList(enchantmentsIndex, lore.size()).clear();
            }
        }

        meta.lore(lore);
        item.setItemMeta(meta);
        return meta;
    }


    private static String romanNumeral(int number) {
        switch (number) {
            case 1:
                return "<bold><gray>" + stylizedRoman("I") + "</gray></bold>";
            case 2:
                return "<bold><gray>" + stylizedRoman("II") + "</gray></bold>";
            case 3:
                return "<bold><gray>" + stylizedRoman("III") + "</gray></bold>";
            case 4:
                return "<bold><gray>" + stylizedRoman("IV") + "</gray></bold>";
            case 5:
                return "<bold><gold>" + stylizedRoman("V") + "</gold></bold>";
            case 6:
                return "<bold><gold>" + stylizedRoman("VI") + "</gold></bold>";
            case 7:
                return "<bold><gold>" + stylizedRoman("VII") + "</gold></bold>";
            case 8:
                return "<bold><gold>" + stylizedRoman("VIII") + "</gold></bold>";
            case 9:
                return "<bold><red>" + stylizedRoman("IX") + "</red></bold>";
            case 10:
                return "<bold><red>" + stylizedRoman("X") + "</red></bold>";
            default:
                return Integer.toString(number);
        }
    }

}
