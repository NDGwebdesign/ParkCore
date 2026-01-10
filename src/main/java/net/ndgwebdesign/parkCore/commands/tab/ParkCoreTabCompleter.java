package net.ndgwebdesign.parkCore.commands.tab;

import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkCoreTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> completions = new ArrayList<>();

        /* ===================== */
        /* /parkcore             */
        /* ===================== */
        if (args.length == 1) {
            completions.add("att");
            completions.add("attraction");
            completions.add("rank");
            return filter(completions, args[0]);
        }

        /* ===================== */
        /* /parkcore att ...     */
        /* ===================== */
        if (args[0].equalsIgnoreCase("att")
                || args[0].equalsIgnoreCase("attraction")) {

            if (args.length == 2) {
                completions.add("create");
                completions.add("delete");
                completions.add("status");
                completions.add("info");
                return filter(completions, args[1]);
            }

            // /parkcore att delete <naam>
            if (args.length == 3 && args[1].equalsIgnoreCase("delete")) {
                return filter(
                        AttractionManager.getAll().stream()
                                .map(Attraction::getName)
                                .collect(Collectors.toList()),
                        args[2]
                );
            }
        }

        /* ===================== */
        /* /parkcore rank ...    */
        /* ===================== */
        if (args[0].equalsIgnoreCase("rank")) {

            if (args.length == 2) {
                completions.add("create");
                completions.add("delete");
                completions.add("perm");
                return filter(completions, args[1]);
            }

            // /parkcore rank delete <naam>
            if (args.length == 3 && args[1].equalsIgnoreCase("delete")) {
                return filter(
                        RankManager.getAllRanks().stream()
                                .map(Rank::getName)
                                .collect(Collectors.toList()),
                        args[2]
                );
            }

            // /parkcore rank perm <sub>
            if (args.length == 3 && args[1].equalsIgnoreCase("perm")) {
                completions.add("add");
                completions.add("remove");
                completions.add("menu");
                return filter(completions, args[2]);
            }

            // /parkcore rank perm <rank>
            if (args.length == 4 && args[1].equalsIgnoreCase("perm")) {
                return filter(
                        RankManager.getAllRanks().stream()
                                .map(Rank::getName)
                                .collect(Collectors.toList()),
                        args[3]
                );
            }
        }

        return completions;
    }

    /* ===================== */
    /* Utils                 */
    /* ===================== */

    private List<String> filter(List<String> list, String input) {
        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
