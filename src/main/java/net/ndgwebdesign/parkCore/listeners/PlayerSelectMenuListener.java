package net.ndgwebdesign.parkCore.listeners;

import net.ndgwebdesign.parkCore.functions.UI.PlayerSelectMenu;
import net.ndgwebdesign.parkCore.functions.UI.RankMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSelectMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player staff)) return;

        // ✅ VEILIGE TITLE CHECK
        if (!event.getView().getTitle().equals(PlayerSelectMenu.TITLE)) return;

        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;
        if (item.getItemMeta().getDisplayName() == null) return;

        String targetName = item.getItemMeta()
                .getDisplayName()
                .replace("§e", "");

        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            staff.sendMessage("§cSpeler is niet meer online.");
            staff.closeInventory();
            return;
        }



        staff.closeInventory();
        RankMenu.open(staff, target); // 🎉 werkt nu altijd
    }
}
