package net.ndgwebdesign.parkCore.objects;

public class Attraction {
    private final String name;
    private final String region;
    private AttractionStatus status;

    public Attraction(String name, String region) {
        this.name = name;
        this.region = region;
        this.status = AttractionStatus.CLOSED;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public AttractionStatus getStatus() {
        return status;
    }

    public void setStatus(AttractionStatus status) {
        this.status = status;
    }
}
