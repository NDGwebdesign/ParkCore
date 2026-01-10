package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.managers.WarpManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import org.bukkit.command.CommandSender;

public class DeleteAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§eUsage:");
            sender.sendMessage("§e/parkcore att delete <name>");
            sender.sendMessage("§e/parkcore att delete <region> <name>");
            return true;
        }

        String region;
        String name;

        // Variant: /parkcore att delete <region> <naam>
        if (args.length >= 4) {
            region = args[2];
            name = args[3];

            if (!AttractionConfigManager.regionExists(region)) {
                sender.sendMessage("§cRegion §e" + region + " §cdoes not exist.");
                return true;
            }

            if (!AttractionManager.exists(name)) {
                sender.sendMessage("§cAttraction §e" + name + " §cdoes not exist.");
                return true;
            }

            Attraction attraction = AttractionManager.getAttraction(name);
            if (!attraction.getRegion().equalsIgnoreCase(region)) {
                sender.sendMessage("§cAttraction §e" + name + " §cis not in region §e" + region + "§c.");
                sender.sendMessage("§7Located in region: §f" + attraction.getRegion());
            }

        }
        // Variant: /parkcore att delete <naam>
        else {
            name = args[2];

            if (!AttractionManager.exists(name)) {
                sender.sendMessage("§cAttraction §e" + name + " §cdoes not exist.");
                return true;
            }

            Attraction attraction = AttractionManager.getAttraction(name);
            region = attraction.getRegion();

            if (!AttractionConfigManager.regionExists(region)) {
                sender.sendMessage("§cCould not find region for attraction §e" + name + "§c.");
                return true;
            }
        }

        // 🔥 Removing from memory
        AttractionManager.removeAttraction(name);

        if (WarpManager.exists(name)) {
            WarpManager.removeWarp(name);
        }

        // 🔥 Removing from config
        boolean removed = AttractionConfigManager.removeAttraction(region, name);

        if (!removed) {
            sender.sendMessage("§cError deleting attraction from config.");
            return true;
        }

        sender.sendMessage("§aAttraction §e" + name + " §ahas been successfully removed!");
        sender.sendMessage("§7Region: §f" + region);

        return true;
    }
}
