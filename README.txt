README
_______________________________________________________________________________________________________
The Climb is a party game where 2-4 players have to climb a wall while dodging obstacles. Whoever gets to the top
first wins. There are also power-ups (See PowerUps section).
_______________________________________________________________________________________________________

  A game I intend to build and further progress inside Unity/Unreal Engine.
  Written from scratch (apart from a basic rendering library that I grabbed from a university project).
  
  Features

  Menu
  - Hover effect
  - Sound feedback
  - Animations
  
  Story Mode (TBD):
  - Functions are built and in-place from previous prototype iterations (allowing me to build it when I have the time to come back to this project!)
  
  Tutorial (in progress):
  - Allows new players to get a fundamental grasp of the game's mechanics
  
  Pre-game features:
  - Game Settings with live visual feedback (e.g. adjusting the spawn rates of objects will be reflected in the background)
  - Map Settings (adjust map speed, player life count)
  - Characters can be unlocked progressively through achievements (for now all characters are unlocked via branching out play time on other characters)
  - Sound feedback on character selection
  - Toggle feature to show more info on Abilities Selection screen (Side Character)
  - Audio Settings (main volume, effects volume are all adjustable)
  
  In-game features:
  - Live scrolling map with configurable settings (a pre-game feature)
  - Upper visual border contains information about map character (See below), including time left before summit is reached, and map character interaction intervals
  - Lower visual border contains information about players (e.g. Lives left, Ability Charged?, Player Status [Slowed, Sped Up, Shielded])
  
  Maps:
  -   Each map has a unique character associated with varying interactions
  -   Maps are easily configurable
  -   Custom Map Creation is available in Main Menu Screen


_______________________________________________________________________________________________________
CONTROLS

Player 1 controls:
Move Up: W
Move Down: S
Move Left: A
Move Right: D
Pick character: T

Player 2 controls:
Move Up: UP Arrow
Move Down: DOWN Arrow
Move Left: LEFT Arrow
Move Right: RIGHT Arrow
Pick character: P

Player 3 controls:
Move Up: Y
Move Down: H
Move Left: G
Move Right: J
Pick character: Z

Player 4 controls:
Move Up: O
Move Down: L
Move Left: K
Move Right: ;
Pick character: M
___________________________________________________________________________________________________________
POWERUPS

SpeedUp: Grants a movement speed buff for 3 seconds
Shield: Grants a shield that breaks when colliding with an obstacle.
Minimiser: Transform into a mini version, be less vulnerable to colliding with obstacles for 3 seconds
Special Ability Gain: Fills special ability bar up.

POWERDOWNS

ObstacleSpeedDown: Grants a movement speed debuff for 3 seconds.
