public class SettingsSingleton {

    private final double frames = 144;
    private static SettingsSingleton single_instance = null;
    private static int gameState = 0;
    private static int lastGameState = 0;
    private static int gameMode = 0;
    private static int players = 0;
    private static String gameStateString = "";
    private static Player winner;
    private double time = 0;
    private boolean theme = ((java.time.LocalTime.now().getHour() > 18) && ((java.time.LocalTime.now().getHour() < 4)));

    public synchronized static SettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new SettingsSingleton();

        }
        return single_instance;
    }

    public int getGameState() { return gameState;}
    public void setGameState(int num) { lastGameState = gameState; gameState = num;}
    public void setGameMode(int num) {gameMode = num;}
    public int getGameMode() {return gameMode;}

    public void setPlayers(int players) {
        SettingsSingleton.players = players;
    }
    public int getPlayers() { return players;}
    public String getGameStateString() {return gameStateString;}
    public void setGameStateString(String string) { gameStateString = string;}
    public void setWinner(Player id) { winner = id;}
    public Player getWinner() { return winner;}
    public double getTime() {return time; }
    public double getFrames() {return frames;}
    public int getLastGameState() {return lastGameState;}
    public boolean isNight() { return theme;}
}
