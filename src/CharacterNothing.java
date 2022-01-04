import bagel.Image;

public class CharacterNothing extends Character{
    private String name = "Nothing";
    Image selected = new Image(String.format("res/Selected/%s_Selected.png", this.name));

    public Image getSelected() { return this.selected;}

    public String getName() {return this.name;}
}
