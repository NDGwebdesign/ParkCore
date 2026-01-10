package net.ndgwebdesign.parkCore.functions.UI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectMenu {

    public static final String TITLE = "§8Select a player";

    public static void open(Player staff) {

        Inventory inv = Bukkit.createInventory(null, 54, TITLE);

        int slot = 0;

        for (Player target : Bukkit.getOnlinePlayers()) {

            if (slot >= inv.getSize())
                break;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(target); // ⭐ BELANGRIJK
            meta.setDisplayName("§e" + target.getName());

            List<String> lore = new ArrayList<>();
            lore.add("§7Click to change rank");
            meta.setLore(lore);

            head.setItemMeta(meta);
            inv.setItem(slot++, head);
        }

        staff.openInventory(inv);
    }
}
