package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.AdminMenu;
import net.ndgwebdesign.parkCore.functions.UI.PlayerSelectMenu;
import net.ndgwebdesign.parkCore.functions.UI.RankPermissionSelectMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AdminMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(AdminMenu.TITLE)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String name = item.getItemMeta().getDisplayName();

        // 📘 Rank Manager (player select)
        if (name.equals("§eRank Manager")) {
            player.closeInventory();
            PlayerSelectMenu.open(player);
            return;
        }

        // 🔒 Permission Manager (rank select)
        if (name.equals("§ePermission Manager")) {
            player.closeInventory();
            RankPermissionSelectMenu.open(player);
        }
    }
}
