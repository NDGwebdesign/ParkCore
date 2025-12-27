package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AttractionMenu {

    private static final int SIZE = 54;

    public static void open(Player player, String selectedRegion) {

        Inventory inv = Bukkit.createInventory(
                null,
                SIZE,
                "§6Attractions §8- §e" + selectedRegion
        );

        /* ---------------------- */
        /* Regions bovenin        */
        /* ---------------------- */
        int slot = 0;
        for (String region : AttractionConfigManager
                .getConfig()
                .getConfigurationSection("attractions.regions")
                .getKeys(false)) {

            ItemStack item = new ItemStack(
                    region.equalsIgnoreCase(selectedRegion)
                            ? Material.GOLD_BLOCK
                            : Material.NAME_TAG
            );

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§e" + region);
            meta.setLore(List.of(
                    "§7Click to open",
                    "§7The region"
            ));
            item.setItemMeta(meta);

            inv.setItem(slot++, item);
            if (slot >= 9) break;
        }

        /* ---------------------- */
        /* Attracties             */
        /* ---------------------- */
        List<String> attractions =
                AttractionConfigManager.getAttractions(selectedRegion);

        int attractionSlot = 9;

        for (String name : attractions) {

            Attraction attraction = AttractionManager.getAttraction(name);
            AttractionStatus status = attraction != null
                    ? attraction.getStatus()
                    : AttractionStatus.CLOSED;

            Material mat = switch (status) {
                case OPEN -> Material.LIME_CONCRETE;
                case CLOSED -> Material.RED_CONCRETE;
                case MAINTENANCE -> Material.ORANGE_CONCRETE;
                default -> Material.GRAY_CONCRETE;
            };

            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName("§e" + name);
            meta.setLore(List.of(
                    "§7Status: " + formatStatus(status),
                    "",
                    "§aLeft-click §7to select this attraction"
            ));

            item.setItemMeta(meta);
            inv.setItem(attractionSlot++, item);

            if (attractionSlot >= 45) break;
        }

        player.openInventory(inv);
    }

    private static String formatStatus(AttractionStatus status) {
        return switch (status) {
            case OPEN -> "§aOPEN";
            case CLOSED -> "§cCLOSED";
            case MAINTENANCE -> "§6MAINTENANCE";
            default -> "§7UNKNOWN";
        };
    }
}
