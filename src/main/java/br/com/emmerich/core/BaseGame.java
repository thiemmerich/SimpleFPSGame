package br.com.emmerich.core;

import br.com.emmerich.entities.ActionsHandler;
import br.com.emmerich.entities.Player;
import br.com.emmerich.trace.BulletTrace;
import br.com.emmerich.entities.CustomCamera;
import br.com.emmerich.hud.HUDManager;
import br.com.emmerich.utils.CollisionDetector;
import br.com.emmerich.utils.CustomEnvironment;
import br.com.emmerich.environment.WorldBuilder;
import br.com.emmerich.utils.TextureManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class BaseGame extends ApplicationAdapter {

    private CustomCamera camera;
    private ModelBatch modelBatch;
    private CustomEnvironment environment;
    private WorldBuilder worldBuilder;
    private ActionsHandler actionsHandler;
    private CollisionDetector collisionDetector;
    private TextureManager textureManager;
    private HUDManager hudManager;
    private BulletTrace bulletTrace;

    // Player collision
    private BoundingBox playerBounds;
    private Player player;

    @Override
    public void create() {
        textureManager = new TextureManager();
        setupCollision();
        setupCamera();
        setupGraphics();
        setupWorld();
        setupInput();
        bulletTrace = new BulletTrace();
        hudManager = new HUDManager();
        player = new Player(camera);
        actionsHandler = new ActionsHandler(camera, hudManager, collisionDetector, player);
    }

    private void setupCamera() {
        camera = new CustomCamera(collisionDetector, 67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupGraphics() {
        modelBatch = new ModelBatch();
        environment = new CustomEnvironment();
    }

    private void setupWorld() {
        worldBuilder = new WorldBuilder(collisionDetector, textureManager);
        worldBuilder.setupWorld();
    }

    private void setupInput() {
        Gdx.input.setCursorCatched(true); // Hide and capture mouse
    }

    private void setupCollision() {
        collisionDetector = new CollisionDetector();
        // Player collision bounds (width, height, depth)
        playerBounds = new BoundingBox(
                new Vector3(-0.3f, 0f, -0.3f),  // min
                new Vector3(0.3f, 1.8f, 0.3f)   // max
        );
    }

    @Override
    public void render() {
        handleInput(Gdx.graphics.getDeltaTime());
        camera.update();

        // Clear screen
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // Render 3D scene
        modelBatch.begin(camera.getPerspectiveCamera());
        modelBatch.render(worldBuilder.getInstances(), environment);

        // Render bullet traces (within the same modelBatch)
        actionsHandler.renderBulletTraces(modelBatch, environment);

        modelBatch.end();

        // Render HUD (on top of everything)
        hudManager.update(Gdx.graphics.getDeltaTime());
        hudManager.setWeapon(player.getCurrentWeapon());
        hudManager.render();

        // Handle actions
        actionsHandler.handleActions();
    }

    private void handleInput(float deltaTime) {
        camera.handleMouseLook();
        camera.handleMovement(deltaTime);
        actionsHandler.handleActions();
    }

    @Override
    public void resize(int width, int height) {
        camera.resize(width, height);
        hudManager.resize(width, height);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        worldBuilder.dispose();
        textureManager.dispose();
        hudManager.dispose();
        bulletTrace.dispose();
    }
}
