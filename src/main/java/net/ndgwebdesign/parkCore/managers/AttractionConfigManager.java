package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttractionManager {

    private static File file;
    private static FileConfiguration config;

    public static void setup() {
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

        // Maak basissectie aan als die niet bestaat
        if (config.getConfigurationSection("attractions.regions") == null) {
            config.createSection("attractions.regions");
            saveConfig();
        }
    }

    public static FileConfiguration getConfig() { return config; }

    public static void saveConfig() {
        try { config.save(file); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /* ------------------------------ */
    /* Attractie functies             */
    /* ------------------------------ */

    public static void addAttraction(String region, String name, AttractionStatus status, Location loc) {

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
        return config.contains("attractions.regions." + region);
    }
    public static boolean attractionExists(String region, String name) {
        return config.contains("attractions.regions." + region + "." + name);
    }

    public static List<String> getAttractions(String region) {
        if (!regionExists(region)) return List.of();
        return List.copyOf(config.getConfigurationSection("attractions.regions." + region).getKeys(false));
    }

    public static AttractionStatus getStatus(String region, String name) {
        String path = "attractions.regions." + region + "." + name + ".status";
        String val = config.getString(path, "CLOSED");
        return AttractionStatus.valueOf(val);
    }

    public static Location getLocation(String region, String name) {
        String base = "attractions.regions." + region + "." + name + ".location";
        if (!config.contains(base)) return null;

        String world = config.getString(base + ".world");
        double x = config.getDouble(base + ".x");
        double y = config.getDouble(base + ".y");
        double z = config.getDouble(base + ".z");

        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
