package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RankMenu {

    public static final String TITLE_PREFIX = "§8Rank Manager: §e";

    public static void open(Player staff, Player target) {

        Inventory inv = Bukkit.createInventory(
                null,
                27,
                TITLE_PREFIX + target.getName());

        int slot = 10;

        for (Rank rank : RankManager.getAllRanks()) {

            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(rank.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add("§7ID: §f" + rank.getName()); // ⭐ BELANGRIJK
            lore.add("");

            if (!rank.getInheritance().isEmpty()) {
                lore.add("§7Inherits from:");
                for (String parent : rank.getInheritance()) {
                    lore.add(" §8- §7" + parent);
                }
                lore.add("");
            }

            lore.add("§aClick to set this rank");
            lore.add("§eRight-click: Permissions");

            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.setItem(slot++, item);
        }

        staff.openInventory(inv);
    }
}
