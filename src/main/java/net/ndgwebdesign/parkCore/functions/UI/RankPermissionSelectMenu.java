package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RankPermissionSelectMenu {

    public static final String TITLE = "§8Select a rank";

    public static void open(Player staff) {

        List<Rank> ranks = RankManager.getAllRanks();

        int size = ((ranks.size() / 9) + 1) * 9;
        if (size > 54)
            size = 54;

        Inventory inv = Bukkit.createInventory(null, size, TITLE);

        int slot = 0;
        for (Rank rank : ranks) {

            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(rank.getDisplayName());
            meta.setLore(List.of(
                    "§7Rank: §f" + rank.getName(),
                    "",
                    "§eClick to manage permissions"));

            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        staff.openInventory(inv);
    }
}
