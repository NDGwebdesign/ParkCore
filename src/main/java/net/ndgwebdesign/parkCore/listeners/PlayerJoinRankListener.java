package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

public class PlayerJoinRankListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Rank rank = RankManager.getPlayerRank(player.getName());

        PermissionAttachment attachment = player.addAttachment(
                net.ndgwebdesign.parkCore.ParkCore.getInstance()
        );

        for (String perm : RankManager.getAllPermissions(rank)) {
            attachment.setPermission(perm, true);
        }

        player.setDisplayName(
                rank.getPrefix() + player.getName() + rank.getSuffix()
        );
    }
}
