package org.flennn;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.flennn.Commands.CustomEnchantsCommands;
import org.flennn.Enchants.Enchants;
import org.flennn.Listeners.EnchantedItemListener;
import org.flennn.Utils.Components;

import java.util.Objects;

public final class CustomEnchants extends JavaPlugin {

    public static CustomEnchants instance;
    public static FileConfiguration config;
    public static Component prefix;

    public static final PluginManager pm = Bukkit.getPluginManager();

    public static Component getPrefix() {
        return prefix;
    }

    public static FileConfiguration getCustomConfig() {
        return config;
    }

    public static CustomEnchants getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        config = getConfig();

        // Load prefix using the mm method first time using MiniMessage uwu
        prefix = Components.mm(config.getString("plugin.prefix", "ᴄᴇɴᴄʜᴀɴᴛs » "));


        // load enchants details from config
        for (Enchants enchant : Enchants.values()) {
            enchant.loadEnchants(config);
        }

        // log loaded enchants from config
        for (Enchants enchant : Enchants.values()) {
            Bukkit.getConsoleSender().sendMessage(prefix.append(Component.text(enchant.getName() + ": " + enchant.getDescription() +
                    ", Max Level: " + enchant.getMaxlvl() +
                    ", Chance: " + enchant.getChance() + "%")));
        }

        // register commands
        Objects.requireNonNull(this.getCommand("customenchant")).setExecutor(new CustomEnchantsCommands(this));


        // register events
        pm.registerEvents(new EnchantedItemListener(this), this);


    }

    @Override
    public void onDisable() {
    }

}
