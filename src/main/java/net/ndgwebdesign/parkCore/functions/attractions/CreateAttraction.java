package net.ndgwebdesign.parkCore.functions.attractions;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.*;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateAttraction {

    public boolean execute(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§eUsage: /parkcore att create <region> <name>");
            return true;
        }

        String region = args[2];
        String name = args[3];

        // Check if attraction already exists in memory
        if (AttractionManager.exists(name)) {
            sender.sendMessage("§cThis attraction already exists.");
            return true;
        }

        Player player = (Player) sender;

        // 1️⃣ Create attraction in memory
        Attraction attraction = new Attraction(name, region);
        attraction.setStatus(AttractionStatus.CLOSED);
        attraction.setLocation(player.getLocation());

        // 2️⃣ Save attraction in memory manager
        AttractionManager.addAttraction(attraction);

        // 3️⃣ Save attraction in YAML config
        AttractionConfigManager.addAttraction(
                region,
                name,
                attraction.getStatus(),
                attraction.getLocation());

        // create warp
        WarpManager.createWarp(name, player.getLocation());

        // Vraag RideOperate panel
        // RideOperate panel vraag
        if (ParkCore.getInstance().getRideOperateHook() != null
                && ParkCore.getInstance().getRideOperateHook().isEnabled()) {

            sender.sendMessage(" ");
            sender.sendMessage("§eDo you want to create a RideOperate control panel?");
            sender.sendMessage("§7Command: §f/createpanel " + name);
            sender.sendMessage("§7Type §aYES §7or §cNO §7in chat (30 seconds)");
            sender.sendMessage(" ");

            RidePanelConfirmationManager.add(
                    player,
                    new PendingPanelRequest(name, player.getLocation()));

            // Timeout na 30 seconden
            Bukkit.getScheduler().runTaskLater(ParkCore.getInstance(), () -> {
                if (RidePanelConfirmationManager.has(player)) {
                    RidePanelConfirmationManager.remove(player);
                    player.sendMessage("§cNo answer received. Panel not created.");
                }
            }, 20L * 30);
        }

        sender.sendMessage("§aAttraction §e" + name + " §ahas been created in region §e" + region + "§a!");
        sender.sendMessage("§7Status: §f" + attraction.getStatus().name());
        sender.sendMessage("§7Location saved at your position!");

        return true;
    }
}