package org.flennn.Enchants;

import org.bukkit.configuration.file.FileConfiguration;

public enum Enchants {
    HEAL("Heal"),
    LIGHTNING("Lightning"),
    CONFUSION("Confusion");

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

    public static String formatName(String name) {
        StringBuilder formattedName = new StringBuilder();
        for (char c : name.toCharArray()) {
            formattedName.append(getStylizedCharacter(c));
        }
        return formattedName.toString();
    }

    public static char getStylizedCharacter(char c) {
        switch (Character.toLowerCase(c)) {
            case 'a': return 'ᴀ';
            case 'b': return 'ʙ';
            case 'c': return 'ᴄ';
            case 'd': return 'ᴅ';
            case 'e': return 'ᴇ';
            case 'f': return 'ғ';
            case 'g': return 'ɢ';
            case 'h': return 'ʜ';
            case 'i': return 'ɪ';
            case 'j': return 'ᴊ';
            case 'k': return 'ᴋ';
            case 'l': return 'ʟ';
            case 'm': return 'ᴍ';
            case 'n': return 'ɴ';
            case 'o': return 'ᴏ';
            case 'p': return 'ᴘ';
            case 'q': return 'ǫ';
            case 'r': return 'ʀ';
            case 's': return 's';
            case 't': return 'ᴛ';
            case 'u': return 'ᴜ';
            case 'v': return 'ᴠ';
            case 'w': return 'ᴡ';
            case 'x': return 'x';
            case 'y': return 'ʏ';
            case 'z': return 'ᴢ';
            default: return c;
        }
    }

    public static String stylizedRoman(String roman) {
        StringBuilder stylized = new StringBuilder();
        for (char c : roman.toCharArray()) {
            stylized.append(Enchants.getStylizedCharacter(c));
        }
        return stylized.toString();
    }



}

