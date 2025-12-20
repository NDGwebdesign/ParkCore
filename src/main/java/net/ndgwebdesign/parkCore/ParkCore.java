package net.ndgwebdesign.parkCore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParkCore extends JavaPlugin {

    private static ParkCore instance;

    @Override
    public void onEnable() {
        instance = this;

        printStartupBanner();

        saveDefaultConfig();

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

}
