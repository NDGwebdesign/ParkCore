package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.ParkCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Menu {

    public static void openMainMenu(Player player) {

        String title = color(ParkCore.getInstance().getMenuConfig().getString("menu.title"));
        int size = ParkCore.getInstance().getMenuConfig().getInt("menu.size");

        Inventory inv = Bukkit.createInventory(null, size, title);

        ConfigurationSection items =
                ParkCore.getInstance().getMenuConfig().getConfigurationSection("menu.items");

        if (items == null) return;

        for (String key : items.getKeys(false)) {

            ConfigurationSection item = items.getConfigurationSection(key);
            if (item == null) continue;

            // 🔐 Permission check
            String permission = item.getString("permission");
            if (permission != null && !permission.isEmpty()) {
                if (!player.hasPermission(permission)) continue;
            }

            Material material = Material.valueOf(item.getString("material"));
            int slot = item.getInt("slot");

            ItemStack stack = new ItemStack(material);
            ItemMeta meta = stack.getItemMeta();

            meta.setDisplayName(color(item.getString("name")));
            meta.setLore(colorList(item.getStringList("lore")));

            stack.setItemMeta(meta);
            inv.setItem(slot, stack);
        }

        player.openInventory(inv);
    }

    private static String color(String s) {
        return s == null ? "" : s.replace("&", "§");
    }

    private static List<String> colorList(List<String> list) {
        return list.stream().map(Menu::color).toList();
    }
}
