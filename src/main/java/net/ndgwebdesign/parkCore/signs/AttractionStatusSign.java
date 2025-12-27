package net.ndgwebdesign.parkCore.signs;

import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class AttractionStatusSign {

    public static void update(Location loc, Attraction attraction) {

        if (loc.getBlock().getState() instanceof Sign sign) {

            sign.setLine(0, "§6[ParkCore]");
            sign.setLine(1, "§eAttractie");
            sign.setLine(2, "§f" + attraction.getName());
            sign.setLine(3, formatStatus(attraction.getStatus()));

            sign.update();
        }
    }

    private static String formatStatus(AttractionStatus status) {
        return switch (status) {
            case OPEN -> "§aOPEN";
            case CLOSED -> "§cCLOSED";
            case MAINTENANCE -> "§6MAINTENANCE";
            default -> "§7UNKNOWN";
        };
    }
}
