package net.ndgwebdesign.parkCore.objects;

import net.ndgwebdesign.parkCore.signs.AttractionStatusSign;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

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

    public void setStatus(AttractionStatus status) {
        this.status = status;

        for (Location loc : signs) {
            AttractionStatusSign.update(loc, this);
        }
    }



    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    private final List<Location> signs = new ArrayList<>();

    public void addSign(Location loc) {
        signs.add(loc);
    }

    public List<Location> getSigns() {
        return signs;
    }

}
