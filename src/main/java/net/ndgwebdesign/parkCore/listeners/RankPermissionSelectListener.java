package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.RankPermissionMenu;
import net.ndgwebdesign.parkCore.functions.UI.RankPermissionSelectMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RankPermissionSelectListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(RankPermissionSelectMenu.TITLE)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String display = item.getItemMeta().getDisplayName();

        Rank rank = RankManager.getRankByDisplay(display);
        if (rank == null) return;

        RankPermissionMenu.open(player, rank, 0);
    }
}
