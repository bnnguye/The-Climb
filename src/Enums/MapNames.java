package Enums;

public enum MapNames {
    TRAINING_GROUND("Training Ground"), Park("PARK"), SPOOKY_SPIKES("Spooky Spikes"), GREED_ISLAND("Greed Island"),
    ROSWAALS_MANSIONS("Roswaals Mansion"), CLAUSTROPHOBIC_LANE("Claustrophobic Lane"),
    UPSIDE_DOWN_CUPS("UpsideDown Cups"), CUSTOM("Custom");

    private final String name;

    MapNames(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}