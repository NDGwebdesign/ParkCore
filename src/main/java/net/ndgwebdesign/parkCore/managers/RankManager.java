package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.objects.Rank;
import net.ndgwebdesign.parkCore.utils.PermissionUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RankManager {

    private static final Map<String, Rank> ranks = new HashMap<>();
    private static final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

    private static File ranksFile;
    private static File playersFile;

    private static FileConfiguration ranksConfig;
    private static FileConfiguration playersConfig;

    /* ===================== */
    /* Setup */
    /* ===================== */
    public static void setup() {

        File folder = new File(ParkCore.getInstance().getDataFolder(), "Ranks");
        if (!folder.exists())
            folder.mkdirs();

        ranksFile = new File(folder, "ranks.yml");
        playersFile = new File(folder, "players.yml");

        if (!ranksFile.exists()) {
            ParkCore.getInstance().saveResource("Ranks/ranks.yml", false);
        }

        if (!playersFile.exists()) {
            ParkCore.getInstance().saveResource("Ranks/players.yml", false);
        }

        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
        playersConfig = YamlConfiguration.loadConfiguration(playersFile);

        loadRanks();
    }

    /* ===================== */
    /* Load ranks */
    /* ===================== */
    private static void loadRanks() {

        ranks.clear();
        ConfigurationSection section = ranksConfig.getConfigurationSection("ranks");
        if (section == null)
            return;

        for (String key : section.getKeys(false)) {

            String path = "ranks." + key;
            Rank rank = new Rank(key.toLowerCase());

            rank.setDisplayName(color(ranksConfig.getString(path + ".display_name")));
            rank.setPrefix(color(ranksConfig.getString(path + ".prefix")));
            rank.setSuffix(color(ranksConfig.getString(path + ".suffix")));

            // ✅ permissions without extra characters
            rank.getPermissions().clear();
            for (String perm : ranksConfig.getStringList(path + ".permissions")) {
                rank.getPermissions().add(perm.trim());
            }

            rank.getInheritance().clear();
            rank.getInheritance().addAll(ranksConfig.getStringList(path + ".inheritance"));

            ranks.put(rank.getName(), rank);
        }
    }

    /* ===================== */
    /* Create / Delete */
    /* ===================== */
    public static void createRank(String name) {
        name = name.toLowerCase();
        if (ranks.containsKey(name))
            return;

        String path = "ranks." + name;
        ranksConfig.set(path + ".display_name", "&7" + name);
        ranksConfig.set(path + ".prefix", "&7[" + name + "] ");
        ranksConfig.set(path + ".suffix", "");
        ranksConfig.set(path + ".permissions", new ArrayList<String>());
        ranksConfig.set(path + ".inheritance", new ArrayList<String>());

        saveRanksFile();
        loadRanks();
    }

    public static void deleteRank(String name) {
        name = name.toLowerCase();
        if (name.equals("visitor") || !ranks.containsKey(name))
            return;

        ranksConfig.set("ranks." + name, null);
        saveRanksFile();
        loadRanks();
    }

    /* ===================== */
    /* Permissions */
    /* ===================== */
    public static Set<String> getAllPermissions(Rank rank) {
        Set<String> perms = new HashSet<>(rank.getPermissions());
        for (String parent : rank.getInheritance()) {
            Rank parentRank = getRank(parent);
            if (parentRank != null)
                perms.addAll(getAllPermissions(parentRank));
        }
        return perms;
    }

    public static void addPermission(String rankName, String permission) {
        Rank rank = getRank(rankName);
        if (rank == null)
            return;
        if (!PermissionUtil.isValidPermission(permission))
            return;

        permission = permission.trim().replace("§", ""); // ✅ altijd schoon

        if (!rank.getPermissions().contains(permission)) {
            rank.getPermissions().add(permission);
            saveRank(rank);

            // apply immediately to online players
            for (Player p : ParkCore.getInstance().getServer().getOnlinePlayers()) {
                if (getPlayerRank(p.getName()).getName().equals(rankName)) {
                    applyRank(p);
                }
            }
        }
    }

    public static void removePermission(String rankName, String permission) {
        Rank rank = getRank(rankName);
        if (rank == null)
            return;

        permission = permission.trim().replace("§", ""); // ✅ altijd schoon

        if (rank.getPermissions().remove(permission)) {
            saveRank(rank);

            for (Player p : ParkCore.getInstance().getServer().getOnlinePlayers()) {
                if (getPlayerRank(p.getName()).getName().equals(rankName)) {
                    applyRank(p);
                }
            }
        }
    }

    /* ===================== */
    /* Player ranks */
    /* ===================== */
    public static void setPlayerRank(String playerName, String rankName) {
        playersConfig.set("players." + playerName + ".rank", rankName.toLowerCase());
        savePlayersFile();

        Player player = ParkCore.getInstance().getServer().getPlayerExact(playerName);
        if (player != null)
            applyRank(player);
    }

    public static void applyRank(Player player) {

        Rank rank = getPlayerRank(player.getName());
        if (rank == null)
            return;

        // Reuse existing attachment or create a new one if it doesn't exist
        PermissionAttachment attachment = attachments.get(player.getUniqueId());
        if (attachment == null) {
            attachment = player.addAttachment(ParkCore.getInstance());
            attachments.put(player.getUniqueId(), attachment);
        } else {
            // Clear all previous permissions in this attachment
            Map<String, Boolean> perms = new HashMap<>(attachment.getPermissions());
            for (String perm : perms.keySet()) {
                attachment.unsetPermission(perm);
            }
        }

        // Apply all permissions for this rank
        for (String perm : getAllPermissions(rank)) {
            attachment.setPermission(perm, true);
        }

        // Update player's display name
        player.setDisplayName(rank.getPrefix() + player.getName() + rank.getSuffix());
    }

    /* ===================== */
    /* Save / Reload */
    /* ===================== */
    public static void saveRank(Rank rank) {

        String path = "ranks." + rank.getName();

        ranksConfig.set(path + ".display_name", uncolor(rank.getDisplayName()));
        ranksConfig.set(path + ".prefix", uncolor(rank.getPrefix()));
        ranksConfig.set(path + ".suffix", uncolor(rank.getSuffix()));

        List<String> cleanedPerms = new ArrayList<>();
        for (String perm : rank.getPermissions()) {
            if (PermissionUtil.isValidPermission(perm)) {
                cleanedPerms.add(perm);
            }
        }
        ranksConfig.set(path + ".permissions", cleanedPerms);
        ranksConfig.set(path + ".inheritance", rank.getInheritance());

        saveRanksFile();
    }

    public static void reloadRanks() {
        ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
        loadRanks();
    }

    private static void saveRanksFile() {
        try {
            ranksConfig.save(ranksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void savePlayersFile() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ===================== */
    /* Utils */
    /* ===================== */
    private static String color(String s) {
        return s == null ? "" : ChatColor.translateAlternateColorCodes('&', s);
    }

    private static String uncolor(String s) {
        return s == null ? "" : s.replace("§", "&");
    }

    /* ===================== */
    /* Getters */
    /* ===================== */
    public static Rank getRank(String name) {
        if (name == null)
            return null;
        return ranks.get(name.toLowerCase());
    }

    public static List<Rank> getAllRanks() {
        return new ArrayList<>(ranks.values());
    }

    public static Rank getRankByDisplay(String display) {
        for (Rank rank : ranks.values()) {
            if (ChatColor.stripColor(rank.getDisplayName())
                    .equalsIgnoreCase(ChatColor.stripColor(display)))
                return rank;
        }
        return null;
    }

    public static Rank getPlayerRank(String playerName) {
        String rankName = playersConfig.getString("players." + playerName + ".rank", "visitor");
        return getRank(rankName);
    }
}
