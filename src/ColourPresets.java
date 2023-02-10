import bagel.util.Colour;

public enum ColourPresets {
    RED(Colour.RED), BLACK(Colour.BLACK), BLUE(Colour.BLUE), GREEN(Colour.GREEN), WHITE(Colour.WHITE),
    DARK(new Colour(0, 0, 0, 0.85));

    private final Colour colour;

    ColourPresets(final Colour colour) {
        this.colour = colour;
    }

    public Colour toColour() {
        return colour;
    }
}