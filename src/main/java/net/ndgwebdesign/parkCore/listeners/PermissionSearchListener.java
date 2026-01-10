package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.functions.UI.RankPermissionMenu;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionSearchSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PermissionSearchListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player player = e.getPlayer();
        Rank rank = PermissionSearchSession.get(player);
        if (rank == null)
            return;

        e.setCancelled(true);

        String msg = e.getMessage().toLowerCase();

        if (msg.equals("cancel")) {
            PermissionSearchSession.stop(player);
            player.sendMessage("§cSearch cancelled.");
            return;
        }

        Bukkit.getScheduler().runTask(
                ParkCore.getInstance(),
                () -> RankPermissionMenu.openSearch(player, rank, msg, 0));

        PermissionSearchSession.stop(player);
    }
}
