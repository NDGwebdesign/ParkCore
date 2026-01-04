package net.ndgwebdesign.parkCore.objects;

import java.util.ArrayList;
import java.util.List;

public class Rank {

    private final String name;
    private String displayName;
    private String prefix;
    private String suffix;
    private final List<String> permissions;
    private final List<String> inheritance;

    public Rank(String name) {
        this.name = name;
        this.permissions = new ArrayList<>();
        this.inheritance = new ArrayList<>();
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

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getInheritance() {
        return inheritance;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
