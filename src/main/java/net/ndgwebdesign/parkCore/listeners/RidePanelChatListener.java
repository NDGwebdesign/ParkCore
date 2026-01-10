package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.PendingPanelRequest;
import net.ndgwebdesign.parkCore.managers.RidePanelConfirmationManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

public class RidePanelChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!RidePanelConfirmationManager.has(player))
            return;

        event.setCancelled(true);

        String msg = event.getMessage().toLowerCase();
        PendingPanelRequest request = RidePanelConfirmationManager.get(player);

        if (msg.equals("ja") || msg.equals("yes")) {

            Bukkit.getScheduler().runTask(ParkCore.getInstance(), () -> {
                ParkCore.getInstance()
                        .getRideOperateHook()
                        .createPanel(player, request.getAttractionName());

                player.sendMessage("§aRideOperate panel created with name §e"
                        + request.getAttractionName() + "§a!");
            });

            RidePanelConfirmationManager.remove(player);
            return;
        }

        if (msg.equals("nee") || msg.equals("no")) {
            player.sendMessage("§eRideOperate panel creation cancelled.");
            RidePanelConfirmationManager.remove(player);
            return;
        }

        player.sendMessage("§7Type §aYES §7or §cNO §7to answer.");
    }
}