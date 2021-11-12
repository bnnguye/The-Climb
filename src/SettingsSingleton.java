import java.util.ArrayList;

public class SettingsSingleton {
    private static SettingsSingleton single_instance = null;
    private ArrayList<Button> buttons;
    private static int gameState = 0;
    private static int level;
    private static int difficulty;
    private static int players = 0;
    private static String gameStateString = "";
    private static int mapNo;
    private static Player winner;

    private SettingsSingleton() {
        buttons = new ArrayList<>();
    }

    public synchronized static SettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new SettingsSingleton();

        }
        return single_instance;
    }

    public void setButtons(ArrayList<Button> buttons) {
        this.buttons = buttons;
    }
    public ArrayList<Button> getButtons() {return buttons;}
    public int getGameState() { return gameState;}
    public void setGameState(int num) { gameState = num;}
    public void setLevel(int num) {level = num;}
    public void setMapNo(int num) { mapNo = num;}
    public int getLevel() {return level;}
    public int getMapNo() {return mapNo;}
    public int getDifficulty () {return difficulty;}
    public void setDifficulty(int num) {difficulty = num;}

    public void setPlayers(int players) {
        SettingsSingleton.players = players;
    }
    public int getPlayers() { return players;}
    public String getGameStateString() {return gameStateString;}
    public void setGameStateString(String string) { gameStateString = string;}
    public void setWinner(Player id) { winner = id;}
    public Player getWinner() { return winner;}

}
