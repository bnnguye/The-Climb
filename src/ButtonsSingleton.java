import java.util.ArrayList;

public class ButtonsSingleton {
    private static ButtonsSingleton single_instance = null;
    private ArrayList<Button> buttons = new ArrayList<>();

    public synchronized static ButtonsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new ButtonsSingleton();

        }
        return single_instance;
    }
    public ArrayList<Button> getButtons() {
        return buttons;
    }
}
