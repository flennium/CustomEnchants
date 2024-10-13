package org.flennn.Enchants;

import org.bukkit.configuration.file.FileConfiguration;

public enum Enchants {
    HEAL("Heal"),
    LIGHTNING("Lightning"),
    INVISIBILITY("Invisibility"),
    FROST("Frost");

    private final String name;
    private String description;
    private int maxlvl;
    private int chance;

    Enchants(String name) {
        this.name = name;
    }

    public static Enchants fromName(String name) {
        for (Enchants enchant : values()) {
            if (enchant.getName().equalsIgnoreCase(name)) {
                return enchant;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxlvl() {
        return maxlvl;
    }

    public int getChance() {
        return chance;
    }

    public void loadEnchants(FileConfiguration config) {
        this.description = config.getString("enchants." + name.toLowerCase() + ".description", "No description available.");
        this.maxlvl = config.getInt("enchants." + name.toLowerCase() + ".max_level", 10);
        this.chance = config.getInt("enchants." + name.toLowerCase() + ".chance", 50);
    }


}

