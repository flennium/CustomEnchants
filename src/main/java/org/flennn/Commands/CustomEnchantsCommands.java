package org.flennn.Commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.flennn.CustomEnchants;
import org.flennn.Enchants.Enchants;
import org.flennn.Utils.Components;
import org.flennn.Managers.EnchantsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomEnchantsCommands implements CommandExecutor, TabCompleter {
    private final CustomEnchants plugin;

    public CustomEnchantsCommands(CustomEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("customenchant")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>Only players can use this command.<red>"));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("customenchant.use")) {
                player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>You do not have permission to use this command.<red>"));
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(Components.mm(CustomEnchants.getPrefix() + " <yellow>Usage: /customenchant <subcommand> [options]<yellow>"));
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "add":
                    return AddEnchant(player, args);
                case "remove":
                    return RemoveEnchant(player, args);
                case "removeall":
                    return RemoveAllEnchants(player);
                case "list":
                    return EnchantList(player);
                default:
                    player.sendMessage(Components.mm(CustomEnchants.getPrefix() + " <yellow>Unknown subcommand.<yellow>"));
                    return true;
            }
        }
        return false;
    }

    private boolean AddEnchant(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + " <yellow>Usage: /customenchant add <enchant_name> <level><yellow>"));
            return true;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "You are not holding any item to enchant!"));
            return true;
        }

        String enchantName = args[1];

        Enchants enchant = Enchants.fromName(enchantName);
        if (enchant == null) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>Unknown enchantment: " + enchantName + "<red>"));
            return false;
        }

        int level;

        try {
            level = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>Invalid level. Please enter a number.<red>"));
            return false;
        }

        int maxLevel = enchant.getMaxlvl();
        if (level > maxLevel) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>The maximum level for " + enchantName + " is " + maxLevel + "!<red>"));
            return false;
        }

        EnchantsManager.addCustomEnchant(heldItem, enchantName, level);

        player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<green>Added " + enchantName + " level " + level + " successfully!<green>"));
        return true;
    }

    private boolean RemoveEnchant(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<yellow>Usage: /customenchant remove <enchant_name><yellow>"));
            return true;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "You are not holding any item to enchant!"));
            return true;
        }

        String enchantName = args[1];

        Enchants enchant = Enchants.fromName(enchantName);
        if (enchant == null) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<red>Unknown enchantment: " + enchantName + "<red>"));
            return false;
        }

        EnchantsManager.removeCustomEnchant(heldItem, enchantName);

        player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<green>Removed " + enchantName + " successfully!<green>"));
        return true;
    }

    private boolean RemoveAllEnchants(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "You are not holding any item to remove enchantments from!"));
            return true;
        }

        EnchantsManager.removeAllCustomEnchants(heldItem);

        player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<green>Removed all enchantments successfully!<green>"));
        return true;
    }

    private boolean EnchantList(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "You are not holding any item to list enchantments!"));
            return true;
        }

        Map<String, Object> customEnchants = EnchantsManager.getCustomEnchants(heldItem);
        if (customEnchants.isEmpty()) {
            player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<yellow>This item has no custom enchantments.<yellow>"));
            return true;
        }

        player.sendMessage(Components.mm(CustomEnchants.getPrefix() + "<yellow>Custom enchantments on this item:"));
        for (Map.Entry<String, Object> entry : customEnchants.entrySet()) {
            String enchantName = entry.getKey();
            int level = (int) entry.getValue();
            player.sendMessage(Components.mm("<yellow> - " + enchantName + " (Level " + level + ")"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("removeall");
            completions.add("list");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            for (Enchants enchant : Enchants.values()) {
                if (enchant != null) {
                    completions.add(enchant.getName().toUpperCase());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            for (int i = 1; i <= 10; i++) {
                completions.add(String.valueOf(i));
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            for (Enchants enchant : Enchants.values()) {
                completions.add(enchant.getName().toUpperCase());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("removeall")) {}

        return completions.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
