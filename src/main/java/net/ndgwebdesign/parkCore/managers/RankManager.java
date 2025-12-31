package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.objects.Rank;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class RankManager {

    private static final Map<String, Rank> ranks = new HashMap<>();
    private static FileConfiguration rankConfig;
    private static FileConfiguration playerConfig;

    /* ===================== */
    /* Setup                  */
    /* ===================== */

    public static void setup() {

        File rankFile = new File(ParkCore.getInstance().getDataFolder(), "Ranks/ranks.yml");
        File playerFile = new File(ParkCore.getInstance().getDataFolder(), "Ranks/players.yml");

        rankFile.getParentFile().mkdirs();

        if (!rankFile.exists()) {
            ParkCore.getInstance().saveResource("Ranks/ranks.yml", false);
        }

        if (!playerFile.exists()) {
            ParkCore.getInstance().saveResource("Ranks/players.yml", false);
        }

        rankConfig = YamlConfiguration.loadConfiguration(rankFile);
        playerConfig = YamlConfiguration.loadConfiguration(playerFile);

        loadRanks();
    }

    /* ===================== */
    /* Load ranks             */
    /* ===================== */

    private static void loadRanks() {

        ranks.clear();

        var section = rankConfig.getConfigurationSection("ranks");
        if (section == null) return;

        for (String key : section.getKeys(false)) {

            String base = "ranks." + key;

            Rank rank = new Rank(
                    key,
                    color(rankConfig.getString(base + ".display_name")),
                    color(rankConfig.getString(base + ".prefix")),
                    color(rankConfig.getString(base + ".suffix")),
                    rankConfig.getStringList(base + ".permissions"),
                    rankConfig.getStringList(base + ".inheritance")
            );

            ranks.put(key.toLowerCase(), rank);
        }
    }

    /* ===================== */
    /* Getters                */
    /* ===================== */

    public static Rank getRank(String name) {
        return ranks.get(name.toLowerCase());
    }

    public static Rank getPlayerRank(String playerName) {

        String rankName = playerConfig.getString(
                "players." + playerName + ".rank",
                "visitor"
        );

        return getRank(rankName);
    }

    /* ===================== */
    /* Permissions            */
    /* ===================== */

    public static Set<String> getAllPermissions(Rank rank) {

        Set<String> perms = new HashSet<>(rank.getPermissions());

        for (String parent : rank.getInheritance()) {
            Rank parentRank = getRank(parent);
            if (parentRank != null) {
                perms.addAll(getAllPermissions(parentRank));
            }
        }
        return perms;
    }

    /* ===================== */
    /* Utils                  */
    /* ===================== */

    private static String color(String s) {
        return s == null ? "" : s.replace("&", "§");
    }

    public static void setPlayerRank(String playerName, String rankName) {

        playerConfig.set("players." + playerName + ".rank", rankName);

        try {
            playerConfig.save(
                    new File(ParkCore.getInstance().getDataFolder(), "Ranks/players.yml")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Rank> getAllRanks() {
        return new ArrayList<>(ranks.values());
    }

    public static Rank getRankByDisplay(String displayName) {
        for (Rank rank : ranks.values()) {
            if (rank.getDisplayName().equalsIgnoreCase(displayName)) {
                return rank;
            }
        }
        return null;
    }

    public static void applyRank(Player player) {

        Rank rank = getPlayerRank(player.getName());

        player.getEffectivePermissions().clear();

        var attachment = player.addAttachment(ParkCore.getInstance());

        for (String perm : getAllPermissions(rank)) {
            attachment.setPermission(perm, true);
        }

        player.setDisplayName(
                rank.getPrefix() + player.getName() + rank.getSuffix()
        );
    }


}
