package net.ndgwebdesign.parkCore;

import net.ndgwebdesign.parkCore.api.ApiServer;
import net.ndgwebdesign.parkCore.commands.GamemodeCommand;
import net.ndgwebdesign.parkCore.commands.ParkCoreCommand;
import net.ndgwebdesign.parkCore.commands.WarpCommand;
import net.ndgwebdesign.parkCore.commands.tab.RankTabCompleter;
import net.ndgwebdesign.parkCore.listeners.*;
import net.ndgwebdesign.parkCore.managers.AttractionConfigManager;
import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.managers.RankManager;
import net.ndgwebdesign.parkCore.managers.WarpManager;
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

    private ApiServer apiServer;

    @Override
    public void onEnable() {
        instance = this;

        printStartupBanner();

        saveDefaultConfig();
        saveResource("Menu/menu.yml", false);
        menuConfig = YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "Menu/menu.yml")
        );


        AttractionConfigManager.setup();
        WarpManager.setup();
        RankManager.setup();

        // Laad alle attracties
        loadAttractions();

        loadAllConfigFiles();

        // Register Commands
        getCommand("parkcore").setExecutor(new ParkCoreCommand());
        getCommand("warp").setExecutor(new WarpCommand());

        getCommand("gmc").setExecutor(new GamemodeCommand());
        getCommand("gms").setExecutor(new GamemodeCommand());
        getCommand("gma").setExecutor(new GamemodeCommand());
        getCommand("gmsp").setExecutor(new GamemodeCommand());

        //register events
        Bukkit.getPluginManager().registerEvents(new RidePanelChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new AttractionMenuClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new AttractionSignCreateListener(), this);

        //rank events
        Bukkit.getPluginManager().registerEvents(new PlayerJoinRankListener(), this);
        Bukkit.getPluginManager().registerEvents(new RankMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSelectMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdminMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new RankPermissionSelectListener(), this);
        Bukkit.getPluginManager().registerEvents(new RankPermissionMenuListener(), this);
        getServer().getPluginManager().registerEvents(new PluginFilterMenuListener(), this);
        getServer().getPluginManager().registerEvents(new PermissionSearchListener(), this);

        //tab complete
        getCommand("parkcore").setTabCompleter(new RankTabCompleter());

        // Start API Server
        apiServer = new ApiServer(this, getConfig().getInt("api.port"));
        apiServer.start();

        Bukkit.getLogger().info("[ParkCore] Successfully enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ParkCore] Disabling ParkCore...");

        // Stop API Server
        if (apiServer != null) {
            apiServer.stop();
        }

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
        getLogger().info(" ParkCore - Themepark Management Plugin");
        getLogger().info(" Version: " + getDescription().getVersion());
        getLogger().info(" Author: FriendsparkMC, NDG-Webdesign");
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

    private void loadAllConfigFiles() {
        loadConfigFile("config.yml");
        loadConfigFile("attractions.yml");
        loadConfigFile("warps.yml");

        loadConfigFile("Ranks/ranks.yml");
        loadConfigFile("Ranks/players.yml");
    }

    private void loadConfigFile(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
            getLogger().info("Created " + name);
        } else {
            getLogger().info("Loaded " + name);
        }
    }
}
