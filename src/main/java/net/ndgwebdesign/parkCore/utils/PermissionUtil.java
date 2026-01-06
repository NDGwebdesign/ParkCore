package net.ndgwebdesign.parkCore.utils;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PermissionUtil {

    public static List<String> getAllPermissions() {

        Set<String> perms = new HashSet<>();

        /* ========================= */
        /* 1. Bukkit geregistreerd   */
        /* ========================= */
        for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
            perms.add(perm.getName());
        }

        /* ========================= */
        /* 2. Plugin.yml permissions */
        /* ========================= */
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            for (Permission permission : plugin.getDescription().getPermissions()) {
                perms.add(permission.getName());
            }
        }

        /* ========================= */
        /* 3. Permissions uit ranks  */
        /* ========================= */
        for (Rank rank : RankManager.getAllRanks()) {
            perms.addAll(rank.getPermissions());
        }

        /* ========================= */
        /* 4. Wildcard uitbreiding   */
        /* ========================= */
        expandWildcards(perms);

        List<String> list = new ArrayList<>(perms);
        Collections.sort(list);
        return list;
    }

    private static void expandWildcards(Set<String> perms) {

        Set<String> expanded = new HashSet<>();

        for (String perm : perms) {
            if (perm.endsWith(".*")) {
                String base = perm.substring(0, perm.length() - 2);

                for (String other : perms) {
                    if (other.startsWith(base + ".")) {
                        expanded.add(other);
                    }
                }
            }
        }

        perms.addAll(expanded);
    }

    public static String getPluginFromPermission(String perm) {
        if (perm == null || !perm.contains(".")) return "other";
        return perm.split("\\.")[0].toLowerCase();
    }

    public static boolean isValidPermission(String perm) {
        if (perm == null) return false;

        perm = perm.replace("§", "").trim();

        // moet minstens 1 punt hebben (plugin.node)
        if (!perm.contains(".")) return false;

        // geen spaties
        if (perm.contains(" ")) return false;

        // alleen geldige tekens
        return perm.matches("^[a-zA-Z0-9._*-]+$");
    }

}
