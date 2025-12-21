package net.ndgwebdesign.parkCore;

import net.ndgwebdesign.parkCore.commands.ParkCoreCommand;
import net.ndgwebdesign.parkCore.listeners.MenuClickListener;
import net.ndgwebdesign.parkCore.listeners.MenuItemListener;
import net.ndgwebdesign.parkCore.listeners.PlayerJoinListener;
import net.ndgwebdesign.parkCore.listeners.RidePanelChatListener;
import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.rides.RideOperateHook;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ParkCore extends JavaPlugin {

    private static ParkCore instance;

    private FileConfiguration menuConfig;

    @Override
    public void onEnable() {
        instance = this;

        printStartupBanner();

        saveDefaultConfig();
        saveResource("menu.yml", false);
        menuConfig = YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "menu.yml")
        );

        AttractionConfigManager.setup();

        // Laad alle attracties
        loadAttractions();

        // Register Commands
        getCommand("parkcore").setExecutor(new ParkCoreCommand());

        //register events
        Bukkit.getPluginManager().registerEvents(new RidePanelChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(), this);

        Bukkit.getLogger().info("[ParkCore] Successfully enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ParkCore] Disabling ParkCore...");

        Bukkit.getLogger().info("[ParkCore] Successfully disabled!");
    }

    /* ---------------------------------------------------------------------- */
    /* Utility */
    /* ---------------------------------------------------------------------- */

    private void printStartupBanner() {
        getLogger().info(" ");
        getLogger().info(" ██████╗  █████╗ ██████╗ ██╗  ██╗  ██████╗ ██████╗ ██████╗ ███████╗");
        getLogger().info(" ██╔══██╗██╔══██╗██╔══██╗██║ ██╔╝ ██╔════╝██╔═══██╗██╔══██╗██╔════╝");
        getLogger().info(" ██████╔╝███████║██████╔╝█████╔╝  ██║     ██║   ██║██████╔╝█████╗  ");
        getLogger().info(" ██╔═══╝ ██╔══██║██╔══██╗██╔═██╗  ██║     ██║   ██║██╔══██╗██╔══╝  ");
        getLogger().info(" ██║     ██║  ██║██║  ██║██║  ██╗ ╚██████╗╚██████╔╝██║  ██║███████╗");
        getLogger().info(" ╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝  ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝");

        getLogger().info(" ");
        getLogger().info(" ParkCore - Themepark Engine");
        getLogger().info(" Version: " + getDescription().getVersion());
        getLogger().info(" Author: NDGWebDesign");
        getLogger().info(" ");
    }

    public static ParkCore getInstance() {
        return instance;
    }

    /* ---------------------------------------------------------------------- */
    /* Attracties laden bij plugin start                                      */
    /* ---------------------------------------------------------------------- */
    private void loadAttractions() {
        AttractionConfigManager.setup();

        // Haal alle regio's op
        for (String region : AttractionConfigManager.getConfig().getConfigurationSection("attractions.regions").getKeys(false)) {
            // Haal alle attracties in de regio op
            for (String name : AttractionConfigManager.getAttractions(region)) {

                // Maak een nieuwe attractie object
                Attraction attraction = new Attraction(name, region);

                // Laad status
                attraction.setStatus(AttractionConfigManager.getStatus(region, name));

                // Laad locatie
                if (AttractionConfigManager.getLocation(region, name) != null) {
                    attraction.setLocation(AttractionConfigManager.getLocation(region, name));
                }

                // Voeg toe aan de manager
                AttractionManager.addAttraction(attraction);

                // Log dat attractie geladen is
                Bukkit.getLogger().info("[ParkCore] Loaded attraction: " + name + " in region: " + region);
            }
        }
    }

    public RideOperateHook getRideOperateHook() {
        // Controleer of de RideOperate plugin actief is
        if (Bukkit.getPluginManager().getPlugin("RideOperate") != null) {
            return new RideOperateHook(this);
        }
        return null;
    }

    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }

}
