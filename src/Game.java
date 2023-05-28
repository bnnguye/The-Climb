import Enums.MapNames;
import bagel.*;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/** "The Climb" - A game created by Bill Nguyen **/

public class Game extends AbstractGame {

    private static final SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private static final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();
    private static final TimeLogger timeLogger = TimeLogger.getInstance();
    private static final ButtonsSingleton buttonsSingleton = ButtonsSingleton.getInstance();
    private static final EventsListenerSingleton eventsListenerSingleton = EventsListenerSingleton.getInstance();
    private static final ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    private static final MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private static final StringDisplays stringDisplays = new StringDisplays();

    private final ArrayList<Button> buttons= buttonsSingleton.getButtons();
    private final ArrayList<Button> buttonsToRemove = buttonsSingleton.getButtonsToRemove();
    private final ArrayList<Button> buttonsToAdd = buttonsSingleton.getButtonsToAdd();
    private final ArrayList<Slider> sliders = buttonsSingleton.getSliders();
    private final ArrayList<Slider> slidersToRemove = buttonsSingleton.getSlidersToRemove();
    private final EventsListenerSingleton.EventsListener eventsListener = eventsListenerSingleton.getEventsListener();
    private final ArrayList<Character> playableCharacters = new ArrayList<>();
    private final ArrayList<Character> allCharacters = new ArrayList<>();
    private final ArrayList<Player> players= settingsSingleton.getPlayers();
    private final ArrayList<Obstacle> obstacles= new ArrayList<>();
    private final ArrayList<Obstacle> obstaclesToRemove= new ArrayList<>();
    private final ArrayList<PowerUp> powerUps= new ArrayList<>();
    private final ArrayList<PowerUp> powerUpsToRemove= new ArrayList<>();
    private final ArrayList<SideCharacter> allSideCharacters= new ArrayList<>();

    private final int frames = settingsSingleton.getRefreshRate();
    private final int DIALOGUE_FONT_SIZE = 30;
    private final int MAX_DIALOGUE_LIMIT = Window.getWidth()/(DIALOGUE_FONT_SIZE - 10);

    private final Stats stats = new Stats();

    private int countDown = 0;
    private int winnerTimer = 0;

    private final Font dialogueFont = new Font(Fonts.DEJAVUSANS, DIALOGUE_FONT_SIZE);
    private final DrawOptions DO = new DrawOptions();

    // Custom Map Vars
    private final ArrayList<Map> playableMaps= new ArrayList<>();
    private final ArrayList<Tile> allTiles= new ArrayList<>();
    private static ArrayList<Tile> customMapTiles= new ArrayList<>();
    private boolean addingTile = false;
    private int page = 0;
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private int offset = 0;

    private boolean toggleInfo = false;

    private String unlocked;
    private boolean canInteract = eventsListenerSingleton.isCanInteract();

//    private String[] dialogueLine;
//    private Scanner dialogueScanner;
//    private ArrayList<String> dialogueWords= new ArrayList<>();

    //Story variables
//    private boolean playingDialogue = false;
//    private boolean playingStory = false;
//    private boolean playingScene = false;
//    private final boolean playingMap = false;
//    private final boolean playingWorld = false;
//    private int currentDialogue = 0;
//    private int currentStory = 0;
//    private int currentScene = 0;
//    private int lastStory = -1;
//    private int lastScene = -1;
//    private String dialogueString = "";
//    private final Colour dialogueColour = new Colour(77.0/255, 57.0/255, 37.0/255, 0.7);
//    private final double dialogueWidth = Window.getWidth()*0.1;
//    private final double dialogueLength = Window.getHeight() - 300;
//    private final int maxLines = 7;
//    private int currentLines = 0;
//    private int dialogueIndex = 0;
//    private boolean alternate = false;
//    private boolean playNextCharacter = true;
//    private Character dialogueCharacter = null;
//    private Character nextCharacter = null;
//    private double shakeTimer = 0;
//    private double transitionTimer = 0;
//    private boolean dark = false;
//    private int dialogueCounter = 0;
//    private int keyboardTimer = 0;
//    private boolean endDialogue = false;
//    private String currentMode;
    private String menuTitle = "";

    Point currentMousePosition;

    // Game Settings variables
    private final ArrayList<PowerUp> allPowerUps= new ArrayList<>();
    private final ArrayList<Obstacle> allObstacles= new ArrayList<>();

    public static void main(String[] args) {
        new Game().run();
    }

    public Game() {

        super(1920, 1080, "The Climb");
        init();
    }

    private void init() {

        imagePointManagerSingleton.setCurrentBackground(null);
        loadAllCharacters();
        loadAllSideCharacters();
        loadStarterCharacters();
        loadPlayableMaps();

        // adds unlocked characters
        checkAchievements();

        loadCustomMapConfigs();
        loadPowerUpSettings();
        loadObstacleSettings();


        stats.getGameStats();
//        loadStory();
//        currentMode = currentStory >= currentScene ? "Scene" : "Story";

        eventsListener.addEvent(new EventStartApp(5 * frames / 2, "Game initiated"));
        musicPlayer.getMainMusic().setVolume(musicPlayer.getMaxMainVol());
    }

