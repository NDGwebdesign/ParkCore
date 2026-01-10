package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.RankPermissionMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RankPermissionMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player))
            return;
        if (!event.getView().getTitle().startsWith("§8Permissions:"))
            return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta())
            return;

        String title = event.getView().getTitle();
        String rankName = title.split("§e")[1].split(" ")[0];
        Rank rank = RankManager.getRank(rankName);
        if (rank == null)
            return;

        String display = item.getItemMeta().getDisplayName();

        // Page navigation
        if (display.contains("Previous")) {
            RankPermissionMenu.open(player, rank, getPage(title) - 1);
            return;
        }

        if (display.contains("Next")) {
            RankPermissionMenu.open(player, rank, getPage(title) + 1);
            return;
        }

        // ✅ Extract permission correctly from GUI
        String clean = ChatColor.stripColor(display);
        String perm = display.replace("§a ", "")
                .replace("§c ", "")
                .trim();

        if (!PermissionUtil.isValidPermission(perm)) {
            // Klik was op zoek/filter/knop → negeren
            return;
        }

        if (rank.getPermissions().contains(perm)) {
            RankManager.removePermission(rank.getName(), perm);
            player.sendMessage("§cPermission §e" + perm + " §chas been removed");
        } else {
            RankManager.addPermission(rank.getName(), perm);
            player.sendMessage("§aPermission §e" + perm + " §ahas been added");
        }

        RankPermissionMenu.open(player, rank, getPage(title));
    }

    private int getPage(String title) {
        String pagePart = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
        return Integer.parseInt(pagePart.split("/")[0]) - 1;
    }
}
