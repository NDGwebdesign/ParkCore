package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.functions.UI.AdminMenu;
import net.ndgwebdesign.parkCore.functions.UI.AttractionMenu;
import net.ndgwebdesign.parkCore.functions.UI.Menu;
import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getView().getTitle() == null) return;

        String title = event.getView().getTitle();
        String menuTitle = ParkCore.getInstance().getMenuConfig()
                .getString("menu.title")
                .replace("&", "§");

        if (!title.equals(menuTitle)) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        int slot = event.getSlot();

        var items = ParkCore.getInstance().getMenuConfig()
                .getConfigurationSection("menu.items");

        if (items == null) return;

        for (String key : items.getKeys(false)) {
            var item = items.getConfigurationSection(key);
            if (item == null) continue;

            if (item.getInt("slot") == slot) {

                String action = item.getString("action");

                if ("ATTRACTION_MENU".equalsIgnoreCase(action)) {
                    String firstRegion = AttractionConfigManager
                            .getConfig()
                            .getConfigurationSection("attractions.regions")
                            .getKeys(false)
                            .iterator()
                            .next();

                    AttractionMenu.open(player, firstRegion);
                }


                if ("COMMAND".equalsIgnoreCase(action)) {
                    String cmd = item.getString("command");
                    player.closeInventory();
                    player.performCommand(cmd);
                }

                if ("ADMIN_GUI".equalsIgnoreCase(action)) {

                    if (!player.hasPermission(item.getString("permission"))) {
                        player.sendMessage("§cJe hebt geen permissie hiervoor.");
                        return;
                    }

                    player.closeInventory();
                    AdminMenu.open(player);
                    return;
                }

                return;
            }
        }
    }
}
