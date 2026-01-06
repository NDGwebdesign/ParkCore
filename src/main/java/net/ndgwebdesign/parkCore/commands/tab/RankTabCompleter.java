package net.ndgwebdesign.parkCore.commands.tab;

import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public class RankTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 2) {
            return List.of("create", "delete", "set", "info", "gui", "perm");
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("perm")) {
            return List.of("add", "remove");
        }

        if (args.length == 4 && args[1].equalsIgnoreCase("perm")) {
            return RankManager.getAllRanks()
                    .stream()
                    .map(r -> r.getName())
                    .collect(Collectors.toList());
        }

        if (args.length == 5 && args[1].equalsIgnoreCase("perm")) {
            return PermissionUtil.getAllPermissions();
        }

        return List.of();
    }
}
