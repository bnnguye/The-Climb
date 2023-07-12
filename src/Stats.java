import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Stats {

    SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    List<String> allCharacterNames = new CharacterNames().getAllCharacterNames();

    // Character Stats [name, played, won]
    ArrayList<Object[]> charactersStats = new ArrayList<>();
    ArrayList<Integer> playersStats = new ArrayList<>();

    // 0: game opened,  1: VS, 2: Story
    int[] statTracker = new int[3];

    public Stats() {
        gameOpened();
        loadPlayerStats();
        loadCharacterStats();
        getGameStats();
    }

    public void saveStats() {
        String tempFile = "stats/Temp.txt";
        String currentFile = "stats/Stats.txt";
        File oldFile = new File(currentFile);
        File newFile = new File(tempFile);
        try {

            FileWriter fw = new FileWriter(tempFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println("Game");
            pw.println(String.format("Amount of times the game has been opened: %d", statTracker[0]));
            pw.println(String.format("Amount of games played in VS Mode: %d", statTracker[1]));
            pw.println(String.format("Amount of games played in Story Mode: %d", statTracker[2]));
            for(Object[] characterStats: charactersStats) {
                pw.println(characterStats[0]);
                pw.println("Played: " + characterStats[1]);
                pw.println("Won: " + characterStats[2]);
            }
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(currentFile);
            newFile.renameTo(dump);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Number of wins in VS
    public void loadPlayerStats() {
        for (int i = 0; i < 4; i++) {
            playersStats.add(0);
        }
    }

    public void loadCharacterStats() {
        for (String name: allCharacterNames) {
            // 0: name, 1: played, 2: won
            Object[] characterStats = new Object[3];
            characterStats[0] = name;
            String line;
            String[] lineSplit;
            try {
                Scanner scanner = new Scanner(new File("stats/Stats.txt"));
                while(scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    if (line.equals(name)) {
                        break;
                    }
                }
                if(scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    lineSplit = line.split(" ");
                    int played = Integer.parseInt(lineSplit[lineSplit.length - 1]);
                    characterStats[1] = played;
                }
                if(scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    lineSplit = line.split(" ");
                    int won = Integer.parseInt(lineSplit[lineSplit.length - 1]);
                    characterStats[2] = won;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            charactersStats.add(characterStats);
        }
    }

    public void gameOpened() { statTracker[0] += 1;}

    public void updateGameStats(ArrayList<Player> players) {
        statTracker[settingsSingleton.getGameMode() + 1] = statTracker[settingsSingleton.getGameMode() + 1] + 1;
        if (settingsSingleton.getWinner() != null) {
            int winnerID = settingsSingleton.getWinner().getId();
            playersStats.set(winnerID, playersStats.get(winnerID) + 1);
            for (Player player: players) {
                if (player.getId() == winnerID) {
                    characterWon(player.getCharacter().getFullName());
                }
                characterPlayed(player.getCharacter().getFullName());
            }
        }
        else {
            for (Player player: players) {
                characterPlayed(player.getCharacter().getFullName());
            }
        }

    }

    public void characterWon(String fullName) {
        for (Object[] stat: charactersStats) {
            if (stat[0].equals(fullName)) {
                Object[] newStats = stat;
                newStats[2] = (int) stat[2] + 1;
                charactersStats.set(charactersStats.indexOf(stat), newStats);
                return;
            }
        }
    }

    public void characterPlayed(String fullName) {
        for (Object[] stat: charactersStats) {
            if (stat[0].equals(fullName)) {
                Object[] newStats = stat;
                newStats[1] = (int) stat[1] + 1;
                charactersStats.set(charactersStats.indexOf(stat), newStats);
                return;
            }
        }
    }

    public void getGameStats() {
        int num;
        try {
            Scanner scanner = new Scanner(new File("stats/Stats.txt"));
            scanner.nextLine();
            for (int i =0; i < 3; i++) {
                String[] stat = scanner.nextLine().split(" ");
                num = Integer.parseInt(stat[stat.length - 1]);
                if (i == 0) {
                    statTracker[i] = num + 1;
                }
                else {
                    statTracker[i] = num;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean hasPassedThreshold(String characterName, int threshold) {
        for (Object[] characterStats: charactersStats) {
            String character = characterStats[0].toString();
            if (character.equalsIgnoreCase(characterName)) {
                return (Integer) characterStats[2] >= threshold;
            }
        }
        return false;
    }

}
