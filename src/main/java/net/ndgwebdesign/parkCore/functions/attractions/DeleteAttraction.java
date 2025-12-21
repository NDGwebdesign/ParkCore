package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import org.bukkit.command.CommandSender;

public class DeleteAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§eGebruik:");
            sender.sendMessage("§e/parkcore att delete <naam>");
            sender.sendMessage("§e/parkcore att delete <region> <naam>");
            return true;
        }

        String region;
        String name;

        // Variant: /parkcore att delete <region> <naam>
        if (args.length >= 4) {
            region = args[2];
            name = args[3];

            if (!AttractionConfigManager.regionExists(region)) {
                sender.sendMessage("§cRegion §e" + region + " §cbestaat niet.");
                return true;
            }

            if (!AttractionManager.exists(name)) {
                sender.sendMessage("§cAttractie §e" + name + " §cbestaat niet.");
                return true;
            }

            Attraction attraction = AttractionManager.getAttraction(name);
            if (!attraction.getRegion().equalsIgnoreCase(region)) {
                sender.sendMessage("§cAttractie §e" + name + " §cstaat niet in region §e" + region + "§c.");
                sender.sendMessage("§7Staat in region: §f" + attraction.getRegion());
                return true;
            }

        }
        // Variant: /parkcore att delete <naam>
        else {
            name = args[2];

            if (!AttractionManager.exists(name)) {
                sender.sendMessage("§cAttractie §e" + name + " §cbestaat niet.");
                return true;
            }

            Attraction attraction = AttractionManager.getAttraction(name);
            region = attraction.getRegion();

            if (!AttractionConfigManager.regionExists(region)) {
                sender.sendMessage("§cKon region van attractie §e" + name + " §cniet vinden.");
                return true;
            }
        }

        // 🔥 Verwijderen uit memory
        AttractionManager.removeAttraction(name);

        // 🔥 Verwijderen uit config
        boolean removed = AttractionConfigManager.removeAttraction(region, name);

        if (!removed) {
            sender.sendMessage("§cFout bij verwijderen van attractie uit config.");
            return true;
        }

        sender.sendMessage("§aAttractie §e" + name + " §ais succesvol verwijderd!");
        sender.sendMessage("§7Region: §f" + region);

        return true;
    }
}
