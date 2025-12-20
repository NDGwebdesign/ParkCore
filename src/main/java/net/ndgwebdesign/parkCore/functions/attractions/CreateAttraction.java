package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.managers.AttractionManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDit command kan alleen door spelers gebruikt worden.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§eGebruik: /parkcore att create <region> <naam>");
            return true;
        }

        String region = args[2];
        String name = args[3];

        if (AttractionManager.exists(name)) {
            sender.sendMessage("§cDeze attractie bestaat al.");
            return true;
        }

        AttractionManager.createAttraction(name, region);

        sender.sendMessage("§aAttractie §e" + name + " §ais aangemaakt!");
        sender.sendMessage("§7Region: §f" + region);

        return true;
    }
}
