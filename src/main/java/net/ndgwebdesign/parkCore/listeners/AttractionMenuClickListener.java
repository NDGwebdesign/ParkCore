package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.AttractionMenu;
import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.WarpManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AttractionMenuClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().startsWith("§6Attractions")) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        int slot = event.getSlot();

        // Region klikken (bovenste rij)
        if (slot <= 8) {
            String region = event.getCurrentItem()
                    .getItemMeta()
                    .getDisplayName()
                    .replace("§e", "");

            if (AttractionConfigManager.regionExists(region)) {
                AttractionMenu.open(player, region);
            }
            return;
        }

        // Attractie klikken
        if (slot >= 9 && slot <= 44) {
            String attractionName = event.getCurrentItem()
                    .getItemMeta()
                    .getDisplayName()
                    .replace("§e", "");

            if (WarpManager.exists(attractionName)) {
                player.teleport(WarpManager.getWarp(attractionName).getLocation());
            }

        }
    }
}
