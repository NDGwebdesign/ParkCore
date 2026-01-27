package net.ndgwebdesign.parkCore.commands;

import net.ndgwebdesign.parkCore.commands.sub.AttractionCommand;
import net.ndgwebdesign.parkCore.commands.sub.RankCommand;
import net.ndgwebdesign.parkCore.commands.sub.ReloadCommand;
import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.managers.WarpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ParkCoreCommand implements CommandExecutor {

    private final AttractionCommand attractionCommand = new AttractionCommand();
    private final RankCommand rankCommand = new RankCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
            sender.sendMessage("§e§l=== ParkCore Help ===");
            sender.sendMessage("§6/parkcore att create <region> <name> §7- Create an attraction and warp");
            sender.sendMessage("§6/parkcore att delete <name> §7- Delete an attraction");
            sender.sendMessage("§6/parkcore att status <name> <open|closed|maintenance> §7- Set attraction status");
            sender.sendMessage("§6/parkcore rank create <name> §7- Create a rank");
            sender.sendMessage("§6/parkcore rank delete <name> §7- Delete a rank");
            sender.sendMessage("§6/parkcore rank set <player> <rank> §7- Assign rank to a player");
            sender.sendMessage("§6/parkcore rank gui <player> §7- Open the Rank GUI for a player");
            sender.sendMessage("§6/parkcore reload §7- Reload ParkCore configuration");
            sender.sendMessage("§6/parkcore info §7- Plugin info and counts");
            return true;
        }

        if (args[0].equalsIgnoreCase("att") || args[0].equalsIgnoreCase("attraction")) {
            return attractionCommand.handle(sender, args);
        }

        if (args[0].equalsIgnoreCase("rank")) {
            return rankCommand.handle(sender, args);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            return ReloadCommand.handle(sender);
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("§e§l=== ParkCore Info ===");
            sender.sendMessage("§6Version: §f" + ParkCore.getInstance().getDescription().getVersion());
            sender.sendMessage(
                    "§6Authors: §f" + String.join(", ", ParkCore.getInstance().getDescription().getAuthors()));
            sender.sendMessage("§6RideOperate hook: §f"
                    + (ParkCore.getInstance().getRideOperateHook() != null ? "Available" : "Not present"));
            return true;
        }

        sender.sendMessage("§cUnknown subcommand. Use §e/parkcore§c for help.");
        return true;
    }
}
