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
            sender.sendMessage("§cDit command kan alleen door spelers gebruikt worden.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§eGebruik: /parkcore att create <region> <naam>");
            return true;
        }

        String region = args[2];
        String name = args[3];

        // Controleer of attractie al bestaat in memory
        if (AttractionManager.exists(name)) {
            sender.sendMessage("§cDeze attractie bestaat al.");
            return true;
        }

        Player player = (Player) sender;

        // 1️⃣ Maak attractie in memory
        Attraction attraction = new Attraction(name, region);
        attraction.setStatus(AttractionStatus.CLOSED);
        attraction.setLocation(player.getLocation());

        // 2️⃣ Sla attractie op in memory manager
        AttractionManager.addAttraction(attraction);

        // 3️⃣ Sla attractie op in YAML config
        AttractionConfigManager.addAttraction(
                region,
                name,
                attraction.getStatus(),
                attraction.getLocation()
        );

        // create warp
        WarpManager.createWarp(name, player.getLocation());

        // Vraag RideOperate panel
        // RideOperate panel vraag
        if (ParkCore.getInstance().getRideOperateHook() != null
                && ParkCore.getInstance().getRideOperateHook().isEnabled()) {

            sender.sendMessage(" ");
            sender.sendMessage("§eWil je een RideOperate bedieningspaneel aanmaken?");
            sender.sendMessage("§7Command: §f/createpanel " + name);
            sender.sendMessage("§7Typ §aJA §7of §cNEE §7in de chat (30 seconden)");
            sender.sendMessage(" ");

            RidePanelConfirmationManager.add(
                    player,
                    new PendingPanelRequest(name, player.getLocation())
            );

            // Timeout na 30 seconden
            Bukkit.getScheduler().runTaskLater(ParkCore.getInstance(), () -> {
                if (RidePanelConfirmationManager.has(player)) {
                    RidePanelConfirmationManager.remove(player);
                    player.sendMessage("§cGeen antwoord ontvangen. Panel niet aangemaakt.");
                }
            }, 20L * 30);
        }



        sender.sendMessage("§aAttractie §e" + name + " §ais aangemaakt in region §e" + region + "§a!");
        sender.sendMessage("§7Status: §f" + attraction.getStatus().name());
        sender.sendMessage("§7Locatie opgeslagen bij je positie!");

        return true;
    }
}