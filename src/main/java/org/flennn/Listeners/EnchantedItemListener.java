package org.flennn.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.flennn.CustomEnchants;
import org.flennn.Utils.EnchantUtils;

import java.util.Map;

public class EnchantedItemListener implements Listener {

    private final NamespacedKey CUSTOM_ENCHANT_KEY;
    private final CustomEnchants plugin;

    public EnchantedItemListener(CustomEnchants plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        CUSTOM_ENCHANT_KEY = new NamespacedKey(plugin, "CustomEnchants");
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand.getType() != Material.AIR) {
            PersistentDataContainer pdc = itemInHand.getItemMeta() != null ?
                    itemInHand.getItemMeta().getPersistentDataContainer() : null;

            String enchantData = pdc != null ? pdc.get(CUSTOM_ENCHANT_KEY, PersistentDataType.STRING) : null;

            if (enchantData != null) {
                logItemDetails(itemInHand, player);
            }
        }
    }

    private void logItemDetails(ItemStack item, Player player) {
        String itemName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : "Unknown Item";
        Material itemType = item.getType();
        int amount = item.getAmount();
        String playerName = player.getName();

        Map<String, Integer> customEnchants = EnchantUtils.getCustomEnchants(item);

        StringBuilder logMessage = new StringBuilder("Item Details on Damage: ");
        logMessage.append("Player: ").append(playerName)
                .append(", Name: ").append(itemName)
                .append(", Type: ").append(itemType)
                .append(", Amount: ").append(amount)
                .append(", Custom Enchantments: ");

        if (!customEnchants.isEmpty()) {
            customEnchants.forEach((enchantName, level) ->
                    logMessage.append(enchantName).append(" (Level ").append(level).append("), "));
        } else {
            logMessage.append("None");
        }

        Bukkit.getConsoleSender().sendMessage(logMessage.toString());
    }
}