    @Override
    protected void update(Input input)  {
        currentMousePosition = input.getMousePosition();

        if (!canInteract) {
            input = null;
        }
        for (Button button: buttons) {
            if (input != null) {
                button.toggleHover(input.getMousePosition());
            }
            if (input != null && input.wasPressed(MouseButtons.LEFT)) {
                if (button.isHovering()) {
                    new Music("music/misc/Click.wav", MusicPlayer.getInstance().getEffectVolume());
                    button.playAction();
                }
            }
        }

//        if (settingsSingleton.getGameState() == -99) {
//            ImagePoint imagePoint = new ImagePoint("res/characters/NINO NAKANO/Render.png", new Point(0,0));
//            if (timeLogger.getFrames() % 144 < 72){
//                imagePoint.setFlashing(true);
//            }
//            else {
//                imagePoint.setFlashing(false);
//            }
//            imagePoint.draw();
//        }

        if (settingsSingleton.getGameState() == 0) {
            if (!"Main Menu".equals(settingsSingleton.getGameStateString())) {
                settingsSingleton.setGameStateString("Main Menu");
                players.clear();
                sliders.clear();
                gameSettingsSingleton.setMap(null);
                obstacles.clear();
                musicPlayer.clear();
                musicPlayer.clearEnded();
                buttons.clear();
                buttons.addAll(Arrays.asList(
                        new Button("PLAY", null,
                                new FontSize(Fonts.DEJAVUSANS, 80),
                                new Rectangle(350, 400, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("TUTORIAL", null,
                                new FontSize(Fonts.DEJAVUSANS, 80),
                                new Rectangle(350, 400 + 80, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("CREATE MAP", null,
                                new FontSize(Fonts.DEJAVUSANS, 80),
                                new Rectangle(350, 400 + 80 * 2, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("SETTINGS", null,
                                new FontSize(Fonts.DEJAVUSANS, 80),
                                new Rectangle(350, 400 + 80 * 3, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("EXIT", null,
                                new FontSize(Fonts.DEJAVUSANS, 80),
                                new Rectangle(350, 400 + 80 * 4, Window.getWidth(),
                                        80),
                                ColourPresets.BLACK.toColour())
                ));
                menuTitle = "";
                imagePointManagerSingleton.getImages().clear();
                musicPlayer.setMainMusic("music/Battle/Giorno.wav");
                musicPlayer.getMainMusic().setVolume(musicPlayer.getMaxMainVol());
                eventsListener.addEvent(new EventGameStateZero());
                ImagePoint frontCover = new ImagePoint(String.format("res/characters/%s/Render.png", allCharacters.get((int) (Math.random() * allCharacters.size())).getFullName()), new Point(1000,24), "frontCover");
                if (!imagePointManagerSingleton.imageWithExistsWithTag("frontCover")) {
                    imagePointManagerSingleton.add(frontCover);
                    imagePointManagerSingleton.get(frontCover.getFilename()).setPos(1000, Window.getHeight() - frontCover.getHeight());
                }
            }
            updateDemo(input);
        }
        else if (settingsSingleton.getGameState() == 1) {
            String storyMenuFileName = "res/menu/StoryMenu.png";
            String vsMenuFileName = "res/menu/vsMenu.png";

            if (!settingsSingleton.getGameStateString().equals("Game Mode")) {
                settingsSingleton.setGameStateString("Game Mode");
                imagePointManagerSingleton.add(new ImagePoint(storyMenuFileName, new Point(Window.getWidth()/2,0)));
                imagePointManagerSingleton.add(new ImagePoint(vsMenuFileName, new Point(-Window.getWidth()/2,0)));
                Button storyButton = new Button("STORY", null,
                        new FontSize(Fonts.DEJAVUSANS, 160),
                        new Rectangle(0,0,new Font(Fonts.DEJAVUSANS, 160).getWidth("STORY"),160),
                        ColourPresets.WHITE.toColour());
                Button versusButton = new Button("VS", null,
                        new FontSize(Fonts.DEJAVUSANS, 160),
                        new Rectangle(Window.getWidth() - new Font(Fonts.DEJAVUSANS, 160).getWidth("VS"),0,new Font(Fonts.DEJAVUSANS, 160).getWidth("VS"),160),
                        ColourPresets.WHITE.toColour());
                buttonsToRemove.addAll(buttons);
                buttons.add(storyButton);
                buttons.add(versusButton);
                menuTitle = "";

            }
            imagePointManagerSingleton.get(storyMenuFileName).setPos(currentMousePosition.x, 0);
            imagePointManagerSingleton.get(vsMenuFileName).setPos(-Window.getWidth() + currentMousePosition.x, 0);

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(0);
                players.clear();
            }
        }
        else if (settingsSingleton.getGameState() == 2) {
            if (!"Players".equals(settingsSingleton.getGameStateString()))  {
                buttonsToRemove.addAll(buttons);
                Button twoPlayerButton = new Button("2", null,
                        new FontSize(Fonts.DEJAVUSANS, 160),
                        new Rectangle(0, Window.getHeight() / 2  - 160, Window.getWidth(), 160),
                        ColourPresets.BLACK.toColour());
                Button threePlayerButton = new Button("3", null,
                        new FontSize(Fonts.DEJAVUSANS, 160),
                        new Rectangle(0, Window.getHeight() /2, Window.getWidth(), 160),
                        ColourPresets.BLACK.toColour());
                Button fourPlayerButton = new Button("4", null,
                        new FontSize(Fonts.DEJAVUSANS, 160),
                        new Rectangle(0, Window.getHeight() / 2 + 160, Window.getWidth(), 160),
                        ColourPresets.BLACK.toColour());
                buttons.addAll(Arrays.asList(twoPlayerButton, threePlayerButton, fourPlayerButton));
                settingsSingleton.setGameStateString("Players");
                musicPlayer.clear();
                imagePointManagerSingleton.getImages().clear();
                imagePointManagerSingleton.setCurrentBackground("res/menu/playerMenu.PNG");
                menuTitle = "PLAYERS";
        }
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(1);
            }
        }
        else if (settingsSingleton.getGameState() == 3) {
            if (!settingsSingleton.getGameStateString().equals("Character")) {
                settingsSingleton.setGameStateString("Character");
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                obstacles.clear();
                powerUps.clear();
                imagePointManagerSingleton.setCurrentBackground(null);
                imagePointManagerSingleton.getImages().clear();
                menuTitle = "";
                if (settingsSingleton.getGameMode() == 1) {
                    buttons.add(new Button("Game Settings", new Image("res/misc/settings.png"), new Point(10,10)));
                }
                sortCharacters();
                adjustCharacterRotation();
                loadCharacterRender();
            }
            Player currentPlayer = players.get(0);
            for (Player player: players) {
                if (player.getCharacter() == null) {
                    currentPlayer = player;
                    break;
                }
            }

            Character currentCharacter = allCharacters.get(allCharacters.size() % 2 == 1 ?
                    allCharacters.size() / 2 + 1 : allCharacters.size() / 2);
            imagePointManagerSingleton.setCurrentBackground((String.format("res/characters/%s/bg.png", currentCharacter.getFullName())));
            imagePointManagerSingleton.getCurrentBackground().setTransparent(false);
            if (!isUnlocked(currentCharacter.getName())) {
                imagePointManagerSingleton.getCurrentBackground().setTransparent(true);
            }

            if (input != null && input.isDown(Keys.RIGHT)) {
                eventsListener.addEvent(new EventCharacterRotate(frames/8, "Rotate LEFT"));
                rotateCharacter("LEFT");
            } else if (input != null && input.isDown(Keys.LEFT)) {
                eventsListener.addEvent(new EventCharacterRotate(frames/8, "Rotate RIGHT"));
                rotateCharacter("RIGHT");
            }

            if (input != null && input.wasPressed(Keys.SPACE)) {
                pickCharacter(currentPlayer, currentCharacter);
            }

            boolean picked = true;
            for (Player player: players) {
                if (player.getCharacter() == null) {
                    picked = false;
                }
            }
            if (picked) {
                eventsListener.addEvent(new EventCharactersPicked(1 * frames, "All players have picked a character"));
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                imagePointManagerSingleton.getCurrentBackground().setTransparent(false);
                settingsSingleton.setGameState(2);
            }

        }
        else if (settingsSingleton.getGameState() == 4) {
            if (!settingsSingleton.getGameStateString().equals("SideCharacter")) {
                settingsSingleton.setGameStateString("SideCharacter");
                imagePointManagerSingleton.getImages().clear();
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                toggleInfo = false;
                int index = 0;
                double minScale = 0.275;
                double maxScale = 1;
                int middleIndex = allSideCharacters.size() % 2 == 1 ?
                        allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2;
                double spacing = Window.getHeight()/4;

                for (SideCharacter character: allSideCharacters) {
                    ImagePoint characterRender = new ImagePoint(String.format("res/SideCharacters/%s/render.png", character.getName()),
                            new Point(0, 0), "SideCharacterRender");
                    characterRender.setScale(minScale);
                    if (index < middleIndex) {
                        characterRender.setPos( ((allSideCharacters.size() * spacing/2) - Window.getWidth())/2 - 490 + (spacing * (index - 1)),
                                Window.getHeight()*3/4);
                    }
                    else if (middleIndex == index) {
                        characterRender.setScale(maxScale);
                        characterRender.setPos(490, Window.getHeight()/8);
                    }
                    else {
                        characterRender.setPos( Window.getWidth()/2 + (spacing * (index - middleIndex)),
                                Window.getHeight()*3/4);
                    }
                    imagePointManagerSingleton.add(characterRender);
                    index++;
                }
            }

            SideCharacter currentCharacter = allSideCharacters.get(allSideCharacters.size() % 2 == 1 ?
                    allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2);

            if (!toggleInfo) {
                Player currentPlayer = players.get(0);
                for (Player player: players) {
                    if (player.getSideCharacter() == null) {
                        currentPlayer = player;
                        break;
                    }
                }
                imagePointManagerSingleton.setCurrentBackground((String.format("res/sidecharacters/%s/bg.png", currentCharacter.getName())));

                if (input != null && input.isDown(Keys.RIGHT)) {
                    eventsListener.addEvent(new EventSideCharacterRotate(frames/8, "Rotate LEFT"));
                    rotateCharacter("LEFT");
                } else if (input != null && input.isDown(Keys.LEFT)) {
                    eventsListener.addEvent(new EventSideCharacterRotate(frames/8, "Rotate RIGHT"));
                    rotateCharacter("RIGHT");
                }

                if (input != null && input.wasPressed(Keys.SPACE)) {
                    pickSideCharacter(currentPlayer, currentCharacter);
                }

                boolean picked = true;
                for (Player player: players) {
                    if (player.getSideCharacter() == null) {
                        picked = false;
                    }
                }
                if (picked) {
                    eventsListener.addEvent(new EventSideCharactersPicked(3 * frames, "All players have picked a side character"));
                }

                if (input != null && input.wasPressed(Keys.ESCAPE)) {
                    settingsSingleton.setGameState(3);
                    for (Player player: players) {
                        player.setSideCharacter(null);
                        player.setCharacter(null);
                    }
                }
            }
            else {
                if (!stringDisplays.exists(currentCharacter.getName())) {
                    stringDisplays.add(new StringDisplay(currentCharacter.getName(), true, new FontSize(Fonts.TCB, 160), new Point(0,Window.getHeight()/3 - 80)));
                    stringDisplays.get(currentCharacter.getName()).setColour(1,1,1,1);
                }
                if (!stringDisplays.exists(currentCharacter.getPower())) {
                    stringDisplays.add(new StringDisplay(currentCharacter.getPower(), true, new FontSize(Fonts.CONFORMABLE, 110), new Point(0,Window.getHeight()/3)));
                    stringDisplays.get(currentCharacter.getPower()).setColour(1,1,1,1);
                }
                if (!stringDisplays.exists(currentCharacter.getDesc())) {
                    stringDisplays.add(new StringDisplay(currentCharacter.getDesc(), true, new FontSize(Fonts.AGENCYB, 40), new Point(0, Window.getHeight()/3 + 80)));
                    stringDisplays.get(currentCharacter.getDesc()).setColour(1,1,1,1);
                }
            }
            if (input != null && input.wasPressed(Keys.I)) {
                toggleInfo = !toggleInfo;
                stringDisplays.clear();
            }
        }
        else if (settingsSingleton.getGameState() == 5)     { // Map
            if (!settingsSingleton.getGameStateString().equals("MAP")) {
                buttonsToRemove.addAll(buttons);
                menuTitle = "Which Climb?";
                imagePointManagerSingleton.setCurrentBackground(null);
                settingsSingleton.setGameStateString("MAP");
            }
            gameSettingsSingleton.setMap(playableMaps.get(1));
            gameSettingsSingleton.getMap().generateMap();
            musicPlayer.clear();
            gameSettingsSingleton.setMap(gameSettingsSingleton.getMap());
            settingsSingleton.setGameState(6);
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                if (!settingsSingleton.getGameStateString().equals("Game")) {
                    buttonsToRemove.addAll(buttons);
                    loadPlayers();
                    //musicPlayer.setMainMusic("music/Giorno.wav");
                    musicPlayer.setMainMusic(String.format("music/battle/Fight%d.wav", Math.round(Math.random()*3)));
                    musicPlayer.clear();
                    settingsSingleton.setGameStateString("Game");
                    eventsListener.addEvent(new EventGameStart());
                    canInteract = false;
                }
                if (canInteract) {
                    boolean playingAnimation = false;
                    boolean theWorld = false;
                    for (Player player: players) {
                        if (player.getCharacter().hasSpecialAbility()) {
                            if (input != null && input.wasPressed(player.getControl("Primary"))) {
                                player.getCharacter().useSpecialAbility();
                                player.getSideCharacter().activateAbility(player, obstacles, powerUps);
                            }
                        }
                        if (player.getSideCharacter().isActivating()) {
                            musicPlayer.setMainVolume(0);
                            if ((player.getSideCharacter().getName().equals(CharacterNames.DIO)) || player.getSideCharacter().getName().equals(CharacterNames.JOTARO)) {
                                theWorld = true;
                            }
                        }
                        if (player.getSideCharacter().isAnimating()) {
                            musicPlayer.setMainVolume(0);
                            playingAnimation = true;
                        }
                    }
                    if(!gameSettingsSingleton.getMap().hasFinished()) {
                        if(!playingAnimation) {
                            musicPlayer.setMainVolume(musicPlayer.getMaxMainVol());
                            spawnObstacles();
                            spawnPowerUps();
                        }
                    }
                    else {
                        for (Player player : players) {
                            if (player.getCharacter().getPos().distanceTo(
                                    new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                settingsSingleton.setGameState(7);
                                settingsSingleton.setWinner(player);
                                settingsSingleton.setGameStateString("Game Finished");
                                break;
                            }
                        }
                    }
                    if (!playingAnimation) {
                        musicPlayer.setMainVolume(musicPlayer.getMaxMainVol());
                        updateExp();
                        if (!theWorld) {
                            updatePlayerMovement(input);
                            updateObjects();
                            if (!gameSettingsSingleton.getMap().hasFinished()) {
                                gameSettingsSingleton.getMap().updateTiles(gameSettingsSingleton.getMapSpeed());
                            }
                        }
                        else {
                            for (Player player: players) {
                                if (player.getSideCharacter().isActivating() &&
                                        Arrays.asList(CharacterNames.JOTARO, CharacterNames.DIO).contains(player.getSideCharacter().getName())) {
                                    player.moveCharacter(input);
                                }
                            }
                        }
                    }
                    updateAbilities();
                    if (canInteract) {
                        checkCollisionPowerUps();
                        checkCollisionObstacles();
                        checkCollisionTiles();
                    }
                    if (!settingsSingleton.getGameStateString().equals("Win")) {
                        int deathCounter = 0;
                        for (Player player : players) {
                            if (player.getCharacter().isDead()) {
                                deathCounter++;
                            }
                        }
                        if (settingsSingleton.getPlayers().size() - deathCounter < 2) {
                            settingsSingleton.setGameStateString("Game Finished");

                            settingsSingleton.setGameState(7);
                            for (Player player : players) {
                                if (!player.getCharacter().isDead()) {
                                    settingsSingleton.setWinner(player);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
//            else {
//                if (!settingsSingleton.getGameStateString().equals("Story")) {
//                    buttonsToRemove.addAll(buttons);
//                    settingsSingleton.setGameStateString("Story");
//                    menuTitle = "";
//                    setPlayersPosition();
//                    gameSettingsSingleton.getMap() = mapToTransitionTo;
//                    if (gameSettingsSingleton.getMap() != null) {
//                        gameSettingsSingleton.getMap().generateMap();
//                    }
//                }
//                if (!dark && shakeTimer <= 0 && transitionTimer <= 0) {
//                    if (playingStory) {
//                        currentMode = "Story";
//                        currentDialogue = currentStory;
//
//                        if (currentStory == 0) {
//                            musicPlayer.setMainMusic("music/Fight0.wav");
//                            if (lastStory != currentStory) {
//                                lastStory = currentStory;
//                                playingDialogue = true;
//                                endDialogue = false;
//                            }
//
//                            if (endDialogue) {
//                                updatePlayerMovement(input);
//                                if(!gameSettingsSingleton.getMap().hasFinished()) {
//                                    gameSettingsSingleton.getMap().updateTiles(1);
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        //mapToTransitionTo = new MapTrainingGround2();
//                                        gameSettingsSingleton.getMap() = mapToTransitionTo;
//                                        gameSettingsSingleton.getMap().generateMap();
//                                        currentStory = 1;
//                                    }
//                                }
//                            }
//                        }
//
//                        else if (currentStory == 1) {
//                            musicPlayer.setMainMusic("music/Fight1.wav");
//                            if (lastStory != currentStory) {
//                                setPlayersPosition();
//                                lastStory = currentStory;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                musicPlayer.setMainMusic("music/Fight2.wav");
//                            }
//
//                            if (endDialogue) {
//                                updatePlayerMovement(input);
//                                render();
//                                checkCollisionTiles();
//                                for (Player player: players) {
//                                    if (player.isDead()) {
//                                        settingsSingleton.setGameState(7);
//                                        failed = true;
//                                    }
//                                }
//                                if(!gameSettingsSingleton.getMap().hasFinished()) {
//                                    gameSettingsSingleton.getMap().updateTiles(0.8);
//                                    displayCharacterStats(players);
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        gameSettingsSingleton.getMap().generateMap();
//                                        currentStory++;
//                                    }
//                                }
//                            }
//                        }
//                        else if (currentStory == 2) {
//                            musicPlayer.setMainMusic("music/Fight2.wav");
//                            if (lastStory != currentStory) {
//                                setPlayersPosition();
//                                lastStory = currentStory;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                musicPlayer.setMainMusic("music/Fight1.wav");
//                                gameSettingsSingleton.getObstaclesSettingsSingleton().applySettings(true, true, false);
//                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("Rock", 0.99);
//                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("Ball", 0.995);
//                            }
//
//                            if (endDialogue) {
//                                updatePlayerMovement(input);
//                                render();
//                                checkCollisionPowerUps();
//                                checkCollisionObstacles();
//                                checkCollisionTiles();
//                                updateObjects();
//                                for (Player player: players) {
//                                    if (player.isDead()) {
//                                        settingsSingleton.setGameState(7);
//                                        failed = true;
//                                    }
//                                }
//                                if(!gameSettingsSingleton.getMap().hasFinished()) {
//                                    gameSettingsSingleton.getMap().updateTiles(0.8);
//                                    displayCharacterStats(players);
//                                    spawnObstacles();
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        playingStory = false;
//                                        playingScene = true;
//                                        currentScene++;
//                                        updateStory(currentScene);
//                                        sceneToTransitionTo = new ImagePoint("res/background/Futaba.png", new Point(0,0));
//                                        startTransition();
//                                    }
//                                }
//                            }
//                        }
//                        else if (currentStory == 3) {
//                            dark = true;
//                            if (lastStory != currentStory) {
//                                setPlayersPosition();
//                                lastStory = currentStory;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                gameSettingsSingleton.getObstaclesSettingsSingleton().applySettings(true, true, true);
//                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("StunBall", 0.995);
//                            }
//
//                            if (endDialogue) {
//                                updatePlayerMovement(input);
//                                render();
//                                checkCollisionPowerUps();
//                                checkCollisionObstacles();
//                                checkCollisionTiles();
//                                updateObjects();
//                                for (Player player: players) {
//                                    if (player.isDead()) {
//                                        settingsSingleton.setGameState(7);
//                                        failed = true;
//                                    }
//                                }
//                                if(!gameSettingsSingleton.getMap().hasFinished()) {
//                                    gameSettingsSingleton.getMap().updateTiles(0.8);
//                                    displayCharacterStats(players);
//                                    spawnObstacles();
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        playingStory = false;
//                                        playingScene = true;
//                                        currentScene++;
//                                        updateStory(currentScene);
//                                        sceneToTransitionTo = new ImagePoint("res/background/Futaba.png", new Point(0,0));
//                                        startTransition();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    else if (playingScene){
//                        currentMode = "Scene";
//                        currentDialogue = currentScene;
//                        gameSettingsSingleton.getMap() = null;
//
//                        if(currentScene == 0) {
//                            if (lastScene != currentScene) {
//                                lastScene = currentScene;
//                                musicPlayer.setMainMusic("music/Rain 1.wav");
//                                changeBackground("res/background/Mountain.png");
//                                dark = true;
//                                playingDialogue = true;
//                                endDialogue = false;
//                            }
//                            if (dialogueCounter == 7) {
//                                shakeTimer = 2 * frames;
//                                dialogueCounter++;
//                            }
//                            if (endDialogue) {
//                                startTransition();
//                                sceneToTransitionTo = new ImagePoint("res/background/Nino.png", new Point(0,0));
//                                currentScene++;
//                            }
//                        }
//
//                        else if (currentScene == 1) {
//                            if (lastScene != currentScene) {
//                                lastScene = currentScene;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                musicPlayer.setMainMusic("music/Idle.wav");
//                            }
//                            if (dialogueCounter == 0) {
//                                dark = true;
//                            }
//                            else if (dialogueCounter > 2) {
//                                dark = false;
//                            }
//                            if (endDialogue) {
//                                startTransition();
//                                currentScene = 2;
//                                sceneToTransitionTo = new ImagePoint("res/background/Nino.png", new Point(0,0));
//                            }
//                        }
//                        else if (currentScene == 2) {
//                            if (lastScene != currentScene) {
//                                if (currentBackground == null) {
//                                    changeBackground("res/background/Nino.png");
//                                }
//                                lastScene = currentScene;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                musicPlayer.setMainMusic("music/Idle.wav");
//                            }
//                            if (endDialogue) {
//                                playingStory = true;
//                                playingScene = false;
//                                currentStory = 0;
//                                startTransition();
//                                mapToTransitionTo = new Map("TrainingGround");
//                                setPlayersPosition();
//                            }
//                        }
//                        else if (currentScene == 3) {
//                            if (lastScene != currentScene) {
//                                if (currentBackground == null) {
//                                    changeBackground("res/background/Nino.png");
//                                }
//                                currentStory = 3;
//                                lastScene = currentScene;
//                                playingDialogue = true;
//                                endDialogue = false;
//                                musicPlayer.setMainMusic("music/Idle.wav");
//                            }
//                            if (endDialogue) {
//                                startTransition();
//                                sceneToTransitionTo = new ImagePoint("res/background/Dio.png", new Point(0,0));
//                                currentScene++;
//                            }
//                        }
//                        else if (currentScene == 4) {
//                            if (lastScene != currentScene) {
//                                lastScene = currentScene;
//                                playingDialogue = true;
//                                endDialogue = false;
//                            }
//                            if (endDialogue) {
//                                startTransition();
//                                //mapToTransitionTo = new MapDioMansion();
//                                playingScene = false;
//                                playingStory = true;
//                                setPlayersPosition();
//                            }
//                        }
//                    }
//                    else if (playingMap) {
//                    }
//
//                    // code for displaying dialogue
//                    if (playingDialogue) {
//                        if (dialogueCharacter == null) {
//                                String name = playDialogue(currentDialogue, currentMode).split(" ")[0];
//                                dialogueCharacter = new Character(name);
//                        }
//                        if (endDialogue) {
//                            playingDialogue = false;
//                            dialogueCounter = 0;
//                            dialogueCharacter = null;
//                        }
//                        else {
//                            if (dialogueCharacter != null) {
//                                ImagePoint characterOnScreen = new ImagePoint(String.format("res/renders/Characters/%s.png",
//                                        dialogueCharacter.getName()), new Point(0,0));
//                                if (alternate) {
//                                    characterOnScreen.setPos(dialogueWidth + characterOnScreen.getWidth()/2,
//                                            dialogueLength - characterOnScreen.getHeight()/2);
//                                }
//                                else {
//                                    characterOnScreen.setPos(Window.getWidth() - dialogueWidth -
//                                            characterOnScreen.getWidth()/2, dialogueLength -
//                                            characterOnScreen.getHeight()/2);
//                                }
//                                characterOnScreen.draw();
//                            }
//                            Drawing.drawRectangle(dialogueWidth, dialogueLength, Window.getWidth()*0.8, 250,
//                                    dialogueColour);
//                            dialogueFont.drawString(dialogueString.replaceAll("Nothing: ", ""), dialogueWidth +
//                                    DIALOGUE_FONT_SIZE, dialogueLength + DIALOGUE_FONT_SIZE);
//                            if (shakeTimer <= 0) {
//                                if (playDialogue(currentDialogue, currentMode).length() - dialogueIndex > 1) {
//                                    if (!playNextCharacter) {
//                                        if(input != null && input.wasPressed(Keys.SPACE)) {
//                                            currentLines = 0;
//                                            if (!dialogueCharacter.getName().equals(nextCharacter.getName())) {
//                                                alternate = !alternate;
//                                            }
//                                            dialogueCharacter = nextCharacter;
//                                            dialogueString = "";
//                                            playNextCharacter = true;
//                                            if (playDialogue(currentDialogue, currentMode).length() -
//                                                    dialogueIndex <= 1) {
//                                                endDialogue = true;
//                                            }
//                                            dialogueCounter++;
//                                        }
//                                    }
//                                    else {
//                                        if ((playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == '\n') ||
//                                                (playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == ' ')) {
//                                            String nextWord = "";
//                                            for(int i = dialogueIndex + 1;
//                                                i < playDialogue(currentDialogue, currentMode).length();
//                                                i ++) {
//                                                if (playDialogue(currentDialogue, currentMode).charAt(i) == ' ') {
//                                                    break;
//                                                }
//                                                else {
//                                                    nextWord += playDialogue(currentDialogue, currentMode).charAt(i);
//                                                }
//                                            }
//                                            for (Character character: allCharacters) {
//                                                if (nextWord.equals(String.format("%s:", character.getName()))) {
//                                                    if (nextWord.substring(0, nextWord.length() - 1).
//                                                            equals(character.getName())) {
//                                                        if (!nextWord.equals(dialogueCharacter.getName())) {
//                                                            playNextCharacter = false;
//                                                            nextCharacter = character;
//                                                            break;
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        if (playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == '\n') {
//                                            currentLines++;
//                                            if(currentLines >= maxLines) {
//                                                if(input != null && input.wasPressed(Keys.SPACE)) {
//                                                    dialogueString = "";
//                                                    currentLines = 0;
//                                                }
//                                            }
//                                        }
//                                        if (currentLines < maxLines) {
//                                            dialogueString += playDialogue(currentDialogue, currentMode).charAt(dialogueIndex);
//                                            if (keyboardTimer > 9) {
//                                                musicPlayer.addMusic("music/Type.wav");
//                                                keyboardTimer = 0;
//                                            }
//                                            else {
//                                                keyboardTimer++;
//                                            }
//                                            if (dialogueIndex < playDialogue(currentDialogue, currentMode).length()) {
//                                                dialogueIndex++;
//                                            }
//                                        }
//                                    }
//                                }
//                                else {
//                                    dialogueString += playDialogue(currentDialogue, currentMode).charAt(dialogueIndex);
//                                    if (input != null && input.wasPressed(Keys.SPACE)) {
//                                        endDialogue = true;
//                                        currentLines = 0;
//                                        dialogueString = "";
//                                        dialogueIndex = 0;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(11);
            }
        }
        else if (settingsSingleton.getGameState() == 7) {
            musicPlayer.getMainMusic().setVolume(musicPlayer.getMaxMainVol());
            for (Player player : players) {
                player.getCharacter().reset();
                if (player.getSideCharacter() != null) {
                    player.getSideCharacter().reset();
                }
            }
            powerUps.removeAll(powerUps);
            obstacles.removeAll(obstacles);
            if (settingsSingleton.getGameMode() == 0) {
                if (settingsSingleton.getGameStateString().equals("Continue")) {
                    musicPlayer.restart(null);
                    buttonsToRemove.addAll(buttons);
                    settingsSingleton.setGameState(6);
                    gameSettingsSingleton.getMap().generateMap();
                }
                else if (settingsSingleton.getGameStateString().equals("Menu")) {
                    settingsSingleton.setGameState(0);
                }
            }
            else {
                if (settingsSingleton.getGameStateString().equalsIgnoreCase("Game Finished")) {
                    settingsSingleton.setGameStateString("Retry or Menu?");
                    musicPlayer.setMainMusic("music/misc/Fail.wav");
                    musicPlayer.addMusic(settingsSingleton.getWinner().getCharacter().playLine());
                    stats.updateGameStats(players);
                    buttonsToRemove.addAll(buttons);
                    buttons.add(new Button("Back", "Back",
                            new FontSize(Fonts.DEJAVUSANS, 160),
                            new Rectangle(0, Window.getHeight() / 1.5 + 160, Window.getWidth(), 160),
                            ColourPresets.WHITE.toColour()));
                    buttons.add(new Button("Retry", "Restart",
                            new FontSize(Fonts.DEJAVUSANS, 160),
                            new Rectangle(0, Window.getHeight() / 2 + 160, Window.getWidth(), 160),
                            ColourPresets.WHITE.toColour()));
                }
                else if (Arrays.asList("Retry", "Back").contains(settingsSingleton.getGameStateString())) {
                    if (settingsSingleton.getGameStateString().equals("Retry")) {
                        buttonsToRemove.addAll(buttons);
                        gameSettingsSingleton.getMap().generateMap();
                        settingsSingleton.setGameState(6);
                        for (Player player: players) {
                            player.getCharacter().reset();
                            player.getSideCharacter().reset();
                        }
                    } else if (settingsSingleton.getGameStateString().equals("Back")) {
                        for (Player player : players) {
                            player.getCharacter().reset();
                            if (player.getSideCharacter() != null) {
                                player.getSideCharacter().reset();
                            }
                            player.reset();
                        }
                        gameSettingsSingleton.setMap(null);
                        unlocked = checkAchievementsInGame();
                        if (unlocked != null) {
                            settingsSingleton.setGameState(8);
                        } else {
                            settingsSingleton.setGameState(3);
                        }
                    }
                    musicPlayer.clear();
                    buttonsToRemove.clear();
                    buttons.clear();
                    obstacles.clear();
                    powerUps.clear();
                    stats.saveStats();
                }
            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            String musicWho = "music/misc/Who.wav";
            musicPlayer.getMainMusic().setVolume(musicPlayer.getMaxMainVol());
            if (!settingsSingleton.getGameStateString().equals("Unlocked")) {
                musicPlayer.getMainMusic().setVolume(musicPlayer.getMaxMainVol());
                musicPlayer.clear();
                musicPlayer.setMainMusic("music/misc/Silence.wav");
                musicPlayer.addMusic(musicWho);
                settingsSingleton.setGameStateString("Unlocked");
            }
            if (musicPlayer.getSound(musicWho) != null && musicPlayer.getSound(musicWho).hasEnded()) {
                musicPlayer.setMainMusic(String.format("music/characters/%s/UnlockTheme.wav", unlocked));
                musicPlayer.addMusic(String.format("music/characters/%s/UnlockVoice.wav", unlocked));
            }
            if (musicPlayer.hasEnded(musicWho)) {
                if (input != null && input.wasPressed(MouseButtons.LEFT)) {
                    musicPlayer.clear();
                    musicPlayer.setMainMusic("music/misc/Game Main Menu.wav");
                    settingsSingleton.setGameState(0);
                }
            }
        }
        else if (settingsSingleton.getGameState() == 9) {
            if (!settingsSingleton.getGameStateString().equals("Create Your Own Map")) {
                offset = 0;
                gameSettingsSingleton.setMap(new Map("Custom"));
                gameSettingsSingleton.getMap().generateMap();
                customMapTiles.clear();
                customMapTiles.addAll(gameSettingsSingleton.getMap().getTiles());
                buttonsToRemove.addAll(buttons);
                settingsSingleton.setGameStateString("Create Your Own Map");
                imagePointManagerSingleton.setCurrentBackground(null);
                menuTitle = null;
            }

            if (!addingTile) {
                if (input != null && input.isDown(Keys.UP)) {
                    updateTiles(15);
                    offset++;
                }
                if (input != null && input.isDown((Keys.DOWN))) {
                    if (offset > 0) {
                        updateTiles(-15);
                        offset--;
                    }
                }
                if (input != null && input.wasPressed(Keys.S)) {
                    if (customMapTiles.size()%4 != 0) {
                        addDisplayString("Error: Cannot save gameSettingsSingleton.getMap(). Map not complete! (Row is not filled)", 3 * frames);
                    }
                    else {
                        addDisplayString("Success: Map saved.", 5);
                        saveCustomMap();
                        settingsSingleton.setGameState(0);
                    }
                }
                if (input != null && input.wasPressed(Keys.A)) {
                    addingTile = true;
                }
                if (input != null && input.wasPressed(Keys.R)) {
                    if (customMapTiles.size() > 0) {
                        customMapTiles.remove(customMapTiles.size() - 1);
                    }
                }
                if (input != null && input.wasPressed(Keys.D)) {
                    while (offset > 0) {
                        updateTiles(-15);
                        offset--;
                    }
                }
                if (input != null && input.wasPressed(Keys.ESCAPE)) {
                    settingsSingleton.setGameState(0);
                }
            }
            else {
                if (input != null && input.wasPressed(Keys.NUM_1)) {
                    addTileToCustomMap(tile1.getName());
                }
                if (input != null && input.wasPressed(Keys.NUM_2)) {
                    addTileToCustomMap(tile2.getName());
                }
                if (input != null && input.wasPressed(Keys.NUM_3)) {
                    addTileToCustomMap(tile3.getName());
                }
                if (input != null && input.wasPressed(Keys.LEFT)) {
                    if (page > 0) {
                        page--;
                    }
                }
                if (input != null && input.wasPressed((Keys.RIGHT))) {
                    if (page < 2) {
                        page++;
                    }
                }
                if (input != null && input.wasPressed(Keys.ESCAPE)) {
                    addingTile = false;
                }
            }
        }
        else if (settingsSingleton.getGameState() == 10) {
            if (!settingsSingleton.getGameStateString().equals("Game Settings")) {
                settingsSingleton.setGameStateString("Game Settings");
            }
            updateSettings();
            if (gameSettingsSingleton.getPage() == 0) {
                menuTitle = "General";
            }
            if (gameSettingsSingleton.getPage() == 1) {
                menuTitle = "PowerUps";
            }
            else if (gameSettingsSingleton.getPage() == 2) {
                menuTitle = "Obstacles";
            }
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(3);
            }
        }
        else if (settingsSingleton.getGameState() == 11) {
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(6);
            }
        }
        else if (settingsSingleton.getGameState() == 12) {
            if (!settingsSingleton.getGameStateString().equals("Tutorial")) {
                settingsSingleton.setGameStateString("Tutorial");
                gameSettingsSingleton.setMap(new Map("Training Ground"));
                gameSettingsSingleton.getMap().generateMap();

            }
        }
        else if (settingsSingleton.getGameState() == 13) {
            int noOfLevels = 4;

        }
        else if (settingsSingleton.getGameState() == 14) {
            if (!settingsSingleton.getGameStateString().equals("SETTINGS")) {
                settingsSingleton.setGameStateString("SETTINGS");
                menuTitle = "SETTINGS";
                buttons.clear();
                sliders.clear();
                sliders.add(new SliderVolume("Main Volume", new Point(Window.getWidth() / 7, Window.getHeight() / 4 + 100)));
                sliders.add(new SliderVolume("Effect Volume", new Point(Window.getWidth() / 7, Window.getHeight() / 3 + 150)));
            }
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(0);
            }


        }

        try {
            musicPlayer.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateDisplayStrings();
        updateButtons();
        updateSliders(input);
        render();
        eventsListener.updateEvents();
        updateTime();
        canInteract = eventsListener.canInteract();

        // TEST GAME
        if (settingsSingleton.getGameState() == -100) {
            settingsSingleton.setPlayers(2);
            players.get(0).setCharacter(new Character(CharacterNames.MIKU));
            players.get(0).setSideCharacter(new SideYugi());
            players.get(0).getCharacter().gainSpecialAbilityBar(500);
            players.get(1).setCharacter(new Character(CharacterNames.MAI));
            players.get(1).setSideCharacter(new SideDio());
            players.get(1).getCharacter().gainSpecialAbilityBar(500);
            gameSettingsSingleton.setMap(new Map("Park"));
            gameSettingsSingleton.getMap().generateMap();
            gameSettingsSingleton.setMap(gameSettingsSingleton.getMap());
            settingsSingleton.setGameMode(1);
            settingsSingleton.setGameState(6);
            settingsSingleton.setGameStateString("Test");
        }
    }

    public void render() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.BLACK.toColour());
        if (settingsSingleton.getGameState() == 0) {
            imagePointManagerSingleton.getCurrentBackground().draw();
            drawGame();
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.4));

            ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
            ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

            if (leftCover != null) {
                leftCover.draw();
            }
            if (rightCover != null) {
                rightCover.draw();
            }
//            new ImagePoint(("res/menu/main/boyandgirl.png"), new Point(1108, 24)).draw();
            imagePointManagerSingleton.draw();
            new ImagePoint("res/menu/main/name.png", new Point(148, 87)).draw();
            for (Button button: buttons) {
                if (button.isHovering()) {
                    Drawing.drawRectangle(new Point(0, button.getPosition().y + 30), Window.getWidth(), button.getWidth() - 30, new Colour(0,0,0,0.05));
                    break;
                }
            }
        }
        if (settingsSingleton.getGameState() == 1) {
            imagePointManagerSingleton.draw();
        }
        else if (settingsSingleton.getGameState() == 2) {
            imagePointManagerSingleton.getCurrentBackground().draw();
            for (Button button: buttons) {
                if ("234".contains(button.getName())) {
                    button.setNight();
                    if (button.isHovering()) {
                        ImagePoint playerArt = new ImagePoint(String.format("res/menu/%sPlayers.PNG",
                                button.getName()), new Point(0, 0));
                        playerArt.setPos(Window.getWidth()/2 - playerArt.getWidth()/2, Window.getHeight() - playerArt.getHeight());
                        playerArt.draw();
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 3) {
            Character currentCharacter = allCharacters.get(allCharacters.size() % 2 == 1 ?
                    allCharacters.size() / 2 + 1 : allCharacters.size() / 2);

            imagePointManagerSingleton.getCurrentBackground().draw();
            Drawing.drawRectangle(new Point(0,0), Window.getWidth(), Window.getHeight()/4 - 30, new Colour(0,0,0,0.8));
            Drawing.drawRectangle(new Point(0,Window.getHeight()*3/4), Window.getWidth(), Window.getHeight()/4, new Colour(0,0,0,0.8));
            if (canInteract) {
                FontSize characterFont = new FontSize(Fonts.TCB, 120);
                if (isUnlocked(currentCharacter.getName()) && isPickable(currentCharacter)) {
                    drawShadowRender(currentCharacter.getFullName());
                    characterFont.draw(currentCharacter.getName(), Window.getWidth()/5
                            - characterFont.getFont().getWidth(currentCharacter.getName())/2, Window.getHeight()/2);
                    characterFont.draw(currentCharacter.getLastName(), Window.getWidth()*4/5
                            - characterFont.getFont().getWidth(currentCharacter.getLastName())/2, Window.getHeight()/2);
                }
                else if (!isUnlocked(currentCharacter.getName())) {
                    characterFont.draw("???", Window.getWidth()/5
                            - characterFont.getFont().getWidth(currentCharacter.getName())/2, Window.getHeight()/2);
                    characterFont.draw("???", Window.getWidth()*4/5
                            - characterFont.getFont().getWidth(currentCharacter.getLastName())/2, Window.getHeight()/2);
                }
            }
            drawBorders(currentCharacter);
            for (Character character: allCharacters) {
                if (!isUnlocked(character.getName()) && imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())) != null) {
                    imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setDarken(true);
                    imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setTransparent(false);
                }
                else {
                    if (imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())) != null) {
                        imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setDarken(false);
                    }
                }
            }
            imagePointManagerSingleton.drawImagesWithTag("CharacterRender");
        }
        else if (settingsSingleton.getGameState() == 4 ) {
            SideCharacter currentCharacter = allSideCharacters.get(allSideCharacters.size() % 2 == 1 ?
                allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2);

            imagePointManagerSingleton.getCurrentBackground().draw();

            if (!toggleInfo) {
                Drawing.drawRectangle(new Point(0,0), Window.getWidth(), Window.getHeight()/4 - 30, new Colour(0,0,0,0.8));
                Drawing.drawRectangle(new Point(0,Window.getHeight()*3/4), Window.getWidth(), Window.getHeight()/4, new Colour(0,0,0,0.8));
                if (isPickable(currentCharacter) && isUnlocked(currentCharacter.getName())) {
                    FontSize characterFont = new FontSize(Fonts.TCB, 120);
                    String firstName = currentCharacter.getName().split(" ")[0];
                    String lastName = "";
                    if (currentCharacter.getName().split(" ").length > 1) {
                        lastName = currentCharacter.getName().split(" ")[1];
                    }
                    characterFont.draw(firstName, Window.getWidth()/5
                            - characterFont.getFont().getWidth(firstName)/2, Window.getHeight()/2);
                    characterFont.draw(lastName, Window.getWidth()*4/5
                            - characterFont.getFont().getWidth(lastName)/2, Window.getHeight()/2);
                }

                if (isPickable(currentCharacter)) {
                    imagePointManagerSingleton.get(String.format("res/SideCharacters/%s/render.png", currentCharacter.getName())).setTransparent(false);
                }
                drawBorders(currentCharacter);

                imagePointManagerSingleton.drawImagesWithTag("SideCharacterRender");
            }
            else {
                Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.5));
                imagePointManagerSingleton.get(String.format("res/SideCharacters/%s/render.png", currentCharacter.getName())).draw();
            }
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                menuTitle = null;
                drawGame();
                displayCharacterStats(players);
                if(!gameSettingsSingleton.getMap().hasFinished()) {
                }
                else {
                    FontSize tempFont = new FontSize(Fonts.AGENCYB, 100);
                    tempFont.draw("REACH THE TOP!", Window.getWidth()/2 - tempFont.getFont().getWidth("REACH THE TOP!")/2, 160);
                }
                renderAbilities();
            }
        }
        else if (settingsSingleton.getGameState() == 7) {
            drawGame();
            Colour transparent = new Colour(0, 0, 0, 0.5);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), transparent);
            if (settingsSingleton.getGameMode() == 0) {
                displayFailScreen();
            }
            else {
                Player winner = settingsSingleton.getWinner();
                ImagePoint characterImage = new ImagePoint(
                        String.format("res/Characters/%s/render.png", winner.getCharacter().getFullName()),
                        new Point(0,0));
                ImagePoint sideCharacterImage = new ImagePoint(
                        String.format("res/SideCharacters/%s/render.png", winner.getSideCharacter().getName()),
                        new Point(0,0));
                characterImage.setPos(Window.getWidth()/2 - characterImage.getWidth()/2, Window.getHeight() - characterImage.getHeight());
                sideCharacterImage.setPos(Window.getWidth()/2, Window.getHeight() - sideCharacterImage.getHeight());
                sideCharacterImage.draw();
                characterImage.draw();

                new Font(Fonts.DEJAVUSANS, 110).drawString(String.format("Player %d: %s is victorious!", settingsSingleton.getWinner().getId(), settingsSingleton.getWinner().getCharacter().getName()), 16, 100);

            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            Font chooseCharacterFont = new Font(Fonts.DEJAVUSANS, 100);
            Colour black = new Colour(0,0,0,0);
            imagePointManagerSingleton.setCurrentBackground(String.format("res/Characters/%s/bg.png", unlocked));
            imagePointManagerSingleton.getCurrentBackground().draw();

            if (musicPlayer.getSound("music/misc/Who.wav") != null) {
                black = new Colour(0, 0, 0,
                        (float) (musicPlayer.getSound("music/misc/Who.wav").getFrameLength() -
                                musicPlayer.getSound("music/misc/Who.wav").getFramePosition()) /
                                (float) musicPlayer.getSound("music/misc/Who.wav").getFrameLength());
            }
            if (musicPlayer.hasEnded("music/misc/Who.wav")) {
                chooseCharacterFont.drawString("NEW CHARACTER UNLOCKED!", Window.getWidth()/2 - chooseCharacterFont.getWidth("NEW CHARACTER UNLOCKED")/2, 100);
                ImagePoint unlockedImage = new ImagePoint(String.format("res/Characters/%s/render.png", unlocked), new Point(0,0));
                unlockedImage.setPos(Window.getWidth()/2 - unlockedImage.getWidth()/2, Window.getHeight() - unlockedImage.getHeight());
                if (imagePointManagerSingleton.get(unlockedImage.getFilename()) == null) {
                    imagePointManagerSingleton.add(unlockedImage);
                }
                drawShadowRender(unlocked);
                unlockedImage.draw();
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
            }
        }
        else if (settingsSingleton.getGameState() == 9) {
            Font playerFont = new Font(Fonts.DEJAVUSANS, 40);
            Font victoryFont = new Font(Fonts.DEJAVUSANS, 110);
            for (Tile tile: customMapTiles) {
                tile.draw();
            }
            if (!addingTile) {
                playerFont.drawString("S: Save and Exit | ESC: Exit without Saving | Arrow Keys: Navigate\nA: Add | R: Remove last block | D: Jump to start", 100, 50);
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.DARK.toColour());
                for (int i = page * 3; i < page * 3 + 3; i++) {
                    if (i < allTiles.size()) {
                        Tile tile = allTiles.get(i);
                        if (i % 3 == 0) {
                            tile.setPos(new Point(100, Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("1", 100, Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2);
                            tile1 = tile;
                        } else if (i % 2 == 0) {
                            tile.setPos(new Point(200 + tile.getImage().getWidth(), Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("2", 200 + tile.getImage().getWidth(), Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2);
                            tile2 = tile;
                        } else {
                            tile.setPos(new Point(300 + tile.getImage().getWidth() * 2, Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("3", 300 + tile.getImage().getWidth() * 2, Window.getHeight() / 2 - allTiles.get(i).getImage().getHeight() / 2);
                            tile3 = tile;
                        }
                        tile.draw();
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 10) {
            Font titleFont = new Font(Fonts.DEJAVUSANS, 100);
            Font gameFont = new Font(Fonts.DEJAVUSANS, 40);
            drawSettings();
//            Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), ColourPresets.DARK.toColour());
            if (gameSettingsSingleton.getPage() == 0) {
                menuTitle = "General";
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300);
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("Lives: %d", gameSettingsSingleton.getLives()), 100, 500);
            }
            else if (gameSettingsSingleton.getPage() == 1 || gameSettingsSingleton.getPage() == 2) {
                gameFont.drawString("Drag the slider across to increase/decrease the spawn rate!", titleFont.getWidth("SETTINGS"), 150, new DrawOptions().setBlendColour(0,0,0, 0.7));
                gameFont.drawString("Click the icon to toggle on/off", titleFont.getWidth("SETTINGS"), 190, new DrawOptions().setBlendColour(0,0,0, 0.7));
            }
            drawSliders();
        }
        else if (settingsSingleton.getGameState() == 11) {
            Font playerFont = new Font(Fonts.DEJAVUSANS, 40);
            drawGame();
            playerFont.drawString("PAUSE", (Window.getWidth() - playerFont.getWidth("PAUSE"))/2, Window.getHeight()/2 - 50);
            playerFont.drawString("Press ESC to resume", (Window.getWidth() - playerFont.getWidth("Press ESC to res"))/2, Window.getHeight()/2);
        }
        else if (settingsSingleton.getGameState() == 14) {
            Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), ColourPresets.WHITE.toColour());
            imagePointManagerSingleton.getCurrentBackground().draw();
            new ImagePoint("res/menu/main/name.png", new Point(148, 87)).draw();
            ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
            ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

            if (leftCover != null) {
                leftCover.draw();
            }
            if (rightCover != null) {
                rightCover.draw();
            }
        }
        drawButtons();
        drawSliders();
        if (menuTitle != null) {
            FontSize titleFont = new FontSize(Fonts.DEJAVUSANS, 110);
            if ((((settingsSingleton.getGameState() == 3) || (settingsSingleton.getGameState() == 10)) || (settingsSingleton.getGameState() == 4)) || (settingsSingleton.getGameState() == 9)) {
                DO.setBlendColour(ColourPresets.BLACK.toColour());
            } else {
                 DO.setBlendColour(ColourPresets.WHITE.toColour());
            }
            titleFont.draw(menuTitle, (Window.getWidth() - titleFont.getFont().getWidth(menuTitle)) / 2, 90);
        }
        showDisplayStrings();
    }

    public void showTime() {
        Font gameFont = new Font(Fonts.DEJAVUSANS, 40);
        gameFont.drawString(timeLogger.getDisplayTime(), Window.getWidth() - gameFont.getWidth(timeLogger.getDisplayTime()), 40);
    }

    public void updateTime() {
        timeLogger.updateFrames();
    }

    public void addToPlayableCharacter(String name) {
        playableCharacters.add(new Character(name));
    }

    public boolean isPlayable(String name) {
        for (Character character: playableCharacters) {
            if (character.getFullName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void checkAchievements() {
        //This file includes achievements and information on how to unlock each unlockable character.
        // Format: [Unlockable Character] then [Character involved in unlocking] [line No of their stat] [value threshold].
        try {
            Scanner achievementScanner = new Scanner(new File("stats/Achievements.txt"));
            while (achievementScanner.hasNextLine()) {
                ArrayList<String> achievementLine = new ArrayList<>(Arrays.asList(achievementScanner.nextLine().split(" ")));
                String newCharacter;
                if (new CharacterNames().getAllCharacterNames().contains(String.format("%s %s", achievementLine.get(0),
                        achievementLine.get(1)))) {
                    newCharacter = String.format("%s %s", achievementLine.get(0), achievementLine.get(1));
                    achievementLine.remove(0);
                }
                else {
                    newCharacter = achievementLine.get(0);
                }
                achievementLine.remove(0);
                int threshold = Integer.valueOf(achievementLine.get(achievementLine.size() - 1));
                achievementLine.remove(achievementLine.size() - 1);
                String existingChar = String.join(" ", achievementLine);
                if (stats.hasPassedThreshold(existingChar, threshold)) {
                    addToPlayableCharacter(newCharacter);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String checkAchievementsInGame() {
        try {
            Scanner achievementScanner = new Scanner(new File("stats/Achievements.txt"));
            while (achievementScanner.hasNextLine()) {
                ArrayList<String> achievementLine = new ArrayList<>(Arrays.asList(achievementScanner.nextLine().split(" ")));
                String newCharacter;
                if (new CharacterNames().getAllCharacterNames().contains(String.format("%s %s", achievementLine.get(0),
                        achievementLine.get(1)))) {
                    newCharacter = String.format("%s %s", achievementLine.get(0), achievementLine.get(1));
                    achievementLine.remove(0);
                }
                else {
                    newCharacter = achievementLine.get(0);
                }
                achievementLine.remove(0);
                int threshold = Integer.valueOf(achievementLine.get(achievementLine.size() - 1));
                achievementLine.remove(achievementLine.size() - 1);
                String existingChar = String.join(" ", achievementLine);
                if (stats.hasPassedThreshold(existingChar, threshold)) {
                    if (!isPlayable(newCharacter)) {
                        addToPlayableCharacter(newCharacter);
                        return newCharacter;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public String playDialogue(Integer dialogueInt, String mode) {
//        String currentDialogue = "";
//        try {
//            if (mode.equals("Story")) {
//                dialogueScanner = new Scanner(new File(String.format("story/%d.txt", dialogueInt)));
//            }
//            else {
//                dialogueScanner = new Scanner(new File(String.format("scene/%d.txt", dialogueInt)));
//            }
//            dialogueWords = new ArrayList<>();
//            while (dialogueScanner.hasNextLine()) {
//                dialogueLine = dialogueScanner.nextLine().split(" ");
//                for (String word: dialogueLine) {
//                    dialogueWords.add(word);
//                }
//            }
//            int currentLineLength = 0;
//            for (String word: dialogueWords) {
//                if (word.endsWith(":")) {
//                    currentLineLength = 0;
//                    // break;
//                }
//                if (currentLineLength + word.length() + 1 < MAX_DIALOGUE_LIMIT) {
//                    currentDialogue = currentDialogue + word + ' ';
//                    currentLineLength += word.length() + 1;
//                }
//                else {
//                    currentDialogue = currentDialogue + '\n' + word + ' ';
//                    currentLineLength = word.length() + 1;
//                }
//            }
//            return currentDialogue;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return currentDialogue;
//    }

    public void drawButtons() {
        if (buttons.size() > 0) {
            for (Button button: buttons) {
                button.draw();
            }
        }
    }

    public void drawSliders() {
        if (sliders.size() > 0) {
            for (Slider slider : sliders) {
                slider.draw();
            }
        }
    }

    public void drawBorders(Character currentCharacter) {
        Image border = new Image("res/misc/Selected.png");

        double spacing = Window.getWidth()/6;

        FontSize playerDesc = new FontSize(Fonts.TCB, 30);

        for (int i = 0; i < settingsSingleton.getPlayers().size(); i++) {
            Player currentPlayer = players.get(i);
            Character playerCharacter = currentPlayer.getCharacter();
            double timeSpace = i > 1 ? 200 : 0;
            border.drawFromTopLeft(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0);
            String name = String.format("P%d\nSelecting...", currentPlayer.getId());
            if (currentPlayer.getId() == getIDOfPlayerPickingCharacter()) {
                if (TimeLogger.getInstance().getFrames() % 144 < 72) {
                    name = String.format("P%d", currentPlayer.getId());
                }
                ImagePoint characterPeek = new ImagePoint(String.format("res/characters/%s/peek.png", currentCharacter.getFullName()),
                        new Point(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0));
                if (isUnlocked(currentCharacter.getName())) {
                    characterPeek.setTransparent(true);
                }
                else {
                    characterPeek.setDarken(true);
                }
                characterPeek.draw();
            }
            else if (playerCharacter != null) {
                name = String.format("P%d\n%s", currentPlayer.getId(), playerCharacter.getFullName());
                ImagePoint characterPeek = new ImagePoint(String.format("res/characters/%s/peek.png", playerCharacter.getFullName()),
                        new Point(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0));
                characterPeek.setDarken(!isUnlocked(playerCharacter.getName()));
                characterPeek.draw();
            }
            else {
                name = String.format("P%d", players.get(i).getId());
            }
            playerDesc.draw(name,
                    i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, border.getHeight());
        }
    }

    public void drawBorders(SideCharacter currentCharacter) {
        Image border = new Image("res/misc/Selected.png");

        double spacing = Window.getWidth()/6;

        FontSize playerDesc = new FontSize(Fonts.TCB, 30);

        for (int i = 0; i < settingsSingleton.getPlayers().size(); i++) {
            Player currentPlayer = players.get(i);
            SideCharacter playerCharacter = currentPlayer.getSideCharacter();
            double timeSpace = i > 1 ? 200 : 0;
            border.drawFromTopLeft(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0);
            String name;
            if (currentPlayer.getId() == getIDOfPlayerPickingCharacter()) {
                name = String.format("P%d\nSelecting...", currentPlayer.getId());
                ImagePoint characterPeek = new ImagePoint(String.format("res/SideCharacters/%s/peek.png", currentCharacter.getName()),
                        new Point(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0));
                if (isUnlocked(currentCharacter.getName())) {
                    characterPeek.setTransparent(true);
                }
                else {
                    characterPeek.setDarken(isUnlocked(currentCharacter.getName()));
                }
                characterPeek.draw();
            }
            else if (playerCharacter != null) {
                name = String.format("P%d\n%s", currentPlayer.getId(), playerCharacter.getName());
                ImagePoint characterPeek = new ImagePoint(String.format("res/sideCharacters/%s/peek.png", playerCharacter.getName()),
                        new Point(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0));
                characterPeek.setDarken(!isUnlocked(playerCharacter.getName()));
                characterPeek.draw();
            }
            else {
                name = String.format("P%d", players.get(i).getId());
            }
            playerDesc.draw(name,
                    i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, border.getHeight());
        }
    }

//    public void loadStory() {
//        int lineNumber = 0;
//        try {
//            Scanner storyScanner = new Scanner(new File("stats/Story.txt"));
//            while(storyScanner.hasNextLine()) {
//                String[] line = storyScanner.nextLine().split(" ");
//                if (lineNumber == 0) {
//                    currentScene = Integer.parseInt(line[line.length- 1]);
//                    if (currentScene < 0) {
//                        currentScene = 0;
//                    }
//                }
//                else if (lineNumber == 1) {
//                    currentStory = Integer.parseInt(line[line.length - 1]);
//                    if (currentStory < 0) {
//                        currentStory = 0;
//                    }
//                }
//                lineNumber++;
//            }
//            if (!playingMap) {
//                playingScene = true;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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

            stringDisplays.add(new StringDisplay("Story saved successfully.", 3, new FontSize(Fonts.DEJAVUSANS, 50), new Point(0,0)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomMap() {
        String currentFile = "res/maps/mapData/Custom.txt";
        File oldFile = new File(currentFile);
        try {
            oldFile.createNewFile();
            FileWriter fw = new FileWriter(oldFile, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            int count = 0;
            for (Tile tile: customMapTiles) {
                String tileName = tile.getName();
                pw.print(tileName);
                if (count > 2) {
                    pw.print('\n');
                    count = 0;
                }
                else {
                    pw.print(",");
                    count++;
                }
            }

            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPickable(Character character) {
        for (Player player : players) {
            if (player.getCharacter() != null) {
                if (player.getCharacter().getFullName().equals(character.getFullName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isPickable(SideCharacter character) {
        for (Player player : players) {
            if (player.getSideCharacter() != null) {
                if (player.getSideCharacter().getName().equals(character.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void pickCharacter(Player player, Character character) {
        if (isUnlocked(character.getName())) {
            if (isPickable(character)) {
                eventsListener.addEvent(new EventCharacterPicked( 1*frames, String.format("Player %d picked: %s", player.getId(), character.getFullName())));
                musicPlayer.addMusic("music/misc/Character Picked.wav");
                player.setCharacter(character);
                if (!musicPlayer.contains(character.playLine())) {
                    musicPlayer.addMusic(character.playLine());
                } else {
                    musicPlayer.remove(character.playLine());
                    musicPlayer.addMusic(character.playLine());
                }
            }
        }
    }

    public void pickSideCharacter(Player player, SideCharacter character) {
        if (isPickable(character)) {
            eventsListener.addEvent(new EventSideCharacterPicked( 1*frames, String.format("Player %d picked: %s", player.getId(), character.getName())));
            player.setSideCharacter(character);
            if (!musicPlayer.contains(character.getSoundPath())) {
                musicPlayer.addMusic(character.getSoundPath());
            } else {
                musicPlayer.restart(character.getSoundPath());
            }
        }
    }

    public void pickMap(Player player, Map map) {
        if (player.getMapChosen() == null) {
            musicPlayer.addMusic("music/misc/Click.wav");
            player.setMapChosen(gameSettingsSingleton.getMap());
        }
    }

    public void checkCollisionTiles() {
        for (Player player: players) {
            if (!gameSettingsSingleton.getMap().hasFinished()) {
                Rectangle bottomRectangle = new Rectangle(0, Window.getHeight() + 40, Window.getWidth(), 20);
                if ((player.getCharacter().getRectangle().intersects(bottomRectangle))) {
                    player.getCharacter().setLives(0);
                    break;
                }
            }
            for (Tile tile : gameSettingsSingleton.getMap().getVisibleTiles()) {
                Rectangle tileRectangle = getBoundingBoxOf(tile.getImage(), new Point(tile.getPos().x, tile.getPos().y));
                if ((tile.getType().equals("Ice")) && (player.getCharacter().getRectangle().intersects(tileRectangle))) {
                    player.getCharacter().onIce();
                }
                else if ((tile.getType().equals("Slow")) && (player.getCharacter().getRectangle().intersects(tileRectangle))) {
                    player.getCharacter().onSlow();
                }
                if (tile.getCollisionBlocks().size() > 0) {
                    for (CollisionBlock block : tile.getCollisionBlocks()) {
                        if (block.hasCollided(player.getCharacter().getRectangle())) {
                            player.getCharacter().setLives(0);
                        }
                    }
                }
            }
        }
    }

    public void kill(Player player) {
        player.getCharacter().setLives(player.getCharacter().getLives() - 1);
    }

    public void updatePlayerMovement(Input input) {
        for (Player player: players) {
            Character character = player.getCharacter();
            if (!player.getCharacter().isDead()) {
                if (character.canMove()) {
                    if (player.getClass().equals(ComputerEasy.class) || player.getClass().equals(ComputerHard.class)) {
                        player.moveComputer(obstacles);
                    }
                    else {
                        player.moveCharacter(input);
                    }
                }
                if (!gameSettingsSingleton.getMap().hasFinished() && !character.isMoving()) {
                    character.slide();
                }
            }
        }
    }

    public void updateObjects() {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.canMove()) {
                obstacle.move();
            }
            if ((obstacle.getPos().y > Window.getHeight()) && (!obstaclesToRemove.contains(obstacle))) {
                obstaclesToRemove.add(obstacle);
            }
        }
        for (PowerUp powerUp : powerUps) {
            if (powerUp.canMove()) {
                powerUp.move();
            }
            if (powerUp.getPos().y > Window.getHeight()) {
                powerUpsToRemove.add(powerUp);
            }
        }
        for (Player player: players) {
            if (player.getCharacter() != null) {
                player.getCharacter().updateCharacter();
            }
        }
    }

    public void updateAbilities() {
        for (Player player: players) {
            if (player.getSideCharacter().isActivating()) {
                player.getSideCharacter().activateAbility(player, obstacles, powerUps);
            }
        }
    }

    public void checkCollisionObstacles() {
        for (Obstacle obstacle: obstacles) {
            Rectangle obstacleRectangle = getBoundingBoxOf(obstacle.getImage(), obstacle.getPos());
            for (Player player : players) {
                if (player.getCharacter().getRectangle().intersects(obstacleRectangle)) {
                    if (!player.getCharacter().isDead()) {
                        obstaclesToRemove.add(obstacle);
                        if (player.getCharacter().hasShield()) {
                            player.getCharacter().popShield();
                        }
                        else {
                            if (obstacle.getName().equals("StunBall")) {
                                player.getCharacter().gotStunned();
                            }
                            else {
                                kill(player);
                            }
                        }
                    }
                }
            }
        }
        obstacles.removeAll(obstaclesToRemove);
    }

    public void checkCollisionPowerUps() {
        for (PowerUp powerUp: powerUps) {
            for (Player player : players) {
                if (player.getCharacter().getRectangle().intersects(getBoundingBoxOf(powerUp.getImage(), powerUp.getPos()))) {
                    if (!player.getCharacter().isDead()) {
                        powerUp.gainPowerUp(player);
                        powerUpsToRemove.add(powerUp);
                        break;
                    }
                }
            }
        }
        powerUps.removeAll(powerUpsToRemove);
    }

    public void spawnPowerUps() {
        double spawnNo = Math.random()*5;
        if (spawnNo < 1) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedUp")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedUp")) {
                    powerUps.add(new PowerUpSpeedUp());
                }
            }
        }
        else if (spawnNo < 2) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedDown")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedDown")) {
                    powerUps.add(new SpeedDown());
                }
            }
        }
        else if (spawnNo < 3) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Minimiser")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Minimiser")) {
                    powerUps.add(new PowerUpMinimiser());
                }
            }
        }
        else if (spawnNo < 4) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Shield")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Shield")) {
                    powerUps.add(new PowerUpShield());
                }
            }
        }
        else {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpecialAbilityPoints")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpecialAbilityPoints")) {
                    powerUps.add(new PowerUpSpecialAbilityPoints());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isRocks()) {
            if (Math.random() < ObstaclesSettingsSingleton.getInstance().getFrequency("Rock")) {
                obstacles.add(new ObstacleRock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isBalls()) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency("Ball")) {
                obstacles.add(new ObstacleBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isStunBalls()) {
            if (Math.random()
                    < ObstaclesSettingsSingleton.getInstance().getFrequency("StunBall")) {
                obstacles.add(new ObstacleStunBall());
            }
        }
    }

    public void addTileToCustomMap(String line) {
        int currentRow = customMapTiles.size()/4;
        int currentBlocksInRow = customMapTiles.size()%4;
        Point point = new Point(480 * currentBlocksInRow, 15*offset + 600 + -475 * currentRow);

        Tile tile = null;
        if (line.equals("Basic")) {
            tile = new TileBasic(point);
        }
        else if (line.equals("BasicTop")) {
            tile = new TileBasicTop(point);
        }
        else if (line.equals("BasicLeft")) {
            tile = new TileBasicLeft(point);
        }
        else if (line.equals("Ice")) {
            tile = new TileIce(point);
        }
        else if (line.equals("IceTop")) {
            tile = new TileIceTop(point);
        }
        else if (line.equals("IceLeft")) {
            tile = new TileIceLeft(point);
        }
        else if (line.equals("Slow")) {
            tile = new TileSlow(point);
        }
        else if (line.equals("SlowLeft")) {
            tile = new TileSlowLeft(point);
        }
        else if (line.equals("SlowTop")) {
            tile = new TileSlowTop(point);
        }
        else if (line.equals("BasicSad")) {
            tile = new TileBasicSad(point);
        }
        if (tile != null) {
            customMapTiles.add(tile);
        }
    }

    public void updateTiles(int offset) {
        for (Tile tile: customMapTiles) {
            tile.setPos(new Point(tile.getPos().x, tile.getPos().y + offset));
        }
    }

    public void addDisplayString(String string, int time) {
        stringDisplays.add(new StringDisplay(string, time, new FontSize(Fonts.DEJAVUSANS, 50), new Point(0,0)));
    }

    public void setPlayersPosition() {
        double spawnDivider = 1;
        for (Player player : players) {
            double width = player.getCharacter().getImage().getWidth();
            player.getCharacter().setPosition(new Point(Window.getWidth()/2 - (players.size() * width / 2)  + spawnDivider * width, Window.getHeight() - 200));
            spawnDivider++;
        }
    }

    public Rectangle getBoundingBoxOf(Image image, Point pos) {
        return image.getBoundingBoxAt(new Point(pos.x + image.getWidth()/2,  pos.y + image.getHeight()/2));
    }

    public void updateExp() {
        gainExpOnObstacles();
        gainExpOnHeight();
    }

    public void gainExpOnHeight() {
        for (Player player: players) {
            if (!player.getSideCharacter().isActivating()) {
                double mapHeight = Window.getHeight();
                player.getCharacter().gainSpecialAbilityBar((mapHeight - player.getCharacter().getPos().y)/mapHeight/90);
            }
        }
    }

    public void gainExpOnObstacles() {
        for (Obstacle obstacle: obstacles) {
            Point obstacleCentre = new Point(obstacle.getPos().x + obstacle.getImage().getWidth()/2,
                    obstacle.getPos().y + obstacle.getImage().getHeight()/2);
            Drawing.drawRectangle(obstacleCentre, 10,10, new Colour(0,0,0));
            for (Player player: players) {
                Point characterCentre = player.getCharacter().getPos();
                Drawing.drawRectangle(characterCentre, 200, 10, new Colour(0,0,0));
                if (!player.getSideCharacter().isActivating()) {
                    if (!obstacle.getPlayersGainedEXP().contains(player.getId())) {
                        if (Math.abs(obstacleCentre.y -characterCentre.y) < 10) {
                            double distance = Math.abs(obstacleCentre.x - characterCentre.x); //distance from x-axis
                            if (distance < 200) {
                                player.getCharacter().gainSpecialAbilityBar((200 - distance)/10);
                                obstacle.updatePlayersGainedEXP(player.getId());
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateButtons() {
        buttons.removeAll(buttonsToRemove);
        buttons.addAll(buttonsToAdd);
        buttonsToAdd.clear();
        buttonsToRemove.clear();
    }

    public void updateSliders(Input input) {
        for (Slider slider: sliders) {
            slider.interact(input);
        }
        sliders.removeAll(slidersToRemove);
        slidersToRemove.clear();
    }

    public void rotateCharacter(String direction) {
        musicPlayer.addMusic("music/misc/CharacterNext.wav");
        if (direction.equals("LEFT")) {
            if (settingsSingleton.getGameState() == 3) {
                Character temp = allCharacters.get(allCharacters.size() - 1);
                allCharacters.remove(allCharacters.size() - 1);
                allCharacters.add(0, temp);
            } else if (settingsSingleton.getGameState() == 4) {
                SideCharacter temp = allSideCharacters.get(allSideCharacters.size() - 1);
                allSideCharacters.remove(allSideCharacters.size() - 1);
                allSideCharacters.add(0, temp);
            }
        }
        else if (direction.equals("RIGHT")) {
            if (settingsSingleton.getGameState() == 3) {
                Character temp = allCharacters.get(0);
                allCharacters.remove(0);
                allCharacters.add(temp);
            } else if (settingsSingleton.getGameState() == 4) {
                SideCharacter temp = allSideCharacters.get(0);
                allSideCharacters.remove(0);
                allSideCharacters.add(temp);
            }
        }
    }

    public boolean isUnlocked(String characterName) {
        for (Character character: playableCharacters) {
            if (character.getName().equals(characterName)) {
                return true;
            }
        }
        for (SideCharacter character: allSideCharacters) {
            if (character.getName().equals(characterName)) {
                return true;
            }
        }
        return false;
    }

    public int getIDOfPlayerPickingCharacter() {
        if (settingsSingleton.getGameState() == 3) {
            for (Player player: players) {
                if (player.getId() <= settingsSingleton.getPlayers().size() + 1) {
                    if (player.getCharacter() == null) {
                        return player.getId();
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 4) {
            for (Player player: players) {
                if (player.getId() <= settingsSingleton.getPlayers().size() + 1) {
                    if (player.getSideCharacter() == null) {
                        return player.getId();
                    }
                }
            }
        }
        return -1;
    }

    public void loadAllCharacters() {
        for (String character: new CharacterNames().getAllCharacterNames()) {
            allCharacters.add(new Character(character));
        }
    }

    public void loadStarterCharacters() {
        playableCharacters.add(new Character(CharacterNames.CHIZURU));
        playableCharacters.add(new Character(CharacterNames.ZEROTWO));
        playableCharacters.add(new Character(CharacterNames.MIKU));
        playableCharacters.add(new Character(CharacterNames.MAI));
    }

    public void loadAllSideCharacters() {
        allSideCharacters.addAll(Arrays.asList(
                new SideHisoka(), new SideJotaro(), new SideDio(),
                new SideYuu(), new SideYugi(), new SideGoku(), new SideZoro(),
                new SideLelouch(), new SideAllMight(), new SideGojo(), new SideItachi()));
    }

    public void loadPlayableMaps() {
        for (MapNames mapName: MapNames.values()) {
            playableMaps.add(new Map(mapName.toString()));
        }
    }

    public void loadCustomMapConfigs() {
        allTiles.addAll(Arrays.asList(
                new TileBasic(new Point(0,0)), new TileBasicLeft(new Point(0,0)) , new TileBasicTop(new Point(0,0)),
                new TileIce(new Point(0,0)), new TileIceLeft(new Point(0,0)), new TileIceTop(new Point(0,0)),
                new TileSlow(new Point(0,0)), new TileSlowLeft(new Point(0,0)), new TileSlowTop(new Point(0,0))));
    }

    public void loadPowerUpSettings() {
        allPowerUps.addAll(Arrays.asList(
                new SpeedDown(), new PowerUpSpeedUp(),
                new PowerUpMinimiser(),
                new PowerUpShield(),
                new PowerUpSpecialAbilityPoints()));
    }

    public void loadObstacleSettings() {
        allObstacles.addAll(Arrays.asList(
                new ObstacleBall(),
                new ObstacleRock(),
                new ObstacleStunBall()));
    }

    public void sortCharacters() {
        ArrayList<Character> unlockedCharacters = new ArrayList<>();
        ArrayList<Character> lockedCharacters = new ArrayList<>();
        for (Character character: allCharacters) {
            if (isUnlocked(character.getName())) {
                unlockedCharacters.add(character);
            }
            else {
                lockedCharacters.add(character);
            }
        }
        allCharacters.clear();
        allCharacters.addAll(unlockedCharacters);
        allCharacters.addAll(lockedCharacters);
    }
    public void adjustCharacterRotation() {
        int middleIndex = allCharacters.size() % 2 == 1 ?
                allCharacters.size() / 2 + 1 : allCharacters.size() / 2;
        while (!isUnlocked(allCharacters.get(middleIndex).getName())) {
            rotateCharacter("LEFT");
        }
    }

    public void loadCharacterRender() {
        double spacing = Window.getHeight()/4;
        int index = 0;
        double minScale = 0.275;
        double maxScale = 1;
        int middleIndex = allCharacters.size() % 2 == 1 ? allCharacters.size() / 2 + 1 : allCharacters.size() / 2;

        for (Character character: allCharacters) {
            ImagePoint characterRender = new ImagePoint(String.format("res/Characters/%s/render.png", character.getFullName()),
                    new Point(0, 0), "CharacterRender");
            characterRender.setScale(minScale);
            if (index < middleIndex) {
                characterRender.setPos( ((allCharacters.size() * spacing/2) - Window.getWidth())/2 - 490 + (spacing * (index - 1)),
                        Window.getHeight()*3/4);
            }
            else if (middleIndex == index) {
                characterRender.setScale(maxScale);
                characterRender.setPos(490, Window.getHeight()/8);
            }
            else {
                characterRender.setPos( Window.getWidth()/2 + (spacing * (index - middleIndex)),
                        Window.getHeight()*3/4);
            }
            imagePointManagerSingleton.add(characterRender);
            index++;
        }
    }

    public void loadPlayers() {
        for (Player player: players) {
            player.getCharacter().setLives(gameSettingsSingleton.getLives());
        }
        setPlayersPosition();
    }

    public void displayCharacterStats(ArrayList<Player> players) {
        if (canInteract) {
            int playerIndex = 0;
            for (Player player: players) {
                ImagePoint characterDisplay = new ImagePoint(String.format("res/characters/%s/Peek.png", player.getCharacter().getFullName()), new Point(0,0));
                characterDisplay.setPos(playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - (characterDisplay.getHeight()));
                if (player.getCharacter().getSpecialAbilityBar() >= 100) {
                    new FontSize(Fonts.TITANONE, 30).getFont().drawString("Power Ready!",
                            characterDisplay.getPos().x, characterDisplay.getPos().y,
                            new DrawOptions().setBlendColour(247d/255, 251d/255, 142d/255));
                }
                Image border = new Image("res/misc/Selected.png");
                border.drawFromTopLeft(characterDisplay.getPos().x, Window.getHeight() - border.getHeight() );
                characterDisplay.draw();

                int buffs = 0;

                if (player.getCharacter().hasShield()) {
                    ImagePoint shieldIcon = new ImagePoint("res/misc/Shield.png", new Point(0,0));
                    shieldIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                            Window.getHeight() - shieldIcon.getHeight()));
                    shieldIcon.draw();
                    buffs++;
                }
                if (player.getCharacter().isMinimised()) {
                    ImagePoint miniIcon = new ImagePoint("res/PowerUp/Minimiser.png", new Point(0,0));
                    miniIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                            Window.getHeight() - miniIcon.getHeight()));
                    miniIcon.draw();
                    buffs++;
                }
                if (player.getCharacter().isSpedUp()) {
                    ImagePoint speedIcon = new ImagePoint("res/PowerUp/SpeedUp.png", new Point(0,0));
                    speedIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                            Window.getHeight() - speedIcon.getHeight()));
                    speedIcon.draw();
                    buffs++;
                }
                if (player.getCharacter().isSpedDown()) {
                    ImagePoint slowIcon = new ImagePoint("res/PowerUp/SpeedDown.png", new Point(0,0));
                    slowIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                            Window.getHeight() - slowIcon.getHeight()));
                    slowIcon.draw();
                    buffs++;
                }

                if (!player.getSideCharacter().getName().equals(CharacterNames.YUGI)) {
                    ImagePoint sideCharacter = new ImagePoint(String.format("res/sideCharacters/%s/inGame.png", player.getSideCharacter().getName()),
                            new Point(0,0));
                    sideCharacter.setPos(characterDisplay.getWidth() + playerIndex*Window.getWidth()/players.size(),
                            Window.getHeight() - characterDisplay.getHeight());
                    sideCharacter.setSection(0, 0, sideCharacter.getWidth(),
                            sideCharacter.getHeight()*player.getCharacter().getSpecialAbilityBar()/100);
                    sideCharacter.draw();
                }
                else {
                    int index = 0;
//                for (ExodiaPiece exodiaPiece: player.getSideCharacter().getExodiaPiecesCollected()) {
//                    exodiaPiece.getImage().drawFromTopLeft(characterDisplay.getWidth() + playerIndex*Window.getWidth()/(players.size()) + exodiaPiece.getImage().getWidth()*index, Window.getHeight() - characterDisplay.getHeight());
//                    index++;
//                }
                }
                playerIndex++;
            }
        }
    }

    public void drawCurrentHeight() {
        Font gameFont = new FontSize(Fonts.DEJAVUSANS, 40).getFont();
        if (!settingsSingleton.isNight()) {
            gameFont.drawString(String.format("%4.0f/%4.0fm", gameSettingsSingleton.getMap().getCurrentHeight()/10, gameSettingsSingleton.getMap().getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(ColourPresets.WHITE.toColour()));
        }
        else {
            gameFont.drawString(String.format("%4.0f/%4.0fm", gameSettingsSingleton.getMap().getCurrentHeight()/10, gameSettingsSingleton.getMap().getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
        }
    }

    public void renderAbilities() {
        for (Player player: players) {
            if (player.getSideCharacter().isActivating()) {
                player.getSideCharacter().renderAbility();
            }
        }
    }

    public void drawGame() {
        gameSettingsSingleton.getMap().draw();
        if (canInteract) {
            for (Player player : players) {
                if (!player.getCharacter().isDead()) {
                    player.getCharacter().draw();
                    Font gameFont = new Font(Fonts.DEJAVUSANS, 40);
                    gameFont.drawString(String.format("P%s", player.getId()), player.getCharacter().getPos().x, player.getCharacter().getPos().y + 100);
                }
            }
        }
        for (PowerUp powerUp: powerUps) {
            powerUp.getImage().drawFromTopLeft(powerUp.getPos().x, powerUp.getPos().y);
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
        }
        if (!gameSettingsSingleton.getMap().hasFinished()) {
            drawCurrentHeight();
        }
    }

    public void drawSettings() {
        gameSettingsSingleton.getMap().draw();
        for (PowerUp powerUp: powerUps) {
            powerUp.getImage().drawFromTopLeft(powerUp.getPos().x, powerUp.getPos().y);
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
        }
    }

    public void displayFailScreen() {
        buttonsToRemove.addAll(buttons);
        buttons.add(new Button("Retry", "Continue?",
                new FontSize(Fonts.DEJAVUSANS, 160),
                new Rectangle(0, Window.getHeight()/2, Window.getWidth(), 160),
                ColourPresets.WHITE.toColour()));
        buttons.add(new Button("Back To Start", "Exit",
                new FontSize(Fonts.DEJAVUSANS, 160),
                new Rectangle(0, Window.getHeight() / 1.5, Window.getWidth(), 160),
                ColourPresets.WHITE.toColour()));
        musicPlayer.setMainMusic("music/misc/Fail.wav");

        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.7));
        Font victoryFont = new Font(Fonts.DEJAVUSANS, 110);
        victoryFont.drawString("YOU FAILED THE CLIMB!", 30, 110);
    }

    public void showDisplayStrings() {
        for (StringDisplay stringDisplay: stringDisplays.getStringDisplays()) {
            stringDisplay.draw();
        }
    }

    public void updateDisplayStrings() {
        ArrayList<StringDisplay> stringDisplaysToRemove = new ArrayList<>();
        for (StringDisplay stringDisplay: stringDisplays.getStringDisplays()) {
            if (stringDisplay.getTime() <= 0 && !stringDisplay.isPermanent()) {
                stringDisplaysToRemove.add(stringDisplay);
            }
            else {
                stringDisplay.update();
            }
        }
        stringDisplays.getStringDisplays().removeAll(stringDisplaysToRemove);
    }

    public void drawShadowRender(String charName) {
        double xOffset = -(currentMousePosition.x - Window.getWidth()/2)/Window.getWidth()*100;
        double yOffset = -(currentMousePosition.y - Window.getHeight()/2)/Window.getHeight()*100;
         if (imagePointManagerSingleton.get(String.format("res/Characters/%s/render.png", charName)) != null) {
             ImagePoint shadow = new ImagePoint(String.format("res/Characters/%s/render.png", charName),
                     imagePointManagerSingleton.get(String.format("res/Characters/%s/render.png", charName)).getPos());
             shadow.setPos(shadow.getPos().x + xOffset, shadow.getPos().y + yOffset);
             shadow.setTransparent(true);
             shadow.setDarken(true);
             shadow.draw();
         }
    }

    public void updateDemo(Input input) {
        if (gameSettingsSingleton.getMap() != null) {
            gameSettingsSingleton.getMap().updateTiles(gameSettingsSingleton.getMapSpeed());
            if (gameSettingsSingleton.getMap().hasFinished()) {
                gameSettingsSingleton.getMap().generateMap();
            }
        }
        else {
            gameSettingsSingleton.setMap(new Map("Training Ground"));
            gameSettingsSingleton.getMap().generateMap();
        }

        if (players.size() == 0) {
            players.add(new ComputerEasy(0));
            players.get(0).setCharacter(new Character(CharacterNames.CHIZURU));
            players.get(0).setSideCharacter(new SideJotaro());
            players.add(new ComputerHard(1));
            players.get(1).setCharacter(new Character(CharacterNames.MAI));
            players.get(1).setSideCharacter(new SideDio());
            setPlayersPosition();
        }
        boolean notDead = false;
        for (Player player: players) {
            if (!player.getCharacter().isDead()) {
                notDead = true;
            }
        }
        if (!notDead) {
            gameSettingsSingleton.getMap().generateMap();
            players.clear();
        }
        updatePlayerMovement(input);
        checkCollisionTiles();
        checkCollisionObstacles();
        checkCollisionPowerUps();
        updateObjects();
        spawnPowerUps();
        spawnObstacles();
    }

    public void updateSettings() {
        spawnObstacles();
        spawnPowerUps();
        updateObjects();
    }
}