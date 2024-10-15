package org.flennn.Managers;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.flennn.Enchants.Enchants;
import org.flennn.Utils.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoreManager {

    public static ItemMeta updateEnchantFromLore(ItemStack item, Map<Enchants, Object> enchants) {
        if (item == null || item.getType().isAir()) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();

        if (lore == null) lore = new ArrayList<>();

        if (lore.isEmpty()) {
            lore.add(Components.mm("<dark_purple><bold>Enchantments:</bold></dark_purple>"));
        }

        for (Map.Entry<Enchants, Object> enchantEntry : enchants.entrySet()) {
            Enchants enchant = enchantEntry.getKey();
            int level = (int) enchantEntry.getValue();

            String enchantName = "<aqua>" + enchant.getName();
            String levelRoman = romanNumeral(level);
            String description = enchant.getDescription();

            lore.add(Components.mm("<gray>➤ " + enchantName + " " + levelRoman + "]</gray>"));
            lore.add(Components.mm("<dark_gray>    <italic>" + description + "</italic></dark_gray>"));
        }

        lore.add(Components.mm("<gray></gray>"));

        meta.lore(lore);
        return meta;
    }

    public static ItemMeta removeEnchantFromLore(ItemStack item, Enchants enchant) {
        if (item == null || item.getType().isAir()) return null;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;

        List<Component> lore = meta.lore();
        if (lore == null || lore.isEmpty()) return meta;

        String enchantName = "<aqua>" + enchant.getName();

        for (int i = 0; i < lore.size(); i++) {
            Component line = lore.get(i);
            String lineText = line.toString();

            if (lineText.contains(enchantName)) {
                lore.remove(i);
                if (i < lore.size()) {
                    lore.remove(i);
                }
                break;
            }
        }

        boolean hasEnchantments = false;
        for (Component line : lore) {
            if (line.toString().contains("➤")) {
                hasEnchantments = true;
                break;
            }
        }

        if (!hasEnchantments) {
            lore.removeIf(line -> line.toString().contains("Enchantments:"));
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
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).toString().contains("Enchantments:")) {
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
                return "<gray>I<gray>";
            case 2:
                return "<gray>II</gray>";
            case 3:
                return "<gray>III</gray>";
            case 4:
                return "<gray>IV</gray>";
            case 5:
                return "<gold>V</gold>";
            case 6:
                return "<gold>VI</gold>";
            case 7:
                return "<gold>VII</gold>";
            case 8:
                return "<gold>VIII</gold>";
            case 9:
                return "<gold>IX</gold>";
            case 10:
                return "<darkred>X</darkred>";
            default:
                return Integer.toString(number);
        }
    }

}
