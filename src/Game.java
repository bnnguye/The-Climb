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

    private final ArrayList<Button> buttons= buttonsSingleton.getButtons();
    private final ArrayList<Button> buttonsToRemove = buttonsSingleton.getButtonsToRemove();
    private final ArrayList<Button> buttonsToAdd = buttonsSingleton.getButtonsToAdd();
    private final ArrayList<Slider> sliders = buttonsSingleton.getSliders();
    private final ArrayList<Slider> slidersToRemove = buttonsSingleton.getSlidersToRemove();
    private final EventsListenerSingleton.EventsListener eventsListener = eventsListenerSingleton.getEventsListener();
    private final ArrayList<Character> characters= new ArrayList<>();
    private final ArrayList<Character> allCharacters = new ArrayList<>();
    private final ArrayList<Player> players= new ArrayList<>();
    private final ArrayList<Obstacle> obstacles= new ArrayList<>();
    private final ArrayList<Obstacle> obstaclesToRemove= new ArrayList<>();
    private final ArrayList<PowerUp> powerUps= new ArrayList<>();
    private final ArrayList<PowerUp> powerUpsToRemove= new ArrayList<>();
    private final ArrayList<SideCharacter> allSideCharacters= new ArrayList<>();
    private final ArrayList<StringDisplay> stringDisplays = new ArrayList<>();

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
    private Map map;
    private final ArrayList<Tile> allTiles= new ArrayList<>();
    private final ArrayList<Tile> customMapTiles= new ArrayList<>();
    private boolean addingTile = false;
    private int page = 0;
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private int offset = 0;


    private boolean playingAnimation = false;
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

    double scale = 1;

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
        players.addAll(Arrays.asList(new Player(1), new Player(2)));
        // initialize characters
        characters.add(new Character(CharacterNames.CHIZURU));
        characters.add(new Character(CharacterNames.ZEROTWO));
        characters.add(new Character(CharacterNames.MIKU));
        characters.add(new Character(CharacterNames.MAI));

        imagePointManagerSingleton.setCurrentBackground(null);

        // adds unlocked characters
        checkAchievements();

        for (String character: new CharacterNames().getAllCharacterNames()) {
            allCharacters.add(new Character(character));
        }

        allSideCharacters.addAll(Arrays.asList(
                new SideHisoka(), new SideJotaro(), new SideDio(),
                new SideYuu(), new SideYugi(), new SideGoku(), new SideZoro(),
                new SideLelouch(), new SideAllMight(), new SideGojo(), new SideItachi()));

        for (MapNames mapName: MapNames.values()) {
            playableMaps.add(new Map(mapName.toString()));
        }


        allTiles.addAll(Arrays.asList(
                new TileBasic(new Point(0,0)), new TileBasicLeft(new Point(0,0)) , new TileBasicTop(new Point(0,0)),
                new TileIce(new Point(0,0)), new TileIceLeft(new Point(0,0)), new TileIceTop(new Point(0,0)),
                new TileSlow(new Point(0,0)), new TileSlowLeft(new Point(0,0)), new TileSlowTop(new Point(0,0))));

        allPowerUps.addAll(Arrays.asList(
                new SpeedDown(), new PowerUpSpeedUp(),
                new PowerUpMinimiser(),
                new PowerUpShield(),
                new PowerUpSpecialAbilityPoints()));

        allObstacles.addAll(Arrays.asList(
                new ObstacleBall(),
                new ObstacleRock(),
                new ObstacleStunBall()));


        stats.getGameStats();
