package net.ndgwebdesign.parkCore.rides;

import net.ndgwebdesign.parkCore.ParkCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RideOperateHook {

    private final ParkCore plugin;

    public RideOperateHook(ParkCore plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("RideOperate");
    }

    /**
     * Make the player execute the RideOperate command
     */
    public void createPanel(Player player, String attractionName) {

        if (player == null || !player.isOnline())
            return;

        String command = "createpanel " + attractionName;

        // Execute command as if the player typed it
        Bukkit.dispatchCommand(player, command);
    }
}
