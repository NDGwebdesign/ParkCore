package net.ndgwebdesign.parkCore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can do this.");
            return true;
        }

        // game mode commands: /gmc, /gms, /gma, /gmsp

        switch (label.toLowerCase()) {
            case "gmc" -> player.setGameMode(org.bukkit.GameMode.CREATIVE);
            case "gms" -> player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            case "gma" -> player.setGameMode(org.bukkit.GameMode.ADVENTURE);
            case "gmsp" -> player.setGameMode(org.bukkit.GameMode.SPECTATOR);
            default -> {
                player.sendMessage("Unknown command.");
                return true;
            }
        }
        player.sendMessage("Your game mode has been changed to " + player.getGameMode().toString() + ".");
        return true;
    }

}
