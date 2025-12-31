package net.ndgwebdesign.parkCore.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rank {

    private final String name;
    private final String displayName;
    private final String prefix;
    private final String suffix;
    private final Set<String> permissions = new HashSet<>();
    private final List<String> inheritance;

    public Rank(String name,
                String displayName,
                String prefix,
                String suffix,
                List<String> permissions,
                List<String> inheritance) {

        this.name = name;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.inheritance = inheritance;

        if (permissions != null) {
            this.permissions.addAll(permissions);
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public List<String> getInheritance() {
        return inheritance;
    }
}
