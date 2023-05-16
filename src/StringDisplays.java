import java.util.ArrayList;

public final class StringDisplays {
    ArrayList<StringDisplay> stringDisplays = new ArrayList<>();

    public ArrayList<StringDisplay> getStringDisplaysWithTag(String tag) {
        ArrayList<StringDisplay> temp = new ArrayList<>();
        for (StringDisplay stringDisplay: stringDisplays) {
            if (tag.equals(stringDisplay.getTag())) {
                temp.add(stringDisplay);
            }
        }
        return temp;
    }

    public void add(StringDisplay stringDisplay) {
        stringDisplays.add(stringDisplay);
    }

    public boolean exists(String displayName) {
        for (StringDisplay stringDisplay: stringDisplays) {
            if (stringDisplay.getName().equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        stringDisplays.clear();
    }

    public ArrayList<StringDisplay> getStringDisplays() {
        return stringDisplays;
    }

    public StringDisplay get(String stringName) {
        for (StringDisplay stringDisplay: stringDisplays) {
            if (stringDisplay.getName().equals(stringName)) {
                return stringDisplay;
            }
        }
        return null;
    }

}
