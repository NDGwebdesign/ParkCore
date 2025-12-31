package net.ndgwebdesign.parkCore.commands;

import net.ndgwebdesign.parkCore.managers.WarpManager;
import net.ndgwebdesign.parkCore.objects.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Alleen spelers kunnen dit gebruiken.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§eGebruik:");
            sender.sendMessage("§e/warp <naam>");
            sender.sendMessage("§e/warp create <naam>");
            sender.sendMessage("§e/warp delete <naam>");
            return true;
        }

        /* -------- CREATE -------- */
        if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 2) {
                sender.sendMessage("§cGebruik: /warp create <naam>");
                return true;
            }

            String name = args[1];

            if (WarpManager.exists(name)) {
                sender.sendMessage("§cWarp bestaat al.");
                return true;
            }

            Warp warp = new Warp(name, player.getLocation());
            WarpManager.saveWarp(warp);

            sender.sendMessage("§aWarp §e" + name + " §aangemaakt!");
            return true;
        }

        /* -------- DELETE -------- */
        if (args[0].equalsIgnoreCase("delete")) {

            if (args.length < 2) {
                sender.sendMessage("§cGebruik: /warp delete <naam>");
                return true;
            }

            String name = args[1];

            if (!WarpManager.exists(name)) {
                sender.sendMessage("§cWarp bestaat niet.");
                return true;
            }

            WarpManager.deleteWarp(name);
            sender.sendMessage("§aWarp §e" + name + " §averwijderd!");
            return true;
        }

        /* -------- TELEPORT -------- */
        String name = args[0];

        if (!WarpManager.exists(name)) {
            sender.sendMessage("§cWarp §e" + name + " §cbestaat niet.");
            return true;
        }

        player.teleport(WarpManager.getWarp(name).getLocation());
        sender.sendMessage("§aGeteleporteerd naar §e" + name + "§a!");
        return true;
    }
}
