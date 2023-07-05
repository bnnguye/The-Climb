public class StorySettingsSingleton {

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

}
