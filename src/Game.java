import Enums.MapNames;
import Enums.Obstacles;
import Enums.TileType;
import bagel.*;
import bagel.Window;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


/** "The Climb" - A game created by Bill Nguyen **/

public class Game extends AbstractGame {

    private static final TimeLogger timeLogger = TimeLogger.getInstance();
    private static final SettingsSingleton settingsSingleton = SettingsSingleton.getInstance();
    private static final GameSettingsSingleton gameSettingsSingleton = GameSettingsSingleton.getInstance();
    private static final StorySettingsSingleton storySettingsSingleton = StorySettingsSingleton.getInstance();
    private static final ButtonsSingleton buttonsSingleton = ButtonsSingleton.getInstance();
    private static final EventsListenerSingleton eventsListenerSingleton = EventsListenerSingleton.getInstance();
    private static final ImagePointManagerSingleton imagePointManagerSingleton = ImagePointManagerSingleton.getInstance();
    private static final MusicPlayer musicPlayer = MusicPlayer.getInstance();
    private static final GameEntities gameEntities = GameEntities.getInstance();
    private static final StringDisplays stringDisplays = new StringDisplays();

    private final ArrayList<Button> buttons= buttonsSingleton.getButtons();
    private final ArrayList<Button> buttonsToRemove = buttonsSingleton.getButtonsToRemove();
    private final ArrayList<Button> buttonsToAdd = buttonsSingleton.getButtonsToAdd();
    private final ArrayList<Slider> sliders = buttonsSingleton.getSliders();
    private final ArrayList<Slider> slidersToRemove = buttonsSingleton.getSlidersToRemove();
    private final EventsListenerSingleton.EventsListener eventsListener = eventsListenerSingleton.getEventsListener();
    private final ArrayList<Character> playableCharacters = new ArrayList<>();
    private final ArrayList<Character> allCharacters = new ArrayList<>();
    private final ArrayList<Player> players = settingsSingleton.getPlayers();
    private final ArrayList<Obstacle> obstacles = gameEntities.getObstacles();
    private final ArrayList<PowerUp> powerUps = gameEntities.getPowerUps();
    private final ArrayList<SideCharacter> allSideCharacters= new ArrayList<>();

    private final int frames = TimeLogger.getInstance().getRefreshRate();

    private final Stats stats = new Stats();
    private final Dialogue dialogue = new Dialogue();

    private final DrawOptions DO = new DrawOptions();

    // Custom Map Vars
    private final ArrayList<Map> playableMaps= new ArrayList<>();
    private final ArrayList<Tile> allTiles= new ArrayList<>();
    private static final ArrayList<Tile> customMapTiles= new ArrayList<>();
    private boolean addingTile = false;
    private int page = 0;
    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private int offset = 0;

    private boolean toggleInfo = false;

    private String unlocked;
    private boolean canInteract = eventsListenerSingleton.isCanInteract();

    private String menuTitle = "";

    Point currentMousePosition;

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

//        eventsListener.addEvent(new EventStartApp(5 * frames / 2, "Game initiated"));
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
                    new Music("music/misc/Click.wav", MusicPlayer.getInstance().getEffectVolume()).play();

