package net.ndgwebdesign.parkCore.functions.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AdminMenu {

    public static final String TITLE = "§8Admin Menu";

    public static void open(Player staff) {

        Inventory inv = Bukkit.createInventory(
                null,
                9,
                TITLE
        );

        // 📘 Rank Manager
        ItemStack rankManager = new ItemStack(Material.BOOK);
        ItemMeta rankMeta = rankManager.getItemMeta();
        rankMeta.setDisplayName("§eRank Manager");
        rankMeta.setLore(java.util.List.of(
                "§7Beheer ranks",
                "",
                "§eKlik om te openen"
        ));
        rankManager.setItemMeta(rankMeta);

        inv.setItem(4, rankManager);

        //permission manager
        ItemStack permissionManager = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta permissionMeta = permissionManager.getItemMeta();
        permissionMeta.setDisplayName("§ePermission Manager");
        permissionMeta.setLore(java.util.List.of(
                "§7Beheer permissies",
                "",
                "§eKlik om te openen"
        ));
        permissionManager.setItemMeta(permissionMeta);
        inv.setItem(2, permissionManager);

        // 🔒 placeholders voor later
        inv.setItem(0, placeholder());
        inv.setItem(8, placeholder());

        staff.openInventory(inv);
    }

    private static ItemStack placeholder() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        return item;
    }
}
