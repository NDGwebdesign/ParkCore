package net.ndgwebdesign.parkCore.utils;

import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionSearchSession {

    private static final Map<UUID, Rank> searching = new HashMap<>();

    public static void start(Player player, Rank rank) {
        searching.put(player.getUniqueId(), rank);
        player.sendMessage("§eTyp een zoekterm in de chat (§ccancel§e om te stoppen)");
    }

    public static Rank get(Player player) {
        return searching.get(player.getUniqueId());
    }

    public static void stop(Player player) {
        searching.remove(player.getUniqueId());
    }
}
