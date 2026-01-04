package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RankPermissionMenu {

    public static final int ITEMS_PER_PAGE = 45;
    public static final int INVENTORY_SIZE = 54;

    public static void open(Player staff, Rank rank, int page) {

        if (rank == null) {
            staff.sendMessage("§cDeze rank bestaat niet.");
            return;
        }

        List<String> permissions = PermissionUtil.getAllPermissions();
        if (permissions.isEmpty()) {
            staff.sendMessage("§cGeen permissions gevonden.");
            return;
        }

        int maxPage = (int) Math.ceil((double) permissions.size() / ITEMS_PER_PAGE);
        page = Math.max(0, Math.min(page, maxPage - 1));

        Inventory inv = Bukkit.createInventory(
                null,
                INVENTORY_SIZE,
                "§8Permissions: §e" + rank.getName() + " §7(" + (page + 1) + "/" + maxPage + ")"
        );

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, permissions.size());

        int slot = 0;

        // Voeg permissions toe
        for (int i = start; i < end; i++) {

            String perm = permissions.get(i).trim(); // verwijder ongewenste spaties
            boolean has = rank.getPermissions().contains(perm);

            ItemStack item = new ItemStack(has ? Material.LIME_DYE : Material.RED_DYE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName((has ? "§a✔ " : "§c✖ ") + perm);
            meta.setLore(List.of(
                    "§7Status: " + (has ? "§aON" : "§cOFF"),
                    "",
                    "§eClick to toggle permission"
            ));

            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        // Paginaknoppen
        if (page > 0) inv.setItem(45, button(Material.ARROW, "§eVorige pagina"));
        if (page + 1 < maxPage) inv.setItem(53, button(Material.ARROW, "§eVolgende pagina"));

        fillBottom(inv);

        staff.openInventory(inv);
    }

    private static ItemStack button(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private static void fillBottom(Inventory inv) {
        ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        meta.setDisplayName(" ");
        filler.setItemMeta(meta);

        for (int i = 46; i < 54; i++) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, filler);
            }
        }
    }
}
