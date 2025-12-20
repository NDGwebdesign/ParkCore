package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.objects.Attraction;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;

public class AttractionManager {

    private static final Map<String, Attraction> attractions = new HashMap<>();

    public static void createAttraction(String name, String region) {
        Attraction attraction = new Attraction(name, region);
        attractions.put(name.toLowerCase(), attraction);
    }

    public static boolean exists(String name) {
        return attractions.containsKey(name.toLowerCase());
    }

    public static Attraction getAttraction(String name) {
        return attractions.get(name.toLowerCase());
    }
}
