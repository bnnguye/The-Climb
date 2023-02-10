import bagel.Image;

public class NPC {
    private String name;
    private String soundPath = String.format("music/%s.wav", name);
    private Image selected = new Image(String.format("res/sidecharacters/%s/Selected.png", name));

    public NPC (String name) {
        this.name = name;
    }
}
