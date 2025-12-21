package net.ndgwebdesign.parkCore.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Attraction {

    private final String name;
    private final String region;
    private AttractionStatus status;
    private Location location;

    public Attraction(String name, String region) {
        this.name = name;
        this.region = region;
        this.status = AttractionStatus.CLOSED;
        this.location = null;
    }

    public String getName() { return name; }

    public String getRegion() { return region; }

    public AttractionStatus getStatus() { return status; }

    public void setStatus(AttractionStatus status) { this.status = status; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }
}
