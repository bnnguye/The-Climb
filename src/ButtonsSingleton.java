import java.util.ArrayList;

public class ButtonsSingleton {
    private static ButtonsSingleton single_instance = null;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<Button> buttonsToRemove = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();
    private ArrayList<Slider> slidersToRemove = new ArrayList<>();

    public synchronized static ButtonsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ButtonsSingleton();
        }
        return single_instance;
    }

    public ArrayList<Button> getButtons() { return this.buttons; }

    public ArrayList<Slider> getSliders() { return this.sliders; }

    public ArrayList<Button> getButtonsToRemove() {
        return buttonsToRemove;
    }

    public ArrayList<Slider> getSlidersToRemove() {
        return slidersToRemove;
    }
}
