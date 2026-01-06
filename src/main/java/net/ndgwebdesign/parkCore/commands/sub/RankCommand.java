package net.ndgwebdesign.parkCore.commands.sub;

import net.ndgwebdesign.parkCore.functions.UI.RankMenu;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand {

    public boolean handle(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sendHelp(sender);
            return true;
        }

        return switch (args[1].toLowerCase()) {
            case "create" -> handleCreate(sender, args);
            case "delete" -> handleDelete(sender, args);
            case "set"    -> handleSet(sender, args);
            case "info"   -> handleInfo(sender, args);
            case "gui"    -> handleGui(sender, args);
            case "perm"   -> handlePermission(sender, args);
            default       -> { sendHelp(sender); yield true; }
        };
    }

    /* ===================== */
    /* PERMISSIONS           */
    /* ===================== */

    private boolean handlePermission(CommandSender sender, String[] args) {

        if (!sender.hasPermission("parkcore.rank.permission")) {
            sender.sendMessage("§cJe hebt geen permissie.");
            return true;
        }

        if (args.length < 5) {
            sender.sendMessage("§eGebruik:");
            sender.sendMessage("§e/parkcore rank perm add <rank> <permission>");
            sender.sendMessage("§e/parkcore rank perm remove <rank> <permission>");
            return true;
        }

        String action = args[2].toLowerCase();
        String rankName = args[3].toLowerCase();
        String permission = args[4].trim().replace("§", "");

        Rank rank = RankManager.getRank(rankName);
        if (rank == null) {
            sender.sendMessage("§cDeze rank bestaat niet.");
            return true;
        }

        switch (action) {

            case "add" -> {
                RankManager.addPermission(rankName, permission);
                sender.sendMessage("§aPermission §e" + permission + " §ais toegevoegd aan §e" + rankName);
            }

            case "remove" -> {
                RankManager.removePermission(rankName, permission);
                sender.sendMessage("§cPermission §e" + permission + " §cis verwijderd van §e" + rankName);
            }

            default -> sender.sendMessage("§cGebruik add of remove.");
        }

        return true;
    }

    /* ===================== */
    /* CREATE                */
    /* ===================== */

    private boolean handleCreate(CommandSender sender, String[] args) {

        if (!sender.hasPermission("parkcore.rank.create")) {
            sender.sendMessage("§cJe hebt geen permissie.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§eGebruik: /parkcore rank create <naam>");
            return true;
        }

        String name = args[2].toLowerCase();

        if (RankManager.getRank(name) != null) {
            sender.sendMessage("§cRank §e" + name + " §cbestaat al.");
            return true;
        }

        RankManager.createRank(name);
        sender.sendMessage("§aRank §e" + name + " §ais aangemaakt!");
        return true;
    }

    /* ===================== */
    /* DELETE                */
    /* ===================== */

    private boolean handleDelete(CommandSender sender, String[] args) {

        if (!sender.hasPermission("parkcore.rank.delete")) {
            sender.sendMessage("§cJe hebt geen permissie.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§eGebruik: /parkcore rank delete <naam>");
            return true;
        }

        String name = args[2].toLowerCase();

        if (name.equals("visitor")) {
            sender.sendMessage("§cDe basisrank §evisitor §ckan niet worden verwijderd.");
            return true;
        }

        if (RankManager.getRank(name) == null) {
            sender.sendMessage("§cRank §e" + name + " §cbestaat niet.");
            return true;
        }

        RankManager.deleteRank(name);
        sender.sendMessage("§aRank §e" + name + " §ais verwijderd!");
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

        Rank rank = RankManager.getRank(args[3]);
        if (rank == null) {
            sender.sendMessage("§cDeze rank bestaat niet.");
            return true;
        }

        RankManager.setPlayerRank(target.getName(), rank.getName());
        RankManager.applyRank(target);

        sender.sendMessage("§aRank van §e" + target.getName()
                + " §ais nu " + rank.getDisplayName());

        target.sendMessage("§aJe rank is veranderd naar " + rank.getDisplayName());
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

        RankManager.getAllPermissions(rank)
                .forEach(p -> sender.sendMessage(" §8- §7" + p));

        return true;
    }

    /* ===================== */
    /* GUI                   */
    /* ===================== */

    private boolean handleGui(CommandSender sender, String[] args) {

        if (!(sender instanceof Player staff)) {
            sender.sendMessage("§cAlleen spelers kunnen dit gebruiken.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§eGebruik: /parkcore rank gui <player>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            staff.sendMessage("§cSpeler niet online.");
            return true;
        }

        RankMenu.open(staff, target);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§e/parkcore rank create <naam>");
        sender.sendMessage("§e/parkcore rank delete <naam>");
        sender.sendMessage("§e/parkcore rank set <player> <rank>");
        sender.sendMessage("§e/parkcore rank perm add <rank> <permission>");
        sender.sendMessage("§e/parkcore rank perm remove <rank> <permission>");
        sender.sendMessage("§e/parkcore rank info <player>");
        sender.sendMessage("§e/parkcore rank gui <player>");
    }
}
