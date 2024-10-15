package org.flennn.Enchants;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.flennn.CustomEnchants;
import org.flennn.Managers.ChanceManager;
import org.flennn.Managers.EnchantsManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnchantmentsHandler {
    private final CustomEnchants plugin;

    public EnchantmentsHandler(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    public void handleEnchantments(Player player, ItemStack itemInHand, Entity target) {
        handleHealEnchant(player, itemInHand);
        handleLightningEnchant(target, itemInHand);
        handleConfusionEnchant(target, itemInHand);
    }

    private void handleHealEnchant(Player player, ItemStack itemInHand) {
        Map<String, Object> customEnchants = EnchantsManager.getCustomEnchants(itemInHand);

        if (customEnchants.containsKey(Enchants.HEAL.getName())) {
            int level = (int) customEnchants.get(Enchants.HEAL.getName());
            int baseChance = Enchants.HEAL.getChance();

            if (ChanceManager.isChanceSuccessful(baseChance, level, 10)) {
                double healAmount = 0.5 * level;
                double currentHealth = player.getHealth();
                player.setHealth(Math.min(currentHealth + healAmount,
                        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));
                player.sendMessage("You have been healed for " + healAmount + " health!");
            }
        }
    }

    private void handleLightningEnchant(Entity target, ItemStack itemInHand) {
        Map<String, Object> customEnchants = EnchantsManager.getCustomEnchants(itemInHand);

        if (customEnchants.containsKey(Enchants.LIGHTNING.getName())) {
            int level = (int) customEnchants.get(Enchants.LIGHTNING.getName());
            int baseChance = Enchants.LIGHTNING.getChance();

            if (ChanceManager.isChanceSuccessful(baseChance, level, 15)) {
                target.getWorld().strikeLightning(target.getLocation());
                if (target instanceof Player targetPlayer) {
                    targetPlayer.sendMessage("You have been struck by lightning!");
                }
            }
        }
    }

    private void handleConfusionEnchant(Entity target, ItemStack itemInHand) {
        Map<String, Object> customEnchants = EnchantsManager.getCustomEnchants(itemInHand);

        if (customEnchants.containsKey(Enchants.CONFUSION.getName())) {
            int level = (int) customEnchants.get(Enchants.CONFUSION.getName());
            int baseChance = Enchants.CONFUSION.getChance();

            if (ChanceManager.isChanceSuccessful(baseChance, level, 15)) {
                if (target instanceof Player targetPlayer) {
                    shuffleHotbar(targetPlayer);
                    targetPlayer.sendMessage("You feel confused! Your hotbar items have been shuffled.");
                }
            }
        }
    }

    private void shuffleHotbar(Player player) {
        List<ItemStack> hotbarItems = new java.util.ArrayList<>(List.of(
                Objects.requireNonNull(player.getInventory().getItem(0)),
                Objects.requireNonNull(player.getInventory().getItem(1)),
                Objects.requireNonNull(player.getInventory().getItem(2)),
                Objects.requireNonNull(player.getInventory().getItem(3)),
                Objects.requireNonNull(player.getInventory().getItem(4)),
                Objects.requireNonNull(player.getInventory().getItem(5)),
                Objects.requireNonNull(player.getInventory().getItem(6)),
                Objects.requireNonNull(player.getInventory().getItem(7)),
                Objects.requireNonNull(player.getInventory().getItem(8))
        ));

        Collections.shuffle(hotbarItems);

        for (int i = 0; i < hotbarItems.size(); i++) {
            player.getInventory().setItem(i, hotbarItems.get(i));
        }
    }
}
