# FPS Game - LibGDX
#### A first-person shooter game built with Java and LibGDX framework featuring 3D graphics, collision detection, textured environments, and a complete menu system.

* https://img.shields.io/badge/Game-FPS-blue
* https://img.shields.io/badge/Framework-LibGDX-green
* https://img.shields.io/badge/Language-Java-orange
* https://img.shields.io/badge/Status-In%2520Development-yellow

## Features
### Core Gameplay
* First-person camera with mouse look controls
* WASD movement with proper physics and collision detection
* Jumping mechanics with gravity simulation
* 3D environment with textured floors, walls, and objects
* Collision system preventing walking through walls and objects

## Graphics & Visuals
* 3D rendering with LibGDX ModelBatch
* Textured environments (grass, brick, wood, concrete)
* Dynamic lighting with directional and ambient light
* Custom camera system with pitch and yaw controls

## User Interface
* Main menu with navigation options
* Pause menu accessible during gameplay
* Options screen with settings management
* HUD with crosshair for aiming

## Technical Features
* Modular architecture with separated concerns
* Custom collision detection system
* Texture management with automatic fallbacks
* Screen management for smooth transitions

## Getting Started
### Prerequisites
* Java 8 or higher
* Maven 3.6+
* Git

### Installation
#### Clone the repository
`git clone https://github.com/yourusername/fps-game-libgdx.git`

`cd fps-game-libgdx`

#### Build the project
`mvn clean compile`

#### Run the game
`mvn exec:java -Dexec.mainClass="br.com.emmerich.DesktopLauncher"`

## Controls
* WASD: Move forward/backward/left/right
* Mouse: Look around
* Space: Jump
* Left Click: Shoot
* R: Reload
* ESC: Pause game / Show cursor

## Game Architecture
### Core Components
* GameManager: Main game controller and screen manager
* BaseGame: Core gameplay logic and rendering
* CustomCamera: First-person camera with input handling
* World: Environment creation and management
* TextureManager: Resource loading and management

### Builder Pattern
* WallBuilder: Creates textured walls with collision
* CrateBuilder: Creates textured crates with collision

### Screen System
* BaseScreen: Abstract base for all screens
* MainMenuScreen: Entry point with navigation
* GameScreen: Main gameplay screen
* PauseMenu: In-game pause functionality
* OptionsScreen: Settings configuration

## Development
### Adding New Features
* New Textures: Place in src/main/resources/assets/textures/
* New Screens: Extend BaseScreen class
* New Game Objects: Use builder pattern or extend existing builders
* New Gameplay Features: Modify BaseGame or add new entity classes

## Dependencies
* LibGDX 1.12.1: Core game framework
* LWJGL3: Desktop backend
* Bullet Physics: 3D physics (optional)
* Maven: Build automation

## Asset Requirements
### Place these files in src/main/resources/assets/textures/:
* grass.png - Ground texture
* brick.png - Wall texture
* wood.png - Crate texture
* concrete.png - Platform texture

## Customization
### Changing Textures
Replace texture files in the assets/textures/ directory. Supported formats: PNG, JPG.

### Modifying Controls
Edit input handling in CustomCamera.java and ActionsHandler.java.

### Adjusting Game World
Modify WorldBuilder.java to change level layout and object placement.

## Acknowledgments
* LibGDX team for the excellent game development framework
* OpenGameArt and Texture Haven for free texture resources
* The Java gaming community for tutorials and examples

