package org.flennn.Managers;

import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.flennn.CustomEnchants;
import org.flennn.Enchants.Enchants;

import java.util.HashMap;
import java.util.Map;

public class EnchantsManager {

    private static final NamespacedKey CUSTOM_ENCHANT_KEY = new NamespacedKey(CustomEnchants.getInstance(), "CustomEnchants");
    private static final Gson gson = new Gson();

    public static void addCustomEnchant(ItemStack item, String enchantName, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String enchantData = pdc.get(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING);

        Map<String, Object> enchantments = enchantData != null ? gson.fromJson(enchantData, Map.class) : new HashMap<>();

        if (enchantments.containsKey(enchantName)) {
            int existingLevel = ((Number) enchantments.get(enchantName)).intValue();

            if (level <= existingLevel) {
                return;
            }
        }

        enchantments.put(enchantName, level);
        String updatedEnchantData = gson.toJson(enchantments);
        pdc.set(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING, updatedEnchantData);

        Map<Enchants, Object> formattedEnchants = formatEnchants(enchantments);
        meta = LoreManager.updateEnchantFromLore(item, formattedEnchants);

        item.setItemMeta(meta);
    }


    private static Map<Enchants, Object> formatEnchants(Map<String, Object> enchantments) {
        Map<Enchants, Object> formattedEnchants = new HashMap<>();
        for (Map.Entry<String, Object> entry : enchantments.entrySet()) {
            Enchants enchant = Enchants.valueOf(entry.getKey());
            formattedEnchants.put(enchant, entry.getValue());
        }
        return formattedEnchants;
    }

    public static Map<String, Object> getCustomEnchants(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String enchantData = pdc.get(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING);

        if (enchantData == null) return new HashMap<>();

        return gson.fromJson(enchantData, Map.class);
    }

    public static boolean hasCustomEnchant(ItemStack item, String enchantName) {
        Map<String, Object> enchants = getCustomEnchants(item);
        return enchants != null && enchants.containsKey(enchantName);
    }

    public static void removeCustomEnchant(ItemStack item, String enchantName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String enchantData = pdc.get(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING);

        Map<String, Object> enchantments = enchantData != null ? gson.fromJson(enchantData, Map.class) : new HashMap<>();

        enchantments.remove(enchantName);

        if (enchantments.isEmpty()) {
            pdc.remove(CUSTOM_ENCHANT_KEY);
        } else {
            String updatedEnchantData = gson.toJson(enchantments);
            pdc.set(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING, updatedEnchantData);
        }

        Enchants enchantToRemove = Enchants.fromName(enchantName);
        if (enchantToRemove != null) {
            meta = LoreManager.removeEnchantFromLore(item, enchantToRemove);
        }

        item.setItemMeta(meta);
    }

    public static void removeAllCustomEnchants(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.remove(CUSTOM_ENCHANT_KEY);

        meta = LoreManager.clearLoreEnchantments(item);
        item.setItemMeta(meta);
    }
}
