import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Game extends AbstractGame {

    private final int FONT_SIZE = 80;
    private final int DIALOGUE_FONT_SIZE = 30 ;
    private final int MAX_DIALOGUE_LIMIT = Window.getWidth()/(DIALOGUE_FONT_SIZE - 10);

    private static SettingsSingleton settingsSingleton;

    // Stat variable
    int[] statTracker = new int[4];


    private final int frames = 144;
    private int countDown = 0;
    private int waitTimer = 840;
    private int winnerTimer = 0;
    private int currentFrame = 0;

    private final Font titleFont = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    private final Font victoryFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 110);
    private final Font playerFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 50);
    private final Font gameFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
    private final Font chooseCharacterFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 100);
    private final Font countdownFont = new Font("res/fonts/conformable.otf", 300);
    private final Font dialogueFont = new Font("res/fonts/DejaVuSans-Bold.ttf", DIALOGUE_FONT_SIZE);
    private final Font playerMapFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
    private final DrawOptions DO;


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

    private double spacer = 300;
    private final Music music;
    private final Music mainMenuMusic;
    private final Music gameMusic;
    private final Music congratulationsMusic;
    private final Music startMusic;
    private final Music challengerMusic;
    private final Music whoMusic;
    private final Music voiceMusic;
    private final Music clickMusic;
    private final Music keyboardMusic;
    private final Music mainMusic;
    private ArrayList<Music> musics;
    private boolean picked;
    private double progress = 0;
    Rectangle bottomRectangle;
    Rectangle topRectangle;

    private ArrayList<Map> playableMaps;
    Map map;

    private boolean winnerPlayed = false;
    private String[] dialogueLine;
    Scanner dialogueScanner;
    ArrayList<String> dialogueWords;

    int timeToSaveStats = 60 * frames;
    int currentTimeToSaveStats = 0;


    //Story variables
    private boolean playingDialogue = false;
    private boolean playingStory = true;
    private boolean startStory = false;
    private int currentDialogue = 0;
    private int currentStory = 0;
    private int currentScene = 0;
    private int lastStory = -1;
    private int lastScene = -1;
    private Image currentBackground;
    private Point currentBackgroundPoint = new Point(0,0);
    private String dialogueString = "";
    private Colour dialogueColour = new Colour(77.0/255, 57.0/255, 37.0/255, 1);
    private double dialogueWidth = Window.getWidth()*0.1;
    private double dialogueLength = Window.getHeight() - 300;
    private int maxLines = 7;
    private int currentLines = 0;
    private int dialogueIndex = 0;
    private boolean alternate = false;
    private boolean playNextCharacter = true;
    private Character dialogueCharacter = null;
    private Character nextCharacter = null;
    private int shakeTimer = 0;
    private double transitionTimer = 0;
    private boolean dark = false;
    private int dialogueCounter = 0;
    private int keyboardTimer = 0;
    private boolean endDialogue = false;
    private String currentMode;

    private Image menuBackground;
    private String menuTitle;

    private final Colour black = new Colour(0, 0, 0, 1);
    private final Colour white = new Colour(1, 1, 1, 1);
    Colour darken = new Colour(0, 0, 0, 0.7);

    private int frame = 0;


    public static void main(String[] args) {
        new Game().run();
    }

    public Game(){
        super(1920, 1080);

        settingsSingleton = SettingsSingleton.getInstance();
        DO = new DrawOptions();
        players = new ArrayList<>();
        players.add( new PlayerOne());
        characters = new ArrayList<>();
        allCharacters = new ArrayList<>();
        sideCharacters= new ArrayList<>();
        allSideCharacters = new ArrayList<>();

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
        sideCharacters.add(Zoro);
        sideCharacters.add(Gojo);
        sideCharacters.add(AllMight);
        sideCharacters.add(Lelouch);
        sideCharacters.add(Hisoka);
        sideCharacters.add(Jotaro);
        sideCharacters.add(Itachi);
        sideCharacters.add(Yugi);
        checkAchievements();
        buttons = new ArrayList<>();
        obstacles = new ArrayList<>();
        obstaclesToRemove = new ArrayList<>();
        powerUps = new ArrayList<>();
        powerUpsToRemove = new ArrayList<>();

        music = new Music();
        mainMenuMusic = new Music();
        gameMusic = new Music();
        startMusic = new Music();
        challengerMusic = new Music();
        whoMusic = new Music();
        voiceMusic = new Music();
        congratulationsMusic = new Music();
        clickMusic = new Music();
        keyboardMusic = new Music();
        mainMusic = new Music();
        musics = new ArrayList<>();
        musics.add(music);
        musics.add(mainMenuMusic);
        musics.add(gameMusic);
        musics.add(startMusic);
        musics.add(challengerMusic);
        musics.add(whoMusic);
        musics.add(congratulationsMusic);
        musics.add(clickMusic);

        playableMaps = new ArrayList<>();
        playableMaps.add(new MapTrainingGround());
        playableMaps.add(new MapPark());
        playableMaps.add(new MapSpookySpikes());
        playableMaps.add(new MapGreedIsland());
        playableMaps.add(new MapMansion());
        playableMaps.add(new MapClaustrophobicLane());
        playableMaps.add(new MapUpsideDownCups());


        menuBackground = new Image("res/menu/MainMenu.PNG");
        menuTitle = "";
        getGameStats();
        loadStory();

    }

    @Override
    protected void update(Input input) {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
        if (frame % 144 == 0) {
            //System.out.println(frame/144);
        }
        frame ++;
        if ((!mainMenuMusic.played) && ((!SettingsSingleton.getInstance().getGameStateString().equals("Game")) && (!SettingsSingleton.getInstance().getGameStateString().equals("Story")))) {
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Unlocked")) {
                mainMenuMusic.run("music/Game Main Menu.wav");
                mainMenuMusic.played = true;
            }
        }
        if ((SettingsSingleton.getInstance().getGameStateString().equals("Game")) && mainMenuMusic.played) {
            mainMenuMusic.stopMusic();
        }
        else if ((SettingsSingleton.getInstance().getGameStateString().equals("Story")) && mainMenuMusic.played) {
            mainMenuMusic.stopMusic();
        }

        if ((!SettingsSingleton.getInstance().getGameStateString().equals("Game")) && (gameMusic.played)) {
            gameMusic.stopMusic();
        }

        menuBackground.drawFromTopLeft(0, 0);
        Drawing.drawRectangle(0, 0, Window.getWidth(), 100, black);
        Drawing.drawRectangle(0, Window.getHeight() - 85, Window.getWidth(), 85, black);

        titleFont.drawString(menuTitle, 0, 75);

        if (SettingsSingleton.getInstance().getButtons().size() > 0 ) {
            for (Button button: SettingsSingleton.getInstance().getButtons()) {
                button.toggleHover(input.getMousePosition());
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if (button.isHovering()) {
                        clickMusic.playMusic("music/Click.wav");
                        button.playAction();
                    }
                }
            }
        }
        if (SettingsSingleton.getInstance().getGameState() == 0) {
            saveStatsChecker();
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Main Menu")) {
                buttons.clear();
                Button playButton = new playButton("PLAY", new Rectangle(new Point(0, Window.getHeight() / 2), Window.getWidth(), 160));
                Button exitButton = new exitButton("EXIT", new Rectangle(new Point(0, Window.getHeight() / 1.5), Window.getWidth(), 160));
                buttons.add(playButton);
                buttons.add(exitButton);
                settingsSingleton.setButtons(buttons);
                SettingsSingleton.getInstance().setGameStateString("Main Menu");
                menuBackground = new Image("res/menu/MainMenu.PNG");
                menuTitle = "THE CLIMB";
            }
            drawButtons();
        }
        else if (SettingsSingleton.getInstance().getGameState() == 1) {
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Level")) {
                Button storyButton = new StoryButton("STORY", new Rectangle(0, Window.getHeight()/2.5 - 160, Window.getWidth(), 160));
                Button fastButton = new fastButton("EASY", new Rectangle(0, Window.getHeight() / 2.5 , Window.getWidth(), 160));
                Button fasterButton = new fasterButton("HARD",new Rectangle(0, Window.getHeight() / 2.5 + 160, Window.getWidth(), 160));
                buttons.clear();
                buttons.add(storyButton);
                buttons.add(fastButton);
                buttons.add(fasterButton);
                settingsSingleton.setButtons(buttons);
                SettingsSingleton.getInstance().setGameStateString("Level");
                menuBackground = new Image("res/menu/levelMenu.PNG");
                menuTitle = "SELECT LEVEL";
            }
            drawButtons();
        }
        else if (SettingsSingleton.getInstance().getGameState() == 2) {
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Players")) {
                buttons.clear();
                Button twoPlayerButton = new twoPlayerButton("2", new Rectangle(0, Window.getHeight() / 2 - 160, Window.getWidth(), 160));
                Button threePlayerButton = new threePlayerButton("3", new Rectangle(0, Window.getHeight() / 2, Window.getWidth(), 160));
                Button fourPlayerButton = new fourPlayerButton("4", new Rectangle(0, Window.getHeight() / 2 + 160, Window.getWidth(), 160));
                buttons.add(twoPlayerButton);
                buttons.add(threePlayerButton);
                buttons.add(fourPlayerButton);
                settingsSingleton.setButtons(buttons);
                SettingsSingleton.getInstance().setGameStateString("Players");
                menuBackground = new Image("res/menu/playerMenu.PNG");
                menuTitle = "PLAYERS";
            }
            if (SettingsSingleton.getInstance().getPlayers() != 0) {
                Image playerArt = new Image(String.format("res/menu/%dPlayers.PNG", SettingsSingleton.getInstance().getPlayers()));
                playerArt.draw(Window.getWidth()/2, Window.getHeight()/2);
            }
            drawButtons();
        }
        else if (SettingsSingleton.getInstance().getGameState() == 3) {
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Character")) {
                spacer = 300;
                buttons.clear();
                players.clear();
                settingsSingleton.setButtons(buttons);
                SettingsSingleton.getInstance().setGameStateString("Character");
                players.add(new PlayerOne());
                players.add(new PlayerTwo());
                if (SettingsSingleton.getInstance().getPlayers() == 3) {
                    players.add(new PlayerThree());
                }
                if (SettingsSingleton.getInstance().getPlayers() == 4) {
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
                character.getIcon().draw(200 + spacer * currentIcon, -50 + spacer * row);
                character.setIconPos(new Point(200 + spacer * currentIcon, -50 + spacer * row));
                currentIcon++;
                if (currentIcon >= 5) {
                    currentIcon = 1;
                    row++;
                }
            }
            // draw filler for locked characters
            for (int i = characters.size(); i < 12; i++) {
                locked.draw(200 + spacer * currentIcon, -50 + spacer * row);
                currentIcon++;
                if (currentIcon >= 5) {
                    currentIcon = 1;
                    row++;
                }
            }
            drawBorders();
                // react to player movement relative to character icons
            for (Character character: characters) {
                for (Player player: players) {
                    Image border = new Image("res/Selected/Selected.png");
                    if (player.getCharacter() == null) {
                        if (character.getIcon().getBoundingBoxAt(new Point(character.getIconPos().x - 25, character.getIconPos().y)).intersects(player.getPos())) {
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
                    if(SettingsSingleton.getInstance().getLevel() == 2) {
                        SettingsSingleton.getInstance().setGameState(6);
                    }
                    else {
                        SettingsSingleton.getInstance().setGameState(4);
                    }
                }
                else {
                    waitTimer--;
                }
            }

        }
        else if (SettingsSingleton.getInstance().getGameState() == 4) { // Comrades
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Comrade")) {
                menuTitle = "CHOOSE HUSBANDO";
                menuBackground = new Image("res/menu/mapMenu.png");
                SettingsSingleton.getInstance().setGameStateString("Comrade");
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
            for (int i = sideCharacters.size(); i < 10; i++) {
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
                    SettingsSingleton.getInstance().setGameState(5);
                }
                else {
                    waitTimer--;
                }
            }
        }
        else if (SettingsSingleton.getInstance().getGameState() == 5) { // Map
            if (!SettingsSingleton.getInstance().getGameStateString().equals("MAP")) {
                menuTitle = "Which Climb?";
                menuBackground = new Image("res/mapMenu.jpg");
                SettingsSingleton.getInstance().setGameStateString("MAP");
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
                player.getCursor().drawFromTopLeft(player.getPos().x, player.getPos().y);
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
                        SettingsSingleton.getInstance().setMapNo(player.getMapChosen().getLevel());
                        for (Map mapChosen: playableMaps) {
                            if (mapChosen.getLevel() == SettingsSingleton.getInstance().getMapNo()) {
                                map = mapChosen;
                                map.generateMap();
                                break;
                            }
                        }
                        break;
                    }
                }
                SettingsSingleton.getInstance().setGameState(6);
            }
        }
        else if (SettingsSingleton.getInstance().getGameState() == 6) {
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
            if (SettingsSingleton.getInstance().getLevel() < 2) {
                if (!SettingsSingleton.getInstance().getGameStateString().equals("Game")) {
                    int playerSize = players.size() + 1;
                    for (Player player : players) {
                        double spawnSpacer = Window.getWidth() / playerSize;
                        Point spawnPoint = new Point(spawnSpacer, Window.getHeight() - 100);
                        player.getCharacter().setPosition(spawnPoint);
                        playerSize -= 1;
                    }
                    music.playMusic("music/Start.wav");
                    int trackNo = (int) Math.round(Math.random()*2 - 0.5);
                    gameMusic.playMusic(String.format("music/Fight%d.wav", trackNo));
                    gameMusic.played = true;
                    bottomRectangle = new Rectangle(0, Window.getHeight() - 20, Window.getWidth(), 20);
                    topRectangle = new Rectangle(0, 0, Window.getWidth(), 20);
                    SettingsSingleton.getInstance().setGameStateString("Game");
                }
                startCountdown();
                map.draw();
                if (java.time.LocalTime.now().getHour() > 18) {
                    gameFont.drawString(String.format("%d/%dm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, DO.setBlendColour(new Colour(1,1,1)));
                }
                else {
                    gameFont.drawString(String.format("%d/%dm", map.getCurrentHeight()/10, map.getHeight()/10), Window.getWidth()/2 - 50, 0 + 50, DO.setBlendColour(new Colour(0, 0, 0)));
                }

                for (Player player : players) {
                    if (!player.isDead()) {
                        player.getCharacter().draw();
                    }
                    gameFont.drawString(String.format("P%s", player.getId()), player.getCharacter().getPos().x, player.getCharacter().getPos().y + 100);
                }
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
                    Zoro.setMap(map);
                    Jotaro.setPowerUps(powerUps);
                    Jotaro.setMap(map);
                    Itachi.setPowerUps(powerUps);
                    Yugi.setMap(map);
                    boolean playingAnimation = false;
                    boolean characterMusic = false;
                    for (Player player : players) {
                        if (player.getSideCharacter().getName().equals("Yugi")) {
                            player.getSideCharacter().activateAbility(player, players, obstacles);
                        }
                        if (input.wasPressed(player.getKey())) {
                            if (player.getCharacter().hasNoblePhantasm()) {
                                if (!player.getSideCharacter().isActivating()) {
                                    player.getCharacter().useNoblePhantasm();
                                }
                                player.getSideCharacter().activateAbility(player, players, obstacles);
                            }
                        }
                        if (player.getSideCharacter().isActivating()) {
                            player.getSideCharacter().activateAbility(player, players, obstacles);
                            if(player.getSideCharacter().isAnimating()) {
                                playingAnimation = true;
                                if (player.getSideCharacter().getName().equals("Itachi")) {
                                    Itachi.setLeft(input.isDown(player.getKey()));
                                }
                            }
                            characterMusic = true;
                            if (gameMusic.played == true) {
                                gameMusic.stopMusic();
                                gameMusic.played = false;
                            }
                        }
                    }
                    if ((!characterMusic) && (!gameMusic.played)) {
                        int trackNo = (int) Math.round(Math.random()*100/3);
                        System.out.println(trackNo);
                        gameMusic.playMusic(String.format("music/Fight%d.wav", trackNo));
                        gameMusic.played = true;
                    }
                    if(!map.hasFinished()) {
                        Colour playColor = new Colour(1, 0.3, 0.3);

                        if(!playingAnimation) {
                            displayCharacterStats(players);
                            Drawing.drawRectangle(0, Window.getHeight() - 20, Window.getWidth(), 20, playColor);
                            Drawing.drawRectangle(0, 0, Window.getWidth(), 20, playColor);
                            if (SettingsSingleton.getInstance().getLevel() == 0) {
                                map.updateTiles(0.4);
                            } else {
                                map.updateTiles(0.8);
                            }
                            if (SettingsSingleton.getInstance().getLevel() == 0) {
                                if (Math.random() > 0.98) {
                                    obstacles.add(new Ball());
                                }
                            } else if (SettingsSingleton.getInstance().getLevel() == 1) {
                                if (Math.random() > 0.96) {
                                    if (Math.random() > 0.8) {
                                        obstacles.add(new Balloon());
                                        music.playMusic("RedBalloon.wav");
                                    } else {
                                        obstacles.add(new Ball());
                                    }
                                }
                            }
                            if (Math.random() < 0.006) {
                                double powerUpNumber = Math.random();
                                if (powerUpNumber <= 0.22) {
                                    powerUps.add(new SpeedUp());
                                } else if ((powerUpNumber > 0.22) && (powerUpNumber < 0.44)) {
                                    powerUps.add(new Shield());
                                } else if ((powerUpNumber >= 0.44) && (powerUpNumber < 0.66)) {
                                    powerUps.add(new Minimiser());
                                } else if ((powerUpNumber >= 0.66) && (powerUpNumber < 0.88)) {
                                    powerUps.add(new SpeedDown());
                                } else {
                                    powerUps.add(new NoblePhantasm());
                                }
                            }
                        }
                    }
                    else {
                        if (!playingAnimation) {
                            titleFont.drawString("CLEAR! REACH THE TOP!", 16, FONT_SIZE, DO.setBlendColour(black));
                        }
                        for (Player player : players) {
                            if (player.getCharacter().getPos().distanceTo(new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                SettingsSingleton.getInstance().setGameState(7);
                                SettingsSingleton.getInstance().setWinner(player);
                                SettingsSingleton.getInstance().setGameStateString("Win");
                                break;
                            }
                        }
                    }

                    for (Obstacle obstacle : obstacles) {
                        obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
                        Drawing.drawRectangle(obstacle.getPos(), obstacle.getImage().getWidth(), obstacle.getImage().getHeight(), new Colour(0, 0, 0, 0.5));
                        if (!playingAnimation) {
                            obstacle.move();
                        }
                        for (Player player : players) {
                            if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(obstacle.getImage().getBoundingBoxAt(new Point(obstacle.getPos().x + obstacle.getImage().getWidth()/2, obstacle.getPos().y + obstacle.getImage().getHeight()/2)))) {
                                if (!player.isDead()) {
                                    if (player.getCharacter().hasShield()) {
                                        player.getCharacter().popShield();
                                        obstaclesToRemove.add(obstacle);
                                    }
                                    else {
                                        player.setDead();
                                        break;
                                    }
                                }
                            }
                        }
                        if ((obstacle.getPos().y > Window.getHeight()) && (!obstaclesToRemove.contains(obstacle))) {
                            obstaclesToRemove.add(obstacle);
                        }
                    }

                    for (Player player: players) {
                        if (!player.isDead()) {
                            if (!playingAnimation) {
                                player.moveCharacter(input);
                            }
                        }
                        if (!map.hasFinished()) {
                            if ((player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(bottomRectangle)) || (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(topRectangle))) {
                                if (!player.isDead()) {
                                    player.setDead();
                                }
                                break;
                            }
                        }
                        for (Tile tile : map.getVisibleTiles()) {
                            if ((tile.getName().equals("Ice")) && (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(tile.getImage().getBoundingBoxAt(new Point(tile.getPos().x + tile.getImage().getWidth()/2, tile.getPos().y + tile.getImage().getHeight()/2))))) {
                                player.getCharacter().onIce();
                            }
                            else if ((tile.getName().equals("Slow")) && (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(tile.getImage().getBoundingBoxAt(new Point(tile.getPos().x + tile.getImage().getWidth()/2, tile.getPos().y + tile.getImage().getHeight()/2))))) {
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
                    for (PowerUp powerUp : powerUps) {
                        powerUp.getImage().drawFromTopLeft(powerUp.getPos().x, powerUp.getPos().y);
                        if (!playingAnimation) {
                            powerUp.move();
                        }

                        for (Player player : players) {
                            if (player.getCharacter().getImage().getBoundingBoxAt(player.getCharacter().getPos()).intersects(powerUp.getImage().getBoundingBoxAt(new Point(powerUp.getPos().x + 25, powerUp.getPos().y + 25)))) {
                                if (!player.isDead()) {
                                    player.getCharacter().getPowerUp(powerUp);
                                    powerUpsToRemove.add(powerUp);
                                    break;
                                }
                            }
                        }
                        if (powerUp.getPos().y > Window.getHeight()) {
                            powerUpsToRemove.add(powerUp);
                        }
                    }
                    if (!SettingsSingleton.getInstance().getGameStateString().equals("Win")) {
                        int deathCounter = 0;
                        for (Player player : players) {
                            if (player.isDead()) {
                                deathCounter++;
                            }
                        }
                        if (SettingsSingleton.getInstance().getPlayers() - deathCounter < 2) {
                            SettingsSingleton.getInstance().setGameStateString("Lose");
                            SettingsSingleton.getInstance().setGameState(7);
                            for (Player player : players) {
                                if (!player.isDead()) {
                                    SettingsSingleton.getInstance().setWinner(player);
                                    break;
                                }
                            }
                        }
                    }
                    powerUps.removeAll(powerUpsToRemove);
                    obstacles.removeAll(obstaclesToRemove);
                }
            }
            else if (SettingsSingleton.getInstance().getLevel() == 2) {
                if (!SettingsSingleton.getInstance().getGameStateString().equals("Story")) {
                    SettingsSingleton.getInstance().setGameStateString("Story");
                }
                updateStory(currentDialogue, currentStory);
                if (map != null) {
                    map.draw();
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
                            if (lastStory != currentStory) {
                                lastStory = currentStory;
                                playingDialogue = true;
                                endDialogue = false;
                                map = new MapTrainingGround();
                                map.generateMap();
                                changeMainMusic("music/Fight1.wav");
                            }

                            if (endDialogue) {
                                for (Player player: players) {
                                    player.getCharacter().draw();
                                    player.moveCharacter(input);
                                }
                                if(!map.hasFinished()) {
                                    map.updateTiles(0.8);
                                }
                                else {
                                    double playersFinished = 0;
                                    for (Player player : players) {
                                        if (player.getCharacter().getPos().distanceTo(new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                            playersFinished++;
                                        }
                                    }
                                    if (playersFinished == players.size()) {
                                        playingStory = false;
                                        transitionTimer = 500;
                                        currentScene++;
                                    }
                                }
                            }

                            if (!startStory) {
                                startStory = true;
                                progress = 0;
                                double spawnDivider = 1;
                                for (Player player : players) {
                                    player.getCharacter().setPosition(new Point(spawnDivider * Window.getWidth() / (players.size() + 2), Window.getHeight() - 100));
                                }
                            }
                        }

                        else if (currentStory == 1) {

                        }
                    }
                    else {
                        currentMode = "Scene";
                        currentDialogue = currentScene;

                        if(currentScene == 0) {
                            if (lastScene != currentScene) {
                                lastScene = currentScene;
                                changeMainMusic("music/Idle.wav");
                                currentBackground = new Image("res/background/Futaba.png");
                                playingDialogue = true;
                                endDialogue = false;
                            }
                            if (dialogueCounter > 9) {
                                dark = true;
                            }
                            if (dialogueCounter == 10) {
                                changeMainMusic("music/Scary.wav");
                                shakeTimer = 1 * frames;
                                dialogueCounter++;
                            }
                            if (endDialogue) {
                                transitionTimer = 500;
                                dark = false;
                                playingStory = true;
                            }
                        }

                        else if (currentScene == 1) {
                            if (lastScene != currentScene) {
                                lastScene = currentScene;
                                playingDialogue = true;
                                endDialogue = false;
                                changeMainMusic("music/Idle.wav");
                                currentBackground = new Image("res/background/Nino.png");
                            }
                        }
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
                                                music.playMusic("music/Type.wav");
                                                keyboardTimer = 0;
                                            }
                                            else {
                                                keyboardTimer++;
                                            }
                                            keyboardMusic.played = true;
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
        else if (SettingsSingleton.getInstance().getGameState() == 7) {
            map.draw();
            for (Obstacle obstacle: obstacles) {
                obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
            }
            for (Player player: players) {
                player.getCharacter().draw();
                player.getCharacter().resetTimer();
                if (!player.isDead()) {
                    if (SettingsSingleton.getInstance().getWinner().getId() == player.getId()) {
                        Image winner = new Image(String.format("res/Renders/%s.png", player.getCharacter().getName()));
                        Image comrade = new Image(String.format("res/Renders/%s.png", player.getSideCharacter().getName()));
                        if (winnerTimer < winner.getWidth() + comrade.getWidth()/2) {
                            winnerTimer+=10;
                        }
                        Colour transparent = new Colour(0, 0, 0, 0.5);
                        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), transparent);
                        winner.draw(Window.getWidth() - winnerTimer, Window.getHeight()/2 +200);
                        comrade.draw(Window.getWidth() - winnerTimer + winner.getWidth(), Window.getHeight()/2 + 200);
                        victoryFont.drawString(String.format("Player %d: %s is victorious!", SettingsSingleton.getInstance().getWinner().getId(), SettingsSingleton.getInstance().getWinner().getCharacter().getName()), 16, 100);
                    }
                }
            }
            if (SettingsSingleton.getInstance().getGameStateString().equals("Win")) {
                music.playMusic("music/Congratulations.wav");
                SettingsSingleton.getInstance().setGameStateString("Congratulations");
            }
            if (!winnerPlayed) {
                for (Player player: players) {
                    player.getCharacter().resetTimer();
                    if (player.getCharacter() != null) {
                        if (player.getCharacter().getName().equals(SettingsSingleton.getInstance().getWinner().getCharacter().getName())) {
                            player.getCharacter().updateStats(true, true);
                            player.recordWin();
                        }
                        else {
                            player.getCharacter().updateStats(true, false);
                        }
                    }
                }
                statTracker[SettingsSingleton.getInstance().getLevel() + 1] = statTracker[SettingsSingleton.getInstance().getLevel() + 1] + 1;
                SettingsSingleton.getInstance().getWinner().getCharacter().playLine();
                winnerPlayed = true;
                buttons.clear();
                Button backToStartButton = new BackToStartButton("Main Menu",new Rectangle(new Point(0, Window.getHeight() / 1.5 + 160), Window.getWidth(), 160));
                Button retryButton = new RetryButton("Restart", new Rectangle(new Point(0, Window.getHeight() / 2 + 160), Window.getWidth(), 160));
                buttons.add(backToStartButton);
                buttons.add(retryButton);
            }
            drawButtons();
            if (input.wasPressed(MouseButtons.LEFT)) {
                if((SettingsSingleton.getInstance().getGameStateString().equals("Retry")) || (SettingsSingleton.getInstance().getGameStateString().equals("Menu"))) {
                    for (SideCharacter character: sideCharacters) {
                        character.reset();
                        character.stopMusic();
                    }
                    for (Character character: characters) {
                        character.stopMusic();
                    }
                    if (SettingsSingleton.getInstance().getGameStateString().equals("Retry")) {
                        for (Player player: players) {
                            player.getCharacter().resetTimer();
                            player.getSideCharacter().reset();
                            if (player.isDead()) {
                                player.setDead();
                            }
                        }
                        map.generateMap();
                        SettingsSingleton.getInstance().setGameState(6);
                        buttons.clear();
                    }
                    else if (SettingsSingleton.getInstance().getGameStateString().equals("Menu")) {

                        map = null;
                        unlocked = checkAchievementsInGame();
                        if (unlocked != null) {
                            SettingsSingleton.getInstance().setGameState(8);
                            saveStats();
                        }
                        else {
                            SettingsSingleton.getInstance().setGameState(0);
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
        else if (SettingsSingleton.getInstance().getGameState() == 8) {
            if (!SettingsSingleton.getInstance().getGameStateString().equals("Unlocked")) {
                for (Music music: musics) {
                    if (music.played) {
                        if(music.clip.isActive()) {
                            music.stopMusic();
                        }
                    }
                }
                for (Character character: characters) {
                    character.stopMusic();
                }
                whoMusic.playMusic("music/Who.wav");
                SettingsSingleton.getInstance().setGameStateString("Unlocked");
            }
            Colour black = new Colour(0, 0, 0, (float) (whoMusic.clip.getFrameLength() - whoMusic.clip.getFramePosition())/ (float) whoMusic.clip.getFrameLength());
            Image background = new Image(String.format("res/background/%s.png", unlocked.getName()));
            background.drawFromTopLeft(0,0);
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), black);
            if (whoMusic.clip.getFramePosition() >= whoMusic.clip.getFrameLength()) {
                if (challengerMusic.played == false) {
                    challengerMusic.playMusic(String.format("music/%sUnlock.wav", unlocked.getName()));
                    challengerMusic.played = true;
                }
                if (!voiceMusic.played) {
                    voiceMusic.playMusic(String.format("music/%sVoice.wav", unlocked.getName()));
                    voiceMusic.played = true;
                }
                chooseCharacterFont.drawString(String.format("NEW CHARACTER UNLOCKED: %s!", unlocked.getName().toUpperCase()), 0, 100);
                Image unlockedImage = new Image(String.format("res/Renders/%s.png", unlocked.getName()));
                unlockedImage.draw(Window.getWidth()/2, Window.getHeight()/2 + 300);
                if (input.wasPressed(MouseButtons.LEFT)) {
                    voiceMusic.stopMusic();
                    challengerMusic.stopMusic();
                    whoMusic.stopMusic();
                    SettingsSingleton.getInstance().setGameState(0);
                }
            }
        }
        if ((SettingsSingleton.getInstance().getGameState() > 0) && (SettingsSingleton.getInstance().getGameState() < 6)) {
            Image back = new Image("res/BackArrow.PNG");
            back.draw(Window.getWidth() - back.getWidth(), back.getHeight()*2);
            if (back.getBoundingBoxAt(new Point(Window.getWidth() - back.getWidth(), back.getHeight()*2)).intersects(input.getMousePosition())) {
                if (input.wasPressed(MouseButtons.LEFT)) {
                    if (SettingsSingleton.getInstance().getGameState() == 3) {
                        players.removeAll(players);
                    }
                    if (SettingsSingleton.getInstance().getGameState() == 5) {
                        for (Player player: players) {
                            player.setMapChosen(null);
                            player.setSideCharacter(null);
                        }
                    }
                    SettingsSingleton.getInstance().setGameState(SettingsSingleton.getInstance().getGameState() - 1);
                }
            }
        }
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
                dialogueScanner = new Scanner(new File(String.format("dialogue/%d.txt", dialogueInt)));
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
                    if (word.substring(0, word.length()-1).equals(character.getName())) {
                        currentLineLength = word.length();
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


    public static void drawButtons() {
        if (SettingsSingleton.getInstance().getButtons().size() > 0) {
            for (Button button: SettingsSingleton.getInstance().getButtons()) {
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
        if(SettingsSingleton.getInstance().getPlayers() == 3) {
            P3.draw(250 + P1.getWidth()*3, Window.getHeight() - 115);
        }
        if (SettingsSingleton.getInstance().getPlayers() == 4) {
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
                    currentScene = Integer.parseInt(line[line.length- 1]) - 1;
                    if (currentScene < 0) {
                        currentScene = 0;
                    }
                }
                else if (lineNumber == 1) {
                    currentStory = Integer.parseInt(line[line.length - 1]) - 1;
                    if (currentStory < 0) {
                        currentStory = 0;
                    }
                }
                lineNumber++;
            }
            playingStory = currentScene > currentStory;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateStory(int currentDialogue, int currentStory) {
        String tempFile = "stats/TempStory.txt";
        String currentFile = "stats/Story.txt";
        File oldFile = new File(currentFile);
        File newFile = new File(tempFile);
        try {
            Scanner storyScanner = new Scanner(new File("stats/Story.txt"));

            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            for (int i = 0; i < 2; i++) {
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
                    if (i == 0) {
                        newLine += String.format("%d", currentDialogue);
                    } else if (i == 1) {
                        newLine += String.format("%d", currentStory);
                    }
                    pw.println(newLine);
                }
            }

            storyScanner.close();
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
        if (transitionTimer > 0) {
            if (transitionTimer > 250) {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 1 - (transitionTimer - 250)/250));
                System.out.println(1 - (transitionTimer - 250)/250);
            }
            else if (transitionTimer == 250) {
                map = null;
                changeBackground(null);
            }
            else if (transitionTimer < 250) {
                Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, transitionTimer/250));
            }
            transitionTimer--;
        }
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
        if (mainMusic.played) {
            mainMusic.stopMusic();
        }
        mainMusic.run(filePath);
        mainMusic.played = true;

    }

    public void changeBackground(Image background) {
        currentBackground = background;
    }

    public void displayCharacterStats(ArrayList<Player> players) {
        int playerIndex = 0;
        for (Player player: players) {
            Image picture;
            Colour border = new Colour(0, 0, 0, 0.3);
            if(player.getCharacter().isMinimised()) {
                picture = new Image(String.format("res/InGame/%s_Mini.png", player.getCharacter().getName()));
                Drawing.drawRectangle(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight(), 200, picture.getHeight(), border);
            }
            else {
                picture = new Image(String.format("res/InGame/%s.png", player.getCharacter().getName()));
                Drawing.drawRectangle(picture.getWidth() + playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight(), 200, picture.getHeight(), border);
            }
            picture.drawFromTopLeft(playerIndex*Window.getWidth()/(players.size()), Window.getHeight() - picture.getHeight());


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
                character.playLine();
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
                character.playLine();
            }
        }
        else {
            player.setSideCharacter(null);
        }
    }

    public void pickMap(Player player, Map map) {
        if (player.getMapChosen() == null) {
            music.playMusic("music/Click.wav");
            player.setMapChosen(map);
        }
    }
}