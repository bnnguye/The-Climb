import bagel.*;
import bagel.Font;
import bagel.Image;
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

    private final int FONT_SIZE = 160;
    private final int DIALOGUE_FONT_SIZE = 30;
    private final int MAX_DIALOGUE_LIMIT = Window.getWidth()/(DIALOGUE_FONT_SIZE - 10);

    private static final SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private static final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();
    private static final TimeLogger timeLogger = TimeLogger.getInstance();
    private static final ButtonsSingleton buttonsSingleton = ButtonsSingleton.getInstance();

    // Stat variable
    int[] statTracker = new int[4];

    private final double frames = settingsSingleton.getFrames();
    private int countDown = 0;
    private double waitTimer = 2 * frames;
    private int winnerTimer = 0;
    private int currentFrame = 0;

    private final ArrayList<StringDisplay> stringDisplays = new ArrayList<>();
    private final ArrayList<String> stringOnScreen = new ArrayList<>();

    private final Font titleFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 100);
    private final Font victoryFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 110);
    private final Font playerFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 50);
    private final Font gameFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
    private final Font chooseCharacterFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 100);
    private final Font countdownFont = new Font("res/fonts/conformable.otf", 300);
    private final Font dialogueFont = new Font("res/fonts/DejaVuSans-Bold.ttf", DIALOGUE_FONT_SIZE);
    private final Font introFont = new Font("res/fonts/Storytime.ttf", 80);
    private final DrawOptions DO = new DrawOptions();

    private double intro = 0;

    private final ArrayList<Button> buttons= buttonsSingleton.getButtons();
    private final ArrayList<Button> buttonsToRemove = buttonsSingleton.getButtonsToRemove();
    private final ArrayList<Button> buttonsToAdd = buttonsSingleton.getButtonsToAdd();
    private final ArrayList<Slider> sliders = buttonsSingleton.getSliders();
    private final ArrayList<Slider> slidersToRemove = buttonsSingleton.getSlidersToRemove();
    private final ArrayList<Character> characters= new ArrayList<>();
    private final ArrayList<Player> players= new Player(0).getInstance();
    private final ArrayList<Obstacle> obstacles= new ArrayList<>();
    private final ArrayList<Obstacle> obstaclesToRemove= new ArrayList<>();
    private final ArrayList<PowerUp> powerUps= new ArrayList<>();
    private final ArrayList<PowerUp> powerUpsToRemove= new ArrayList<>();
    private final ArrayList<Character> allCharacters= new ArrayList<>();
    private final ArrayList<SideCharacter> sideCharacters= new ArrayList<>();
    private final ArrayList<SideCharacter> allSideCharacters= new ArrayList<>();

    private final SideCharacter Zoro = new SideZoro();
    private final SideCharacter Gojo = new SideGojo();
    private final SideCharacter AllMight = new SideAllMight();
    private final SideCharacter Lelouch = new SideLelouch();
    private final SideCharacter Hisoka = new SideHisoka();
    private final SideCharacter Jotaro = new SideJotaro();
    private final SideCharacter Dio = new SideDio();
    private final SideCharacter Itachi = new SideItachi();
    private final SideCharacter Yugi = new SideYugi();
    private final SideCharacter Puck = new SidePuck();
    private final SideCharacter Yuu = new SideYuu();
    private final SideCharacter Senkuu = new SideSenkuu();

    private double spacer = 300;
    private String currentMusic = "music/Silence.wav";
    private final Music soundEffectMusic = new Music();
    private final Music mainMusic = new Music();
    private final ArrayList<Music> musics = new ArrayList<>();
    private boolean picked = false;
    private final Rectangle bottomRectangle = new Rectangle(0, Window.getHeight() + 40, Window.getWidth(), 20);

    // custom map variables
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


    private boolean winnerPlayed = false;
    private boolean playingAnimation = false;
    private Character unlocked;
    private String[] dialogueLine;
    private Scanner dialogueScanner;
    private ArrayList<String> dialogueWords= new ArrayList<>();

    private final double timeToSaveStats = 60 * frames;
    private int currentTimeToSaveStats = 0;

    //Story variables
    private boolean playingDialogue = false;
    private boolean playingStory = false;
    private boolean playingScene = false;
    private final boolean playingMap = false;
    private final boolean playingWorld = false;
    private int currentDialogue = 0;
    private int currentStory = 0;
    private int currentScene = 0;
    private int lastStory = -1;
    private int lastScene = -1;
    private Image currentBackground;
    private Point currentBackgroundPoint = new Point(0,0);
    private String dialogueString = "";
    private final Colour dialogueColour = new Colour(77.0/255, 57.0/255, 37.0/255, 0.7);
    private final double dialogueWidth = Window.getWidth()*0.1;
    private final double dialogueLength = Window.getHeight() - 300;
    private final int maxLines = 7;
    private int currentLines = 0;
    private int dialogueIndex = 0;
    private boolean alternate = false;
    private boolean playNextCharacter = true;
    private Character dialogueCharacter = null;
    private Character nextCharacter = null;
    private double shakeTimer = 0;
    private double transitionTimer = 0;
    private boolean dark = false;
    private int dialogueCounter = 0;
    private int keyboardTimer = 0;
    private boolean endDialogue = false;
    private String currentMode;
    private Map mapToTransitionTo;
    private Image sceneToTransitionTo;
    private boolean failed = false;

    private Image menuBackground;
    private String menuTitle = "";

    public final Colour black = new Colour(0, 0, 0);
    public final Colour white = new Colour(1, 1, 1);
    public Colour red = new Colour(153.0/255.0, 27.0/255.0, 0);
    public Colour darken = new Colour(0, 0, 0, 0.85);
    double gameModeOffset = 0;
    Point mousePosition;
    Point currentMousePosition;

    // Game Settings variables
    private final ArrayList<PowerUp> allPowerUps= new ArrayList<>();
    private final ArrayList<Obstacle> allObstacles= new ArrayList<>();
    private ArrayList<Slider> settingsSliders = new ArrayList<>();
    private String pageType;

    public static void main(String[] args) {
        new Game().run();
    }

    public Game() {
        super(1920, 1080, "The Climb");
        new Player(1);

        // initialize characters
        characters.add(new Character("Chizuru"));
        characters.add(new Character("02"));
        characters.add(new Character("Miku"));
        characters.add(new Character("Mai"));
        for (Character character: characters) {
            if (!allCharacters.contains(character)) {
                allCharacters.add(character);
            }
        }
        allCharacters.add(new Character("Nino"));
        allCharacters.add(new Character("Futaba"));
        allCharacters.add(new Character("Ruka"));
        allCharacters.add(new Character("Aki"));
        allCharacters.add(new Character("Chika"));
        allCharacters.add(new Character("Emilia"));
        allCharacters.add(new Character("Asuna"));
        allCharacters.add(new Character("Raphtalia"));
        for(Character character: allCharacters) {
            character.setStats();
        }

        sideCharacters.add(Zoro);
        sideCharacters.add(Gojo);
        sideCharacters.add(AllMight);
        sideCharacters.add(Lelouch);
        sideCharacters.add(Hisoka);
        sideCharacters.add(Jotaro);
        sideCharacters.add(Dio);
        sideCharacters.add(Itachi);
        sideCharacters.add(Yugi);
        sideCharacters.add(Puck);
        sideCharacters.add(Yuu);
        sideCharacters.add(Senkuu);
        checkAchievements();

        playableMaps.add(new Map("Training Ground"));
        playableMaps.add(new Map("Park"));
        playableMaps.add(new Map("Spooky Spikes"));
        playableMaps.add(new Map("Greed Island"));
        playableMaps.add(new Map("Roswaals Mansion"));
        playableMaps.add(new Map("Claustrophobic Lane"));
        playableMaps.add(new Map("UpsideDown Cups"));
        playableMaps.add(new Map("Custom"));

        allTiles.add(new TileBasic(new Point(0,0)));
        allTiles.add(new TileBasicLeft(new Point(0,0)));
        allTiles.add(new TileBasicTop(new Point(0,0)));
        allTiles.add(new TileIce(new Point(0,0)));
        allTiles.add(new TileIceLeft(new Point(0,0)));
        allTiles.add(new TileIceTop(new Point(0,0)));
        allTiles.add(new TileSlow(new Point(0,0)));
        allTiles.add(new TileSlowLeft(new Point(0,0)));
        allTiles.add(new TileSlowTop(new Point(0,0)));

        allPowerUps.add(new SpeedDown());
        allPowerUps.add(new PowerUpSpeedUp());
        allPowerUps.add(new PowerUpMinimiser());
        allPowerUps.add(new PowerUpShield());
        allPowerUps.add(new PowerUpSpecialAbilityPoints());
        allObstacles.add(new ObstacleBall());
        allObstacles.add(new ObstacleRock());
        allObstacles.add(new ObstacleStunBall());


        menuBackground = new Image("res/menu/MainMenu.PNG");
        getGameStats();
        loadStory();
        if (currentStory >= currentScene) {
            currentMode = "Scene";
        }
        else {
            currentMode = "Story";
        }

    }

    @Override
    protected void update(Input input) {
        currentMousePosition = input.getMousePosition();
        changeMainMusic(currentMusic);
        updateDisplayStrings();
        updateSounds();
        updateTime();

        if (settingsSingleton.getGameState() == -100) {
            new Player(2);
            players.get(0).setCharacter(new Character("Miku"));
            players.get(0).setSideCharacter(new SideJotaro());
            players.get(1).setCharacter(new Character("Mai"));
            players.get(1).setSideCharacter(new SideDio());
            settingsSingleton.setPlayers(players.size());
            map = new Map("Park");
            map.generateMap();
            gameSettingsSingleton.setMap(map);
            settingsSingleton.setGameMode(1);
            settingsSingleton.setGameState(6);
            settingsSingleton.setGameStateString("Test");

        }

        if (settingsSingleton.getGameState() > -1) {
            if (buttons.size() > 0) {
                for (Button button: buttons) {
                    button.toggleHover(input.getMousePosition());
                    if (input.wasPressed(MouseButtons.LEFT)) {
                        if (button.isHovering()) {
                            playSound("music/Click.wav");
                            button.playAction();
                        }
                    }
                }
            }
        }

        if (settingsSingleton.getGameState() == 0) {
            currentMusic = "music/Game Main Menu.wav";
            saveStatsChecker();
            if (!settingsSingleton.getGameStateString().equals("Main Menu")) {
                buttonsToRemove.addAll(buttons);
                buttons.add(new Button("PLAY", FONT_SIZE, Window.getWidth(), 160, new Point(0, 280)));
                buttons.add(new Button("CREATE MAP", FONT_SIZE, Window.getWidth(), 160, (new Point(0, 440))));
                buttons.add(new Button("EXIT", FONT_SIZE, Window.getWidth(), 160, new Point(0, 600)));
                settingsSingleton.setGameStateString("Main Menu");
                menuBackground = new Image("res/menu/MainMenu.PNG");
                menuTitle = "THE CLIMB";
            }
        }
        else if (settingsSingleton.getGameState() == 1) {
            if (!settingsSingleton.getGameStateString().equals("Level")) {
                if (!settingsSingleton.getGameStateString().equals("STORY") && !settingsSingleton.getGameStateString().equals("VS")) {
                    gameModeOffset = 0;
                    Button storyButton = new Button("STORY", FONT_SIZE,
                            titleFont.getWidth("STORYYY"), 160,
                            new Point(0, 0));
                    Button versusButton = new Button("VS", FONT_SIZE,
                            titleFont.getWidth("VSS"), 160,
                            new Point(Window.getWidth() - titleFont.getWidth("VSSs"), 0));
                    buttonsToRemove.addAll(buttons);
                    buttons.add(storyButton);
                    buttons.add(versusButton);
                    settingsSingleton.setGameStateString("Level");
                    menuTitle = "SELECT GAME MODE";
                }
            }
            if (settingsSingleton.getGameStateString().equals("STORY")) {
                gameModeOffset += 8;
                buttonsToRemove.addAll(buttons);
                if (Window.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) + gameModeOffset > Window.getWidth())  {
                    settingsSingleton.setGameState(2);
                }
            }
            else if (settingsSingleton.getGameStateString().equals("VS")) {
                gameModeOffset -= 8;
                buttonsToRemove.addAll(buttons);
                if (Window.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) + gameModeOffset < 0)  {
                    settingsSingleton.setGameState(2);
                }
            }
            else {
                mousePosition = currentMousePosition;
            }
        }
        else if (settingsSingleton.getGameState() == 2) {
            if (!settingsSingleton.getGameStateString().equals("Players")) {
                buttonsToRemove.addAll(buttons);
                Button twoPlayerButton = new Button("2", FONT_SIZE,
                        Window.getWidth(), 160,
                        new Point(0, Window.getHeight() / 2 - 160));
                Button threePlayerButton = new Button("3", FONT_SIZE,
                        Window.getWidth(), 160,
                        new Point(0, Window.getHeight() / 2));
                Button fourPlayerButton = new Button("4", FONT_SIZE,
                        Window.getWidth(), 160,
                        new Point(0, Window.getHeight() / 2 + 160));
                buttons.add(twoPlayerButton);
                buttons.add(threePlayerButton);
                buttons.add(fourPlayerButton);
                settingsSingleton.setGameStateString("Players");
                menuBackground = new Image("res/menu/playerMenu.PNG");
                menuTitle = "PLAYERS";
            }
        }
        else if (settingsSingleton.getGameState() == 3) {
            if (!settingsSingleton.getGameStateString().equals("Character")) {
                spacer = 300;
                buttonsToRemove.addAll(buttons);
                if (settingsSingleton.getGameMode() == 1) {
                    buttons.add(new Button("Game Settings", FONT_SIZE,
                            titleFont.getWidth("Settings"), 100,
                            new Point(0, 0)));
                }
                players.clear();
                settingsSingleton.setGameStateString("Character");
                new Player(1);
                new Player(2);
                if (settingsSingleton.getPlayers() == 3) {
                    new Player(3);
                }
                if (settingsSingleton.getPlayers() == 4) {
                    new Player(3);
                    new Player(4);
                }
                menuBackground = new Image("res/menu/characterMenu.png");
                menuTitle = "CHOOSE WAIFU";
                slidersToRemove.addAll(sliders);
            }

            int row = 1;
            int currentIcon = 1;
            for (Character character: characters) {
                character.setIconPos(new Point(-240 + spacer * currentIcon + character.getIcon().getWidth()/2, -200 + spacer * row + character.getIcon().getHeight()/2));
                currentIcon++;
                if (currentIcon >= 7) {
                    currentIcon = 1;
                    row++;
                }
            }

            for (Player player: players) {
                for (Character character: characters) {
                    if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
                        if (input.wasPressed((player.getControl("Primary")))) {
                            pickCharacter(player, character);
                        }
                    }
                }
            }

            picked = true;
            for (Player player: players) {
                player.setPos(input);
                if (player.getCharacter() == null) {
                    picked = false;
                }
            }
            if (picked) {
                if (waitTimer < 0) {
                    if(settingsSingleton.getGameMode() == 0) {
                        settingsSingleton.setGameState(6);
                    }
                    else {
                        for (Player player: players) {
                            player.getCharacter().setPlayer(player);
                        }
                        settingsSingleton.setGameState(4);
                    }
                }
                else {
                    waitTimer--;
                }
            }
            else {
                waitTimer = 2 * frames;
            }

        }
        else if (settingsSingleton.getGameState() == 4) { // Comrades
            if (!settingsSingleton.getGameStateString().equals("Comrade")) {
                buttonsToRemove.addAll(buttons);
                buttons.add(new Button("Game Settings", FONT_SIZE,
                        titleFont.getWidth("Settings"), 100,
                        new Point(0, 0)));
                menuTitle = "CHOOSE YOUR POWER";
                settingsSingleton.setGameStateString("Comrade");
                slidersToRemove.addAll(sliders);
            }

            for (Player player: players) {
                for (SideCharacter character: sideCharacters) {
                    if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
                        if (input.wasPressed((player.getControl("Primary")))) {
                            pickSideCharacter(player, character);
                        }
                    }
                }
            }

            picked = true;
            for (Player player: players) {
                player.setPos(input);
                if (player.getSideCharacter() == null) {
                    picked = false;
                }
            }
            if (picked) {
                if (waitTimer < 0) {
                    settingsSingleton.setGameState(5);
                }
                else {
                    waitTimer--;
                }
            }
        }
        else if (settingsSingleton.getGameState() == 5) { // Map
            if (!settingsSingleton.getGameStateString().equals("MAP")) {
                buttonsToRemove.addAll(buttons);
                buttons.add(new Button("Game Settings", FONT_SIZE,
                        titleFont.getWidth("Settings"), 100,
                        new Point(0, 0)));
                menuTitle = "Which Climb?";
                menuBackground = null;
                settingsSingleton.setGameStateString("MAP");
            }
            ArrayList<Map> mapsChosen = new ArrayList<>();
            for (Player player : players) {
                player.getCharacter().setPosition(player.getPos());
                player.setPos(input);
                int row = 1;
                int currentIcon = 2;
                for (Map map : playableMaps) {
                    if (map.getMapPeek().getBoundingBoxAt(new Point(-425 + map.getMapPeek().getWidth() * currentIcon + (row - 1) *400, -50 + spacer * row)).intersects(player.getPos())) {
                        if(input.wasPressed(player.getControl("Primary"))) {
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
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                if (!settingsSingleton.getGameStateString().equals("Game")) {
                    buttonsToRemove.addAll(buttons);
                    setPlayersPosition();
                    //currentMusic = "music/Giorno.wav";
                    currentMusic = String.format("music/Fight%d.wav", Math.round(Math.random()*3));
                    resetMusic();
                    playSound("music/Start.wav");
                    settingsSingleton.setGameStateString("Game");
                }
                startCountdown();
                if (!(countDown < 3 * frames)) {
                    if(!map.hasFinished()) {
                        if(!playingAnimation) {
                            mainMusic.setVolume((float)1);
                            map.updateTiles(gameSettingsSingleton.getMapSpeed());
                            spawnObstacles();
                            spawnPowerUps();
                        }
                    }
                    else {
                        for (Player player : players) {
                            if (player.getCharacter().getPos().distanceTo(new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                settingsSingleton.setGameState(7);
                                settingsSingleton.setWinner(player);
                                settingsSingleton.setGameStateString("Win");
                                break;
                            }
                        }
                    }
                    playingAnimation = false;
                    boolean playingActivation = false;
                    for (Player player: players) {
                        if (player.getSideCharacter().getName().equals("Yugi")) {
                            player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                        }
                        else {
                            if (input.wasPressed(player.getControl("Primary"))) {
                                if (player.getCharacter().hasSpecialAbility()) {
                                    if (!player.getSideCharacter().isActivating()) {
                                        player.getCharacter().useSpecialAbility();
                                        player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                                        playSound(player.getSideCharacter().getSoundPath());
                                        if (hasSound(player.getSideCharacter().getSoundPath())) {
                                            removeSound(findSound(player.getSideCharacter().getSoundPath()));
                                        }
                                    }
                                }
                            }
                            if (player.getSideCharacter().isActivating()) {
                                mainMusic.setVolume(0);
                                playingActivation = true;
                            }
                        }
                        if (player.getSideCharacter().isAnimating()) {
                            mainMusic.setVolume(0);
                            playingAnimation = true;
                        }
                    }
                    if ((!playingActivation) && (!playingAnimation)) {
                        mainMusic.setVolume(1);
                    }

                    if (!playingAnimation) {
                        updateExp();
                        updateObjects();
                        updatePlayerMovement(input);
                        checkCollisionPowerUps();
                        checkCollisionObstacles();
                        checkCollisionTiles();
                        recordButtons(input);
                    }
                    updateAbilities();
                    if (!settingsSingleton.getGameStateString().equals("Win")) {
                        int deathCounter = 0;
                        for (Player player : players) {
                            if (player.isDead()) {
                                deathCounter++;
                            }
                        }
                        if (settingsSingleton.getPlayers() - deathCounter < 2) {
                            settingsSingleton.setGameStateString("Lose");
                            settingsSingleton.setGameState(7);
                            for (Player player : players) {
                                if (!player.isDead()) {
                                    settingsSingleton.setWinner(player);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            else {
                if (!settingsSingleton.getGameStateString().equals("Story")) {
                    buttonsToRemove.addAll(buttons);
                    settingsSingleton.setGameStateString("Story");
                    setPlayersPosition();
                    map = mapToTransitionTo;
                    if (map != null) {
                        map.generateMap();
                    }
                }
                else if (currentBackground != null) {
                    currentBackground.drawFromTopLeft(currentBackgroundPoint.x, currentBackgroundPoint.y);
                }
                if (dark) {
                    darken();
                }
                if ((shakeTimer > 0) || (transitionTimer > 0)) {
                    shakeImage();
                    transition();
                }
                else {
                    if (playingStory) {
                        currentMode = "Story";
                        currentDialogue = currentStory;

                        if (currentStory == 0) {
                            currentMusic = "music/Fight0.wav";
                            if (lastStory != currentStory) {
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
                            }

                            if (endDialogue) {
                                updatePlayerMovement(input);
                                if(!map.hasFinished()) {
                                    map.updateTiles(1);
                                }
                                else {
                                    if (playersPassed()) {
                                        //mapToTransitionTo = new MapTrainingGround2();
                                        map = mapToTransitionTo;
                                        map.generateMap();
                                        currentStory = 1;
                                    }
                                }
                            }
                        }

                        else if (currentStory == 1) {
                            currentMusic = "music/Fight1.wav";
                            if (lastStory != currentStory) {
                                setPlayersPosition();
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
                                currentMusic = "music/Fight2.wav";
                            }

                            if (endDialogue) {
                                updatePlayerMovement(input);
                                render();
                                checkCollisionTiles();
                                for (Player player: players) {
                                    if (player.isDead()) {
                                        settingsSingleton.setGameState(7);
                                        failed = true;
                                    }
                                }
                                if(!map.hasFinished()) {
                                    map.updateTiles(0.8);
                                    displayCharacterStats(players);
                                }
                                else {
                                    if (playersPassed()) {
                                        map.generateMap();
                                        currentStory++;
                                    }
                                }
                            }
                        }
                        else if (currentStory == 2) {
                            currentMusic = "music/Fight2.wav";
                            if (lastStory != currentStory) {
                                setPlayersPosition();
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
                                currentMusic = "music/Fight1.wav";
                                gameSettingsSingleton.getObstaclesSettingsSingleton().applySettings(true, true, false);
                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("Rock", 0.99);
                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("Ball", 0.995);
                            }

                            if (endDialogue) {
                                updatePlayerMovement(input);
                                render();
                                checkCollisionPowerUps();
                                checkCollisionObstacles();
                                checkCollisionTiles();
                                updateObjects();
                                for (Player player: players) {
                                    if (player.isDead()) {
                                        settingsSingleton.setGameState(7);
                                        failed = true;
                                    }
                                }
                                if(!map.hasFinished()) {
                                    map.updateTiles(0.8);
                                    displayCharacterStats(players);
                                    spawnObstacles();
                                }
                                else {
                                    if (playersPassed()) {
                                        playingStory = false;
                                        playingScene = true;
                                        currentScene++;
                                        updateStory(currentScene);
                                        sceneToTransitionTo = new Image("res/background/Futaba.png");
                                        startTransition();
                                    }
                                }
                            }
                        }
                        else if (currentStory == 3) {
                            dark = true;
                            if (lastStory != currentStory) {
                                setPlayersPosition();
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
                                gameSettingsSingleton.getObstaclesSettingsSingleton().applySettings(true, true, true);
                                gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency("StunBall", 0.995);
                            }

                            if (endDialogue) {
                                updatePlayerMovement(input);
                                render();
                                checkCollisionPowerUps();
                                checkCollisionObstacles();
                                checkCollisionTiles();
                                updateObjects();
                                for (Player player: players) {
                                    if (player.isDead()) {
                                        settingsSingleton.setGameState(7);
                                        failed = true;
                                    }
                                }
                                if(!map.hasFinished()) {
                                    map.updateTiles(0.8);
                                    displayCharacterStats(players);
                                    spawnObstacles();
                                }
                                else {
                                    if (playersPassed()) {
                                        playingStory = false;
                                        playingScene = true;
                                        currentScene++;
                                        updateStory(currentScene);
                                        sceneToTransitionTo = new Image("res/background/Futaba.png");
                                        startTransition();
                                    }
                                }
                            }
                        }
                    }
                    else if (playingScene){
                        currentMode = "Scene";
                        currentDialogue = currentScene;
                        map = null;

                        if(currentScene == 0) {
                            if (lastScene != currentScene) {
                                lastScene = currentScene;
                                currentMusic = "music/Rain 1.wav";
                                changeBackground(new Image("res/background/Mountain.png"));
                                dark = true;
                                playingDialogue = true;
                                endDialogue = false;
                            }
                            if (dialogueCounter == 7) {
                                shakeTimer = 2 * frames;
                                dialogueCounter++;
                            }
                            if (endDialogue) {
                                startTransition();
                                sceneToTransitionTo = new Image("res/background/Nino.png");
                                currentScene++;
                            }
                        }

                        else if (currentScene == 1) {
                            if (lastScene != currentScene) {
                                lastScene = currentScene;
                                playingDialogue = true;
                                endDialogue = false;
                                currentMusic = "music/Idle.wav";
                            }
                            if (dialogueCounter == 0) {
                                dark = true;
                            }
                            else if (dialogueCounter > 2) {
                                dark = false;
                            }
                            if (endDialogue) {
                                startTransition();
                                currentScene = 2;
                                sceneToTransitionTo = new Image("res/background/Nino.png");
                            }
                        }
                        else if (currentScene == 2) {
                            if (lastScene != currentScene) {
                                if (currentBackground == null) {
                                    changeBackground(new Image("res/background/Nino.png"));
                                }
                                lastScene = currentScene;
                                playingDialogue = true;
                                endDialogue = false;
                                currentMusic = "music/Idle.wav";
                            }
                            if (endDialogue) {
                                playingStory = true;
                                playingScene = false;
                                currentStory = 0;
                                startTransition();
                                mapToTransitionTo = new Map("TrainingGround");
                                setPlayersPosition();
                            }
                        }
                        else if (currentScene == 3) {
                            if (lastScene != currentScene) {
                                if (currentBackground == null) {
                                    changeBackground(new Image("res/background/Nino.png"));
                                }
                                currentStory = 3;
                                lastScene = currentScene;
                                playingDialogue = true;
                                endDialogue = false;
                                currentMusic = "music/Idle.wav";
                            }
                            if (endDialogue) {
                                startTransition();
                                sceneToTransitionTo = new Image("res/background/Dio.png");
                                currentScene++;
                            }
                        }
                        else if (currentScene == 4) {
                            if (lastScene != currentScene) {
                                lastScene = currentScene;
                                playingDialogue = true;
                                endDialogue = false;
                            }
                            if (endDialogue) {
                                startTransition();
                                //mapToTransitionTo = new MapDioMansion();
                                playingScene = false;
                                playingStory = true;
                                setPlayersPosition();
                            }
                        }
                    }
                    else if (playingMap) {

                    }
                    // code for displaying dialogue
                    if (playingDialogue) {
                        if (dialogueCharacter == null) {
                            for (Character character : allCharacters) {
                                String name = playDialogue(currentDialogue, currentMode).split(" ")[0];
                                if (character.getName().equals(name.substring(0, name.length() - 1))) {
                                    dialogueCharacter = character;
                                }
                            }
                        }
                        if (endDialogue) {
                            playingDialogue = false;
                            dialogueCounter = 0;
                            dialogueCharacter = null;
                        }
                        else {
                            if (dialogueCharacter != null) {
                                Image characterOnScreen = new Image(String.format("res/Selected/%s_Selected.png", dialogueCharacter.getName()));
                                if (alternate) {
                                    characterOnScreen.draw(dialogueWidth + characterOnScreen.getWidth()/2, dialogueLength - characterOnScreen.getHeight()/2);
                                }
                                else {
                                    characterOnScreen.draw(Window.getWidth() - dialogueWidth - characterOnScreen.getWidth()/2, dialogueLength - characterOnScreen.getHeight()/2);
                                }
                            }
                            Drawing.drawRectangle(dialogueWidth, dialogueLength, Window.getWidth()*0.8, 250, dialogueColour);
                            dialogueFont.drawString(dialogueString.replaceAll("Nothing: ", ""), dialogueWidth + DIALOGUE_FONT_SIZE, dialogueLength + DIALOGUE_FONT_SIZE);
                            if (shakeTimer <= 0) {
                                if (playDialogue(currentDialogue, currentMode).length() - dialogueIndex > 1) {
                                    if (!playNextCharacter) {
                                        if(input.wasPressed(Keys.SPACE)) {
                                            currentLines = 0;
                                            if (!dialogueCharacter.getName().equals(nextCharacter.getName())) {
                                                alternate = !alternate;
                                            }
                                            dialogueCharacter = nextCharacter;
                                            dialogueString = "";
                                            playNextCharacter = true;
                                            if (playDialogue(currentDialogue, currentMode).length() - dialogueIndex <= 1) {
                                                endDialogue = true;
                                            }
                                            dialogueCounter++;
                                        }
                                    }
                                    else {
                                        if ((playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == '\n') || (playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == ' ') ) {
                                            String nextWord = "";
                                            for(int i = dialogueIndex + 1; i < playDialogue(currentDialogue, currentMode).length(); i ++) {
                                                if (playDialogue(currentDialogue, currentMode).charAt(i) == ' ') {
                                                    break;
                                                }
                                                else {
                                                    nextWord += playDialogue(currentDialogue, currentMode).charAt(i);
                                                }
                                            }
                                            for (Character character: allCharacters) {
                                                if (nextWord.equals(String.format("%s:", character.getName()))) {
                                                    if (nextWord.substring(0, nextWord.length() - 1).equals(character.getName())) {
                                                        if (!nextWord.equals(dialogueCharacter.getName())) {
                                                            playNextCharacter = false;
                                                            nextCharacter = character;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (playDialogue(currentDialogue, currentMode).charAt(dialogueIndex) == '\n') {
                                            currentLines++;
                                            if(currentLines >= maxLines) {
                                                if(input.wasPressed(Keys.SPACE)) {
                                                    dialogueString = "";
                                                    currentLines = 0;
                                                }
                                            }
                                        }
                                        if (currentLines < maxLines) {
                                            dialogueString += playDialogue(currentDialogue, currentMode).charAt(dialogueIndex);
                                            if (keyboardTimer > 9) {
                                                soundEffectMusic.playMusic("music/Type.wav");
                                                keyboardTimer = 0;
                                            }
                                            else {
                                                keyboardTimer++;
                                            }
                                            if (dialogueIndex < playDialogue(currentDialogue, currentMode).length()) {
                                                dialogueIndex++;
                                            }
                                        }
                                    }
                                }
                                else {
                                    dialogueString += playDialogue(currentDialogue, currentMode).charAt(dialogueIndex);
                                    if (input.wasPressed(Keys.SPACE)) {
                                        endDialogue = true;
                                        currentLines = 0;
                                        dialogueString = "";
                                        dialogueIndex = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        else if (settingsSingleton.getGameState() == 7) {
            if (settingsSingleton.getGameMode() == 0) {
                if (settingsSingleton.getGameStateString().equals("Continue")) {
                    changeMainMusic(currentMusic);
                    buttonsToRemove.addAll(buttons);
                    settingsSingleton.setGameState(6);
                    map.generateMap();
                    for (Player player : players) {
                        player.getCharacter().resetTimer();
                        if (player.isDead()) {
                            player.setDead();
                        }
                    }
                    setPlayersPosition();
                    powerUps.removeAll(powerUps);
                    obstacles.removeAll(obstacles);
                }
                else if (settingsSingleton.getGameStateString().equals("Menu")) {
                    failed = false;
                    settingsSingleton.setGameState(0);
                    for (Player player : players) {
                        player.getCharacter().resetTimer();
                        if (player.isDead()) {
                            player.setDead();
                        }
                    }
                    setPlayersPosition();
                    powerUps.removeAll(powerUps);
                    obstacles.removeAll(obstacles);
                }
            }
            else {
                currentMusic = "music/Fail.wav";
                countDown = 0;
                currentFrame = 0;
                for (Player player: players) {
                    player.getCharacter().resetTimer();
                    if (!player.isDead()) {
                        if (settingsSingleton.getWinner().getId() == player.getId()) {
                            Image winner = new Image(String.format("res/Renders/%s.png", player.getCharacter().getName()));
                            Image comrade = new Image(String.format("res/Renders/%s.png", player.getSideCharacter().getName()));
                            if (winnerTimer < winner.getWidth() + comrade.getWidth()/2) {
                                winnerTimer+=10;
                            }
                            Colour transparent = new Colour(0, 0, 0, 0.5);
                            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), transparent);
                            winner.draw(Window.getWidth() - winnerTimer, Window.getHeight()/2 +200);
                            comrade.draw(Window.getWidth() - winnerTimer + winner.getWidth(), Window.getHeight()/2 + 200);
                            victoryFont.drawString(String.format("Player %d: %s is victorious!", settingsSingleton.getWinner().getId(), settingsSingleton.getWinner().getCharacter().getName()), 16, 100);
                        }
                    }
                }
                if (settingsSingleton.getGameStateString().equals("Win")) {
                    playSound("music/Congratulations.wav");
                    settingsSingleton.setGameStateString("Congratulations");
                }
                if (!winnerPlayed) {
                    for (Player player: players) {
                        player.getCharacter().resetTimer();
                        if (player.getCharacter() != null) {
                            if (player.getCharacter().getName().equals(settingsSingleton.getWinner().getCharacter().getName())) {
                                playSound(player.getCharacter().playLine());
                                player.getCharacter().updateStats(true, true);
                                player.recordWin();
                            }
                            else {
                                player.getCharacter().updateStats(true, false);
                            }
                        }
                    }
                    statTracker[settingsSingleton.getGameMode() + 1] = statTracker[settingsSingleton.getGameMode() + 1] + 1;
                    settingsSingleton.getWinner().getCharacter().playLine();
                    winnerPlayed = true;
                    buttonsToRemove.addAll(buttons);
                    buttons.add(new Button("Back To Start", "Main Menu", FONT_SIZE,
                            Window.getWidth(), 160,
                            new Point(0, Window.getHeight() / 1.5 + 160)));
                    buttons.add(new Button("Retry", "Restart", FONT_SIZE,
                            Window.getWidth(), 160,
                            new Point(0, Window.getHeight() / 2 + 160)));
                }
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if((settingsSingleton.getGameStateString().equals("Retry")) || (settingsSingleton.getGameStateString().equals("Menu"))) {
                        resetMusic();
                        for (SideCharacter character: sideCharacters) {
                            character.reset();
                        }
                        if (settingsSingleton.getGameStateString().equals("Retry")) {
                            for (Player player: players) {
                                player.getCharacter().resetTimer();
                                player.getSideCharacter().reset();
                                if (player.isDead()) {
                                    player.setDead();
                                }
                            }
                            map.generateMap();
                            settingsSingleton.setGameState(6);
                            buttonsToRemove.addAll(buttons);
                        }
                        else if (settingsSingleton.getGameStateString().equals("Menu")) {
                            map = null;
                            unlocked = checkAchievementsInGame();
                            if (unlocked != null) {
                                if (!isPlayable(unlocked.getName())) {
                                    addToPlayableCharacter(unlocked.getName());
                                    settingsSingleton.setGameState(8);
                                    saveStats();
                                }
                            }
                            else {
                                settingsSingleton.setGameState(0);
                            }
                            players.clear();
                            buttonsToRemove.addAll(buttons);
                        }
                        obstacles.removeAll(obstacles);
                        powerUps.removeAll(powerUps);
                        waitTimer = 840;
                        winnerTimer = 0;
                        winnerPlayed = false;
                        currentFrame = 0;
                        countDown = 0;
                        saveStats();
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            if (!settingsSingleton.getGameStateString().equals("Unlocked")) {
                resetMusic();
                currentMusic = "music/Who.wav";
                mainMusic.clip.setFramePosition(0);
                settingsSingleton.setGameStateString("Unlocked");
            }
            if (mainMusic.clip.getFramePosition() >= mainMusic.clip.getFrameLength()) {
                currentMusic = String.format("music/%sUnlock.wav", unlocked.getName());
                if (!currentMusic.replaceAll("/", "\\\\").equals(mainMusic.musicPath.getPath())) {
                    playSound(String.format("music/%sVoice.wav", unlocked.getName()));
                }
            }
            if (currentMusic.equals(String.format("music/%sUnlock.wav", unlocked.getName()))) {
                chooseCharacterFont.drawString(String.format("NEW CHARACTER UNLOCKED: %s!", unlocked.getName().toUpperCase()), 0, 100);
                Image unlockedImage = new Image(String.format("res/Renders/%s.png", unlocked.getName()));
                unlockedImage.draw(Window.getWidth()/2, Window.getHeight()/2 + 300);
                if (input.wasPressed(MouseButtons.LEFT)) {
                    resetMusic();
                    settingsSingleton.setGameState(0);
                }
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
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
                menuBackground = null;
                menuTitle = null;
            }

            if (!addingTile) {
                if (input.wasPressed(Keys.UP)) {
                    updateTiles(100);
                    offset++;
                }
                if (input.wasPressed((Keys.DOWN))) {
                    if (offset > 0) {
                        updateTiles(-100);
                        offset--;
                    }
                }
                if (input.wasPressed(Keys.S)) {
                    if (customMapTiles.size()%4 != 0) {
                        addDisplayString("Error: Cannot save map. Map not complete! (Row is not filled)", 5);
                    }
                    else {
                        addDisplayString("Success: Map saved.", 5);
                        saveCustomMap();
                        settingsSingleton.setGameState(0);
                    }
                }
                if (input.wasPressed(Keys.A)) {
                    addingTile = true;
                }
                if (input.wasPressed(Keys.R)) {
                    if (customMapTiles.size() > 0) {
                        customMapTiles.remove(customMapTiles.size() - 1);
                    }
                }
                if (input.wasPressed(Keys.ESCAPE)) {
                    settingsSingleton.setGameState(0);
                }
            }
            else {
                if (input.wasPressed(Keys.NUM_1)) {
                    addTileToCustomMap(tile1.getName());
                }
                if (input.wasPressed(Keys.NUM_2)) {
                    addTileToCustomMap(tile2.getName());
                }
                if (input.wasPressed(Keys.NUM_3)) {
                    addTileToCustomMap(tile3.getName());
                }
                if (input.wasPressed(Keys.LEFT)) {
                    if (page > 0) {
                        page--;
                    }
                }
                if (input.wasPressed((Keys.RIGHT))) {
                    if (page < 4) {
                        page++;
                    }
                }
                if (input.wasPressed(Keys.ESCAPE)) {
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
                titleFont.drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300 + 100);
                pageType = "General";
            }

            if (gameSettingsSingleton.getPage() == 0) {
                pageType = "General";
            }
            else if (gameSettingsSingleton.getPage() == 1) {
                pageType = "PowerUps";
            }
            else if (gameSettingsSingleton.getPage() == 2) {
                pageType = "Obstacles";
            }
            else {
                pageType = "";
            }
            menuTitle = pageType;
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            if (settingsSingleton.getGameState() == 1) {
                settingsSingleton.setGameState(0);
            }
            else if (settingsSingleton.getGameState() == 2) {
                settingsSingleton.setGameState(1);
            }
            else if (settingsSingleton.getGameState() == 3) {
                players.removeAll(players);
                settingsSingleton.setGameState(settingsSingleton.getGameState() - 1);
            }
            else if (settingsSingleton.getGameState() == 4) {
                settingsSingleton.setGameState(3);
            } else if (settingsSingleton.getGameState() == 5) {
                for (Player player : players) {
                    player.setMapChosen(null);
                    player.setSideCharacter(null);
                    settingsSingleton.setGameState(4);
                }
            }
            else if (settingsSingleton.getGameState() == 6) {
                settingsSingleton.setGameState(11);
            } else if (settingsSingleton.getGameState() == 10) {
                settingsSingleton.setGameState(settingsSingleton.getLastGameState());
            }
            else if (settingsSingleton.getGameState() == 11) {
                settingsSingleton.setGameState(6);
            }
        }
        for (Slider slider: sliders) {
            slider.interact(input);
        }
        updateButtons();
        updateSliders();
        render();
    }

    public void render() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
        if (menuBackground != null) {
            menuBackground.drawFromTopLeft(0, 0);
        } else {
            drawMapBackground();
        }
        if (settingsSingleton.getGameState() < 6) {
            if (settingsSingleton.getGameState() == -1 ) {
                Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), black);
                playIntro();
            }
            if (settingsSingleton.getGameState() == 1) {
                Image storyImage = new Image("res/menu/storyMenu.png");
                Image vsImage = new Image("res/menu/vsMenu.png");
                if (settingsSingleton.getGameStateString().equals("STORY")) {
                    vsImage.drawFromTopLeft(vsImage.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) + gameModeOffset, 0);
                    storyImage.drawFromTopLeft(-storyImage.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) +gameModeOffset, 0);
                }
                else if (settingsSingleton.getGameStateString().equals("VS")) {
                    storyImage.drawFromTopLeft(-storyImage.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) + gameModeOffset, 0);
                    vsImage.drawFromTopLeft(vsImage.getWidth()/2 - (mousePosition.x/3 - Window.getWidth()/6) + gameModeOffset, 0);
                }
                else {
                    storyImage.drawFromTopLeft(-storyImage.getWidth()/2 - (currentMousePosition.x/3 - Window.getWidth()/6), 0);
                    vsImage.drawFromTopLeft(vsImage.getWidth()/2 - (currentMousePosition.x/3 - Window.getWidth()/6), 0);
                }
            }
            else if (settingsSingleton.getGameState() == 2) {
                for (Button button: buttons) {
                    if ("234".contains(button.getName())) {
                        button.setNight();
                        if (button.isHovering()) {
                            Image playerArt = new Image(String.format("res/menu/%sPlayers.PNG", button.getName()));
                            playerArt.draw(Window.getWidth()/2, Window.getHeight()/2);
                        }
                    }
                }
            }
            else if (settingsSingleton.getGameState() == 3) {
                // draw character Icons
                int row = 1;
                int currentIcon = 1;
                Image locked = new Image("res/icons/Unknown.png");
                for (Character character: characters) {
                    character.getIcon().drawFromTopLeft(-240 +spacer * currentIcon, -200 + spacer * row);
                    currentIcon++;
                    if (currentIcon >= 7) {
                        currentIcon = 1;
                        row++;
                    }
                }
                // draw filler for locked characters
                for (int i = characters.size(); i < 12; i++) {
                    locked.drawFromTopLeft(-240 + spacer * currentIcon, -200 + spacer * row);
                    currentIcon++;
                    if (currentIcon >= 7) {
                        currentIcon = 1;
                        row++;
                    }
                }
                drawBorders();
                // react to player movement relative to character icons
                for (Player player: players) {
                    for (Character character: characters) {
                        Image border = new Image("res/Selected/Selected.png");
                        if (player.getCharacter() == null) {
                            Rectangle iconRectangle = new Rectangle(character.getIconPos().x - 165, character.getIconPos().y - 150, 299, 299);
                            if (iconRectangle.intersects(player.getPos())) {
                                character.getSelected().draw(250 + border.getWidth()*1.5*(player.getId()-1), Window.getHeight() - 100);
                                playerFont.drawString(String.format("P%d: %s", player.getId(), character.getName()), 310 + (player.getId() - 1) * border.getWidth()*3/2, Window.getHeight());
                            }
                            else {
                                playerFont.drawString(String.format("P%d: ", player.getId()), 310 + (player.getId() - 1) * border.getWidth()*3/2, Window.getHeight());
                            }
                        }
                        else {
                            player.getCharacter().getSelected().draw(250 + border.getWidth()*1.5*(player.getId()-1), Window.getHeight() - 100);
                            playerFont.drawString(String.format("P%d: %s", player.getId(), player.getCharacter().getName()), 310 + (player.getId() - 1) * border.getWidth()*3/2, Window.getHeight());
                        }
                        playerFont.drawString(String.format("Wins: %d", player.getNoOfWins()), 310 + (player.getId() - 1) * border.getWidth()*3/2, Window.getHeight() - 40);
                    }
                    player.getCursor().drawFromTopLeft(player.getPos().x, player.getPos().y);
                }
            }
            else if (settingsSingleton.getGameState() == 4 ) {
                spacer = 300;
                int row = 1;
                int currentIcon = 1;
                Image locked = new Image("res/icons/Unknown.png");
                for (SideCharacter character : sideCharacters) {
                    character.getIcon().draw(200 + spacer * currentIcon, -50 + spacer * row);
                    character.setIconPos(new Point(200 + spacer * currentIcon, -50 + spacer * row));
                    currentIcon++;
                    if (currentIcon >= 5) {
                        currentIcon = 1;
                        row++;
                    }
                }
                // draw filler for locked characters
                for (int i = sideCharacters.size(); i < 12; i++) {
                    locked.draw(200 + spacer * currentIcon, -50 + spacer * row);
                    currentIcon++;
                    if (currentIcon >= 5) {
                        currentIcon = 1;
                        row++;
                    }
                }
                drawBorders();
                // react to player movement relative to character icons
                for (Player player : players) {
                    for (SideCharacter character : sideCharacters) {
                        Image border = new Image("res/Selected/Selected.png");
                        if (player.getSideCharacter() == null) {
                            if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
                                character.getSelected().draw(250 + border.getWidth() * 1.5 * (player.getId() - 1), Window.getHeight() - 150);
                                playerFont.drawString(String.format("P%d: %s", player.getId(), character.getName()), 310 + (player.getId() - 1) * border.getWidth() * 3 / 2, Window.getHeight());
                            } else {
                                playerFont.drawString(String.format("P%d: ", player.getId()), 310 + (player.getId() - 1) * border.getWidth() * 3 / 2, Window.getHeight());
                            }
                        } else {
                            player.getSideCharacter().getSelected().draw(250 + border.getWidth() * 1.5 * (player.getId() - 1), Window.getHeight() - 150);
                            playerFont.drawString(String.format("P%d: %s", player.getId(), player.getSideCharacter().getName()), 310 + (player.getId() - 1) * border.getWidth() * 3 / 2, Window.getHeight());
                        }
                        playerFont.drawString(String.format("Wins: %d", player.getNoOfWins()), 310 + (player.getId() - 1) * border.getWidth() * 3 / 2, Window.getHeight() - 40);
                    }
                    player.getCursor().drawFromTopLeft(player.getPos().x, player.getPos().y);
                }
            }
            else if (settingsSingleton.getGameState() == 5) {
                spacer = 400;
                int row = 1;
                int currentIcon = 1;
                Image locked = new Image("res/mapPeeks/locked.png");
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
                        if (!map.getMapPeek().getBoundingBoxAt(new Point(-425 + map.getMapPeek().getWidth() * currentIcon + (row - 1) *400, -50 + spacer * row)).intersects(player.getPos())) {
                            if(player.getMapChosen() != null) {
                                gameFont.drawString(String.format("P%d: %s", player.getId(), player.getMapChosen().getName()), (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1), Window.getHeight() - 15, DO.setBlendColour(white));
                            }
                        }
                        else {
                            if(player.getMapChosen() == null) {
                                gameFont.drawString(String.format("P%d: %s", player.getId(), map.getName()), (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1), Window.getHeight() - 15, DO.setBlendColour(white));
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
                    locked.draw(spacer * currentIcon, -50 + spacer * row);
                    currentIcon++;
                    if (currentIcon > 5) {
                        currentIcon = 1;
                        if (row < 2) {
                            row++;
                        }
                    }
                }
            }
        }
        if (settingsSingleton.getGameState() == 6) {
            menuTitle = null;
            drawGame();
            if (countDown < 3 * frames) {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, (390.0 - currentFrame)/390.0));
                String string;
                if (currentFrame < 150) {
                    string = "3..";
                } else if (currentFrame < 270) {
                    string = "2..";
                } else if (currentFrame < 390) {
                    string = "1..";
                } else {
                    string = "GO!";
                }
                countdownFont.drawString(String.format("%s", string), Window.getWidth() / 2 - 125, Window.getHeight() / 2);
                currentFrame++;
            }
            else {
                if(!map.hasFinished()) {
                    if(!playingAnimation) {
                        drawBoundaries();
                        displayCharacterStats(players);
                    }
                }
                else {
                    if (!playingAnimation) {
                        titleFont.drawString("CLEAR! REACH THE TOP!", 16, FONT_SIZE, DO.setBlendColour(black));
                    }
                }
                renderAbilities();
            }
        }
        else if (settingsSingleton.getGameState() == 7) {
            drawGame();
            if (settingsSingleton.getGameMode() == 0) {
                displayFailScreen();
            }
            else {
                countDown = 0;
                currentFrame = 0;
                for (Player player: players) {
                    player.getCharacter().resetTimer();
                    if (!player.isDead()) {
                        if (settingsSingleton.getWinner().getId() == player.getId()) {
                            Image winner = new Image(String.format("res/Renders/%s.png", player.getCharacter().getName()));
                            Image comrade = new Image(String.format("res/Renders/%s.png", player.getSideCharacter().getName()));
                            if (winnerTimer < winner.getWidth() + comrade.getWidth()/2) {
                                winnerTimer+=10;
                            }
                            Colour transparent = new Colour(0, 0, 0, 0.5);
                            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), transparent);
                            winner.draw(Window.getWidth() - winnerTimer, Window.getHeight()/2 +200);
                            comrade.draw(Window.getWidth() - winnerTimer + winner.getWidth(), Window.getHeight()/2 + 200);
                            victoryFont.drawString(String.format("Player %d: %s is victorious!", settingsSingleton.getWinner().getId(), settingsSingleton.getWinner().getCharacter().getName()), 16, 100);
                        }
                    }
                }
            }
        }
        else if (settingsSingleton.getGameState() == 8) {
            Colour black = new Colour(0, 0, 0, (float) (mainMusic.clip.getFrameLength() - mainMusic.clip.getFramePosition())/ (float) mainMusic.clip.getFrameLength());
            Image background = new Image(String.format("res/background/%s.png", unlocked.getName()));
            background.drawFromTopLeft(0,0);
            if (currentMusic.equals(String.format("music/%sUnlock.wav", unlocked.getName()))) {
                chooseCharacterFont.drawString(String.format("NEW CHARACTER UNLOCKED: %s!", unlocked.getName().toUpperCase()), 0, 100);
                Image unlockedImage = new Image(String.format("res/Renders/%s.png", unlocked.getName()));
                unlockedImage.draw(Window.getWidth()/2, Window.getHeight()/2 + 300);
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
            }
        }
        else if (settingsSingleton.getGameState() == 9) {
            int index = 0;
            for (Tile tile: customMapTiles) {
                tile.draw();
                index++;
            }
            if (!addingTile) {
                playerFont.drawString("S: Save and Exit ESC: Exit without Saving Arrow Keys: Navigate\nA: Add, R: Remove last block", 100, 50);
            }
            else {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
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
            menuBackground = null;
            if ("General".equalsIgnoreCase(pageType)) {
                titleFont.drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300 + 0);
            }
            else if (Arrays.asList("PowerUps", "Obstacles").contains(pageType)) {
                gameFont.drawString("Drag the slider across to increase/decrease the spawn rate!", titleFont.getWidth("SETTINGS"), 150, new DrawOptions().setBlendColour(0,0,0, 0.7));
                gameFont.drawString("Click the icon to toggle on/off", titleFont.getWidth("SETTINGS"), 190, new DrawOptions().setBlendColour(0,0,0, 0.7));
            }
        }
        else if (settingsSingleton.getGameState() == 11) {
            drawGame();
            playerFont.drawString("PAUSE", (Window.getWidth() - playerFont.getWidth("PAUSE"))/2, Window.getHeight()/2 - 50);
            gameFont.drawString("Press ESC to resume", (Window.getWidth() - playerFont.getWidth("Press ESC to res"))/2, Window.getHeight()/2);
        }
        drawButtons();
        drawSliders();
        if (menuTitle != null) {
            if ((((settingsSingleton.getGameState() == 3) || (settingsSingleton.getGameState() == 10)) || (settingsSingleton.getGameState() == 4)) || (settingsSingleton.getGameState() == 9)) {
                titleFont.drawString(menuTitle, (Window.getWidth() - titleFont.getWidth(menuTitle)) / 2, 90, DO.setBlendColour(black));
            } else {
                titleFont.drawString(menuTitle, (Window.getWidth() - titleFont.getWidth(menuTitle)) / 2, 90, DO.setBlendColour(white));
            }
        }
        showDisplayStrings();
        showTime();
    }

    public void showTime() {
        gameFont.drawString(timeLogger.getDisplayTime(), Window.getWidth() - gameFont.getWidth(timeLogger.getDisplayTime()), 40);

    }

    public void updateTime() {
        timeLogger.updateTime();
    }

    public void record(Input input) {
        recordButtons(input);
    }

    public void recordButtons(Input input) {
        ArrayList<Keys> buttons = new ArrayList<>();
        for (Keys key: Keys.values()) {
            if (input.wasPressed(key)) {
                buttons.add(key);
            }
        }
        timeLogger.updateButtonsLogger(buttons);
    }

    public boolean checkStats(String characterName, int line, int threshold) {
        for (Character character: allCharacters) {
            if (character.getName().equals(characterName)) {
                return character.getStats()[line] >= threshold;
            }
        }
        return false;
    }

    public void saveStatsChecker() {
        if (currentTimeToSaveStats > timeToSaveStats) {
            currentTimeToSaveStats = 0;
            saveStats();
        }
        else {
            currentTimeToSaveStats++;
        }
    }

    public void saveStats() {
        currentTimeToSaveStats = 0;
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
            pw.println(String.format("Amount of games played in Easy Mode: %d", statTracker[1]));
            pw.println(String.format("Amount of games played in Hard Mode: %d", statTracker[2]));
            pw.println(String.format("Amount of games played in Story Mode: %d", statTracker[3]));
            for(Character character: allCharacters) {
                pw.println(character.getName());
                int[] stats = character.getStats();
                pw.println(String.format("Played: %d", stats[0]));
                pw.println(String.format("Won: %d", stats[1]));
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

    public void addToPlayableCharacter(String name) {
        for (Character character: allCharacters) {
            if (character.getName().equals(name)) {
                characters.add(character);
            }
        }
    }

    public boolean isPlayable(String name) {
        for (Character character: characters) {
            if (character.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Character findCharacter(String name) {
        for (Character character: allCharacters) {
            if (character.getName().equals(name)) {
                return character;
            }
        }
        return null;
    }

    public void checkAchievements() {
        //This file includes achievements and information on how to unlock each unlockable character.
        // Format: [Unlockable Character] then [Character involved in unlocking] [line No of their stat] [value threshold].
        try {
            Scanner achievementScanner = new Scanner(new File("stats/Achievements.txt"));
            while (achievementScanner.hasNextLine()) {
                String[] achievementLine = achievementScanner.nextLine().split(" ");
                String name = achievementLine[1];
                int threshold = Integer.parseInt(achievementLine[3]);
                int line = Integer.parseInt(achievementLine[2]);
                String newCharacter = achievementLine[0];
                if (checkStats(name, line, threshold)) {
                    addToPlayableCharacter(newCharacter);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Character checkAchievementsInGame() {
        try {
            Scanner achievementScanner = new Scanner(new File("stats/Achievements.txt"));
            while (achievementScanner.hasNextLine()) {
                String[] achievementLine = achievementScanner.nextLine().split(" ");
                String newCharacter = achievementLine[0];
                String name = achievementLine[1];
                int threshold = Integer.parseInt(achievementLine[3]);
                int line = Integer.parseInt(achievementLine[2]);
                if (checkStats(name, line, threshold)) {
                    if (!isPlayable(newCharacter)) {
                        return findCharacter(newCharacter);
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String playDialogue(Integer dialogueInt, String mode) {
        String currentDialogue = "";
        try {
            if (mode.equals("Story")) {
                dialogueScanner = new Scanner(new File(String.format("story/%d.txt", dialogueInt)));
            }
            else {
                dialogueScanner = new Scanner(new File(String.format("scene/%d.txt", dialogueInt)));
            }
            dialogueWords = new ArrayList<>();
            while (dialogueScanner.hasNextLine()) {
                dialogueLine = dialogueScanner.nextLine().split(" ");
                for (String word: dialogueLine) {
                    dialogueWords.add(word);
                }
            }
            int currentLineLength = 0;
            for (String word: dialogueWords) {
                for (Character character: allCharacters) {
                    if (word.endsWith(":")) {
                        if (word.substring(0, word.length()-1).equals(character.getName())) {
                            currentLineLength = 0;
                            break;
                        }
                    }
                }
                if (currentLineLength + word.length() + 1 < MAX_DIALOGUE_LIMIT) {
                    currentDialogue = currentDialogue + word + ' ';
                    currentLineLength += word.length() + 1;
                }
                else {
                    currentDialogue = currentDialogue + '\n' + word + ' ';
                    currentLineLength = word.length() + 1;
                }
            }
            return currentDialogue;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return currentDialogue;
    }

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

    public void drawBorders() {
        Image P1  = new Image("res/Selected/P1.png");
        Image P2  = new Image("res/Selected/P2.png");
        Image P3  = new Image("res/Selected/P3.png");
        Image P4  = new Image("res/Selected/P4.png");

        for (Player player: players) {
            if (player.getId() == 1) {
                if (player.getCharacterChosen()) {
                    P1  = new Image("res/Selected/P1_Selected.png");
                }
            }
            else if (player.getId() == 2) {
                if (player.getCharacterChosen()) {
                    P2  = new Image("res/Selected/P2_Selected.png");
                }
            }
            else if (player.getId() == 3) {
                if (player.getCharacterChosen()) {
                    P3  = new Image("res/Selected/P3_Selected.png");
                }
            }
            else if (player.getId() == 4) {
                if (player.getCharacterChosen()) {
                    P4  = new Image("res/Selected/P4_Selected.png");
                }
            }
        }
        P1.draw(250, Window.getHeight() - 115);
        P2.draw(250 + P1.getWidth()*1.5, Window.getHeight() - 115);
        if(settingsSingleton.getPlayers() == 3) {
            P3.draw(250 + P1.getWidth()*3, Window.getHeight() - 115);
        }
        if (settingsSingleton.getPlayers() == 4) {
            P3.draw(250 + P1.getWidth()*3, Window.getHeight() - 115);
            P4.draw(250 + P1.getWidth()*4.5, Window.getHeight() - 115);
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
            if (!playingMap) {
                playingScene = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public void shakeImage() {
        if (shakeTimer > 0) {
            if (shakeTimer % 6 == 0) {
                currentBackgroundPoint = new Point(currentBackgroundPoint.x - 50, currentBackgroundPoint.y);
            }
            else if (shakeTimer % 5 == 0) {
                currentBackgroundPoint = new Point(currentBackgroundPoint.x + 50, currentBackgroundPoint.y);
            }
            shakeTimer --;
        }
        else {
            currentBackgroundPoint = new Point(0, 0);
        }
    }

    public void transition() {
        if (transitionTimer >= 3 * frames) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 1 - (transitionTimer - 3*frames)/(2*frames)));
        }
        else if ((transitionTimer < 3 * frames) && (transitionTimer > 2 * frames)) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,1));
        }
        else if (transitionTimer <= 2 * frames) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, transitionTimer/(2*frames)));
        }
        if (transitionTimer == 2.5 * frames) {
            dark = false;
            if (playingStory) {
                map = mapToTransitionTo;
                map.generateMap();
            }
            else {
                changeBackground(sceneToTransitionTo);
            }
        }
        transitionTimer--;
    }

    public void darken() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), darken);
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

    public void getGameStats() {
        int num;
        try {
            Scanner scanner = new Scanner(new File("stats/Stats.txt"));
            scanner.nextLine();
            for (int i =0; i < 4; i++) {
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

    public void changeMainMusic(String filePath) {
        if (mainMusic.musicPath == null) {
            mainMusic.run(filePath);
        }
        else {
            if (!currentMusic.replaceAll("/", "\\\\").equals(mainMusic.musicPath.getPath())) {
                mainMusic.stopMusic();
                mainMusic.run(filePath);
            }
        }
        mainMusic.musicPath = new File(filePath);
    }

    public void changeBackground(Image background) {
        currentBackground = background;
    }

    public void displayCharacterStats(ArrayList<Player> players) {
        int playerIndex = 0;
        for (Player player: players) {
            Image picture = new Image(String.format("res/InGame/%s.png", player.getCharacter().getName()));
            Colour border = new Colour(0, 0, 0, 0.3);
            if (player.getCharacter().getSpecialAbilityBar() >= 100) {
                border = new Colour(255, 255, 0, 0.5);
            }
            Drawing.drawRectangle(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight(), 200, picture.getHeight(), border);
            if (player.getCharacter().isMinimised()) {
                picture.drawFromTopLeft(playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight(), new DrawOptions().setScale(0.5, 0.5));
            }
            else {
                picture.drawFromTopLeft(playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight());
            }

            if (player.getCharacter().hasShield()) {
                Image shield = new Image("res/InGame/Shield_Selected.png");
                shield.draw(picture.getWidth()/2 + playerIndex*Window.getWidth()/(players.size()), picture.getHeight()/2 + Window.getHeight() - picture.getHeight());
            }
            if (!player.getSideCharacter().getName().equals("Yugi")) {
                Image sideCharacter = new Image(String.format("res/inGame/%s.png", player.getSideCharacter().getName()));
                sideCharacter.drawFromTopLeft(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()),
                        Window.getHeight() - picture.getHeight(),
                        DO.setSection(0, 0, sideCharacter.getWidth(),
                                sideCharacter.getHeight()*player.getCharacter().getSpecialAbilityBar()/100));
            }
            else {
                int index = 0;
                for (ExodiaPiece exodiaPiece: Yugi.getExodiaPiecesCollected()) {
                    exodiaPiece.getImage().drawFromTopLeft(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()) + exodiaPiece.getImage().getWidth()*index, Window.getHeight() - picture.getHeight());
                    index++;
                }
            }
            playerIndex++;
        }
    }

    public void pickCharacter(Player player, Character character) {
        boolean pickable = true;
        if (player.getCharacter() == null) {
            for (Player otherPlayer : players) {
                if (otherPlayer.getId() != player.getId()) {
                    if (otherPlayer.getCharacterChosen()) {
                        if (otherPlayer.getCharacter().getName().equals(character.getName())) {
                            pickable = false;
                        }
                    }
                }
            }
            if (pickable) {
                player.setCharacter(character);
                playSound(character.playLine());
            }
        }
        else {
            player.setCharacter(null);
        }
    }

    public void pickSideCharacter(Player player, SideCharacter character) {
        boolean pickable = true;
        if (player.getSideCharacter() == null) {
            for (Player otherPlayer : players) {
                if (otherPlayer.getId() != player.getId()) {
                    if (otherPlayer.getSideCharacterChosen()) {
                        if (otherPlayer.getSideCharacter().getName().equals(character.getName())) {
                            pickable = false;
                        }
                    }
                }
            }
            if (pickable) {
                player.setSideCharacter(character);
                playSound(character.playLine());
            }
        }
        else {
            player.setSideCharacter(null);
        }
    }

    public void pickMap(Player player, Map map) {
        if (player.getMapChosen() == null) {
            soundEffectMusic.playMusic("music/Click.wav");
            player.setMapChosen(map);
        }
    }

    public void drawCurrentHeight() {
        if (!settingsSingleton.isNight()) {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(white));
        }
        else {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(black));
        }
    }

    public void startTransition() {
        transitionTimer = 5 * frames;
    }

    public void checkCollisionTiles() {
        for (Player player: players) {
            if (!map.hasFinished()) {
                if ((player.getCharacter().getRectangle().intersects(bottomRectangle))) {
                    if (!player.isDead()) {
                        player.setDead();
                    }
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
        if(!player.isDead()) {
            player.setDead();
        }
    }

    public void drawBoundaries() {
        Drawing.drawRectangle(0, Window.getHeight() - 20, Window.getWidth(), 20, red);
    }

    public void updatePlayerMovement(Input input) {
        for (Player player: players) {
            Character character = player.getCharacter();
            if (!player.isDead()) {
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
                    if (!player.isDead()) {
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
                    if (!player.isDead()) {
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
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedUp")) {
                    powerUps.add(new PowerUpSpeedUp());
                }
            }
        }
        else if (spawnNo < 2) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedDown")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedDown")) {
                    powerUps.add(new SpeedDown());
                }
            }
        }
        else if (spawnNo < 3) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Minimiser")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Minimiser")) {
                    powerUps.add(new PowerUpMinimiser());
                }
            }
        }
        else if (spawnNo < 4) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Shield")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Shield")) {
                    powerUps.add(new PowerUpShield());
                }
            }
        }
        else {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Special Ability")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Special Ability")) {
                    powerUps.add(new PowerUpSpecialAbilityPoints());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isRocks()) {
            if (Math.random() > gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("Rock")) {
                obstacles.add(new ObstacleRock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isBalls()) {
            if (Math.random()
                    > gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("Ball")) {
                obstacles.add(new ObstacleBall());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isStunBalls()) {
            if (Math.random()
                    > gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getFrequency("StunBall")) {
                obstacles.add(new ObstacleStunBall());
            }
        }
    }

    public void drawGame() {
        if (map != null) {
            map.draw();
        }
        for (Player player : players) {
            if (!player.isDead()) {
                player.getCharacter().draw();
            }
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
                titleFont.drawString("CLEAR! REACH THE TOP!", 16, FONT_SIZE, DO.setBlendColour(black));
            }
        }
        else {
            drawCurrentHeight();
        }
    }

    public void displayFailScreen() {
        if (failed) {
            failed = false;
            buttonsToRemove.addAll(buttons);
            buttons.add(new Button("Retry", "Continue?", FONT_SIZE,
                    Window.getWidth(), 160,
                    new Point(0, Window.getHeight() / 2)));
            buttons.add(new Button("Back To Start", "Exit", FONT_SIZE,
                    Window.getWidth(), 160,
                    new Point(0, Window.getHeight() / 1.5)));
            currentMusic = "music/Fail.wav";
        }
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.7));
        victoryFont.drawString("YOU FAILED THE CLIMB!", 30, 110);
    }

    public void playSound(String filePath) {
        Music sound = new Music();
        musics.add(sound);
        sound.playMusic(filePath);
    }

    public void updateSounds() {
        ArrayList<Music> musicToRemove = new ArrayList<>();
        for (Music music: musics) {
            if (!music.clip.isActive()) {
                musicToRemove.add(music);
            }
        }
        musics.removeAll(musicToRemove);
    }

    public void resetMusic() {
        for (Music music: musics) {
            music.stopMusic();
        }
        musics.removeAll(musics);
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
            gameFont.drawString(stringDisplay.getName(), Window.getWidth() - gameFont.getWidth(stringDisplay.getName()), 60*i);
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

    public void playIntro() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1, 1, 1, intro/(2*frames)));
        intro++;
        if (intro >= 2*frames) {
            introFont.drawString("Made by Bill Nguyen", Window.getWidth()/2 - introFont.getWidth("Made by Bill Nguyen")/2, Window.getHeight()/2, DO.setBlendColour(new Colour(0,0,0,(intro - 2*frames)/(1*frames))));
            if (intro == 2*frames) {
                //soundEffectMusic.playMusic("music/Intro.wav");
            }
        }
        if (intro >= 5*frames) {
            settingsSingleton.setGameState(0);
        }
    }

    public void drawMapBackground() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1, 1, 1, 1));
        ArrayList<Image> tileBackground = new ArrayList<>();
        if ((java.time.LocalTime.now().getHour() > 18) || (java.time.LocalTime.now().getHour() < 4)) {
            for (int i = 0; i < 16; i++) {
                tileBackground.add(new Image("res/Tiles/BasicTileNight.png"));
            }
        } else {
            for (int i = 0; i < 16; i++) {
                tileBackground.add(new Image("res/Tiles/BasicTile.png"));
            }
        }

        int amountInRow = 0;
        int rows = 1;
        for (Image image: tileBackground) {
            image.drawFromTopLeft(amountInRow * image.getWidth(), Window.getHeight() - (rows * image.getHeight()));
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

    public void removeSound(Music music) {
        musics.remove(music);
    }

    public boolean hasSound(String filePath) {
        for (Music music: musics) {
            if (music.musicPath.equals(filePath)) {
                return true;
            }
        }
        return false;
    }

    public Music findSound(String filePath) {
        for (Music music: musics) {
            if (music.musicPath.equals(filePath)) {
                return music;
            }
        }
        return null;
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
}