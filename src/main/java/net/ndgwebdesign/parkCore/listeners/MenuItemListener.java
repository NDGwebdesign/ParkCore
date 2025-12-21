package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.Menu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MenuItemListener implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent event) {

        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.NETHER_STAR) return;

        if (!event.getAction().isRightClick()) return;

        event.setCancelled(true);
        Menu.openMainMenu(event.getPlayer());
    }
}
