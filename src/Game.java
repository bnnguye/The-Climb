import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;


/** "The Climb" - A game created by Bill Nguyen **/

public class Game extends AbstractGame {

    private final int FONT_SIZE = 80;
    private final int DIALOGUE_FONT_SIZE = 30 ;
    private final int MAX_DIALOGUE_LIMIT = Window.getWidth()/(DIALOGUE_FONT_SIZE - 10);

    private static SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private static GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();

    // Stat variable
    int[] statTracker = new int[4];

    private final double frames = settingsSingleton.getFrames();
    private int countDown = 0;
    private int waitTimer = 840;
    private int winnerTimer = 0;
    private int currentFrame = 0;

    private ArrayList<StringDisplay> stringDisplays;

    private final Font titleFont = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    private final Font victoryFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 110);
    private final Font playerFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 50);
    private final Font gameFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
    private final Font chooseCharacterFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 100);
    private final Font countdownFont = new Font("res/fonts/conformable.otf", 300);
    private final Font dialogueFont = new Font("res/fonts/DejaVuSans-Bold.ttf", DIALOGUE_FONT_SIZE);
    private final Font playerMapFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
    private final Font introFont = new Font("res/fonts/Storytime.ttf", 80);
    private final DrawOptions DO;

    private double intro = 0;

    private ArrayList<Button> buttons;
    private ArrayList<Character> characters;
    private ArrayList<Player> players;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Obstacle> obstaclesToRemove;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<PowerUp> powerUpsToRemove;
    private ArrayList<Character> allCharacters;
    Character Chizuru;
    Character zeroTwo;
    Character Miku;
    Character Mai;
    Character Nino;
    Character Futaba;
    Character Ruka;
    Character Sakuta;
    Character unlocked;
    Character Unknown;
    Character Aki;
    Character Tutorial;
    Character Dio;
    Character Chika;
    Character Emilia;
    Character Asuna;
    Character Raphtalia;
    private ArrayList<SideCharacter> sideCharacters;
    private ArrayList<SideCharacter> allSideCharacters;
    SideCharacter Zoro;
    SideCharacter Gojo;
    SideCharacter AllMight;
    SideCharacter Lelouch;
    SideCharacter Hisoka;
    SideCharacter Jotaro;
    SideCharacter Itachi;
    SideCharacter Yugi;
    SideCharacter Puck;
    SideCharacter DioS;
    SideCharacter Yuu;

    private double spacer = 300;
    private String currentMusic;
    private final Music soundEffectMusic;
    private final Music mainMusic;
    private ArrayList<Music> musics;
    private boolean picked;
    Rectangle bottomRectangle;
    Rectangle topRectangle;

    // custom map variables
    private ArrayList<Map> playableMaps;
    Map map;
    ArrayList<Tile> allTiles;
    ArrayList<Tile> customMapTiles;
    boolean addingTile = false;
    int page = 0;
    Tile tile1;
    Tile tile2;
    Tile tile3;
    int offset = 0;


    private boolean winnerPlayed = false;
    boolean playingAnimation = false;
    private String[] dialogueLine;
    Scanner dialogueScanner;
    ArrayList<String> dialogueWords;

    double timeToSaveStats = 60 * frames;
    int currentTimeToSaveStats = 0;

    //Computer variables
    private ArrayList<Point> route;


    //Story variables
    private boolean playingDialogue = false;
    private boolean playingStory = false;
    private boolean playingScene = false;
    private boolean playingMap = false;
    private int currentDialogue = 0;
    private int currentStory = 0;
    private int currentScene = 0;
    private int lastStory = -1;
    private int lastScene = -1;
    private Image currentBackground;
    private Point currentBackgroundPoint = new Point(0,0);
    private String dialogueString = "";
    private Colour dialogueColour = new Colour(77.0/255, 57.0/255, 37.0/255, 0.5);
    private double dialogueWidth = Window.getWidth()*0.1;
    private double dialogueLength = Window.getHeight() - 300;
    private int maxLines = 7;
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
    private String menuTitle;

    private final Colour black = new Colour(0, 0, 0, 1);
    private final Colour white = new Colour(1, 1, 1, 1);
    Colour red = new Colour(1, 0.3, 0.3);
    Colour darken = new Colour(0, 0, 0, 0.85);

    // Game Settings variables
    ArrayList<PowerUp> allPowerUps;
    ArrayList<Obstacle> allObstacles;
    String pageType;

    public static void main(String[] args) {
        new Game().run();
    }

    public Game()   {
        super(1920, 1080, "The Climb");

        DO = new DrawOptions();
        stringDisplays = new ArrayList<>();
        players = new ArrayList<>();
        players.add( new PlayerOne());
        characters = new ArrayList<>();
        allCharacters = new ArrayList<>();
        sideCharacters= new ArrayList<>();
        allSideCharacters = new ArrayList<>();
        route = new ArrayList<>();
        allPowerUps = new ArrayList<>();
        allObstacles = new ArrayList<>();

        // initialize characters
        Chizuru = new CharacterChizuru();
        zeroTwo = new CharacterZeroTwo();
        Miku = new CharacterMiku();
        Mai = new CharacterMai();
        Nino = new CharacterNino();
        Futaba = new CharacterFutaba();
        Ruka = new CharacterRuka();
        Aki = new CharacterAki();
        Chika = new CharacterChika();
        Emilia = new CharacterEmilia();
        Asuna = new CharacterAsuna();
        Raphtalia = new CharacterRaphtalia();
        Tutorial = new Tutorial();
        Dio = new CharacterDio();
        characters.add(Chizuru);
        characters.add(zeroTwo);
        characters.add(Miku);
        characters.add(Mai);
        Sakuta = new Sakuta();
        Unknown = new Unknown();
        allCharacters.add(Unknown);
        allCharacters.add(Sakuta);
        allCharacters.add(Chizuru);
        allCharacters.add(zeroTwo);
        allCharacters.add(Miku);
        allCharacters.add(Mai);
        allCharacters.add(Nino);
        allCharacters.add(Futaba);
        allCharacters.add(Ruka);
        allCharacters.add(Aki);
        allCharacters.add(Tutorial);
        allCharacters.add(Chika);
        allCharacters.add(Emilia);
        allCharacters.add(Asuna);
        allCharacters.add(Dio);
        for(Character character: allCharacters) {
            character.setStats();
        }
        Zoro = new SideZoro();
        Gojo = new SideGojo();
        AllMight = new SideAllMight();
        Lelouch = new SideLelouch();
        Hisoka = new SideHisoka();
        Jotaro = new SideJotaro();
        Itachi = new SideItachi();
        Yugi = new SideYugi();
        Puck = new SidePuck();
        DioS = new SideDio();
        Yuu = new SideYuu();
        sideCharacters.add(Zoro);
        sideCharacters.add(Gojo);
        sideCharacters.add(AllMight);
        sideCharacters.add(Lelouch);
        sideCharacters.add(Hisoka);
        sideCharacters.add(Jotaro);
        sideCharacters.add(DioS);
        sideCharacters.add(Itachi);
        sideCharacters.add(Yugi);
        sideCharacters.add(Puck);
        sideCharacters.add(Yuu);
        checkAchievements();
        buttons = new ArrayList<>();
        obstacles = new ArrayList<>();
        obstaclesToRemove = new ArrayList<>();
        powerUps = new ArrayList<>();
        powerUpsToRemove = new ArrayList<>();

        soundEffectMusic = new Music();
        mainMusic = new Music();
        musics = new ArrayList<>();

        playableMaps = new ArrayList<>();
        playableMaps.add(new MapTrainingGround());
        playableMaps.add(new MapPark());
        playableMaps.add(new MapSpookySpikes());
        playableMaps.add(new MapGreedIsland());
        playableMaps.add(new MapMansion());
        playableMaps.add(new MapClaustrophobicLane());
        playableMaps.add(new MapUpsideDownCups());
        playableMaps.add(new MapCustom());
        customMapTiles = new ArrayList<>();
        allTiles = new ArrayList<>();
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
        allPowerUps.add(new SpeedUp());
        allPowerUps.add(new Minimiser());
        allPowerUps.add(new Shield());
        allPowerUps.add(new NoblePhantasm());
        allObstacles.add(new Ball());
        allObstacles.add(new Rock());



        bottomRectangle = new Rectangle(0, Window.getHeight() - 20, Window.getWidth(), 20);
        topRectangle = new Rectangle(0, 0, Window.getWidth(), 20);
        menuBackground = new Image("res/menu/MainMenu.PNG");
        menuTitle = "";
        currentMusic = "music/Silence.wav";
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
        changeMainMusic(currentMusic);
        updateDisplayStrings();
        updateSounds();
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
        settingsSingleton.updateTime();

        if (settingsSingleton.getGameState() > -1) {
            if (menuBackground != null) {
                menuBackground.drawFromTopLeft(0, 0);
            }
            else {
                drawMapBackground();
            }

            if (menuTitle != null) {
                titleFont.drawString(menuTitle, 0, 65, DO.setBlendColour(black));
            }
            if (buttons.size() > 0 ) {
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


        if (settingsSingleton.getGameState() == -1) {
            playIntro();
        }
        else if (settingsSingleton.getGameState() == 0) {
            currentMusic = "music/Game Main Menu.wav";
            saveStatsChecker();
            if (!settingsSingleton.getGameStateString().equals("Main Menu")) {
                buttons.clear();
                buttons.add(new ButtonPlay("PLAY", new Rectangle(new Point(0, 440), Window.getWidth(), 160)));
                buttons.add(new ButtonCreateMap("CREATE MAP", new Rectangle(new Point(0, 600), Window.getWidth(), 160)));
                buttons.add(new ButtonExit("EXIT", new Rectangle(new Point(0, 760), Window.getWidth(), 160)));
                settingsSingleton.setGameStateString("Main Menu");
                menuBackground = new Image("res/menu/MainMenu.PNG");
                menuTitle = "THE CLIMB";
            }
        }
        else if (settingsSingleton.getGameState() == 1) {
            if (!settingsSingleton.getGameStateString().equals("Level")) {
                Button storyButton = new ButtonStory("STORY", new Rectangle(0, Window.getHeight()/2.5 - 160, Window.getWidth(), 160));
                Button versusButton = new ButtonVersus("VS",new Rectangle(0, Window.getHeight() / 2.5 + 160, Window.getWidth(), 160));
                buttons.clear();
                buttons.add(storyButton);
                buttons.add(versusButton);
                settingsSingleton.setGameStateString("Level");
                menuBackground = new Image("res/menu/levelMenu.PNG");
                menuTitle = "SELECT GAME MODE";
            }
        }
        else if (settingsSingleton.getGameState() == 2) {
            if (!settingsSingleton.getGameStateString().equals("Players")) {
                buttons.clear();
                Button twoPlayerButton = new ButtonTwoPlayer("2", new Rectangle(0, Window.getHeight() / 2 - 160, Window.getWidth(), 160));
                Button threePlayerButton = new ButtonThreePlayer("3", new Rectangle(0, Window.getHeight() / 2, Window.getWidth(), 160));
                Button fourPlayerButton = new ButtonFourPlayer("4", new Rectangle(0, Window.getHeight() / 2 + 160, Window.getWidth(), 160));
                buttons.add(twoPlayerButton);
                buttons.add(threePlayerButton);
                buttons.add(fourPlayerButton);
                settingsSingleton.setGameStateString("Players");
                menuBackground = new Image("res/menu/playerMenu.PNG");
                menuTitle = "PLAYERS";
            }
            if (settingsSingleton.getPlayers() != 0) {
                Image playerArt = new Image(String.format("res/menu/%dPlayers.PNG", settingsSingleton.getPlayers()));
                playerArt.draw(Window.getWidth()/2, Window.getHeight()/2);
            }
        }
        else if (settingsSingleton.getGameState() == 3) {
            if (!settingsSingleton.getGameStateString().equals("Character")) {
                spacer = 300;
                buttons.clear();
                buttons.add(new ButtonGameSettings("Settings", new Rectangle(Window.getWidth()/2, 0 , titleFont.getWidth("Settings"), 100)));
                players.clear();
                settingsSingleton.setGameStateString("Character");
                players.add(new PlayerOne());
                players.add(new PlayerTwo());
                if (settingsSingleton.getPlayers() == 3) {
                    players.add(new PlayerThree());
                }
                if (settingsSingleton.getPlayers() == 4) {
                    players.add(new PlayerThree());
                    players.add(new PlayerFour());
                }
                menuBackground = new Image("res/menu/characterMenu.png");
                menuTitle = "CHOOSE WAIFU";
            }

            // draw character Icons
            int row = 1;
            int currentIcon = 1;
            Image locked = new Image("res/icons/Unknown.png");
            for (Character character: characters) {
                character.getIcon().drawFromTopLeft(-240 +spacer * currentIcon, -200 + spacer * row);
                character.setIconPos(new Point(-240 +spacer * currentIcon + character.getIcon().getWidth()/2, -200 + spacer * row + character.getIcon().getHeight()/2));
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
                    if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
                        if (input.wasPressed(player.getKey())) {
                            pickCharacter(player, character);
                        }
                    }
                    playerFont.drawString(String.format("Wins: %d", player.getNoOfWins()), 310 + (player.getId() - 1) * border.getWidth()*3/2, Window.getHeight() - 40);
                }
            }

            // draw player cursors
            picked = true;
            for (Player player: players) {

                player.getCursor().drawFromTopLeft(player.getPos().x, player.getPos().y);
                player.setPos(input);
                if (player.getCharacter() == null) {
                    picked = false;
                }
            }
            if (picked) {
                if (waitTimer < 0) {
                    if(settingsSingleton.getGameMode() == 99) {
                        settingsSingleton.setGameState(6);
                    }
                    else {
                        settingsSingleton.setGameState(4);
                    }
                }
                else {
                    waitTimer--;
                }
            }
            else {
                waitTimer = 840;
            }

        }
        else if (settingsSingleton.getGameState() == 4) { // Comrades
            if (!settingsSingleton.getGameStateString().equals("Comrade")) {
                menuTitle = "CHOOSE HUSBANDO";
                menuBackground = new Image("res/menu/mapMenu.png");
                settingsSingleton.setGameStateString("Comrade");
            }
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
            for (SideCharacter character : sideCharacters) {
                for (Player player : players) {
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
                    if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
                        if (input.wasPressed(player.getKey())) {
                            pickSideCharacter(player, character);
                        }
                    }
                    playerFont.drawString(String.format("Wins: %d", player.getNoOfWins()), 310 + (player.getId() - 1) * border.getWidth() * 3 / 2, Window.getHeight() - 40);
                }
            }

            // draw player cursors
            picked = true;
            for (Player player: players) {
                player.getCursor().drawFromTopLeft(player.getPos().x, player.getPos().y);
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
                menuTitle = "Which Climb?";
                menuBackground = null;
                settingsSingleton.setGameStateString("MAP");
            }
            spacer = 400;
            int row = 1;
            int currentIcon = 1;
            Image locked = new Image("res/mapPeeks/locked.png");
            ArrayList<Map> mapsChosen = new ArrayList<>();
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
            for (Player player : players) {
                player.getCharacter().setPosition(player.getPos());
                player.getCharacter().draw();
                player.setPos(input);
                row = 1;
                currentIcon = 2;
                for (Map map : playableMaps) {
                    if (map.getMapPeek().getBoundingBoxAt(new Point(-425 + map.getMapPeek().getWidth() * currentIcon + (row - 1) *400, -50 + spacer * row)).intersects(player.getPos())) {
                        if(input.wasPressed(player.getKey())) {
                            pickMap(player, map);
                        }

                        // display map name being hovered by players
                        if(player.getMapChosen() == null) {
                            playerMapFont.drawString(String.format("P%d: %s", player.getId(), map.getName()), (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1), Window.getHeight() - 15, DO.setBlendColour(white));
                        }
                    }
                    else {
                        if(player.getMapChosen() != null) {
                            playerMapFont.drawString(String.format("P%d: %s", player.getId(), player.getMapChosen().getName()), (player.getId() - 1) * (Window.getWidth()+300) / (players.size() + 1), Window.getHeight() - 15, DO.setBlendColour(white));
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
                        settingsSingleton.setMapNo(player.getMapChosen().getLevel());
                        for (Map mapChosen: playableMaps) {
                            if (mapChosen.getLevel() == settingsSingleton.getMapNo()) {
                                map = mapChosen;
                                map.generateMap();
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
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
            if (settingsSingleton.getGameMode() == 0) {
                if (!settingsSingleton.getGameStateString().equals("Game")) {
                    buttons.clear();
                    setPlayersPosition();
                    currentMusic = String.format("music/Fight%d.wav", settingsSingleton.getMapNo());
                    resetMusic();
                    playSound("music/Start.wav");
                    settingsSingleton.setGameStateString("Game");
                }
                startCountdown();
                render();
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
                } else {
                    playingAnimation = false;
                    boolean characterMusic = false;
                    for (Player player : players) {
                        if (player.getSideCharacter().getName().equals("Yugi")) {
                            player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                        }
                        if (input.wasPressed(player.getKey())) {
                            if (player.getCharacter().hasNoblePhantasm()) {
                                if (!player.getSideCharacter().isActivating()) {
                                    playSound(String.format("music/%s.wav", player.getSideCharacter().getName()));
                                    player.getCharacter().useNoblePhantasm();
                                }
                                player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                            }
                        }
                        if (player.getSideCharacter().isActivating()) {
                            currentMusic = "music/Silence.wav";
                            player.getSideCharacter().activateAbility(player, players, obstacles, powerUps, map);
                            if(player.getSideCharacter().isAnimating()) {
                                playingAnimation = true;
                                if (player.getSideCharacter().getName().equals("Itachi")) {
                                    Itachi.setLeft(input.isDown(player.getKey()));
                                }
                            }
                            characterMusic = true;
                        }
                    }
                    if (!characterMusic) {
                        currentMusic = String.format("music/Fight%d.wav", settingsSingleton.getMapNo());
                    }
                    if(!map.hasFinished()) {
                        if(!playingAnimation) {
                            displayCharacterStats(players);
                            map.updateTiles(gameSettingsSingleton.getMapSpeed());
                            spawnObstacles();
                            spawnPowerUps();
                        }
                    }
                    else {
                        if (!playingAnimation) {
                            titleFont.drawString("CLEAR! REACH THE TOP!", 16, FONT_SIZE, DO.setBlendColour(black));
                        }
                        for (Player player : players) {
                            if (player.getCharacter().getPos().distanceTo(new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                settingsSingleton.setGameState(7);
                                settingsSingleton.setWinner(player);
                                settingsSingleton.setGameStateString("Win");
                                break;
                            }
                        }
                    }
                    updateObjects();
                    updatePlayerMovement(input);
                    checkCollisionPowerUps();
                    checkCollisionObstacles();
                    checkCollisionTiles();
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
                    buttons.clear();
                    settingsSingleton.setGameStateString("Story");
                    setPlayersPosition();
                    map = mapToTransitionTo;
                    if (map != null) {
                        map.generateMap();
                    }
                }
                if (map != null) {
                    render();
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
                                        mapToTransitionTo = new MapTrainingGround2();
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
                            currentMusic = "music/Dio.wav";
                            dark = true;
                            if (lastStory != currentStory) {
                                setPlayersPosition();
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
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
                                mapToTransitionTo = new MapTrainingGround();
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
                                currentMusic = Dio.playLine();
                            }
                            if (endDialogue) {
                                startTransition();
                                mapToTransitionTo = new MapDioMansion();
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
                            dialogueFont.drawString(dialogueString, dialogueWidth + DIALOGUE_FONT_SIZE, dialogueLength + DIALOGUE_FONT_SIZE);
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
                                            soundEffectMusic.played = true;
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
            render();
            if (settingsSingleton.getGameMode() == 99) {
                displayFailScreen();
                if (settingsSingleton.getGameStateString().equals("Continue")) {
                    changeMainMusic(currentMusic);
                    buttons.clear();
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
                    buttons.clear();
                    Button backToStartButton = new ButtonBackToStart("Main Menu",new Rectangle(new Point(0, Window.getHeight() / 1.5 + 160), Window.getWidth(), 160));
                    Button retryButton = new ButtonRetry("Restart", new Rectangle(new Point(0, Window.getHeight() / 2 + 160), Window.getWidth(), 160));
                    buttons.add(backToStartButton);
                    buttons.add(retryButton);
                }
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if((settingsSingleton.getGameStateString().equals("Retry")) || (settingsSingleton.getGameStateString().equals("Menu"))) {
                        resetMusic();
                        for (SideCharacter character: sideCharacters) {
                            character.reset();
                        }
                        for (Character character: characters) {
                            character.stopMusic();
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
                            buttons.clear();
                        }
                        else if (settingsSingleton.getGameStateString().equals("Menu")) {
                            map = null;
                            unlocked = checkAchievementsInGame();
                            if (unlocked != null) {
                                settingsSingleton.setGameState(8);
                                saveStats();
                            }
                            else {
                                settingsSingleton.setGameState(0);
                            }
                            players.clear();
                            buttons.clear();
                        }
                        if (map != null) {
                            map.setJotaroAbility(false);
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
                settingsSingleton.setGameStateString("Unlocked");
            }
            Colour black = new Colour(0, 0, 0, (float) (mainMusic.clip.getFrameLength() - mainMusic.clip.getFramePosition())/ (float) mainMusic.clip.getFrameLength());
            Image background = new Image(String.format("res/background/%s.png", unlocked.getName()));
            background.drawFromTopLeft(0,0);
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
                map = new MapCustom();
                map.generateMap();
                customMapTiles.removeAll(customMapTiles);
                for (Tile tile: map.getTiles()) {
                    customMapTiles.add(tile);
                }
                buttons.clear();
                settingsSingleton.setGameStateString("Create Your Own Map");
                menuBackground = null;
                menuTitle = null;
            }

            for (Tile tile: customMapTiles) {
                tile.draw();
            }


            if (!addingTile) {
                playerFont.drawString("S: Save and Exit ESC: Exit without Saving Arrow Keys: Navigate\nA: Add, R: Remove last block", 100, 50);
                if (input.wasPressed(Keys.UP)) {
                    updateTiles(100);
                    offset++;
                }
                if (input.wasPressed((Keys.DOWN))) {
                    updateTiles(-100);
                    offset--;
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
                settingsSingleton.setGameStateString("Game Settings");
                buttons.clear();
                buttons.add(new ButtonLeftArrow("", new Rectangle(new Point(Window.getWidth() - 350, 100), 100, 100)));
                buttons.add(new ButtonRightArrow("", new Rectangle(new Point(Window.getWidth() - 200, 100), 100, 100)));
                pageType = "PowerUps";
            }
            int index = 0;
            double minimumFrequency = 0.98;
            if (pageType.equals("General")) {
                titleFont.drawString(String.format("Map Speed: %1.2f", gameSettingsSingleton.getMapSpeed()), 100, 300 + index*100);
                Image leftButton = new Image("res/LeftArrow.png");
                Image rightButton = new Image("res/RightArrow.png");
                leftButton.drawFromTopLeft(titleFont.getWidth("Map Speed: 1000") + 50, 175 + index*100, new DrawOptions().setScale(0.3,0.3));
                rightButton.drawFromTopLeft(titleFont.getWidth("Map Speed: 1000") + 50 + rightButton.getWidth()*0.5, 175 + index*100, new DrawOptions().setScale(0.3, 0.3));
                Rectangle leftButtonRectangle = new Rectangle(new Point(titleFont.getWidth("Map Speed: 1000") + 60 + leftButton.getWidth()*0.3, 175 + index*100 + rightButton.getHeight()*0.35), leftButton.getWidth()*0.3, leftButton.getHeight()*0.3);
                Rectangle rightButtonRectangle = new Rectangle(new Point(titleFont.getWidth("Map Speed: 1000") + 60 + rightButton.getWidth()*0.3 + rightButton.getWidth()*0.5, 175 + index*100 + rightButton.getHeight()*0.35), leftButton.getWidth()*0.3, leftButton.getHeight()*0.3);
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if (leftButtonRectangle.intersects(input.getMousePosition())) {
                        if (gameSettingsSingleton.getMapSpeed() > 1) {
                            gameSettingsSingleton.setMapSpeed(gameSettingsSingleton.getMapSpeed() - 0.1);
                        }
                    }
                    else if (rightButtonRectangle.intersects(input.getMousePosition())) {
                        if (gameSettingsSingleton.getMapSpeed() < 3) {
                            gameSettingsSingleton.setMapSpeed(gameSettingsSingleton.getMapSpeed() + 0.1);
                        }
                    }
                }
            }
            else if (pageType.equals("PowerUps")) {
                for (int i = 0; i < allPowerUps.size(); i++) {
                    PowerUp currentPowerUp = allPowerUps.get(i);
                    Image currentImage = currentPowerUp.getImage();
                    double currentFrequency = 500 - (gameSettingsSingleton.getInstance().getPowerUpsSettingsSingleton().getFrequency(currentPowerUp.getName()) - minimumFrequency)*500/(1 - minimumFrequency);
                    Rectangle maxRectangle = new Rectangle(100 + currentImage.getWidth()*2, 300 + index*100, 500, currentImage.getHeight());
                    currentImage.drawFromTopLeft(100, 300 + index*100);
                    if ((input.wasPressed(MouseButtons.LEFT)) && (currentImage.getBoundingBoxAt(new Point(100 + currentImage.getWidth()/2, currentImage.getHeight()/2 + 300 + index*100)).intersects(input.getMousePosition()))) {
                        gameSettingsSingleton.getPowerUpsSettingsSingleton().toggle(currentPowerUp.getName());
                    }
                    else if ((input.isDown(MouseButtons.LEFT)) && (maxRectangle.intersects(input.getMousePosition()))) {
                        double position = input.getMouseX() - maxRectangle.left();
                        gameSettingsSingleton.getPowerUpsSettingsSingleton().changeFrequency(currentPowerUp.getName(), 1 - (1 - minimumFrequency)*(position/(maxRectangle.right() - maxRectangle.left())));

                    }
                    if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp(currentPowerUp.getName())) {
                        Drawing.drawRectangle(100 + currentImage.getWidth()*2, 300 + index*100, 500, currentImage.getHeight(), new Colour(0, 0, 0, 0.5));
                        Drawing.drawRectangle(100 + currentImage.getWidth()*2, 300 + index*100, currentFrequency, currentImage.getHeight(), new Colour(0, 0, 0));
                    }
                    index++;
                }
            }
            else if (pageType.equals("Obstacles")) {
                for (int j = 0; j < allObstacles.size(); j++) {
                    Obstacle currentObstacle = allObstacles.get(j);
                    Image currentImage = currentObstacle.getImage();
                    double currentFrequency = 500 - (gameSettingsSingleton.getInstance().getObstaclesSettingsSingleton().getFrequency(currentObstacle.getName()) - minimumFrequency)*500/(1 - minimumFrequency);
                    Rectangle maxRectangle = new Rectangle(100 + currentImage.getWidth()*2, 300 + index*100, 500, currentImage.getHeight());
                    currentImage.drawFromTopLeft(100, 300 + index*100);
                    if ((input.wasPressed(MouseButtons.LEFT)) && (currentImage.getBoundingBoxAt(new Point(100 + currentImage.getWidth()/2, currentImage.getHeight()/2 + 300 + index*100)).intersects(input.getMousePosition()))) {
                        gameSettingsSingleton.getObstaclesSettingsSingleton().toggle(currentObstacle.getName());
                    }
                    else if ((input.isDown(MouseButtons.LEFT)) && (maxRectangle.intersects(input.getMousePosition()))) {
                        double position = input.getMouseX() - maxRectangle.left();
                        gameSettingsSingleton.getObstaclesSettingsSingleton().changeFrequency(currentObstacle.getName(), 1 - (1 - minimumFrequency)*(position/(maxRectangle.right() - maxRectangle.left())));

                    }
                    if (gameSettingsSingleton.getObstaclesSettingsSingleton().isObstacle(currentObstacle.getName())) {
                        Drawing.drawRectangle(100 + currentImage.getWidth()*2, 300 + index*100, 500, currentImage.getHeight(), new Colour(0, 0, 0, 0.5));
                        Drawing.drawRectangle(100 + currentImage.getWidth()*2, 300 + index*100, currentFrequency, currentImage.getHeight(), new Colour(0, 0, 0));
                    }
                    index++;
                }
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
            menuTitle = pageType;
        }
        if (((settingsSingleton.getGameState() > 0) && (settingsSingleton.getGameState() < 6)) || (settingsSingleton.getGameState() == 10)) {
            Image back = new Image("res/BackArrow.PNG");
            back.draw(Window.getWidth() - back.getWidth(), back.getHeight());
            if (back.getBoundingBoxAt(new Point(Window.getWidth() - back.getWidth(), back.getHeight())).intersects(input.getMousePosition())) {
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if (settingsSingleton.getGameState() == 3) {
                        players.removeAll(players);
                        settingsSingleton.setGameState(settingsSingleton.getGameState() - 1);
                    }
                    else if (settingsSingleton.getGameState() == 5) {
                        for (Player player: players) {
                            player.setMapChosen(null);
                            player.setSideCharacter(null);
                            settingsSingleton.setGameState(settingsSingleton.getGameState() - 1);
                        }
                    }
                    else if (settingsSingleton.getGameState() == 10) {
                        if (back.getBoundingBoxAt(new Point(Window.getWidth() - back.getWidth(), back.getHeight())).intersects(input.getMousePosition())) {
                            settingsSingleton.setGameState(settingsSingleton.getLastGameState());
                        }
                    }
                    else {
                        settingsSingleton.setGameState(settingsSingleton.getGameState() - 1);
                    }
                }
            }
        }
        drawButtons();
        showDisplayStrings();
    }

    public boolean checkStats(String characterName, int line, int threshold) {
        for (Character character: allCharacters) {
            if (character.getName().equals(characterName)) {
                return character.getStats()[line] >= threshold;
            }
        }
        return false;
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
                            //currentLineLength = word.length() + 2;
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

    public void checkAchievements() {
        try {
            Scanner achievementScanner = new Scanner(new File("stats/Achievements.txt"));
            while (achievementScanner.hasNextLine()) {
                String[] achievementLine = achievementScanner.nextLine().split(" ");
                switch (achievementLine[0]) {
                    case "Nino": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Nino);
                        }
                        break;
                    }
                    case "Futaba": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Futaba);
                        }
                        break;
                    }
                    case "Ruka": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Ruka);
                        }
                        break;
                    }
                    case "Aki": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Aki);
                        }
                        break;
                    }
                    case "Chika": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Chika);
                        }
                        break;
                    }
                    case "Emilia": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Emilia);
                        }
                        break;
                    }
                    case "Asuna": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Asuna);
                        }
                        break;
                    }
                    case "Raphtalia": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            characters.add(Raphtalia);
                        }
                        break;
                    }
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
                switch (achievementLine[0]) {
                    case "Nino": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Nino)) {
                                characters.add(Nino);
                                return Nino;
                            }
                        }
                        break;
                    }
                    case "Futaba": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Futaba)) {
                                characters.add(Futaba);
                                return Futaba;
                            }
                        }
                        break;
                    }
                    case "Ruka": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Ruka)) {
                                characters.add(Ruka);
                                return Ruka;
                            }
                        }
                        break;
                    }
                    case "Aki": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Aki)) {
                                characters.add(Aki);
                                return Aki;
                            }
                        }
                        break;
                    }
                    case "Chika": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Chika)) {
                                characters.add(Chika);
                                return Chika;
                            }
                        }
                        break;
                    }
                    case "Emilia": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Emilia)) {
                                characters.add(Emilia);
                                return Emilia;
                            }
                        }
                        break;
                    }
                    case "Asuna": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Asuna)) {
                                characters.add(Asuna);
                                return Asuna;
                            }
                        }
                        break;
                    }
                    case "Raphtalia": {
                        String name = achievementLine[1];
                        int threshold = Integer.parseInt(achievementLine[3]);
                        int line = Integer.parseInt(achievementLine[2]);
                        if (checkStats(name, line, threshold)) {
                            if (!characters.contains(Raphtalia)) {
                                characters.add(Raphtalia);
                                return Raphtalia;
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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

            stringDisplays.add(new StringDisplay("Story saved successfully.", 3));

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
            if (player.getCharacter().hasNoblePhantasm()) {
                if (!player.getSideCharacter().getName().equals("Yugi")) {
                    Image sideCharacter = new Image(String.format("res/inGame/%s.png", player.getSideCharacter().getName()));
                    sideCharacter.drawFromTopLeft(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight());
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
        if (java.time.LocalTime.now().getHour() > 18) {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(new Colour(1,1,1)));
        }
        else {
            gameFont.drawString(String.format("%4.0f/%4.0fm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, new DrawOptions().setBlendColour(new Colour(0, 0, 0)));
        }
    }

    public void startTransition() {
        transitionTimer = 5 * frames;
    }

    public void checkCollisionTiles() {
        for (Player player: players) {
            if (!map.hasFinished()) {
                Drawing.drawRectangle(0, Window.getHeight() - 20, Window.getWidth(), 20, red);
                Drawing.drawRectangle(0, 0, Window.getWidth(), 20, red);
                if ((player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(bottomRectangle)) || (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(topRectangle))) {
                    if (!player.isDead()) {
                        player.setDead();
                    }
                    break;
                }
            }
            for (Tile tile : map.getVisibleTiles()) {
                if ((tile.getType().equals("Ice")) && (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(tile.getImage().getBoundingBoxAt(new Point(tile.getPos().x + tile.getImage().getWidth()/2, tile.getPos().y + tile.getImage().getHeight()/2))))) {
                    player.getCharacter().onIce();
                }
                else if ((tile.getType().equals("Slow")) && (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(tile.getImage().getBoundingBoxAt(new Point(tile.getPos().x + tile.getImage().getWidth()/2, tile.getPos().y + tile.getImage().getHeight()/2))))) {
                    player.getCharacter().onSlow();
                }
                if (tile.getCollisionBlocks().size() > 0) {
                    if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(tile.getImage().getBoundingBoxAt(new Point(tile.getPos().x + tile.getImage().getWidth()/2, tile.getPos().y + tile.getImage().getHeight()/2)))) {
                        for (CollisionBlock block : tile.getCollisionBlocks()) {
                            if (block.hasCollided(player)) {
                                if(!player.isDead()) {
                                    player.setDead();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updatePlayerMovement(Input input) {
        for (Player player: players) {
            if (!player.isDead()) {
                if (!playingAnimation) {
                    player.moveCharacter(input);
                }
            }
        }
    }

    public void checkCollisionObstacles() {
        for (Obstacle obstacle: obstacles) {
            for (Player player : players) {
                if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(obstacle.getImage().getBoundingBoxAt(new Point(obstacle.getPos().x + obstacle.getImage().getWidth()/2, obstacle.getPos().y + obstacle.getImage().getHeight()/2)))) {
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
                                player.setDead();
                                break;
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
                if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(powerUp.getImage().getBoundingBoxAt(new Point(powerUp.getPos().x + 25, powerUp.getPos().y + 25)))) {
                    if (!player.isDead()) {
                        player.getCharacter().getPowerUp(powerUp);
                        powerUpsToRemove.add(powerUp);
                        break;
                    }
                }
            }
        }
        powerUps.removeAll(powerUpsToRemove);
    }

    public void spawnPowerUps() {
        double spawnNo = Math.random()*5;;
        if (spawnNo < 1) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("SpeedUp")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("SpeedUp")) {
                    powerUps.add(new SpeedUp());
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
                    powerUps.add(new Minimiser());
                }
            }
        }
        else if (spawnNo < 4) {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("Shield")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("Shield")) {
                    powerUps.add(new Shield());
                }
            }
        }
        else {
            if (gameSettingsSingleton.getPowerUpsSettingsSingleton().isPowerUp("NoblePhantasm")) {
                if (Math.random() > gameSettingsSingleton.getPowerUpsSettingsSingleton().getFrequency("NoblePhantasm")) {
                    powerUps.add(new NoblePhantasm());
                }
            }
        }
    }

    public void spawnObstacles() {
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isRocks()) {
            if (Math.random() > gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getRockFrequency()) {
                obstacles.add(new Rock());
            }
        }
        if (gameSettingsSingleton.getObstaclesSettingsSingleton().isBalls()) {
            if (Math.random() > gameSettingsSingleton.getObstaclesSettingsSingleton().getInstance().getBallFrequency()) {
                obstacles.add(new Ball());
            }
        }
    }

    public void render() {
        map.draw();
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
            titleFont.drawString("CLEAR! REACH THE TOP!", 16, FONT_SIZE, DO.setBlendColour(black));
        }
        else {
            drawCurrentHeight();
        }
    }

    public void displayFailScreen() {
        if (failed) {
            failed = false;
            buttons.clear();
            buttons.add(new ButtonRetry("Continue?", new Rectangle(new Point(0, Window.getHeight() / 2), Window.getWidth(), 160)));
            buttons.add(new ButtonBackToStart("Exit", new Rectangle(new Point(0, Window.getHeight() / 1.5), Window.getWidth(), 160)));
            currentMusic = "music/Fail.wav";
        }
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.7));
        victoryFont.drawString("YOU FAILED THE CLIMB!", 30, 110);
    }

    public void updateObjects() {
        for (Obstacle obstacle : obstacles) {
            if (!playingAnimation) {
                obstacle.move();
            }
            if ((obstacle.getPos().y > Window.getHeight()) && (!obstaclesToRemove.contains(obstacle))) {
                obstaclesToRemove.add(obstacle);
            }
        }
        for (PowerUp powerUp : powerUps) {
            if (!playingAnimation) {
                powerUp.move();
            }
            if (powerUp.getPos().y > Window.getHeight()) {
                powerUpsToRemove.add(powerUp);
            }
        }
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
            playerFont.drawString(stringDisplay.getName(), Window.getWidth() - playerFont.getWidth(stringDisplay.getName()), 60*i);
            i++;
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
        stringDisplays.add(new StringDisplay(string, time));
    }

    public void setPlayersPosition() {
        double spawnDivider = 1;
        for (Player player : players) {
            player.getCharacter().setPosition(new Point(spawnDivider * Window.getWidth() / (players.size() + 2), Window.getHeight() - 100));
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
        if (playersFinished >= players.size()) {
            return true;
        }
        return false;
    }

    public void playIntro() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(1, 1, 1, intro/(2*frames)));
        intro++;
        if (intro >= 2*frames) {
            introFont.drawString("Made by Bill Nguyen", Window.getWidth()/2 - introFont.getWidth("Made by Bill Nguyen")/2, Window.getHeight()/2, DO.setBlendColour(new Colour(0,0,0,(intro - 2*frames)/(1*frames))));
            if (intro == 2*frames) {
                soundEffectMusic.playMusic("music/Intro.wav");
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

}