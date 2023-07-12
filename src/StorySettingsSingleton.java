import bagel.util.Point;

import java.io.*;
import java.util.Scanner;

public class StorySettingsSingleton {

    private int initialTime;
    private String mode;
    private static StorySettingsSingleton single_instance = null;

    // Story variables
    private int currentStory = 0;
    private int currentScene = 0;
    private int lastStory = -1;
    private int lastScene = -1;
    private boolean alternate = false;
    private boolean playNextCharacter = true;
    private Character nextCharacter = null;
    private double shakeTimer = 0;
    private double transitionTimer = 0;
    private boolean dark = false;
    private int dialogueInt = 0;
    private int keyboardTimer = 0;
    private boolean endDialogue = false;

    public synchronized static StorySettingsSingleton getInstance() {
        if (single_instance == null) {
            single_instance = new StorySettingsSingleton();
        }
        return single_instance;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() { return mode;}

    public void setDialogueInt(int num) {
        dialogueInt = num;
    }

    public int getDialogueInt() { return dialogueInt;}

    public void initialiseTime() {
        initialTime = TimeLogger.getInstance().getFrames();
    }

    public int getInitialTime() { return initialTime;}

    public void updateStory(int currentProgress) {
        String tempFile = "stats/TempStory.txt";
        String currentFile = "stats/Story.txt";
        File oldFile = new File(currentFile);
        File newFile = new File(tempFile);
        try {
            Scanner storyScanner = new Scanner(new File("stats/Story.txt"));

            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            for (int i = 0; i < 1; i++) {
                if (!storyScanner.hasNextLine()) {
                    System.out.println("Scanner could not find given line no.");
                    return;
                }
                else {
                    String[] line = storyScanner.nextLine().split(" ");
                    String newLine = "";
                    for (int j = 0; j < line.length - 1; j++) {
                        newLine += line[j] + " ";
                    }
                    newLine += String.format("%d", currentProgress);
                    pw.println(newLine);
                }
            }

            storyScanner.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(currentFile);
            newFile.renameTo(dump);

//            stringDisplays.add(new StringDisplay("Story saved successfully.", 3, new FontSize(Fonts.DEJAVUSANS, 50), new Point(0,0)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void loadStory() {
        int lineNumber = 0;
        try {
            Scanner storyScanner = new Scanner(new File("stats/Story.txt"));
            while(storyScanner.hasNextLine()) {
                String[] line = storyScanner.nextLine().split(" ");
                if (lineNumber == 0) {
                    currentScene = Integer.parseInt(line[line.length- 1]);
                    if (currentScene < 0) {
                        currentScene = 0;
                    }
                }
                else if (lineNumber == 1) {
                    currentStory = Integer.parseInt(line[line.length - 1]);
                    if (currentStory < 0) {
                        currentStory = 0;
                    }
                }
                lineNumber++;
            }
//            if (!playingMap) {
//                playingScene = true;
//            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
