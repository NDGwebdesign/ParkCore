package net.ndgwebdesign.parkCore.managers;

import net.ndgwebdesign.parkCore.objects.Attraction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AttractionManager {

    private static final Map<String, Attraction> attractions = new HashMap<>();

    public static void addAttraction(Attraction attraction) {
        attractions.put(attraction.getName().toLowerCase(), attraction);
    }

    public static boolean exists(String name) {
        return attractions.containsKey(name.toLowerCase());
    }

    public static Attraction getAttraction(String name) {
        return attractions.get(name.toLowerCase());
    }

    public static void removeAttraction(String name) {
        attractions.remove(name.toLowerCase());
    }

    public static Collection<Attraction> getAll() {
        return Collections.unmodifiableCollection(attractions.values());
    }

}
