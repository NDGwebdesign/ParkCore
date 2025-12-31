package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.RankMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RankMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player staff)) return;

        String title = event.getView().getTitle();
        if (!title.startsWith(RankMenu.TITLE_PREFIX)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;
        if (!item.getItemMeta().hasLore()) return;

        // 🎯 Target uit title halen
        String targetName = title.replace(RankMenu.TITLE_PREFIX, "");
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            staff.sendMessage("§cSpeler is niet meer online.");
            staff.closeInventory();
            return;
        }

        // 🎯 Rank ID uit lore halen
        List<String> lore = item.getItemMeta().getLore();
        if (lore == null || lore.isEmpty()) return;

        String idLine = lore.get(0);
        if (!idLine.startsWith("§7ID: §f")) return;

        String rankId = idLine.replace("§7ID: §f", "");
        Rank rank = RankManager.getRank(rankId);
        if (rank == null) return;

        RankManager.setPlayerRank(target.getName(), rankId);
        RankManager.applyRank(target);

        staff.sendMessage("§aRank van §e" + target.getName()
                + " §ais gezet naar " + rank.getDisplayName());

        target.sendMessage("§aJe rank is veranderd naar " + rank.getDisplayName());

        staff.closeInventory();
    }
}
