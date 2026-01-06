package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.PluginFilterMenu;
import net.ndgwebdesign.parkCore.functions.UI.RankPermissionMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PluginFilterMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!e.getView().getTitle().startsWith("§8Filter permissions:")) return;

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String plugin = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String rankName = e.getView().getTitle().split("§e")[1];

        Rank rank = RankManager.getRank(rankName);
        if (rank == null) return;

        RankPermissionMenu.openFiltered(player, rank, plugin, 0);
    }
}