//        loadStory();
//        currentMode = currentStory >= currentScene ? "Scene" : "Story";

        eventsListener.addEvent(new EventStartApp(5 * frames / 2, "Game initiated"));
    }

    @Override
    protected void update(Input input) {

        if (settingsSingleton.getGameState() == -999) {
            if (input.wasPressed(Keys.LEFT)) {
                scale -= 0.01;
            }
            else if (input.wasPressed(Keys.RIGHT)) {
                scale += 0.01;
            }
            ImagePoint test = new ImagePoint("res/renders/Characters/NAO TOMORI.png", new Point(0,0));
            test.setScale(scale);
            test.draw();
        }

        if (!canInteract) {
            input = null;
        }
        for (Button button: buttons) {
            if (input != null) {
                button.toggleHover(input.getMousePosition());
            }
            if (input != null && input.wasPressed(MouseButtons.LEFT)) {
                if (button.isHovering()) {
                    musicPlayer.addMusic("music/Click.wav");
                    button.playAction();
                }
            }
        }

        if (settingsSingleton.getGameState() == 0) {
            if (!"Main Menu".equals(settingsSingleton.getGameStateString())) {
                settingsSingleton.setGameStateString("Main Menu");
                musicPlayer.clear();
                buttons.clear();
                buttons.addAll(Arrays.asList(
                        new Button("PLAY", null,
                                new FontSize(Fonts.DEJAVUSANS, 160),
                                new Rectangle(0, 280, Window.getWidth(), 160),
                                ColourPresets.WHITE.toColour()),
                        new Button("TUTORIAL", null,
                                new FontSize(Fonts.DEJAVUSANS, 160),
                                new Rectangle(0, 280 + 160, Window.getWidth(), 160),
                                ColourPresets.WHITE.toColour()),
                        new Button("CREATE MAP", null,
                                new FontSize(Fonts.DEJAVUSANS, 160),
                                new Rectangle(0, 280 + 320, Window.getWidth(), 160),
                                ColourPresets.WHITE.toColour()),
                        new Button("EXIT", null,
                                new FontSize(Fonts.DEJAVUSANS, 160),
                                new Rectangle(0, 280 + 3*160, Window.getWidth(),
                                        160),
                                ColourPresets.WHITE.toColour())
                ));
                menuTitle = "THE CLIMB";
                imagePointManagerSingleton.getImages().clear();
                imagePointManagerSingleton.setCurrentBackground("res/menu/MainMenu.PNG");
                musicPlayer.setMainMusic("music/Giorno.wav");
            }
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
                System.out.println(settingsSingleton.getPlayers());
                imagePointManagerSingleton.setCurrentBackground(null);
                menuTitle = "";
                if (settingsSingleton.getGameMode() == 1) {
                    buttons.add(new Button("Game Settings", new Image("res/settings.png"), new Point(10,10)));
                }
                int index = 0;
                double minScale = 0.275;
                double maxScale = 1;
                int middleIndex = allCharacters.size() % 2 == 1 ?
                        allCharacters.size() / 2 + 1 : allCharacters.size() / 2;
                double spacing = Window.getHeight()/4;

                for (Character character: allCharacters) {
                    ImagePoint characterRender = new ImagePoint(String.format("res/renders/Characters/%s.png", character.getFullName()),
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
            Player currentPlayer = players.get(0);
            for (Player player: players) {
                if (player.getCharacter() == null) {
                    currentPlayer = player;
                    break;
                }
            }

            Character currentCharacter = allCharacters.get(allCharacters.size() % 2 == 1 ?
                    allCharacters.size() / 2 + 1 : allCharacters.size() / 2);
            imagePointManagerSingleton.setCurrentBackground((String.format("res/background/%s.png", currentCharacter.getFullName())));

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
                eventsListener.addEvent(new EventCharactersPicked(3 * frames, "All players have picked a character"));
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(2);
                for (Player player: players) {
                    player.reset();
                }
                // remove p3 and p4
            }

        }
        else if (settingsSingleton.getGameState() == 4) {
            if (!settingsSingleton.getGameStateString().equals("SideCharacter")) {
                settingsSingleton.setGameStateString("SideCharacter");
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                imagePointManagerSingleton.getImages().clear();
                int index = 0;
                double minScale = 0.275;
                double maxScale = 1;
                int middleIndex = allSideCharacters.size() % 2 == 1 ?
                        allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2;
                double spacing = Window.getHeight()/4;

                for (SideCharacter character: allSideCharacters) {
                    ImagePoint characterRender = new ImagePoint(String.format("res/renders/SideCharacters/%s.png", character.getName()),
                            new Point(0, 0), "CharacterRender");
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
            Player currentPlayer = players.get(0);
            for (Player player: players) {
                if (player.getCharacter() == null) {
                    currentPlayer = player;
                    break;
                }
            }

            SideCharacter currentCharacter = allSideCharacters.get(allSideCharacters.size() % 2 == 1 ?
                    allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2);
            imagePointManagerSingleton.setCurrentBackground((String.format("res/background/%s.png", currentCharacter.getName())));

            if (input != null && input.isDown(Keys.RIGHT)) {
                eventsListener.addEvent(new EventSideCharacterRotate(frames/8, "Rotate LEFT"));
                rotateSideCharacter("LEFT");
            } else if (input != null && input.isDown(Keys.LEFT)) {
                eventsListener.addEvent(new EventSideCharacterRotate(frames/8, "Rotate RIGHT"));
                rotateSideCharacter("RIGHT");
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
                eventsListener.addEvent(new EventCharactersPicked(3 * frames, "All players have picked a side character"));
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(3);
                for (Player player: players) {
                    player.setSideCharacter(null);
                    player.setCharacter(null);
                }
            }
        }
        else if (settingsSingleton.getGameState() == 5) { // Map
            if (!settingsSingleton.getGameStateString().equals("MAP")) {
                buttonsToRemove.addAll(buttons);
                menuTitle = "Which Climb?";
                imagePointManagerSingleton.setCurrentBackground(null);
                settingsSingleton.setGameStateString("MAP");
            }
            ArrayList<Map> mapsChosen = new ArrayList<>();
            for (Player player : players) {
                player.getCharacter().setPosition(player.getCursorPos());
                player.setCursorPos(input);
                int spacer = 300;
                int row = 1;
                int currentIcon = 2;
                for (Map map : playableMaps) {
                    if (map.getMapPeek().getBoundingBoxAt(new Point(-425 + map.getMapPeek().getWidth() * currentIcon +
                            (row - 1) *400, -50 + spacer * row)).intersects(player.getCursorPos())) {
                        if(input != null && input.wasPressed(player.getControl("Primary"))) {
                            pickMap(player, map);
                        }
                    }
                    currentIcon++;
                    if (currentIcon > 5) {
                        currentIcon = 1;
                        if (row < 2) {
                            row++;
                        }
                    }
                }
            }
            boolean allPlayersChoseMap = true;
            for (Player player: players) {
                if (player.getMapChosen() == null) {
                    allPlayersChoseMap = false;
                    break;
                }
                else {
                    if (!mapsChosen.contains(player.getMapChosen())) {
                        mapsChosen.add(player.getMapChosen());
                    }
                }
            }
            if (allPlayersChoseMap) {
                int playerMap = (int) Math.round(Math.random()*(players.size()-1)) + 1;
                for (Player player: players) {
                    if (player.getId() == playerMap) {
                        for (Map mapChosen: playableMaps) {
                            if (mapChosen.getName().equals(player.getMapChosen().getName())) {
                                map = mapChosen;
                                map.generateMap();
                                gameSettingsSingleton.setMap(map);
                                break;
                            }
                        }
                        break;
                    }
                }
                settingsSingleton.setGameState(6);
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                for (Player player : players) {
                    player.setMapChosen(null);
                    player.setSideCharacter(null);
                    settingsSingleton.setGameState(4);
                }
            }
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                if (!settingsSingleton.getGameStateString().equals("Game")) {
                    buttonsToRemove.addAll(buttons);
                    setPlayersPosition();
                    //musicPlayer.setMainMusic("music/Giorno.wav");
                    musicPlayer.setMainMusic(String.format("music/Fight%d.wav", Math.round(Math.random()*3)));
                    musicPlayer.clear();
                    musicPlayer.addMusic("music/Start.wav");
                    settingsSingleton.setPlayers(players.size());
                    settingsSingleton.setGameStateString("Game");
                    startCountdown();
                }
                if (!(countDown < 3 * frames)) {
                    if(!map.hasFinished()) {
                        if(!playingAnimation) {
                            musicPlayer.setMainVolume(musicPlayer.getMaxMainVol());
                            spawnObstacles();
                            spawnPowerUps();
                        }
                    }
                    else {
                        // when top is reached, first one to touch top wins
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
                    playingAnimation = false;
                    boolean playingActivation = false;
                    boolean theWorld = false;
                    for (Player player: players) {
                        if ("Yugi".equals(player.getSideCharacter().getName())) {
                            player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                        }
                        else {
                            if (player.getCharacter().hasSpecialAbility()) {
                                if (input != null && input.wasPressed(player.getControl("Primary"))) {
                                    if (!player.getSideCharacter().isActivating()) {
                                        player.getCharacter().useSpecialAbility();
                                        player.getSideCharacter().activateAbility(player, players, obstacles, powerUps,
                                                map);
                                        musicPlayer.addMusic(player.getSideCharacter().getSoundPath());
                                    }
                                }
                            }
                            if (player.getSideCharacter().isActivating()) {
                                musicPlayer.setMainVolume(0);
                                playingActivation = true;
                                if (Arrays.asList("Jotaro", "Dio").contains(player.getSideCharacter().getName())) {
                                    theWorld = true;
                                }
                            }
                        }
                        if (player.getSideCharacter().isAnimating()) {
                            musicPlayer.setMainVolume(0);
                            playingAnimation = true;
                        }
                    }
                    if ((!playingActivation) && (!playingAnimation)) {
                        musicPlayer.setMainVolume(1);
                    }
                    if (!playingAnimation) {
                        updateExp();
                        if (!theWorld) {
                            updatePlayerMovement(input);
                            updateObjects();
                            if (!map.hasFinished()) {
                                map.updateTiles(gameSettingsSingleton.getMapSpeed());
                            }
                        }
                        else {
                            for (Player player: players) {
                                if (player.getSideCharacter().isActivating() &&
                                        Arrays.asList("Jotaro", "Dio").contains(player.getSideCharacter().getName())) {
                                    player.moveCharacter(input);
                                }
                            }
                        }
                    }
                    updateAbilities();
                    checkCollisionPowerUps();
                    checkCollisionObstacles();
                    checkCollisionTiles();
                    if (!settingsSingleton.getGameStateString().equals("Win")) {
                        int deathCounter = 0;
                        for (Player player : players) {
                            if (player.getCharacter().isDead()) {
                                deathCounter++;
                            }
                        }
                        if (settingsSingleton.getPlayers() - deathCounter < 2) {
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
//                    map = mapToTransitionTo;
//                    if (map != null) {
//                        map.generateMap();
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
//                                if(!map.hasFinished()) {
//                                    map.updateTiles(1);
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        //mapToTransitionTo = new MapTrainingGround2();
//                                        map = mapToTransitionTo;
//                                        map.generateMap();
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
//                                if(!map.hasFinished()) {
//                                    map.updateTiles(0.8);
//                                    displayCharacterStats(players);
//                                }
//                                else {
//                                    if (playersPassed()) {
//                                        map.generateMap();
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
//                                if(!map.hasFinished()) {
//                                    map.updateTiles(0.8);
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
//                                if(!map.hasFinished()) {
//                                    map.updateTiles(0.8);
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
//                        map = null;
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
            if (settingsSingleton.getGameMode() == 0) {
                if (settingsSingleton.getGameStateString().equals("Continue")) {
                    musicPlayer.restart(null);
                    buttonsToRemove.addAll(buttons);
                    settingsSingleton.setGameState(6);
                    map.generateMap();
                    for (Player player : players) {
                        player.getCharacter().reset();
                    }
                    setPlayersPosition();
                    powerUps.removeAll(powerUps);
                    obstacles.removeAll(obstacles);
                }
                else if (settingsSingleton.getGameStateString().equals("Menu")) {
                    settingsSingleton.setGameState(0);
                    for (Player player : players) {
                        player.getCharacter().reset();
                        player.reset();
                    }
                    setPlayersPosition();
                    powerUps.removeAll(powerUps);
                    obstacles.removeAll(obstacles);
                }
            }
            else {
                if (settingsSingleton.getGameStateString().equalsIgnoreCase("Game Finished")) {
                    settingsSingleton.setGameStateString("Retry or Menu?");
                    musicPlayer.setMainMusic("music/Fail.wav");
                    stats.updateGameStats(players);
                    musicPlayer.addMusic(settingsSingleton.getWinner().getCharacter().playLine());
                    buttonsToRemove.addAll(buttons);
                    buttons.add(new Button("Back To Start", "Main Menu",
                            new FontSize(Fonts.DEJAVUSANS, 160),
                            new Rectangle(0, Window.getHeight() / 1.5 + 160, Window.getWidth(), 160),
                            ColourPresets.WHITE.toColour()));
                    buttons.add(new Button("Retry", "Restart",
                            new FontSize(Fonts.DEJAVUSANS, 160),
                            new Rectangle(0, Window.getHeight() / 2 + 160, Window.getWidth(), 160),
                            ColourPresets.WHITE.toColour()));
                }
                else if (Arrays.asList("Retry", "Menu").contains(settingsSingleton.getGameStateString())) {
                    if (settingsSingleton.getGameStateString().equals("Retry")) {
                        buttonsToRemove.addAll(buttons);
                        map.generateMap();
                        settingsSingleton.setGameState(6);
                        for (Player player: players) {
                            player.getCharacter().reset();
                            player.getSideCharacter().reset();
                        }
                    } else if (settingsSingleton.getGameStateString().equals("Menu")) {
                        for (Player player : players) {
                            player.reset();
                        }
                        map = null;
                        unlocked = checkAchievementsInGame();
                        if (unlocked != null) {
                            settingsSingleton.setGameState(8);
                        } else {
                            settingsSingleton.setGameState(0);
                        }
                    }
                    musicPlayer.clear();
                    buttonsToRemove.addAll(buttons);
                    obstacles.removeAll(obstacles);
                    powerUps.removeAll(powerUps);
                    stats.saveStats();
                }
            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            String musicWho = "music/Who.wav";

            if (!settingsSingleton.getGameStateString().equals("Unlocked")) {
                musicPlayer.clear();
                musicPlayer.setMainMusic("music/Silence.wav");
                musicPlayer.addMusic(new Music(musicWho));
                settingsSingleton.setGameStateString("Unlocked");
            }
            if (musicPlayer.getSound(musicWho) != null && musicPlayer.getSound(musicWho).hasEnded()) {
                musicPlayer.setMainMusic(String.format("music/%sUnlock.wav", unlocked));
                musicPlayer.addMusic(String.format("music/%sVoice.wav", unlocked));
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("NEW CHARACTER UNLOCKED: %s!", unlocked), 0, 100);
                ImagePoint unlockedImage = new ImagePoint(String.format("res/renders/Characters/%s.png", unlocked),
                        new Point(Window.getWidth()/2, Window.getHeight()/2 + 300));
                unlockedImage.draw();
                if (input != null && input.wasPressed(MouseButtons.LEFT)) {
                    musicPlayer.clear();
                    musicPlayer.setMainMusic("music/Fail.wav");
                    settingsSingleton.setGameState(0);
                }
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.BLACK.toColour());
            }
        }
        else if (settingsSingleton.getGameState() == 9) {
            if (!settingsSingleton.getGameStateString().equals("Create Your Own Map")) {
                map = new Map("Custom");
                map.generateMap();
                customMapTiles.removeAll(customMapTiles);
                for (Tile tile: map.getTiles()) {
                    customMapTiles.add(tile);
                }
                buttonsToRemove.addAll(buttons);
                settingsSingleton.setGameStateString("Create Your Own Map");
                imagePointManagerSingleton.setCurrentBackground(null);
                menuTitle = null;
            }

            if (!addingTile) {
                if (input != null && input.wasPressed(Keys.UP)) {
                    updateTiles(100);
                    offset++;
                }
                if (input != null && input.wasPressed((Keys.DOWN))) {
                    if (offset > 0) {
                        updateTiles(-100);
                        offset--;
                    }
                }
                if (input != null && input.wasPressed(Keys.S)) {
                    if (customMapTiles.size()%4 != 0) {
                        addDisplayString("Error: Cannot save map. Map not complete! (Row is not filled)", 5);
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
                gameSettingsSingleton.setPage(0);
                settingsSingleton.setGameStateString("Game Settings");
                buttonsToRemove.addAll(buttons);
                buttons.add(new Button("Left Arrow",
                        new Image("res/arrows/LeftArrow.png"),
                        new Point(600, 0),
                        0.5));
                buttons.add(new Button("Right Arrow",
                        new Image("res/arrows/RightArrow.png"),
                        new Point(Window.getWidth() - 750, 0),
                        0.5));
                buttons.add(new Button("Decrease Map Speed",
                        new Image("res/arrows/LeftArrow.png"),
                        new Point(Window.getWidth()/2 + 30, 200),
                        0.5));
                buttons.add(new Button("Increase Map Speed",
                        new Image("res/arrows/RightArrow.png"),
                        new Point(Window.getWidth()/2 + 130, 200),
                        0.5));
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300 + 100);
            }
        }
        else if (settingsSingleton.getGameState() == 11) {
            if (!settingsSingleton.getGameStateString().equals("Tutorial")) {
                settingsSingleton.setGameStateString("Tutorial");
                map = new Map("Training Ground");
                map.generateMap();

            }
            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(6);
            }
        }

        currentMousePosition = input == null ? null : input.getMousePosition();
        musicPlayer.update();
        updateDisplayStrings();
        updateTime();
        for (Slider slider: sliders) {
            slider.interact(input);
        }
        updateButtons();
        updateSliders();
        render();
        eventsListener.updateEvents();
        canInteract = eventsListener.canInteract();

        // TEST GAME
        if (settingsSingleton.getGameState() == -100) {
            players.get(0).setCharacter(new Character(CharacterNames.MIKU));
            players.get(0).setSideCharacter(new SideJotaro());
            players.get(0).getCharacter().gainSpecialAbilityBar(500);
            players.get(1).setCharacter(new Character(CharacterNames.MAI));
            players.get(1).setSideCharacter(new SideDio());
            players.get(1).getCharacter().gainSpecialAbilityBar(500);
            settingsSingleton.setPlayers(players.size());
            map = new Map("Spooky Spikes");
            map.generateMap();
            gameSettingsSingleton.setMap(map);
            settingsSingleton.setGameMode(1);
            settingsSingleton.setGameState(6);
            settingsSingleton.setGameStateString("Test");

        }
    }

    public void render() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.BLACK.toColour());
        if (settingsSingleton.getGameState() == -1 ) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.WHITE.toColour());
        }
        if (settingsSingleton.getGameState() == 0) {
            imagePointManagerSingleton.getCurrentBackground().draw();
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
            if (isUnlocked(currentCharacter.getName())) {
                FontSize characterFont = new FontSize(Fonts.TCB, 120);
                characterFont.draw(currentCharacter.getName(), Window.getWidth()/5
                        - characterFont.getFont().getWidth(currentCharacter.getName())/2, Window.getHeight()/2);
                characterFont.draw(currentCharacter.getLastName(), Window.getWidth()*4/5
                        - characterFont.getFont().getWidth(currentCharacter.getLastName())/2, Window.getHeight()/2);
            }
            if (isPickable(currentCharacter)) {
                imagePointManagerSingleton.get(String.format("res/renders/Characters/%s.png", currentCharacter.getFullName())).setTransparent(false);
            }
            else {
                imagePointManagerSingleton.get(String.format("res/renders/Characters/%s.png", currentCharacter.getFullName())).setTransparent(true);
            }
            drawBorders(currentCharacter);

            imagePointManagerSingleton.drawCharacterSelection();
        }
        else if (settingsSingleton.getGameState() == 4 ) {
            SideCharacter currentCharacter = allSideCharacters.get(allSideCharacters.size() % 2 == 1 ?
                allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2);

            imagePointManagerSingleton.getCurrentBackground().draw();
            Drawing.drawRectangle(new Point(0,0), Window.getWidth(), Window.getHeight()/4 - 30, new Colour(0,0,0,0.8));
            Drawing.drawRectangle(new Point(0,Window.getHeight()*3/4), Window.getWidth(), Window.getHeight()/4, new Colour(0,0,0,0.8));

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
            if (isPickable(currentCharacter)) {
                imagePointManagerSingleton.get(String.format("res/renders/SideCharacters/%s.png", currentCharacter.getName())).setTransparent(false);
            }
            else {
                imagePointManagerSingleton.get(String.format("res/renders/SideCharacters/%s.png", currentCharacter.getName())).setTransparent(true);
            }
            drawBorders(currentCharacter);

            imagePointManagerSingleton.drawCharacterSelection();
        }
        else if (settingsSingleton.getGameState() == 5) {
            int spacer = 400;
            int row = 1;
            int currentIcon = 1;
            for (Map map : playableMaps) {
                map.getMapPeek().draw(spacer * currentIcon, -50 + spacer * row);
                currentIcon++;
                if (currentIcon >= 5) {
                    currentIcon = 1;
                    if (row < 2) {
                        row++;
                    }
                }
            }
            row = 1;
            currentIcon = 2;
            for (Map map : playableMaps) {
                for (Player player: players) {
                    if (!map.getMapPeek().getBoundingBoxAt(new Point(-425 + map.getMapPeek().getWidth() * currentIcon + (row - 1) *400, -50 + spacer * row)).intersects(player.getCursorPos())) {
                        if(player.getMapChosen() != null) {
                            new Font(Fonts.DEJAVUSANS, 40).drawString(String.format("P%d: %s", player.getId(),
                                    player.getMapChosen().getName()),
                                    (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1),
                                    Window.getHeight() - 15, DO.setBlendColour(ColourPresets.WHITE.toColour()));
                        }
                    }
                    else {
                        if(player.getMapChosen() == null) {
                            new Font(Fonts.DEJAVUSANS, 40).drawString(String.format("P%d: %s", player.getId(),
                                    map.getName()),
                                    (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1),
                                    Window.getHeight() - 15,
                                    DO.setBlendColour(Colour.WHITE));
                        }
                    }
                    if (player.getCharacter().getPos() != null) {
                        player.getCharacter().draw();
                    }
                }
                currentIcon++;
                if (currentIcon > 5) {
                    currentIcon = 1;
                    if (row < 2) {
                        row++;
                    }
                }
            }
            for (int i = playableMaps.size(); i < 8; i++) {
                ImagePoint locked = new ImagePoint("res/mapPeeks/locked.png", new Point(spacer * currentIcon, -50 + spacer * row));
                locked.draw();
                currentIcon++;
                if (currentIcon > 5) {
                    currentIcon = 1;
                    if (row < 2) {
                        row++;
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                menuTitle = null;
                drawGame();
                if (countDown < 3 * frames) {
                    Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, (390.0 - countDown)/390.0));
                    String countString;
                    if (countDown < 150) {
                        countString = "3..";
                    } else if (countDown < 270) {
                        countString = "2..";
                    } else if (countDown < 390) {
                        countString = "1..";
                    } else {
                        countString = "GO!";
                    }
                    new Font(Fonts.CONFORMABLE, 300).drawString(String.format("%s", countString), Window.getWidth() / 2 - 125, Window.getHeight() / 2);
                    countDown++;
                }
                else {
                    if(!map.hasFinished()) {
                        if(!playingAnimation) {
                            drawCollisionBoundaries();
                            displayCharacterStats(players);
                        }
                    }
                    else {
                        if (!playingAnimation) {
                            new Font(Fonts.DEJAVUSANS, 100).drawString("CLEAR! REACH THE TOP!", 16, 160, DO.setBlendColour(ColourPresets.BLACK.toColour()));
                        }
                    }
                    renderAbilities();
                }
            }
//            else {
//                currentBackground.draw();
//                if (dark) {
//                    darken();
//                }
//                if ((shakeTimer > 0) || (transitionTimer > 0)) {
//                    shakeImage();
//                    transition();
//                }
//            }
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
                Image characterImage = new Image(String.format("res/renders/Characters/%s.png", winner.getCharacter().getName()));
                Image sideCharacterImage = new Image(String.format("res/renders/SideCharacters/%s.png", winner.getSideCharacter().getName()));
                characterImage.draw(Window.getWidth() - winnerTimer, Window.getHeight()/2 +200);
                sideCharacterImage.draw(Window.getWidth() + characterImage.getWidth()/2, Window.getHeight()/2 + 200);
                new Font(Fonts.DEJAVUSANS, 110).drawString(String.format("Player %d: %s is victorious!", settingsSingleton.getWinner().getId(), settingsSingleton.getWinner().getCharacter().getName()), 16, 100);

            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            Font chooseCharacterFont = new Font(Fonts.DEJAVUSANS, 100);
            Colour black = new Colour(0, 0, 0,
                    (float) (musicPlayer.getSound("music/Who.wav").getClip().getFrameLength() -
                            musicPlayer.getMainMusic().getClip().getFramePosition()) /
                            (float) musicPlayer.getSound("music/Who.wav").getClip().getFrameLength());
            imagePointManagerSingleton.setCurrentBackground(String.format("res/background/%s.png", unlocked));
            if (musicPlayer.getSound("music/Who.wav").hasEnded()) {
                chooseCharacterFont.drawString(String.format("NEW CHARACTER UNLOCKED: %s!", unlocked), 0, 100);
                ImagePoint unlockedImage = new ImagePoint(String.format("res/renders/Characters/%s.png", unlocked), new Point(Window.getWidth()/2, Window.getHeight()/2 + 300));
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
                playerFont.drawString("S: Save and Exit ESC: Exit without Saving Arrow Keys: Navigate\nA: Add, R: Remove last block", 100, 50);
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
            imagePointManagerSingleton.setCurrentBackground(null);
            Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0, 0.2));

            if (gameSettingsSingleton.getPage() == 0) {
                menuTitle = "General";
                titleFont.drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300 + 0);
            }
            else if (gameSettingsSingleton.getPage() == 1 || gameSettingsSingleton.getPage() == 2) {
                if (gameSettingsSingleton.getPage() == 1) {
                    menuTitle = "PowerUps";
                }
                else if (gameSettingsSingleton.getPage() == 2) {
                    menuTitle = "Obstacles";
                }
                gameFont.drawString("Drag the slider across to increase/decrease the spawn rate!", titleFont.getWidth("SETTINGS"), 150, new DrawOptions().setBlendColour(0,0,0, 0.7));
                gameFont.drawString("Click the icon to toggle on/off", titleFont.getWidth("SETTINGS"), 190, new DrawOptions().setBlendColour(0,0,0, 0.7));
            }
        }
        else if (settingsSingleton.getGameState() == 11) {
            Font playerFont = new Font(Fonts.DEJAVUSANS, 40);
            drawGame();
            playerFont.drawString("PAUSE", (Window.getWidth() - playerFont.getWidth("PAUSE"))/2, Window.getHeight()/2 - 50);
            playerFont.drawString("Press ESC to resume", (Window.getWidth() - playerFont.getWidth("Press ESC to res"))/2, Window.getHeight()/2);
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
        characters.add(new Character(name));
    }

    public boolean isPlayable(String name) {
        for (Character character: characters) {
            if (character.getName().equals(name)) {
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
                    achievementLine.remove(0);
                }
                else {
                    newCharacter = achievementLine.get(0);
                    achievementLine.remove(0);
                }
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
                String[] achievementLine = achievementScanner.nextLine().split(" ");
                String newCharacterName = achievementLine[0];
                String name = achievementLine[1];
                int threshold = Integer.parseInt(achievementLine[3]);
                if (stats.hasPassedThreshold(name, threshold)) {
                    if (!isPlayable(newCharacterName)) {
                        addToPlayableCharacter(newCharacterName);
                        return newCharacterName;
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
            for (Slider slider: sliders) {
                slider.draw();
            }
        }
    }

    public void drawBorders(Character currentCharacter) {
        Image border = new Image("res/Selected/Selected.png");

        double spacing = Window.getWidth()/6;

        FontSize playerDesc = new FontSize(Fonts.TCB, 30);

        for (int i = 0; i < settingsSingleton.getPlayers(); i++) {
            Player currentPlayer = players.get(i);
            Character playerCharacter = currentPlayer.getCharacter();
            double timeSpace = i > 1 ? 200 : 0;
            border.drawFromTopLeft(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0);
            String name;
            if (currentPlayer.getId() == getIDOfPlayerPicking()) {
                name = String.format("P%d\nSelecting...", currentPlayer.getId());
                ImagePoint characterPeek = new ImagePoint(String.format("res/characters/%s/peek.png", currentCharacter.getFullName()),
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
        Image border = new Image("res/Selected/Selected.png");

        double spacing = Window.getWidth()/6;

        FontSize playerDesc = new FontSize(Fonts.TCB, 30);

        for (int i = 0; i < settingsSingleton.getPlayers(); i++) {
            Player currentPlayer = players.get(i);
            SideCharacter playerCharacter = currentPlayer.getSideCharacter();
            double timeSpace = i > 1 ? 200 : 0;
            border.drawFromTopLeft(i * ((border.getWidth()*2/3) + spacing) + spacing/3 + timeSpace, 0);
            String name;
            if (currentPlayer.getId() == getIDOfPlayerPicking()) {
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

            stringDisplays.add(new StringDisplay("Story saved successfully.", 3, 50, new Point(0,0)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCountdown() {
        countDown++;
    }

    public void saveCustomMap() {
        String tempFile = "res/mapData/Temp.txt";
        String currentFile = "res/mapData/Custom.txt";
        File oldFile = new File(currentFile);
        File newFile = new File(tempFile);
        try {

            FileWriter fw = new FileWriter(tempFile, false);
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
            oldFile.delete();
            File dump = new File(currentFile);
            newFile.renameTo(dump);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeBackground(String filename) {
        imagePointManagerSingleton.setCurrentBackground(filename);
    }

    public void displayCharacterStats(ArrayList<Player> players) {
        int playerIndex = 0;
        for (Player player: players) {
            ImagePoint characterDisplay = new ImagePoint(String.format("res/InGame/%s.png", player.getCharacter().getName()), new Point(0,0));
            characterDisplay.setPos(playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - characterDisplay.getHeight());
            characterDisplay.setScale(1);
            Colour border = new Colour(0, 0, 0, 0.3);
            if (player.getCharacter().getSpecialAbilityBar() >= 100) {
                border = new Colour(255, 255, 0, 0.5);
            }
            if (player.getCharacter().isMinimised()) {
                characterDisplay.setScale(0.5);
            }

            Drawing.drawRectangle(characterDisplay.getWidth() + playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - characterDisplay.getHeight(), 200, characterDisplay.getHeight(), border);
            characterDisplay.draw();

            if (player.getCharacter().hasShield()) {
                ImagePoint shield = new ImagePoint("res/InGame/Shield_Selected.png",
                        new Point(characterDisplay.getWidth()/2 + playerIndex*Window.getWidth()/(players.size()),
                                characterDisplay.getHeight()/2 + Window.getHeight() - characterDisplay.getHeight()));
                shield.draw();
            }

            if (!player.getSideCharacter().getName().equals("Yugi")) {
                ImagePoint sideCharacter = new ImagePoint(String.format("res/inGame/%s.png", player.getSideCharacter().getName()),
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

    public void pickCharacter(Player player, Character character) {
        if (isUnlocked(character.getName())) {
            if (isPickable(character)) {
                eventsListener.addEvent(new EventCharacterPicked( 1*frames, String.format("Player %d picked: %s", player.getId(), character.getFullName())));
                musicPlayer.addMusic("music/Character Picked.wav");
                player.setCharacter(character);
                if (!musicPlayer.contains(character.playLine())) {
                    musicPlayer.addMusic(character.playLine());
                } else {
                    musicPlayer.restart(character.playLine());
                }
            }
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

    public void pickSideCharacter(Player player, SideCharacter character) {
        boolean pickable = true;
        if (player.getSideCharacter() == null) {
            for (Player otherPlayer : players) {
                if (otherPlayer.getId() != player.getId()) {
                    if (otherPlayer.getSideCharacter() != null) {
                        if (otherPlayer.getSideCharacter().getName().equals(character.getName())) {
                            pickable = false;
                        }
                    }
                }
            }
            if (pickable) {
                player.setSideCharacter(character);
                if (!musicPlayer.contains(player.getSideCharacter().getSoundPath())) {
                    musicPlayer.addMusic(character.getSoundPath());
                }
                else {
                    musicPlayer.remove(player.getSideCharacter().getSoundPath());
                    musicPlayer.restart(character.getSoundPath());
                }
            }
        }
        else {
            player.setSideCharacter(null);
        }
    }

    public void pickMap(Player player, Map map) {
        if (player.getMapChosen() == null) {
            musicPlayer.addMusic("music/Click.wav");
            player.setMapChosen(map);
        }
    }

    public void drawCurrentHeight() {
        Font gameFont = new FontSize(Fonts.DEJAVUSANS, 40).getFont();
        if (!settingsSingleton.isNight()) {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(ColourPresets.WHITE.toColour()));
        }
        else {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
        }
    }

    public void checkCollisionTiles() {
        for (Player player: players) {
            if (!map.hasFinished()) {
                Rectangle bottomRectangle = new Rectangle(0, Window.getHeight() + 40, Window.getWidth(), 20);
                if ((player.getCharacter().getRectangle().intersects(bottomRectangle))) {
                    player.getCharacter().setDead(true);
                    break;
                }
            }
            for (Tile tile : map.getVisibleTiles()) {
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
                            kill(player);
                        }
                    }
                }
            }
        }
    }

    public void kill(Player player) {
        player.getCharacter().setDead(true);
    }

    public void drawCollisionBoundaries() {
        Drawing.drawRectangle(0, Window.getHeight() - 20, Window.getWidth(), 20, ColourPresets.RED.toColour());
    }

    public void updatePlayerMovement(Input input) {
        for (Player player: players) {
            Character character = player.getCharacter();
            if (!player.getCharacter().isDead()) {
                if (character.canMove()) {
                    player.moveCharacter(input);
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
            player.getCharacter().updateCharacter();
        }
    }

    public void updateAbilities() {
        for (Player player: players) {
            if (player.getSideCharacter().isActivating()) {
                player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
            }
        }
    }

    public void renderAbilities() {
        for (Player player: players) {
            if (player.getSideCharacter().isActivating()) {
                player.getSideCharacter().renderAbility();
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
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Special Ability")) {
                if (Math.random() < gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Special Ability")) {
                    powerUps.add(new PowerUpSpecialAbilityPoints());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isRocks()) {
            if (Math.random() < gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("Rock")) {
                obstacles.add(new ObstacleRock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isBalls()) {
            if (Math.random()
                    < gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("Ball")) {
                obstacles.add(new ObstacleBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isStunBalls()) {
            if (Math.random()
                    < gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("StunBall")) {
                obstacles.add(new ObstacleStunBall());
            }
        }
    }

    public void drawGame() {
        if (map != null) {
            map.draw();
        }
        for (Player player : players) {
            if (!player.getCharacter().isDead()) {
                player.getCharacter().draw();
            }
            Font gameFont = new Font(Fonts.DEJAVUSANS, 40);
            gameFont.drawString(String.format("P%s", player.getId()), player.getCharacter().getPos().x, player.getCharacter().getPos().y + 100);
        }
        for (PowerUp powerUp: powerUps) {
            powerUp.getImage().drawFromTopLeft(powerUp.getPos().x, powerUp.getPos().y);
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
        }
        if (map.hasFinished()) {
            if (settingsSingleton.getGameState() == 6) {
                Font titleFont = new Font(Fonts.DEJAVUSANS, 100);
                titleFont.drawString("CLEAR! REACH THE TOP!", 16, 160, DO.setBlendColour(ColourPresets.BLACK.toColour()));
            }
        }
        else {
            drawCurrentHeight();
        }
    }

    public void displayFailScreen() {
        buttonsToRemove.addAll(buttons);
        buttons.add(new Button("Retry", "Continue?",
                new FontSize(Fonts.DEJAVUSANS, 160),
                new Rectangle(0, Window.getHeight() / 2, Window.getWidth(), 160),
                ColourPresets.WHITE.toColour()));
        buttons.add(new Button("Back To Start", "Exit",
                new FontSize(Fonts.DEJAVUSANS, 160),
                new Rectangle(0, Window.getHeight() / 1.5, Window.getWidth(), 160),
                ColourPresets.WHITE.toColour()));
        musicPlayer.setMainMusic("music/Fail.wav");

        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.7));
        Font victoryFont = new Font(Fonts.DEJAVUSANS, 110);
        victoryFont.drawString("YOU FAILED THE CLIMB!", 30, 110);
    }

    public void addTileToCustomMap(String line) {
        int currentRow = customMapTiles.size()/4;
        int currentBlocksInRow = customMapTiles.size()%4;
        Point point = new Point(480 * currentBlocksInRow, 100*offset + 600 + -475 * currentRow);

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

    public void showDisplayStrings() {
        int i = 1;
        for (StringDisplay stringDisplay: stringDisplays) {
            stringDisplay.draw();
            FontSize gameFont = new FontSize(Fonts.DEJAVUSANS, 40);
            gameFont.getFont().drawString(stringDisplay.getName(), Window.getWidth() - gameFont.getFont().getWidth(stringDisplay.getName()), 60*i);
        }
    }

    public void updateDisplayStrings() {
        ArrayList<StringDisplay> stringDisplaysToRemove = new ArrayList<>();
        for (StringDisplay stringDisplay: stringDisplays) {
            if (stringDisplay.getTime() < 0) {
                stringDisplaysToRemove.add(stringDisplay);
            }
            else {
                stringDisplay.update();
            }
        }
        stringDisplays.removeAll(stringDisplaysToRemove);
    }

    public void addDisplayString(String string, int time) {
        stringDisplays.add(new StringDisplay(string, time, 50, new Point(0,0)));
    }

    public void setPlayersPosition() {
        double spawnDivider = 1;
        for (Player player : players) {
            double width = player.getCharacter().getImage().getWidth();
            player.getCharacter().setPosition(new Point(Window.getWidth()/2 - (players.size() * width / 2)  + spawnDivider * width, Window.getHeight() - 200));
            spawnDivider++;
        }
    }

    public boolean playersPassed() {
        double playersFinished = 0;
        for (Player player : players) {
            if (player.getCharacter().getPos().distanceTo(new Point(player.getCharacter().getPos().x, 0)) < 10) {
                playersFinished++;
            }
        }
        return playersFinished >= players.size();
    }

    public void drawMapBackground() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1, 1, 1, 1));
        ArrayList<ImagePoint> tileBackground = new ArrayList<>();
        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            for (int i = 0; i < 16; i++) {
                tileBackground.add(new ImagePoint("res/Tiles/BasicTileNight.png", new Point(0,0)));
            }
        } else {
            for (int i = 0; i < 16; i++) {
                tileBackground.add(new ImagePoint("res/Tiles/BasicTile.png", new Point(0,0)));
            }
        }

        int amountInRow = 0;
        int rows = 1;
        for (ImagePoint image: tileBackground) {
            image.setPos(amountInRow * image.getWidth(), Window.getHeight() - (rows * image.getHeight()));
            image.draw();
            amountInRow++;
            if (amountInRow > 4) {
                amountInRow = 0;
                rows++;
            }
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

    public void updateSliders() {
        sliders.removeAll(slidersToRemove);
        slidersToRemove.clear();
    }

    public void rotateCharacter(String direction) {
        musicPlayer.addMusic("music/CharacterNext.wav");
        if (direction.equals("LEFT")) {
            Character temp = allCharacters.get(allCharacters.size() - 1);
            allCharacters.remove(allCharacters.size() - 1);
            allCharacters.add(0, temp);
        }
        else if (direction.equals("RIGHT")) {
            Character temp = allCharacters.get(0);
            allCharacters.remove(0);
            allCharacters.add(temp);
        }
    }

    public void rotateSideCharacter(String direction) {
        musicPlayer.addMusic("music/CharacterNext.wav");
        if (direction.equals("LEFT")) {
            SideCharacter temp = allSideCharacters.get(allSideCharacters.size() - 1);
            allSideCharacters.remove(allSideCharacters.size() - 1);
            allSideCharacters.add(0, temp);
        }
        else if (direction.equals("RIGHT")) {
            SideCharacter temp = allSideCharacters.get(0);
            allSideCharacters.remove(0);
            allSideCharacters.add(temp);
        }
    }

    public boolean isUnlocked(String characterName) {
        for (Character character: characters) {
            if (character.getName().equals(characterName)) {
                return true;
            }
        }
        return false;
    }

    public int getIDOfPlayerPicking() {
        for (Player player: players) {
            if (player.getId() <= settingsSingleton.getPlayers() + 1) {
                if (player.getCharacter() == null) {
                    return player.getId();
                }
            }
        }
        return -1;
    }

    public void generateBalls() {
        if (Math.random() > 0.98) {

        }
    }

}