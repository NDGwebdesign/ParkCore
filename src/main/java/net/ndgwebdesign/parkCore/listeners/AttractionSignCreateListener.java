package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.signs.AttractionStatusSign;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class AttractionSignCreateListener implements Listener {

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {

        if (!"[parkcore]".equalsIgnoreCase(event.getLine(0)))
            return;
        if (!"attraction".equalsIgnoreCase(event.getLine(1)))
            return;

        String attractionName = event.getLine(2);
        if (attractionName == null || attractionName.isEmpty()) {
            event.getPlayer().sendMessage("§cNo attraction name provided.");
            return;
        }

        if (!AttractionManager.exists(attractionName)) {
            event.getPlayer().sendMessage("§cAttraction does not exist.");
            return;
        }

        Attraction attraction = AttractionManager.getAttraction(attractionName);

        // Set initial lines
        event.setLine(0, "§6[ParkCore]");
        event.setLine(1, "§eAttraction");
        event.setLine(2, "§f" + attractionName);
        event.setLine(3, "§7Loading...");

        // BELANGRIJK: update pas NA 1 tick
        Bukkit.getScheduler().runTask(
                ParkCore.getInstance(),
                () -> {
                    AttractionStatusSign.update(
                            event.getBlock().getLocation(),
                            attraction);
                    attraction.addSign(event.getBlock().getLocation());
                });

        event.getPlayer().sendMessage("§aAttraction status sign created!");
    }

}
