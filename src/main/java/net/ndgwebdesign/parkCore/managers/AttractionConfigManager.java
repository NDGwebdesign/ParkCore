package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AttractionConfigManager {

    private static File file;
    private static FileConfiguration config;

    /**
     * Ensure the config is always loaded
     */
    public static void setup() {
        if (config != null)
            return; // al geladen

        file = new File(ParkCore.getInstance().getDataFolder(), "attractions.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        // Create base section if it does not exist
        if (config.getConfigurationSection("attractions.regions") == null) {
            config.createSection("attractions.regions");
            saveConfig();
        }
    }

    public static FileConfiguration getConfig() {
        if (config == null)
            setup(); // fallback
        return config;
    }

    public static void saveConfig() {
        if (config == null)
            setup();
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        if (file == null)
            setup();
        config = YamlConfiguration.loadConfiguration(file);
    }

    /* ------------------------------ */
    /* Attraction functions */
    /* ------------------------------ */

    public static void addAttraction(String region, String name, AttractionStatus status, Location loc) {
        setup();

        String path = "attractions.regions." + region + "." + name;

        config.set(path + ".status", status.name());

        if (loc != null) {
            config.set(path + ".location.world", loc.getWorld().getName());
            config.set(path + ".location.x", loc.getX());
            config.set(path + ".location.y", loc.getY());
            config.set(path + ".location.z", loc.getZ());
        }

        saveConfig();
    }

    public static boolean regionExists(String region) {
        setup();
        return config.contains("attractions.regions." + region);
    }

    public static List<String> getAttractions(String region) {
        setup();
        if (!regionExists(region))
            return List.of();
        return List.copyOf(config.getConfigurationSection("attractions.regions." + region).getKeys(false));
    }

    public static AttractionStatus getStatus(String region, String name) {
        setup();
        String path = "attractions.regions." + region + "." + name + ".status";
        String val = config.getString(path, "CLOSED");
        return AttractionStatus.valueOf(val);
    }

    public static Location getLocation(String region, String name) {
        setup();
        String base = "attractions.regions." + region + "." + name + ".location";
        if (!config.contains(base))
            return null;

        String world = config.getString(base + ".world");
        double x = config.getDouble(base + ".x");
        double y = config.getDouble(base + ".y");
        double z = config.getDouble(base + ".z");

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static boolean removeAttraction(String region, String name) {
        setup();

        String path = "attractions.regions." + region + "." + name;

        if (!config.contains(path)) {
            return false;
        }

        config.set(path, null);

        // If region is empty, remove region as well (optional but tidy)
        if (config.getConfigurationSection("attractions.regions." + region).getKeys(false).isEmpty()) {
            config.set("attractions.regions." + region, null);
        }

        saveConfig();
        return true;
    }

}
