package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class StatusAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 4) {
            sender.sendMessage("§eUsage: /parkcore att status <attraction> <open|closed|maintenance>");
            return true;
        }

        String attractionName = args[2];
        String statusArg = args[3].toUpperCase();

        if (!AttractionManager.exists(attractionName)) {
            sender.sendMessage("§cAttraction §e" + attractionName + " §cdoes not exist.");
            return true;
        }

        AttractionStatus status;
        try {
            status = AttractionStatus.valueOf(statusArg);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("§cInvalid status. Use: open, closed or maintenance.");
            return true;
        }

        Attraction attraction = AttractionManager.getAttraction(attractionName);
        attraction.setStatus(status);

        // Save in config
        AttractionConfigManager.addAttraction(
                attraction.getRegion(),
                attractionName,
                status,
                attraction.getLocation());

        sender.sendMessage("§aStatus of attraction §e" + attractionName
                + " §ais now §f" + formatStatus(status) + "§a.");

        //notify all players
        Bukkit.broadcastMessage(
                "§e[Attraction Update] §f" + attractionName + " §ais now §f"
                        + formatStatus(status) + "§a.");

        return true;
    }

    private String formatStatus(AttractionStatus status) {
        return switch (status) {
            case OPEN -> "§aOPEN";
            case CLOSED -> "§cCLOSED";
            case MAINTENANCE -> "§6MAINTENANCE";
            default -> "§7UNKNOWN";
        };
    }
}
