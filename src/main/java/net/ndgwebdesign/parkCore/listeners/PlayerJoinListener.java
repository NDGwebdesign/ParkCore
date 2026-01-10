package net.ndgwebdesign.parkCore.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        meta.setDisplayName("§6Park Menu");
        star.setItemMeta(meta);

        // Give only if player doesn't have one yet
        if (!player.getInventory().contains(star)) {
            player.getInventory().setItem(4, star);
        }
    }
}
