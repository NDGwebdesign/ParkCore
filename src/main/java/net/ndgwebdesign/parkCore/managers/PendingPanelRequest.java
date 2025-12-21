package net.ndgwebdesign.parkCore.managers;

import org.bukkit.Location;

public class PendingPanelRequest {

    private final String attractionName;
    private final Location location;

    public PendingPanelRequest(String attractionName, Location location) {
        this.attractionName = attractionName;
        this.location = location;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public Location getLocation() {
        return location;
    }
}
