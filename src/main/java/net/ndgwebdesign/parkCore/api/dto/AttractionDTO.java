package net.ndgwebdesign.parkCore.api.dto;

import net.ndgwebdesign.parkCore.objects.Attraction;
import net.ndgwebdesign.parkCore.objects.AttractionStatus;

import java.util.List;

public class AttractionDTO {

    public String name;
    public String region;
    public AttractionStatus status;
    public String world;
    public double x;
    public double y;
    public double z;

    public static AttractionDTO from(Attraction attraction) {
        AttractionDTO dto = new AttractionDTO();

        dto.name = attraction.getName();
        dto.region = attraction.getRegion();
        dto.status = attraction.getStatus();

        if (attraction.getLocation() != null) {
            dto.world = attraction.getLocation().getWorld().getName();
            dto.x = attraction.getLocation().getX();
            dto.y = attraction.getLocation().getY();
            dto.z = attraction.getLocation().getZ();
        }

        return dto;
    }
}
