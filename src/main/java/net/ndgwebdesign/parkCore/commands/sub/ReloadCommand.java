package net.ndgwebdesign.parkCore.commands.sub;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.managers.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand {

    public static boolean handle(CommandSender sender) {

        if (!sender.hasPermission("parkcore.admin")) {
            sender.sendMessage("§cYou do not have permission to do this.");
            return true;
        }

        sender.sendMessage("§eReloading ParkCore...");

        /* ===================== */
        /* Save configs */
        /* ===================== */
        AttractionConfigManager.saveConfig();
        RankManager.reloadRanks(); // reload includes save safety
        WarpManager.setup(); // reload warps.yml

        /* ===================== */
        /* Reload configs */
        /* ===================== */
        AttractionConfigManager.reloadConfig();
        RankManager.reloadRanks();
        WarpManager.loadWarps();

        /* ===================== */
        /* Re-apply ranks live */
        /* ===================== */
        for (Player player : ParkCore.getInstance().getServer().getOnlinePlayers()) {
            RankManager.applyRank(player);
        }

        sender.sendMessage("§aParkCore successfully reloaded!");
        return true;
    }
}
