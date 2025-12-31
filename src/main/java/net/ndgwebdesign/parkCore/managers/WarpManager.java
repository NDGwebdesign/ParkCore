package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.ParkCore;
import net.ndgwebdesign.parkCore.objects.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

    private static final Map<String, Warp> warps = new HashMap<>();
    private static File file;
    private static FileConfiguration config;

    /* -------------------- */
    /* Setup                */
    /* -------------------- */

    public static void setup() {
        file = new File(ParkCore.getInstance().getDataFolder(), "warps.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadWarps();
    }

    /* -------------------- */
    /* Load / Save           */
    /* -------------------- */

    public static void loadWarps() {
        warps.clear();

        if (!config.contains("warps")) return;

        for (String name : config.getConfigurationSection("warps").getKeys(false)) {

            String base = "warps." + name;
            String world = config.getString(base + ".world");

            Location loc = new Location(
                    Bukkit.getWorld(world),
                    config.getDouble(base + ".x"),
                    config.getDouble(base + ".y"),
                    config.getDouble(base + ".z"),
                    (float) config.getDouble(base + ".yaw"),
                    (float) config.getDouble(base + ".pitch")
            );

            warps.put(name.toLowerCase(), new Warp(name, loc));
        }
    }

    public static void saveWarp(Warp warp) {
        String base = "warps." + warp.getName();

        Location l = warp.getLocation();
        config.set(base + ".world", l.getWorld().getName());
        config.set(base + ".x", l.getX());
        config.set(base + ".y", l.getY());
        config.set(base + ".z", l.getZ());
        config.set(base + ".yaw", l.getYaw());
        config.set(base + ".pitch", l.getPitch());

        saveFile();
        warps.put(warp.getName().toLowerCase(), warp);
    }

    public static void deleteWarp(String name) {
        config.set("warps." + name, null);
        saveFile();
        warps.remove(name.toLowerCase());
    }

    public static void createWarp(String name, Location location) {
        if (exists(name)) return;
        saveWarp(new Warp(name, location));
    }

    public static void removeWarp(String name) {
        if (!exists(name)) return;
        deleteWarp(name);
    }


    private static void saveFile() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* -------------------- */
    /* Getters               */
    /* -------------------- */

    public static boolean exists(String name) {
        return warps.containsKey(name.toLowerCase());
    }

    public static Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }
}
