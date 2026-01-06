package net.ndgwebdesign.parkCore.functions.UI;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionSearchSession;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class RankPermissionMenu {

    private static final int ITEMS_PER_PAGE = 45;

    /* ===================== */
    /* OPEN METHODS          */
    /* ===================== */

    public static void open(Player p, Rank rank, int page) {
        openInternal(p, rank, PermissionUtil.getAllPermissions(), page, null);
    }

    public static void openFiltered(Player p, Rank rank, String plugin, int page) {

        List<String> filtered = PermissionUtil.getAllPermissions().stream()
                .filter(perm -> PermissionUtil.getPluginFromPermission(perm).equals(plugin))
                .collect(Collectors.toList());

        openInternal(p, rank, filtered, page, "Plugin: " + plugin);
    }

    public static void openSearch(Player p, Rank rank, String query, int page) {

        List<String> filtered = PermissionUtil.getAllPermissions().stream()
                .filter(perm -> perm.toLowerCase().contains(query))
                .collect(Collectors.toList());

        openInternal(p, rank, filtered, page, "Zoek: " + query);
    }

    /* ===================== */
    /* CORE GUI              */
    /* ===================== */

    private static void openInternal(Player p, Rank rank, List<String> perms, int page, String footer) {

        int maxPage = Math.max(1, (int) Math.ceil((double) perms.size() / ITEMS_PER_PAGE));
        page = Math.max(0, Math.min(page, maxPage - 1));

        Inventory inv = Bukkit.createInventory(
                null,
                54,
                "§8Permissions: §e" + rank.getName() + " §7(" + (page + 1) + "/" + maxPage + ")"
        );

        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, perms.size());

        int slot = 0;

        for (int i = start; i < end; i++) {

            String perm = perms.get(i);
            boolean has = rank.getPermissions().contains(perm);

            ItemStack item = new ItemStack(has ? Material.LIME_DYE : Material.RED_DYE);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName((has ? "§a✔ " : "§c✖ ") + perm);
            meta.setLore(List.of(
                    "§7Status: " + (has ? "§aAAN" : "§cUIT"),
                    "",
                    "§eKlik om te togglen"
            ));

            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        inv.setItem(48, button(Material.COMPASS, "§eZoeken"));
        inv.setItem(50, button(Material.CHEST, "§eFilter per plugin"));

        if (page > 0) inv.setItem(45, button(Material.ARROW, "§eVorige"));
        if (page + 1 < maxPage) inv.setItem(53, button(Material.ARROW, "§eVolgende"));

        p.openInventory(inv);
    }

    private static ItemStack button(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
