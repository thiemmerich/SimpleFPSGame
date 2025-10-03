package br.com.emmerich.core;

import br.com.emmerich.screens.MainMenuScreen;
import com.badlogic.gdx.Game;

public class GameManager extends Game {

    @Override
    public void create() {
        // Start with the main menu
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}
