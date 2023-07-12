import bagel.Drawing;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Dialogue {

    private final int DIALOGUE_FONT_SIZE = 40;
    private final int MAX_DIALOGUE_LIMIT = Window.getWidth() / (DIALOGUE_FONT_SIZE - 20);

    private boolean playingDialogue = false;

    private String currentDialogue = "";
    private final ArrayList<String> dialogueLines = new ArrayList<>();
    private String dialogueCharacter = null;

    private boolean alternate = true;
    private final Colour dialogueColour = new Colour(77.0 / 255, 57.0 / 255, 37.0 / 255, 0.7);
    private final FontSize dialogueFont = new FontSize(Fonts.TCB, DIALOGUE_FONT_SIZE);
    private ImagePoint characterOnScreen;

    private final double dialogueWidth = Window.getWidth() * 0.1;
    private final double dialogueLength = Window.getHeight() - 300;

    private int dialogueLinesIndex = 0;
    private int dialogueLineIndex = 0;

    public void getDialogue(Integer dialogueInt, String mode) {
        String currentDialogue = "";
        try {
            Scanner dialogueScanner = new Scanner(new File(String.format("script/%s/%d.txt", mode, dialogueInt)));
            ArrayList<String> dialogueWords = new ArrayList<>();
            while (dialogueScanner.hasNextLine()) {
                dialogueWords.addAll(Arrays.asList(dialogueScanner.nextLine().split(" ")));
            }
            int currentLineLength = 0;
            for (String word : dialogueWords) {
                if (word.endsWith(":")) {
                    if (!currentDialogue.equalsIgnoreCase("")) {
                        dialogueLines.add(currentDialogue);
                        currentDialogue = "";
                    }
                    currentLineLength = 0;
                    // break;
                }
                if (currentLineLength + word.length() + 1 < MAX_DIALOGUE_LIMIT) {
                    currentDialogue = currentDialogue + word + ' ';
                    currentLineLength += word.length() + 1;
                } else {
                    currentDialogue = currentDialogue + '\n' + word + ' ';
                    currentLineLength = word.length() + 1;
                }
            }
            dialogueLines.add(currentDialogue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayingDialogue() {
        return playingDialogue;
    }

    public void setPlayingDialogue(boolean bool) {
        playingDialogue = bool;
    }

    public void draw() {
        if (characterOnScreen != null) {
            characterOnScreen.draw();
        }
        Drawing.drawRectangle(dialogueWidth, dialogueLength, Window.getWidth() * 0.8, 250,
                dialogueColour);
        String dialogueString = currentDialogue.replaceAll("Nothing: ", "");

        dialogueFont.draw(dialogueString, dialogueWidth +
                DIALOGUE_FONT_SIZE, dialogueLength + DIALOGUE_FONT_SIZE);
    }

//    public String getFullName(String firstName) {
//        for (String character : new CharacterNames().getAllNames()) {
//            if (character.contains(firstName.toUpperCase())) {
//                return character;
//            }
//        }
//        return firstName;
//    }

    public int getLineNo() { return dialogueLinesIndex;}


    public void update(Input input) {
        if (playingDialogue) {
            if (dialogueLines.size() == 0) {
                getDialogue(StorySettingsSingleton.getInstance().getDialogueInt(), StorySettingsSingleton.getInstance().getMode());
            }

            String dialogueLine = dialogueLines.get(dialogueLinesIndex);
            String newCharacter = dialogueLine.split(" ")[0].substring(0, dialogueLine.split(" ")[0].length() - 1);
            characterOnScreen = new ImagePoint(String.format("res/dialogueRenders/%s.png",
                    newCharacter), new Point(0, 0));
            if (dialogueCharacter == null) {
                dialogueCharacter = newCharacter;
            }
            if (!dialogueCharacter.equalsIgnoreCase(newCharacter)) {
                alternate = !alternate;
            }
            dialogueCharacter = newCharacter;

            if (alternate) {
                characterOnScreen.setPos(dialogueWidth,
                        Window.getHeight() - 300 - characterOnScreen.getHeight());
            } else {
                characterOnScreen.setPos(Window.getWidth() - dialogueWidth - characterOnScreen.getWidth(),
                        Window.getHeight() - 300 - characterOnScreen.getHeight());
            }

            if (dialogueLines.get(dialogueLinesIndex).length() > currentDialogue.length()) {
                currentDialogue += dialogueLines.get(dialogueLinesIndex).charAt(dialogueLineIndex);
                dialogueLineIndex++;

                if (input != null && input.wasPressed(Keys.SPACE)) {
                    currentDialogue = dialogueLines.get(dialogueLinesIndex);
                }
            }
            else {
                if (input != null && input.wasPressed(Keys.SPACE)) {
                    currentDialogue = "";
                    dialogueLineIndex = 0;
                    dialogueLinesIndex++;

                    if (dialogueLinesIndex == dialogueLines.size()) {
                        characterOnScreen = null;
                        dialogueLines.clear();
                        dialogueLinesIndex = 0;
                        playingDialogue = false;
                    }
                }
            }
        }
    }
}
