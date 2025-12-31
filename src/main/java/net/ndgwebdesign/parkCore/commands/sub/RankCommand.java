package net.ndgwebdesign.parkCore.commands.sub;

import net.ndgwebdesign.parkCore.functions.UI.RankMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.ParkCore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Set;

public class RankCommand {

    public boolean handle(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sendHelp(sender);
            return true;
        }

        if (args[1].equalsIgnoreCase("set")) {
            return handleSet(sender, args);
        }

        if (args[1].equalsIgnoreCase("info")) {
            return handleInfo(sender, args);
        }

        if (args[1].equalsIgnoreCase("gui")) {

            if (args.length < 3) {
                sender.sendMessage("§eGebruik: /parkcore rank gui <player>");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[2]);
            if (target == null) {
                sender.sendMessage("§cSpeler niet online.");
                return true;
            }

            RankMenu.open((Player) sender, target);
            return true;
        }


        sendHelp(sender);
        return true;
    }

    /* ===================== */
    /* SET                   */
    /* ===================== */

    private boolean handleSet(CommandSender sender, String[] args) {

        if (!sender.hasPermission("parkcore.rank.set")) {
            sender.sendMessage("§cJe hebt geen permissie.");
            return true;
        }

        if (args.length < 4) {
            sender.sendMessage("§eGebruik: /parkcore rank set <player> <rank>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage("§cSpeler niet online.");
            return true;
        }

        String rankName = args[3].toLowerCase();
        Rank rank = RankManager.getRank(rankName);

        if (rank == null) {
            sender.sendMessage("§cRank §e" + rankName + " §cbestaat niet.");
            return true;
        }

        RankManager.setPlayerRank(target.getName(), rankName);
        applyRank(target, rank);

        sender.sendMessage("§aRank van §e" + target.getName()
                + " §ais nu §f" + rank.getDisplayName());

        target.sendMessage("§aJe rank is veranderd naar §f" + rank.getDisplayName());

        return true;
    }

    /* ===================== */
    /* INFO                  */
    /* ===================== */

    private boolean handleInfo(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§eGebruik: /parkcore rank info <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage("§cSpeler niet online.");
            return true;
        }

        Rank rank = RankManager.getPlayerRank(target.getName());

        sender.sendMessage("§7---- §eRank info §7----");
        sender.sendMessage("§eSpeler: §f" + target.getName());
        sender.sendMessage("§eRank: §f" + rank.getDisplayName());
        sender.sendMessage("§ePrefix: §f" + rank.getPrefix());
        sender.sendMessage("§ePermissions:");

        for (String perm : RankManager.getAllPermissions(rank)) {
            sender.sendMessage(" §8- §7" + perm);
        }

        return true;
    }

    /* ===================== */
    /* APPLY                 */
    /* ===================== */

    private void applyRank(Player player, Rank rank) {

        PermissionAttachment attachment = player.addAttachment(ParkCore.getInstance());

        Set<String> perms = RankManager.getAllPermissions(rank);
        for (String perm : perms) {
            attachment.setPermission(perm, true);
        }

        player.setDisplayName(
                rank.getPrefix() + player.getName() + rank.getSuffix()
        );
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§e/parkcore rank set <player> <rank>");
        sender.sendMessage("§e/parkcore rank info <player>");
    }
}
