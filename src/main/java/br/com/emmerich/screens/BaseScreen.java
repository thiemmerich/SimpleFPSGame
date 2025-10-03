package br.com.emmerich.screens;

import br.com.emmerich.core.GameManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseScreen implements Screen {
    protected GameManager game;
    protected Stage stage;

    public BaseScreen(GameManager game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        // Set input processor for UI
        com.badlogic.gdx.Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    protected void clearScreen() {
        com.badlogic.gdx.graphics.GL20 gl = com.badlogic.gdx.Gdx.gl;
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        gl.glClear(com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT);
    }
}
