package net.ndgwebdesign.parkCore.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RidePanelConfirmationManager {

    private static final Map<UUID, PendingPanelRequest> pending = new HashMap<>();

    public static void add(Player player, PendingPanelRequest request) {
        pending.put(player.getUniqueId(), request);
    }

    public static PendingPanelRequest get(Player player) {
        return pending.get(player.getUniqueId());
    }

    public static void remove(Player player) {
        pending.remove(player.getUniqueId());
    }

    public static boolean has(Player player) {
        return pending.containsKey(player.getUniqueId());
    }
}