                    button.playAction();
                }
            }
        }

        if (settingsSingleton.getGameState() == 0) {
            if (!"Main Menu".equals(settingsSingleton.getGameStateString())) {
                players.clear();
                sliders.clear();
                gameSettingsSingleton.setMap(null);
                obstacles.clear();
                musicPlayer.clear();
                musicPlayer.clearEnded();
                buttons.clear();
                buttons.addAll(Arrays.asList(
                        new Button("PLAY", null,
                                new FontSize(Fonts.GEOMATRIX, 80),
                                new Rectangle(350, 400, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("TUTORIAL", null,
                                new FontSize(Fonts.GEOMATRIX, 80),
                                new Rectangle(350, 400 + 80, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("CREATE MAP", null,
                                new FontSize(Fonts.GEOMATRIX, 80),
                                new Rectangle(350, 400 + 80 * 2, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("SETTINGS", null,
                                new FontSize(Fonts.GEOMATRIX, 80),
                                new Rectangle(350, 400 + 80 * 3, Window.getWidth(), 80),
                                ColourPresets.BLACK.toColour()),
                        new Button("EXIT", null,
                                new FontSize(Fonts.GEOMATRIX, 80),
                                new Rectangle(350, 400 + 80 * 4, Window.getWidth(),
                                        80),
                                ColourPresets.BLACK.toColour())
                ));
                menuTitle = "";
                musicPlayer.setMainMusic("music/Battle/Giorno.wav");
                musicPlayer.getMainMusic().setVolume(musicPlayer.getMainVolume());
                if (!settingsSingleton.getGameStateString().equals("Game Mode")) {
                    imagePointManagerSingleton.getImages().clear();
                    eventsListener.addEvent(new EventGameStateZero());
                }
                String frontCharacter = playableCharacters.get((int) (Math.random() * playableCharacters.size())).getFullName();
                ImagePoint frontCover = new ImagePoint(String.format("res/characters/%s/Render.png", frontCharacter), new Point(1000,24), "frontCover");
                ImagePoint shadow = new ImagePoint(String.format("res/characters/%s/Render.png", frontCharacter), new Point(1000,24), "shadow");
                shadow.setDarken(true);
                shadow.setScale(1.1);
                shadow.setOpacity(0.5);
                if (!imagePointManagerSingleton.imageWithExistsWithTag("frontCover")) {
                    imagePointManagerSingleton.add(shadow);
                    imagePointManagerSingleton.add(frontCover);
                    imagePointManagerSingleton.get(frontCover).setPos(1000, Window.getHeight() - frontCover.getHeight());
                }
                settingsSingleton.setGameStateString("Main Menu");
            }
            if (input!= null) {
                imagePointManagerSingleton.getImageWithTag("shadow").setPos(1000 + (Window.getWidth()/2d - input.getMouseX())/200, 24 + (Window.getHeight()/2d - input.getMouseY())/200);
            }
            double opacity = Math.abs(Math.sin(timeLogger.getFrames()/250d))*0.3 + 0.4;
            imagePointManagerSingleton.getImageWithTag("shadow").setOpacity(opacity);
            updateDemo(input);
        }
        else if (settingsSingleton.getGameState() == 1) {

            if (!settingsSingleton.getGameStateString().equals("Game Mode")) {
                settingsSingleton.setGameStateString("Game Mode");
                Button storyButton = new Button("STORY", null,
                        new FontSize(Fonts.GEOMATRIX, 80),
                        new Rectangle(350, 480, Window.getWidth(), 80),
                        ColourPresets.BLACK.toColour());
                Button versusButton = new Button("VERSUS", null,
                        new FontSize(Fonts.GEOMATRIX, 80),
                        new Rectangle(350, 580, Window.getWidth(), 80),
                        ColourPresets.BLACK.toColour());
                buttonsToRemove.addAll(buttons);
                buttons.add(storyButton);
                buttons.add(versusButton);

                String frontCharacter = playableCharacters.get((int) (Math.random() * playableCharacters.size())).getFullName();
                ImagePoint frontCover = new ImagePoint(String.format("res/characters/%s/Render.png", frontCharacter), new Point(1000,24), "frontCover");
                ImagePoint shadow = new ImagePoint(String.format("res/characters/%s/Render.png", frontCharacter), new Point(1000,24), "shadow");
                shadow.setDarken(true);
                shadow.setScale(1.1);
                shadow.setOpacity(0.5);
                if (!imagePointManagerSingleton.imageWithExistsWithTag("frontCover")) {
                    imagePointManagerSingleton.add(shadow);
                    imagePointManagerSingleton.add(frontCover);
                    imagePointManagerSingleton.get(frontCover).setPos(1000, Window.getHeight() - frontCover.getHeight());
                }
            }
            if (input!= null) {
                imagePointManagerSingleton.getImageWithTag("shadow").setPos(1000 + (Window.getWidth()/2d - input.getMouseX())/200, 24 + (Window.getHeight()/2d - input.getMouseY())/200);
            }
            double opacity = Math.abs(Math.sin(timeLogger.getFrames()/250d))*0.3 + 0.4;
            imagePointManagerSingleton.getImageWithTag("shadow").setOpacity(opacity);
            updateDemo(input);

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(0);
                eventsListener.addEvent(new EventGameMode());
                players.clear();
            }
        }
        else if (settingsSingleton.getGameState() == 3) {
            if (!settingsSingleton.getGameStateString().equals("Character")) {
                settingsSingleton.setGameStateString("Character");
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                obstacles.clear();
                powerUps.clear();
                menuTitle = "";
                imagePointManagerSingleton.setCurrentBackground(null);
                imagePointManagerSingleton.getImages().clear();
                if (settingsSingleton.getGameMode() == 1) {
                    Image settingsImage = new Image("res/misc/settings.png");
                    Image addImage = new Image("res/misc/add.png");
                    double spacing = Window.getWidth()/6d;

                    buttons.add(new Button("Game Settings", settingsImage, new Point((Window.getWidth() - settingsImage.getWidth())/2,20)));
                    if (players.size() <= 1) {
                        buttons.add(new Button("Add", addImage, new Point(settingsSingleton.getPlayers().size() * (addImage.getWidth()*2/3 + spacing) + spacing/3, 0)));
                    }
                    else {
                        buttons.add(new Button("Add", addImage, new Point(settingsSingleton.getPlayers().size() * (addImage.getWidth()*2/3 + spacing) + spacing/3 + 200, 0)));
                    }
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
            imagePointManagerSingleton.getCurrentBackground().setOpacity(1);
            if (!isUnlocked(currentCharacter.getName())) {
                imagePointManagerSingleton.getCurrentBackground().setOpacity(0.4);
            }

            if (input != null && input.isDown(Keys.RIGHT)) {
                eventsListener.addEvent(new EventCharacterRotate("Rotate LEFT"));
                rotateCharacter("LEFT");
            } else if (input != null && input.isDown(Keys.LEFT)) {
                eventsListener.addEvent(new EventCharacterRotate("Rotate RIGHT"));
                rotateCharacter("RIGHT");
            }

            if (input != null && input.wasPressed(Keys.SPACE)) {
                pickCharacter(currentPlayer, currentCharacter);
            }

            boolean picked = true;
            for (Player player: players) {
                if (player.getCharacter() == null) {
                    picked = false;
                    break;
                }
            }
            if (picked && !eventsListener.contains(EventCharacterPicked.class) && !eventsListener.contains(EventCharactersPicked.class)) {
                eventsListener.addEvent(new EventCharactersPicked("All players have picked a character"));
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                if (settingsSingleton.getPlayers().size() == 1) {
                    imagePointManagerSingleton.getCurrentBackground().setOpacity(1);
                    imagePointManagerSingleton.getImages().clear();
                    eventsListener.addEvent(new EventGameStateZero());
                    settingsSingleton.setGameState(1);
                }
                else {
                    settingsSingleton.getPlayers().remove(settingsSingleton.getPlayers().size() -1);

                    double spacing = Window.getWidth()/6d;

                    for (Button button: buttons) {
                        if (button.getName().equalsIgnoreCase("add")) {
                            button.setPosition(new Point(settingsSingleton.getPlayers().size() * (button.getImage().getWidth()*2/3 + spacing) + spacing/3 + 200, 0));

                            if (settingsSingleton.getPlayers().size() == 1  ) {
                                button.setPosition(new Point(button.getWidth()*2/3 + spacing*2/3 + 200, 0));
                            }
                            break;
                        }
                    }
                }
            }

        }
        else if (settingsSingleton.getGameState() == 4) {
            if (!settingsSingleton.getGameStateString().equals("SideCharacter")) {
                settingsSingleton.setGameStateString("SideCharacter");
                imagePointManagerSingleton.getImages().clear();
                musicPlayer.clear();
                buttonsToRemove.addAll(buttons);
                slidersToRemove.addAll(sliders);
                toggleInfo = false;
                int index = 0;
                double minScale = 0.275;
                double maxScale = 1;
                int middleIndex = allSideCharacters.size() % 2 == 1 ?
                        allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2;
                double spacing = Window.getHeight()/4d;

                for (SideCharacter character: allSideCharacters) {
                    ImagePoint characterRender = new ImagePoint(String.format("res/SideCharacters/%s/render.png", character.getName()),
                            new Point(0, 0), "SideCharacterRender");
                    characterRender.setScale(minScale);
                    if (index < middleIndex) {
                        characterRender.setPos(((allSideCharacters.size() * spacing/2) - Window.getWidth())/2 - 490 + (spacing * (index - 1)),
                                Window.getHeight()*3/4d);
                    }
                    else if (middleIndex == index) {
                        characterRender.setScale(maxScale);
                        characterRender.setPos(490, Window.getHeight()/8d);
                    }
                    else {
                        characterRender.setPos( Window.getWidth()/2d + (spacing * (index - middleIndex)),
                                Window.getHeight()*3/4d);
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
                    eventsListener.addEvent(new EventSideCharacterRotate("Rotate LEFT"));
                    rotateCharacter("LEFT");
                } else if (input != null && input.isDown(Keys.LEFT)) {
                    eventsListener.addEvent(new EventSideCharacterRotate("Rotate RIGHT"));
                    rotateCharacter("RIGHT");
                }

                if (input != null && input.wasPressed(Keys.SPACE)) {
                    pickSideCharacter(currentPlayer, currentCharacter);
                }

                boolean picked = true;
                for (Player player: players) {
                    if (player.getSideCharacter() == null) {
                        picked = false;
                        break;
                    }
                }
                if (picked && !eventsListener.contains(EventSideCharacterPicked.class) &&
                        !eventsListener.contains(EventSideCharactersPicked.class)) {
                    eventsListener.addEvent(new EventSideCharactersPicked("All players have picked a side character"));
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
                    stringDisplays.add(new StringDisplay(currentCharacter.getName(), true, new FontSize(Fonts.TCB, 160), new Point(0,Window.getHeight()/3d - 80)));
                    Objects.requireNonNull(stringDisplays.get(currentCharacter.getName())).setColour(1,1,1,1);
                }
                if (!stringDisplays.exists(currentCharacter.getPower())) {
                    stringDisplays.add(new StringDisplay(currentCharacter.getPower(), true, new FontSize(Fonts.CONFORMABLE, 110), new Point(0,Window.getHeight()/3d)));
                    Objects.requireNonNull(stringDisplays.get(currentCharacter.getPower())).setColour(1,1,1,1);
                }
                if (!stringDisplays.exists(currentCharacter.getDesc())) {
                    stringDisplays.add(new StringDisplay(currentCharacter.getDesc(), true, new FontSize(Fonts.AGENCYB, 40), new Point(0, Window.getHeight()/3d + 80)));
                    Objects.requireNonNull(stringDisplays.get(currentCharacter.getDesc())).setColour(1,1,1,1);
                }
            }
            if (input != null && input.wasPressed(Keys.I)) {
                toggleInfo = !toggleInfo;
                stringDisplays.clear();
            }
        }
        else if (settingsSingleton.getGameState() == 5) { // Map
            if (!settingsSingleton.getGameStateString().equals("MAP")) {
                buttonsToRemove.addAll(buttons);
                imagePointManagerSingleton.setCurrentBackground(null);
                musicPlayer.clear();
                settingsSingleton.setGameStateString("MAP");
                loadMapRender();
            }

            int middleIndex = playableMaps.size() % 2 == 1 ? playableMaps.size() / 2 + 1 : playableMaps.size() / 2;

            Player currentPlayer = players.get(0);
            for (Player player: players) {
                if (player.getMapChosen() == null) {
                    currentPlayer = player;
                    break;
                }
            }

            if (input != null && input.isDown(Keys.LEFT)) {
                rotateMap("UP");
                eventsListener.addEvent(new EventMapRotate("UP"));
                musicPlayer.addMusic("music/misc/CharacterNext.wav");
            }
            else if (input != null && input.isDown(Keys.RIGHT)) {
                rotateMap("DOWN");
                eventsListener.addEvent(new EventMapRotate("DOWN"));
                musicPlayer.addMusic("music/misc/CharacterNext.wav");
            }
            else if (input != null && input.wasPressed(Keys.SPACE)) {
                pickMap(currentPlayer, playableMaps.get(middleIndex));
            }

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                for (Player player: players) {
                    player.setSideCharacter(null);
                    player.setMapChosen(null);
                }
                settingsSingleton.setGameState(4);
            }

            boolean allPlayersChosen = true;
            for (Player player: players) {
                if (player.getMapChosen() == null) {
                    allPlayersChosen = false;
                    break;
                }
            }


            if (allPlayersChosen) {
                int random = Math.min((int) Math.round(Math.random() * players.size()), players.size() - 1);
                Map mapChosen = players.get(random).getMapChosen();
                gameSettingsSingleton.setMap(mapChosen);
                settingsSingleton.setGameState(6);
            }
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                if (!settingsSingleton.getGameStateString().equals("Game")) {
                    imagePointManagerSingleton.getImages().clear();
                    gameSettingsSingleton.getMap().generateMap();
                    if (settingsSingleton.getPlayers().size() == 1) {
                        System.out.println("Loaded computer!");
                        ComputerHard computer = new ComputerHard(2);
                        loadComputer(computer);
                        settingsSingleton.getPlayers().add(computer);
                    }
                    buttonsToRemove.addAll(buttons);
                    loadPlayers();
                    musicPlayer.setMainMusic(String.format("music/battle/Fight%d.wav", Math.round(Math.random() * 3)));
                    musicPlayer.clear();
                    settingsSingleton.setGameStateString("Game");
                    eventsListener.addEvent(new EventGameStart());
                    canInteract = false;
                }
                if (canInteract) {
                    boolean playingAnimation = false;
                    boolean theWorld = false;
                    for (Player player : players) {
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
                    if (!gameSettingsSingleton.getMap().hasFinished()) {
                        if (!playingAnimation) {
                            musicPlayer.setMainVolume(musicPlayer.getMainVolume());
                            gameEntities.spawnObstacles();
                            gameEntities.spawnPowerUps();
                        }
                    }
                    else {
                        for (Player player : players) {
                            if (player.getCharacter().getPos().distanceTo(
                                    new Point(player.getCharacter().getPos().x, 0)) < 10) {
                                settingsSingleton.setWinner(player);
                                eventsListener.addEvent(new EventGameFinished());
                                break;
                            }
                        }
                    }
                    if (!playingAnimation) {
                        musicPlayer.setMainVolume(musicPlayer.getMainVolume());
                        updateExp();
                        if (!theWorld) {
                            updatePlayerMovement(input);
                            gameEntities.updateObjects();
                            if (!gameSettingsSingleton.getMap().hasFinished()) {
                                gameSettingsSingleton.getMap().updateTiles(gameSettingsSingleton.getMapSpeed());
                            }
                        } else {
                            for (Player player : players) {
                                if (player.getSideCharacter().isActivating() &&
                                        Arrays.asList(CharacterNames.JOTARO, CharacterNames.DIO).contains(player.getSideCharacter().getName())) {
                                    if (input != null) {
                                        player.moveCharacter(input);
                                    }
                                }
                            }
                        }
                    }
                    updateAbilities();
                    if (canInteract) {
                        gameEntities.checkCollisionPowerUps();
                        gameEntities.checkCollisionObstacles();
                        checkCollisionTiles();
                    }

                    int deathCounter = 0;
                    for (Player player : players) {
                        if (player.getCharacter().isDead()) {
                            deathCounter++;
                        }
                    }
                    if (settingsSingleton.getPlayers().size() - deathCounter < 2) {
                        for (Player player : players) {
                            if (!player.getCharacter().isDead()) {
                                settingsSingleton.setWinner(player);
                                break;
                            }
                        }
                        if (!eventsListener.contains(EventGameFinished.class)) {
                            eventsListener.addEvent(new EventGameFinished());
                        }
                    }
                }
                gameSettingsSingleton.getMap().update();
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
            powerUps.clear();
            obstacles.clear();
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
                    if (settingsSingleton.getWinner() != null) {
                        musicPlayer.addMusic(settingsSingleton.getWinner().getCharacter().playLine());
                    }
                    stats.updateGameStats(players);
                    buttonsToRemove.addAll(buttons);
                    buttons.add(new Button("Back", "Back",
                            new FontSize(Fonts.GEOMATRIX, 100),
                            new Rectangle(0, Window.getHeight() - 100, Window.getWidth(), 100),
                            ColourPresets.WHITE.toColour()));
                    buttons.add(new Button("Retry", "Restart",
                            new FontSize(Fonts.GEOMATRIX, 100),
                            new Rectangle(0, Window.getHeight() - 250, Window.getWidth(), 100),
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
                gameSettingsSingleton.setMap(new Map(MapNames.CUSTOM));
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
                    addTileToCustomMap(tile1.getType());
                }
                if (input != null && input.wasPressed(Keys.NUM_2)) {
                    addTileToCustomMap(tile2.getType());
                }
                if (input != null && input.wasPressed(Keys.NUM_3)) {
                    addTileToCustomMap(tile3.getType());
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
                gameSettingsSingleton.setMap(new Map(MapNames.TRAINING_GROUND));
                gameSettingsSingleton.getMap().generateMap();
                obstacles.clear();
                powerUps.clear();
                settingsSingleton.setPlayers(1);
                settingsSingleton.getPlayers().get(0).setCharacter(new Character(CharacterNames.CHIZURU));
                settingsSingleton.getPlayers().get(0).setSideCharacter(new SideHisoka());
                setPlayersPosition();
                imagePointManagerSingleton.getImages().clear();
                buttons.clear();
                storySettingsSingleton.setMode("Tutorial");
                dialogue.setPlayingDialogue(true);
            }
            long currentMapHeight = Math.round(gameSettingsSingleton.getMap().getCurrentHeight());
            int dialogueInt = storySettingsSingleton.getDialogueInt();
            Character character = players.get(0).getCharacter();

            dialogue.update(input);
            if (!dialogue.isPlayingDialogue()) {
                if (dialogueInt == 0) {
                    storySettingsSingleton.setDialogueInt(1);
                    stringDisplays.add(new StringDisplay("This shows how much of the Climb is\n left before you reach the top", 3 * frames,
                            new FontSize(Fonts.AGENCYB, 40), new Point(Window.getWidth()/2.0 - 150, 90)));
                    storySettingsSingleton.initialiseTime();
                }
                if (timeLogger.getFrames() - storySettingsSingleton.getInitialTime() == Math.round(4.2 * frames)) {
                    obstacles.add(new ObstacleRock(new Point(players.get(0).getCharacter().getPos().x, 0)));
                }
                else if (timeLogger.getFrames() - storySettingsSingleton.getInitialTime() == Math.round(4.5 * frames)) {
                    dialogue.setPlayingDialogue(true);
                }
                if (obstacles.size() == 1) {
                    if (obstacles.get(0).getBoundingBox().intersects(
                            character.getRectangle())) {
                        storySettingsSingleton.setDialogueInt(2);
                        dialogue.setPlayingDialogue(true);
                        obstacles.clear();
                    }
                }

                if (currentMapHeight == 1000 && dialogueInt != 3) {
                    storySettingsSingleton.setDialogueInt(3);
                    dialogue.setPlayingDialogue(true);
                }

                if (dialogueInt == 3 && character.hasShield()) {
                    storySettingsSingleton.setDialogueInt(4);
                    dialogue.setPlayingDialogue(true);
                }
                else if (dialogueInt == 3 && !character.hasShield() && powerUps.size() == 0) {
                    dialogue.setPlayingDialogue(true);
                    dialogue.setLineNo(12);
                    gameSettingsSingleton.getMap().setCurrentHeight(1000);
                }

                if (dialogueInt == 4 && character.getLives() == 0) {
                    character.setLives(1);
                    dialogue.setPlayingDialogue(true);
                }

                if (!gameSettingsSingleton.getMap().hasFinished()) {
                    gameSettingsSingleton.getMap().updateTiles(1);
                }
                gameEntities.checkCollisionPowerUps();
                gameEntities.checkCollisionObstacles();
                updatePlayerMovement(input);
                gameEntities.updateObjects();
            }
            else {
                if (dialogueInt == 1) {
                    updatePlayerMovement(input);
                }
                if (dialogueInt == 3 && dialogue.getLineNo() == 12 && powerUps.size() == 0) {
                    powerUps.add(new PowerUpShield(new Point(character.getPos().x, -80)));
                }
                if (dialogueInt == 4) {
                    if (!dialogue.isLoading() && dialogue.getLineNo() == 2) {
                        if (input != null && input.wasPressed(Keys.SPACE)) {
                            eventsListener.addEvent(new EventShakeMap(2*frames));
                            for (int i = 0; i < 32; i ++) {
                                obstacles.add(new ObstacleRock(new Point(i * 80, -1400)));
                            }
                        }
                    }
                    if (!dialogue.isLoading() && dialogue.getLineNo() == 4) {
                        if (input != null && input.wasPressed(Keys.SPACE)) {
                            dialogue.setPlayingDialogue(false);
                        }
                    }
                }
                if (dialogueInt == 5) { 
                    if (dialogue.getLineNo() == 10) {
                        eventsListener.addEvent(new EventCharacterJoinsTheClimb("Naofumi"));
                    }
                }
            }
            gameSettingsSingleton.getMap().update();

            if (input != null && input.wasPressed(Keys.ESCAPE)) {
                settingsSingleton.setGameState(0);
                storySettingsSingleton.setDialogueInt(0);
                dialogue.setPlayingDialogue(false);
                stringDisplays.clear();
            }
        }
        else if (settingsSingleton.getGameState() == 13) {
        }
        else if (settingsSingleton.getGameState() == 14) {
            if (!settingsSingleton.getGameStateString().equals("SETTINGS")) {
                settingsSingleton.setGameStateString("SETTINGS");
                menuTitle = "SETTINGS";
                buttons.clear();
                sliders.clear();
                sliders.add(new SliderVolume("Main Volume", new Point(Window.getWidth() / 7d, Window.getHeight() / 4d + 100)));
                sliders.add(new SliderVolume("Effect Volume", new Point(Window.getWidth() / 7d, Window.getHeight() / 3d + 150)));
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
        timeLogger.updateFrames();
        canInteract = eventsListener.canInteract();

        // TEST GAME
        if (settingsSingleton.getGameState() == -100) {
            settingsSingleton.setGameState(5);
            settingsSingleton.setGameMode(1);
            settingsSingleton.setPlayers(2);
            players.get(0).setCharacter(new Character(CharacterNames.MIKU));
            players.get(0).setSideCharacter(new SideJotaro());
            players.get(0).getCharacter().setPowerUp(new PowerUpMinimiser());
            players.get(1).setCharacter(new Character(CharacterNames.NAO));
            players.get(1).setSideCharacter(new SideHisoka());
            gameSettingsSingleton.setMapSpeed(0);
            gameSettingsSingleton.getObstaclesSettingsSingleton().toggle(Obstacles.BALL);
            gameSettingsSingleton.getObstaclesSettingsSingleton().toggle(Obstacles.ROCK);
        }
    }

    public void render() {
        Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), ColourPresets.BLACK.toColour());
        if (settingsSingleton.getGameState() == 0) {
            imagePointManagerSingleton.getCurrentBackground().draw();
            drawGame();
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.6));

            ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
            ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");


            if (leftCover != null) {
                leftCover.draw();
            }
            if (rightCover != null) {
                rightCover.draw();
            }
            imagePointManagerSingleton.draw();
            new ImagePoint("res/menu/main/name2.png", new Point(148, 87)).draw();
            drawButtons();
//            for (Button button: buttons) {
//                if (button.isHovering()) {
//                    Drawing.drawRectangle(new Point(0, button.getPosition().y + 30), Window.getWidth(), button.getWidth() - 30, new Colour(0,0,0,0.05));
//                    break;
//                }
//            }
        }
        if (settingsSingleton.getGameState() == 1) {
            drawGame();
            Drawing.drawRectangle(0,0,Window.getWidth(),Window.getHeight(), new Colour(0,0,0,0.6));

            ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
            ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");


            if (leftCover != null) {
                leftCover.draw();
            }
            if (rightCover != null) {
                rightCover.draw();
            }
            imagePointManagerSingleton.draw();
            new ImagePoint("res/menu/gamemodeMenu.png", new Point(148,87)).draw();
            drawButtons();
        }
        else if (settingsSingleton.getGameState() == 3) {
            Character currentCharacter = allCharacters.get(allCharacters.size() % 2 == 1 ?
                    allCharacters.size() / 2 + 1 : allCharacters.size() / 2);

            imagePointManagerSingleton.getCurrentBackground().draw();

            double opacity = eventsListener.contains(EventCharacterPicked.class) ? 1: 0.8;
            FontSize characterFont = new FontSize(Fonts.TCB, 120);

            if (canInteract || eventsListener.contains(EventCharacterRotate.class)) {
                Drawing.drawRectangle(new Point(0,0), Window.getWidth(), Window.getHeight()/4d - 30, new Colour(0,0,0,opacity));
                Drawing.drawRectangle(new Point(0,Window.getHeight()*3/4d), Window.getWidth(), Window.getHeight()/4d, new Colour(0,0,0,opacity));
            }
            else {
                if (eventsListener.contains(EventCharacterPicked.class)) {
                    EventInterface event = eventsListener.getEvent(EventCharacterPicked.class);
                    Drawing.drawRectangle(0, Window.getHeight()/2d - characterFont.getSize()/2d,
                            5*timeLogger.getFrames()/ (double) event.getFrames(), characterFont.getSize(),
                            new Colour(1,1,1,0.5));
                }
            }
            if (isUnlocked(currentCharacter.getName())) {
                if (eventsListener.contains(EventCharacterPicked.class)) {
                    Drawing.drawRectangle(0, Window.getHeight()/2d - characterFont.getSize()/2d - 35, Window.getWidth(), characterFont.getSize(), new Colour(0,0,0,0.3));
                    drawShadowRender(currentCharacter.getFullName());
                    characterFont.draw(currentCharacter.getName(), Window.getWidth()/5d
                            - characterFont.getFont().getWidth(currentCharacter.getName())/2, Window.getHeight()/2d);
                    characterFont.draw(currentCharacter.getLastName(), Window.getWidth()*4/5d
                            - characterFont.getFont().getWidth(currentCharacter.getLastName())/2, Window.getHeight()/2d);
                }
                else {
                    characterFont.draw("CHOOSE                      WAIFU",Window.getWidth()/5d - 150, Window.getHeight()/2d);
                }
            }
            else {
                characterFont.draw("???", Window.getWidth()/5d
                        - characterFont.getFont().getWidth(currentCharacter.getName())/2, Window.getHeight()/2d);
                characterFont.draw("???", Window.getWidth()*4/5d
                        - characterFont.getFont().getWidth(currentCharacter.getLastName())/2, Window.getHeight()/2d);
            }
            if (!eventsListener.contains(EventCharacterPicked.class)) {
                drawBorders(currentCharacter);
                imagePointManagerSingleton.drawImagesWithTag("CharacterRender");
            }
            else {
                imagePointManagerSingleton.get(imagePointManagerSingleton.getImages().size()/2).draw();
            }
            for (Character character: allCharacters) {
                if (!isUnlocked(character.getName()) && imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())) != null) {
                    imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setDarken(true);
                    imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setOpacity(1);
                }
                else {
                    if (imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())) != null) {
                        imagePointManagerSingleton.get(String.format("res/characters/%s/Render.png", character.getFullName())).setDarken(false);
                    }
                }
            }
            if (canInteract || eventsListener.contains(EventCharacterRotate.class)) {
                drawButtons();
            }
        }
        else if (settingsSingleton.getGameState() == 4 ) {
            SideCharacter currentCharacter = allSideCharacters.get(allSideCharacters.size() % 2 == 1 ?
                allSideCharacters.size() / 2 + 1 : allSideCharacters.size() / 2);

            imagePointManagerSingleton.getCurrentBackground().draw();

            if (!toggleInfo) {
                Drawing.drawRectangle(new Point(0,0), Window.getWidth(), Window.getHeight()/4d - 30, new Colour(0,0,0,0.8));
                Drawing.drawRectangle(new Point(0,Window.getHeight()*3/4d), Window.getWidth(), Window.getHeight()/4d, new Colour(0,0,0,0.8));
                if (canBePicked(currentCharacter) && isUnlocked(currentCharacter.getName())) {
                    FontSize characterFont = new FontSize(Fonts.TCB, 120);
                    String firstName = currentCharacter.getName().split(" ")[0];
                    String lastName = "";
                    if (currentCharacter.getName().split(" ").length > 1) {
                        lastName = currentCharacter.getName().split(" ")[1];
                    }
                    characterFont.draw(firstName, Window.getWidth()/5d
                            - characterFont.getFont().getWidth(firstName)/2, Window.getHeight()/2d);
                    characterFont.draw(lastName, Window.getWidth()*4/5d
                            - characterFont.getFont().getWidth(lastName)/2, Window.getHeight()/2d);
                }

                if (canBePicked(currentCharacter)) {
                    imagePointManagerSingleton.get(String.format("res/SideCharacters/%s/render.png", currentCharacter.getName())).setOpacity(1);
                }
                drawBorders(currentCharacter);

                imagePointManagerSingleton.drawImagesWithTag("SideCharacterRender");
            }
            else {
                Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.5));
                imagePointManagerSingleton.get(String.format("res/SideCharacters/%s/render.png", currentCharacter.getName())).draw();
            }
            if (canInteract) {
                drawButtons();
            }
        }
        else if (settingsSingleton.getGameState() == 5) {

            int middleIndex = playableMaps.size() % 2 == 1 ? playableMaps.size() / 2 + 1 : playableMaps.size() / 2;

            new Image(String.format("res/maps/mapPeeks/%s.png", playableMaps.get(middleIndex).getName())).drawFromTopLeft(0,0);
            Drawing.drawRectangle(0,0, Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.7));
            imagePointManagerSingleton.drawImagesWithTag("MapRender");
            String mapName = "";
            for (String word: playableMaps.get(middleIndex).getName().split(" ")) {
                mapName += word + '\n';
            }
            new ImagePoint("res/menu/bar.png", new Point(-200,0)).draw();

            drawMapPicked();
            Image dio = new Image("res/maps/mapcharacters/dio.png");
            dio.drawFromTopLeft(Window.getWidth() - dio.getWidth(), Window.getHeight() - dio.getHeight());
            new FontSize(Fonts.TITANONE, 90).draw(mapName, 1275 - 200,Window.getHeight()/2d);
        }
        else if (settingsSingleton.getGameState() == 6) {
            if (settingsSingleton.getGameMode() == 1) {
                menuTitle = null;
                drawGame();
                if (canInteract) {
                    displayCharacterStats();
                }
                if(!gameSettingsSingleton.getMap().hasFinished()) {
                }
                else {
                    FontSize tempFont = new FontSize(Fonts.AGENCYB, 100);
                    tempFont.draw("REACH THE TOP!", Window.getWidth()/2d - tempFont.getFont().getWidth("REACH THE TOP!")/2, 160);
                }
                renderAbilities();
            }
        }
        else if (settingsSingleton.getGameState() == 7) {
            drawGame();
            Drawing.drawRectangle(0, 0, Window.getWidth(), Window.getHeight(), new Colour(0, 0, 0, 0.8));
            if (settingsSingleton.getGameMode() == 0) {
                displayFailScreen();
            }
            else {
                Player winner = settingsSingleton.getWinner();
                if (winner == null) {
                    new Font(Fonts.DEJAVUSANS, 110).drawString("DRAW?!?!!", 16, 100);
                }
                else {
                    ImagePoint characterImage = new ImagePoint(
                            String.format("res/Characters/%s/render.png", winner.getCharacter().getFullName()),
                            new Point(0,0));
                    ImagePoint sideCharacterImage = new ImagePoint(
                            String.format("res/SideCharacters/%s/render.png", winner.getSideCharacter().getName()),
                            new Point(0,0));
                    characterImage.setPos(Window.getWidth()/2d - characterImage.getWidth()/2, Window.getHeight() - characterImage.getHeight());
                    sideCharacterImage.setPos(Window.getWidth()/2d, Window.getHeight() - sideCharacterImage.getHeight());
                    sideCharacterImage.draw();
                    characterImage.draw();
                    new Font(Fonts.GEOMATRIX, 110).drawString(String.format("P%d: %s WINS!", settingsSingleton.getWinner().getId(), settingsSingleton.getWinner().getCharacter().getName()), 16, 100);
                    new Font(Fonts.TITANONE, 60).drawString(String.format("SCORE: %.0f", winner.getPlayerStats().getPoints()), Window.getWidth()/2d, Window.getHeight()*3/4d);
                }
            }
            imagePointManagerSingleton.draw();
            drawButtons();
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
                chooseCharacterFont.drawString("NEW CHARACTER UNLOCKED!", Window.getWidth()/2d - chooseCharacterFont.getWidth("NEW CHARACTER UNLOCKED")/2, 100);
                ImagePoint unlockedImage = new ImagePoint(String.format("res/Characters/%s/render.png", unlocked), new Point(0,0));
                unlockedImage.setPos(Window.getWidth()/2d - unlockedImage.getWidth()/2, Window.getHeight() - unlockedImage.getHeight());
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
                            tile.setPos(new Point(100, Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("1", 100, Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2);
                            tile1 = tile;
                        } else if (i % 2 == 0) {
                            tile.setPos(new Point(200 + tile.getImage().getWidth(), Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("2", 200 + tile.getImage().getWidth(), Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2);
                            tile2 = tile;
                        } else {
                            tile.setPos(new Point(300 + tile.getImage().getWidth() * 2, Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2));
                            victoryFont.drawString("3", 300 + tile.getImage().getWidth() * 2, Window.getHeight()/2d - allTiles.get(i).getImage().getHeight() / 2);
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
            Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.1));
            if (gameSettingsSingleton.getPage() == 0) {
                menuTitle = "General";
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("Map Speed: %1.2f",
                        gameSettingsSingleton.getMapSpeed()), 100, 300);
                new Font(Fonts.DEJAVUSANS, 100).drawString(String.format("Lives: %d", gameSettingsSingleton.getLives()),
                        100, 500);
            }
            else if (gameSettingsSingleton.getPage() == 1 || gameSettingsSingleton.getPage() == 2) {
                gameFont.drawString("Drag the slider across to increase/decrease the spawn rate!",
                        titleFont.getWidth("SETTINGS"), 150, new DrawOptions().setBlendColour(0,0,0, 0.7));
                gameFont.drawString("Click the icon to toggle on/off", titleFont.getWidth("SETTINGS"), 190,
                        DO.setBlendColour(0,0,0, 0.7));
            }
            drawSliders();
            drawButtons();
        }
        else if (settingsSingleton.getGameState() == 11) {
            Font playerFont = new Font(Fonts.DEJAVUSANS, 40);
            drawGame();
            playerFont.drawString("PAUSE", (Window.getWidth() - playerFont.getWidth("PAUSE"))/2, Window.getHeight()/2d -
                    50);
            playerFont.drawString("Press ESC to resume", (Window.getWidth() - playerFont.getWidth("Press ESC to res"))/2,
                    Window.getHeight()/2d);
            showTime();
        }
        else if (settingsSingleton.getGameState() == 12) {
            int dialogueInt = storySettingsSingleton.getDialogueInt();
            drawGame();
            imagePointManagerSingleton.draw();
            if (dialogue.isPlayingDialogue()) {
                if (dialogueInt == 1) {
                    Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), new Colour(0,0,0,0.5));
                }
                if (canInteract) {
                    dialogue.draw();
                }

                if (dialogueInt == 3) {
                    if (dialogue.getLineNo() == 11) {
                        DO.setScale(2.5,2.5);
                        new Image("res/PowerUps/Shield.png").draw(Window.getWidth()/2.0, Window.getHeight()/2.0, DO);
                        DO.setScale(1,1);
                    }
                }
            }
            else {

            }
        }
        else if (settingsSingleton.getGameState() == 14) {
            Drawing.drawRectangle(0,0,Window.getWidth(), Window.getHeight(), ColourPresets.WHITE.toColour());
            imagePointManagerSingleton.getCurrentBackground().draw();
            new ImagePoint("res/menu/main/name2.png", new Point(148, 87)).draw();
            ImagePoint leftCover = imagePointManagerSingleton.get("res/menu/main/leftcover.png");
            ImagePoint rightCover = imagePointManagerSingleton.get("res/menu/main/rightcover.png");

            if (leftCover != null) {
                leftCover.draw();
            }
            if (rightCover != null) {
                rightCover.draw();
            }
            drawButtons();
        }
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
        FontSize gameFont = new FontSize(Fonts.DEJAVUSANS, 40);
        gameFont.draw(timeLogger.getDisplayTime(), Window.getWidth() - gameFont.getFont().getWidth(timeLogger.getDisplayTime()), 40);
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
                int threshold = Integer.parseInt(achievementLine.get(achievementLine.size() - 1));
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
                int threshold = Integer.parseInt(achievementLine.get(achievementLine.size() - 1));
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

        double spacing = Window.getWidth()/6d;

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
                    characterPeek.setOpacity(0.3);
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

        double spacing = Window.getWidth()/6d;

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
                    characterPeek.setOpacity(0.3);
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


    public boolean canBePicked(Character character) {
        for (Player player : players) {
            if (player.getCharacter() != null) {
                if (player.getCharacter().getFullName().equals(character.getFullName())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canBePicked(SideCharacter character) {
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
            if (canBePicked(character)) {
                eventsListener.addEvent(new EventCharacterPicked(new Music(character.playLine(), 0).getFrameLength(), String.format("Player %d picked: %s", player.getId(), character.getFullName())));
                musicPlayer.addMusic("music/misc/Character Picked.wav");
                player.setCharacter(character);
                if (musicPlayer.contains(character.playLine())) {
                    musicPlayer.remove(character.playLine());
                }
                musicPlayer.addMusic(character.playLine());
            }
        }
    }

    public void pickSideCharacter(Player player, SideCharacter character) {
        if (canBePicked(character)) {
            String selectFile = String.format("music/sideCharacters/%s/select.wav", character.getName());
            Music select = new Music(selectFile, musicPlayer.getEffectVolume());
            eventsListener.addEvent(new EventSideCharacterPicked(select.getFrameLength(),
                    String.format("Player %d picked: %s", player.getId(), character.getName())));
            player.setSideCharacter(character);
            if (!musicPlayer.contains(selectFile)) {
                musicPlayer.addMusic(selectFile);
            } else {
                musicPlayer.restart(selectFile);
            }
        }
    }

    public void pickMap(Player player, Map map) {
        if (player.getMapChosen() == null) {
            musicPlayer.addMusic("music/misc/Click.wav");
            player.setMapChosen(map);
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
                String tileName = tile.getType().toString();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkCollisionTiles() {
        for (Player player: players) {
            if (!gameSettingsSingleton.getMap().hasFinished()) {
                Rectangle bottomRectangle = new Rectangle(0, Window.getHeight() + 40, Window.getWidth(), 20);
                if ((player.getCharacter().getRectangle().intersects(bottomRectangle))) {
                    player.getCharacter().setLives(0);
                }
            }
            for (Tile tile : gameSettingsSingleton.getMap().getVisibleTiles()) {
                Rectangle tileRectangle = new Rectangle(new Point(tile.getPos().x, tile.getPos().y),
                        tile.getImage().getWidth(), tile.getImage().getHeight());
                if (player.getCharacter().getRectangle().intersects(tileRectangle)) {
                    if (tile.getType().toString().contains(TileType.ICE.toString())) {
                        player.getCharacter().onIce();
                    }
                    else if (tile.getType().toString().contains(TileType.SLOW.toString())) {
                        player.getCharacter().onSlow();
                    }
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
                if (gameSettingsSingleton.getMap().hasUpdated() && !gameSettingsSingleton.getMap().hasFinished() && !character.isMoving()) {
                    character.slide();
                }
                player.getCharacter().update();
            }
        }
    }

    public void updateAbilities() {
        for (Player player: players) {
            if (player.getSideCharacter().isActivating()) {
                player.getSideCharacter().activateAbility(player);
            }
        }
    }

    public void addTileToCustomMap(TileType line) {
        int currentRow = customMapTiles.size()/4;
        int currentBlocksInRow = customMapTiles.size()%4;
        Point point = new Point(480 * currentBlocksInRow, 15*offset + 600 + -475 * currentRow);

        Tile tile = null;
        switch (line) {
            case BASIC:
                tile = new TileBasic(point);
                break;
            case BASICTOP:
                tile = new TileBasicTop(point);
                break;
            case BASICLEFT:
                tile = new TileBasicLeft(point);
                break;
            case ICE:
                tile = new TileIce(point);
                break;
            case ICETOP:
                tile = new TileIceTop(point);
                break;
            case ICELEFT:
                tile = new TileIceLeft(point);
                break;
            case SLOW:
                tile = new TileSlow(point);
                break;
            case SLOWLEFT:
                tile = new TileSlowLeft(point);
                break;
            case SLOWTOP:
                tile = new TileSlowTop(point);
                break;
            case BASICSAD:
                tile = new TileBasicSad(point);
                break;
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
            player.getCharacter().setPosition(new Point(Window.getWidth()/2d - (players.size() * width / 2) +
                    spawnDivider * width, Window.getHeight() - 200));
            spawnDivider++;
        }
    }

    public void updateExp() {
        updateObstacleInteraction();
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

    public void updateObstacleInteraction() {
        for (Obstacle obstacle: obstacles) {
            Point obstacleCentre = new Point(obstacle.getPos().x + obstacle.getImage().getWidth()/2,
                    obstacle.getPos().y + obstacle.getImage().getHeight()/2);
            Drawing.drawRectangle(obstacleCentre, 10,10, new Colour(0,0,0));
            for (Player player: players) {
                Point characterCentre = player.getCharacter().getPos();
                Drawing.drawRectangle(characterCentre, 200, 10, new Colour(0,0,0));
                if (!player.getSideCharacter().isActivating()) {
                    if (!obstacle.getPlayersInteracted().contains(player.getId())) {
                        if (Math.abs(obstacleCentre.y -characterCentre.y) < 10) {
                            double distance = Math.abs(obstacleCentre.x - characterCentre.x);
                            if (distance < 200) {
                                if (distance < 20) {
                                    player.getPlayerStats().closeCall();
                                }
                                player.getCharacter().gainSpecialAbilityBar((200 - distance)/10);
                                player.getPlayerStats().obstacleDodged();
                                obstacle.updatePlayersInteracted(player.getId());
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
            playableMaps.add(new Map(mapName));
        }
    }

    public void loadCustomMapConfigs() {
        allTiles.addAll(Arrays.asList(
                new TileBasic(new Point(0,0)), new TileBasicLeft(new Point(0,0)) , new TileBasicTop(new Point(0,0)),
                new TileIce(new Point(0,0)), new TileIceLeft(new Point(0,0)), new TileIceTop(new Point(0,0)),
                new TileSlow(new Point(0,0)), new TileSlowLeft(new Point(0,0)), new TileSlowTop(new Point(0,0))));
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
        double spacing = Window.getHeight()/4d;
        int index = 0;
        double minScale = 0.275;
        double maxScale = 1;
        int middleIndex = allCharacters.size() % 2 == 1 ? allCharacters.size() / 2 + 1 : allCharacters.size() / 2;

        for (Character character: allCharacters) {
            ImagePoint characterRender = new ImagePoint(String.format("res/Characters/%s/render.png",
                    character.getFullName()),
                    new Point(0, 0), "CharacterRender");
            characterRender.setScale(minScale);
            characterRender.setOpacity(0.5);
            if (index < middleIndex) {
                characterRender.setPos(((allCharacters.size() * spacing/2) - Window.getWidth())/2 - 490 +
                                (spacing * (index - 1)),
                        Window.getHeight()*3/4d);
            }
            else if (middleIndex == index) {
                characterRender.setScale(maxScale);
                characterRender.setPos(490, Window.getHeight()/8d);
                characterRender.setOpacity(1);
            }
            else {
                characterRender.setPos( Window.getWidth()/2d + (spacing * (index - middleIndex)),
                        Window.getHeight()*3/4d);
            }
            imagePointManagerSingleton.add(characterRender);
            index++;
        }
    }

    public void loadPlayers() {
        for (Player player: players) {
            player.getPlayerStats().reset();
            player.getCharacter().setLives(gameSettingsSingleton.getLives());
        }
        setPlayersPosition();
    }

    public void displayCharacterStats() {
        ArrayList<Player> players = settingsSingleton.getPlayers();
        int playerIndex = 0;
        for (Player player: players) {
            Character character = player.getCharacter();
            ImagePoint characterDisplay = new ImagePoint(String.format("res/characters/%s/Peek.png",
                    character.getFullName()), new Point(0,0));
            if (character.getSpecialAbilityBar() >= 100) {
                new FontSize(Fonts.TITANONE, 30).getFont().drawString("Power Ready!",
                        characterDisplay.getPos().x, characterDisplay.getPos().y,
                        new DrawOptions().setBlendColour(247d/255, 251d/255, 142d/255));
            }
            ImagePoint bigBorder = new ImagePoint("res/misc/Selected.png", new Point(0,0));
            ImagePoint smallBorder = new ImagePoint("res/misc/Selected.png", new Point(0,0));
            smallBorder.setScale(0.375);
            bigBorder.setPos((playerIndex + 1) * 185 + (playerIndex) * bigBorder.getWidth(), Window.getHeight() -
                    bigBorder.getHeight());
            smallBorder.setPos((playerIndex +1 ) * 185 + (playerIndex) * bigBorder.getWidth() + 50, Window.getHeight() -
                    smallBorder.getHeight()*smallBorder.getScale());
            characterDisplay.setPos(bigBorder.getPos().x, bigBorder.getPos().y);
            bigBorder.draw();
            smallBorder.draw();
            characterDisplay.draw();
            if (character.hasPowerUp()) {
                new Image(String.format("res/powerUps/%s.png", character.getPowerUp().getType())).
                        drawFromTopLeft(smallBorder.getPos().x, smallBorder.getPos().y);
            }
            new FontSize(Fonts.TITANONE, 50).draw(String.format("%.0f", player.getPlayerStats().getPoints()),
                    characterDisplay.getPos().x, Window.getHeight() - 25);

            int buffs = 0;

            if (player.getCharacter().hasShield()) {
                ImagePoint shieldIcon = new ImagePoint("res/misc/Shield.png", new Point(0,0));
                shieldIcon.setPos(new Point(characterDisplay.getPos().x,
                        Window.getHeight() - shieldIcon.getHeight()));
                shieldIcon.draw();
                buffs++;
            }
            if (player.getCharacter().isMinimised()) {
                ImagePoint miniIcon = new ImagePoint("res/PowerUps/Minimiser.png", new Point(0,0));
                miniIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                        Window.getHeight() - miniIcon.getHeight()));
                miniIcon.draw();
                buffs++;
            }
            if (player.getCharacter().isSpedUp()) {
                ImagePoint speedIcon = new ImagePoint("res/PowerUps/SpeedUp.png", new Point(0,0));
                speedIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                        Window.getHeight() - speedIcon.getHeight()));
                speedIcon.draw();
                buffs++;
            }
            if (player.getCharacter().isSpedDown()) {
                ImagePoint slowIcon = new ImagePoint("res/obstacles/SpeedDown.png", new Point(0,0));
                slowIcon.setPos(new Point(characterDisplay.getPos().x + buffs * 50,
                        Window.getHeight() - slowIcon.getHeight()));
                slowIcon.draw();
            }

            if (!player.getSideCharacter().getName().equals(CharacterNames.YUGI)) {
                ImagePoint sideCharacter = new ImagePoint(String.format("res/sideCharacters/%s/inGame.png",
                        player.getSideCharacter().getName()),
                        new Point(0,0));
                sideCharacter.setPos(characterDisplay.getWidth() +
                                playerIndex*Window.getWidth()/(double)players.size(),
                        Window.getHeight() - characterDisplay.getHeight());
                sideCharacter.setSection(0, 0, sideCharacter.getWidth(),
                        sideCharacter.getHeight()*player.getCharacter().getSpecialAbilityBar()/100);
                sideCharacter.draw();
            }
            else {
                SideYugi yugi = (SideYugi) player.getSideCharacter();
                int index = 0;
                for (ExodiaPiece exodiaPiece: yugi.getExodiaPiecesCollected()) {
                    exodiaPiece.getImage().drawFromTopLeft(
                            characterDisplay.getWidth() + playerIndex*Window.getWidth()/(double)players.size() +
                                    exodiaPiece.getImage().getWidth()*index,
                            Window.getHeight() - characterDisplay.getHeight());
                    index++;
                }
            }
            playerIndex++;
        }
    }

    public void drawCurrentHeight() {
        Font gameFont = new FontSize(Fonts.DEJAVUSANS, 40).getFont();
        if (!settingsSingleton.isNight()) {
            gameFont.drawString(String.format("%4.0f/%4.0fm", gameSettingsSingleton.getMap().getCurrentHeight()/10,
                    gameSettingsSingleton.getMap().getHeight()/10), Window.getWidth()/2d - 50, 50,
                    new DrawOptions().setBlendColour(ColourPresets.WHITE.toColour()));
        }
        else {
            gameFont.drawString(String.format("%4.0f/%4.0fm", gameSettingsSingleton.getMap().getCurrentHeight()/10,
                    gameSettingsSingleton.getMap().getHeight()/10), Window.getWidth()/2d - 50, 50,
                    new DrawOptions().setBlendColour(ColourPresets.BLACK.toColour()));
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
        if (gameSettingsSingleton.getMap() != null) {
            gameSettingsSingleton.getMap().draw();
        }
        if (canInteract) {
            for (Player player : players) {
                if (player.getCharacter() != null && !player.getCharacter().isDead()) {
                    player.getCharacter().draw();
                    Font gameFont = new Font(Fonts.GEOMATRIX, 40);
                    gameFont.drawString(String.format("P%s", player.getId()), player.getCharacter().getPos().x,
                            player.getCharacter().getPos().y + 100);
                }
            }
        }
        for (PowerUp powerUp: powerUps) {
            powerUp.draw();
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.getImage().drawFromTopLeft(obstacle.getPos().x, obstacle.getPos().y);
        }
        if (gameSettingsSingleton.getMap() != null && !gameSettingsSingleton.getMap().hasFinished()) {
            drawCurrentHeight();
        }
    }

    public void drawSettings() {
        if (gameSettingsSingleton.getMap() != null) {
            gameSettingsSingleton.getMap().draw();
        }
        for (PowerUp powerUp: powerUps) {
            powerUp.draw();
        }
        for (Obstacle obstacle: obstacles) {
            obstacle.draw();
        }
    }

    public void displayFailScreen() {
        buttonsToRemove.addAll(buttons);
        buttons.add(new Button("Retry", "Continue?",
                new FontSize(Fonts.DEJAVUSANS, 160),
                new Rectangle(0, Window.getHeight()/2d, Window.getWidth(), 160),
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
        double xOffset = -(currentMousePosition.x - Window.getWidth()/2d + 800)/Window.getWidth()*100;
        double yOffset = -(currentMousePosition.y - Window.getHeight()/2d + 400)/Window.getHeight()*100;
         if (imagePointManagerSingleton.get(String.format("res/Characters/%s/render.png", charName)) != null) {
             ImagePoint shadow = new ImagePoint(String.format("res/Characters/%s/render.png", charName),
                     imagePointManagerSingleton.get(String.format("res/Characters/%s/render.png", charName)).getPos());
             shadow.setPos(shadow.getPos().x + xOffset, shadow.getPos().y + yOffset);
             double opacity = Math.abs(Math.sin(timeLogger.getFrames()/70d))*0.3 + 0.4;
             shadow.setOpacity(opacity);
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
            gameSettingsSingleton.setMap(new Map(MapNames.TRAINING_GROUND));
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
            if (player.getCharacter() != null && !player.getCharacter().isDead()) {
                notDead = true;
            }
        }
        if (!notDead) {
            gameSettingsSingleton.getMap().generateMap();
            players.clear();
        }
        updatePlayerMovement(input);
        checkCollisionTiles();
        gameEntities.checkCollisionObstacles();
        gameEntities.checkCollisionPowerUps();
        gameEntities.updateObjects();
        gameEntities.spawnPowerUps();
        gameEntities.spawnObstacles();
    }

    public void updateSettings() {
        if (gameSettingsSingleton.getMap() == null) {
            gameSettingsSingleton.setMap(new Map(MapNames.TRAINING_GROUND));
            gameSettingsSingleton.getMap().generateMap();
        }
        gameEntities.spawnObstacles();
        gameEntities.spawnPowerUps();
        gameEntities.updateObjects();
    }

    public void loadMapRender() {
        double xOffset = 400;
        double spacing = 50;
        int index = 0;
        double minScale = 0.2;
        double maxScale = 0.3;
        int middleIndex = playableMaps.size() % 2 == 1 ? playableMaps.size() / 2 + 1 : playableMaps.size() / 2;

        for (Map map: playableMaps) {
            ImagePoint mapRender = new ImagePoint(String.format("res/maps/mapPeeks/%s.png", map.getName()),
                    new Point(0, 0), "MapRender");
            mapRender.setScale(minScale);
            mapRender.setOpacity(0.3);
            if (middleIndex == index) {
                mapRender.setScale(maxScale);
                mapRender.setOpacity(1);
            }

            double yOffset = index > middleIndex ? maxScale*mapRender.getHeight() - minScale*mapRender.getHeight() : 0;


            mapRender.setPos(-200 + Window.getWidth()/2d - (maxScale*mapRender.getWidth()/2) +
                            (Math.abs(middleIndex - index) * xOffset),
                    (Window.getHeight()/2d - (maxScale*mapRender.getHeight()/2) + yOffset +
                            (index - middleIndex) * (spacing + (mapRender.getHeight()*mapRender.getScale()))));

            imagePointManagerSingleton.add(mapRender);
            index++;
        }
    }

    public void rotateMap(String direction) {
        if (direction.equals("UP")) {
            Map temp = playableMaps.get(0);
            playableMaps.remove(0);
            playableMaps.add(temp);
        }
        else if (direction.equals("DOWN")) {
            Map temp = playableMaps.get(playableMaps.size() - 1);
            playableMaps.remove(temp);
            playableMaps.add(0, temp);
        }
    }

    public void drawMapPicked() {
        int notPicked = 0;
        int picked = 0;
        int middleIndex = playableMaps.size() % 2 == 1 ? playableMaps.size() / 2 + 1 : playableMaps.size() / 2;
        ImagePoint middleMapRender = imagePointManagerSingleton.get(String.format("res/maps/mapPeeks/%s.png", playableMaps.get(middleIndex).getName()));
        for (Player player: players) {
            ImagePoint playerAvatar = new ImagePoint(String.format("res/characters/%s/Peek.png", player.getCharacter().getFullName()), new Point(0,0));
            if (player.getMapChosen() != null && player.getMapChosen().getName().equals(playableMaps.get(middleIndex).getName())) {
                playerAvatar.setScale(0.5);
                playerAvatar.setPos(- 200 + middleMapRender.getPos().x + middleMapRender.getWidth()*middleMapRender.getScale() - ((1 + picked) * playerAvatar.getWidth()*playerAvatar.getScale()),
                        middleMapRender.getPos().y + middleMapRender.getHeight()*middleMapRender.getScale() - playerAvatar.getHeight()*playerAvatar.getScale());
                playerAvatar.draw();
                picked ++;
            }
            else if (player.getMapChosen() == null) {
                playerAvatar.setScale(0.3);
                playerAvatar.setPos(- 200 + Window.getWidth()/3d - ((1 + notPicked) * playerAvatar.getWidth()*playerAvatar.getScale()), Window.getHeight()/2d - playerAvatar.getHeight()*playerAvatar.getScale()/2);
                playerAvatar.draw();
                notPicked ++;
            }
        }
    }

    public void loadComputer(Computer computer) {
        for (Character character: playableCharacters) {
            if (!players.get(0).getCharacter().getName().equalsIgnoreCase(character.getName())) {
                computer.setCharacter(new Character(character.getFullName()));
                System.out.println("Computer : " + computer.getCharacter().getFullName());
                break;
            }
        }
        for (SideCharacter character: allSideCharacters) {
            if (!players.get(0).getSideCharacter().getName().equalsIgnoreCase(character.getName())) {
                computer.setSideCharacter(character);
                System.out.println("Computer : " + computer.getSideCharacter().getName());
                break;
            }
        }
    }
}