package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PluginFilterMenu {

    public static void open(Player player, Rank rank) {

        Set<String> plugins = new TreeSet<>();

        for (String perm : PermissionUtil.getAllPermissions()) {
            plugins.add(PermissionUtil.getPluginFromPermission(perm));
        }

        Inventory inv = Bukkit.createInventory(
                null,
                54,
                "§8Filter permissions: §e" + rank.getName()
        );

        int slot = 0;
        for (String plugin : plugins) {
            if (slot >= 45) break;

            ItemStack item = new ItemStack(Material.CHEST);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§e" + plugin);
            meta.setLore(List.of(
                    "§7Toon alleen permissions",
                    "§7van deze plugin",
                    "",
                    "§eKlik om te openen"
            ));

            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        player.openInventory(inv);
    }
}
