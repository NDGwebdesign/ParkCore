package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.command.CommandSender;

public class StatusAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 4) {
            sender.sendMessage("§eGebruik: /parkcore att status <attractie> <open|closed|maintenance>");
            return true;
        }

        String attractionName = args[2];
        String statusArg = args[3].toUpperCase();

        if (!AttractionManager.exists(attractionName)) {
            sender.sendMessage("§cAttractie §e" + attractionName + " §cbestaat niet.");
            return true;
        }

        AttractionStatus status;
        try {
            status = AttractionStatus.valueOf(statusArg);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("§cOngeldige status. Gebruik: open, closed of maintenance.");
            return true;
        }

        Attraction attraction = AttractionManager.getAttraction(attractionName);
        attraction.setStatus(status);

        // Opslaan in config
        AttractionConfigManager.addAttraction(
                attraction.getRegion(),
                attractionName,
                status,
                attraction.getLocation()
        );

        sender.sendMessage("§aStatus van attractie §e" + attractionName
                + " §ais nu §f" + formatStatus(status) + "§a.");

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
