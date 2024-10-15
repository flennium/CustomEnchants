package org.flennn.Listeners;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.flennn.CustomEnchants;
import org.flennn.Enchants.Enchants;
import org.flennn.Enchants.EnchantmentsHandler;
import org.flennn.Managers.EnchantsManager;

import java.util.Map;
import java.util.Objects;

public class EnchantedItemListener implements Listener {

    private final NamespacedKey CUSTOM_ENCHANT_KEY = new NamespacedKey(CustomEnchants.getInstance(), "CustomEnchants");
    private final CustomEnchants plugin;
    private final EnchantmentsHandler enchantmentsHandler;

    public EnchantedItemListener(CustomEnchants plugin) {
        this.plugin = plugin;
        this.enchantmentsHandler = new EnchantmentsHandler(plugin);
    }

    @EventHandler
    public void onEntityDamage(PrePlayerAttackEntityEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() != Material.AIR) {
            PersistentDataContainer pdc = itemInHand.getItemMeta() != null ?
                    itemInHand.getItemMeta().getPersistentDataContainer() : null;

            String enchantData = pdc != null ? pdc.get(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING) : null;

            if (enchantData != null) {
                Entity target = event.getAttacked();
                enchantmentsHandler.handleEnchantments(player, itemInHand, target);
                logItemDetails(itemInHand, player);
            }
        }
    }





    private void logItemDetails(ItemStack item, Player player) {
        String itemName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : "Unknown Item";
        Material itemType = item.getType();
        int amount = item.getAmount();
        String playerName = player.getName();

        Map<String, Object> customEnchants = EnchantsManager.getCustomEnchants(item);

        StringBuilder logMessage = new StringBuilder("Item Details on Damage: ");
        logMessage.append("Player: ").append(playerName)
                .append(", Name: ").append(itemName)
                .append(", Type: ").append(itemType)
                .append(", Amount: ").append(amount)
                .append(", Custom Enchantments: ");

        if (!customEnchants.isEmpty()) {
            customEnchants.forEach((enchantName, levelObj) -> {
                int level = (levelObj instanceof Double) ? ((Double) levelObj).intValue() : (int) levelObj;
                logMessage.append(enchantName).append(" (Level ").append(level).append("), ");
            });
        } else {
            logMessage.append("None");
        }

        Bukkit.getConsoleSender().sendMessage(logMessage.toString());
    }

}
