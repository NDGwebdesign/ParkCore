package net.ndgwebdesign.parkCore.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.*;

public class PermissionUtil {

    public static List<String> getAllPermissions() {

        Set<String> perms = new HashSet<>();

        // ✅ 1. Alle geregistreerde permissions via Bukkit
        for (Permission perm : Bukkit.getPluginManager().getPermissions()) {
            perms.add(perm.getName());

            // ✅ 2. Ook child permissions meenemen
            if (perm.getChildren() != null) {
                perms.addAll(perm.getChildren().keySet());
            }
        }

        // ✅ 3. Extra: permissions van online players (failsafe)
        Bukkit.getOnlinePlayers().forEach(player ->
                player.getEffectivePermissions().forEach(info ->
                        perms.add(info.getPermission())
                )
        );

        // Sorteren
        List<String> list = new ArrayList<>(perms);
        Collections.sort(list);
        return list;
    }
}
