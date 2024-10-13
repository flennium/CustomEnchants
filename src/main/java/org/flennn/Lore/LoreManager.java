package org.flennn.Lore;

import net.md_5.bungee.api.ChatColor;

public class LoreManager {


    private static String romanNumeral(int number) {
        switch (number) {
            case 1:
                return ChatColor.GRAY + "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return Integer.toString(number);
        }
    }


}
