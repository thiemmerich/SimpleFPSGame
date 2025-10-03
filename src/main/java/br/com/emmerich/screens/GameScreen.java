package br.com.emmerich.screens;

import br.com.emmerich.core.BaseGame;
import br.com.emmerich.core.GameManager;
import br.com.emmerich.menu.PauseMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameScreen extends BaseScreen {
    private final BaseGame baseGame;
    private boolean isPaused = false;
    private final PauseMenu pauseMenu;

    public GameScreen(GameManager game) {
        super(game);
        baseGame = new BaseGame();
        baseGame.create();
        pauseMenu = new PauseMenu(stage, game);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        // Handle pause input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        if (!isPaused) {
            // Render actual game (FPSGame handles its own rendering)
            baseGame.render();
        } else {
            // Render pause menu
            clearScreen();
            super.render(delta); // This renders the stage for UI
        }
    }

    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            Gdx.input.setCursorCatched(false);
            pauseMenu.show();
            // Use stage input processor for UI
            Gdx.input.setInputProcessor(stage);
        } else {
            Gdx.input.setCursorCatched(true);
            pauseMenu.hide();
            // No input processor needed for FPS game (it handles its own input)
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        baseGame.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        baseGame.dispose();
    }
}
