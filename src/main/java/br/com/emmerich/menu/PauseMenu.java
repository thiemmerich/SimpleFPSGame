package br.com.emmerich.menu;

import br.com.emmerich.core.GameManager;
import br.com.emmerich.screens.MainMenuScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PauseMenu {
    private Stage stage;
    private GameManager game;
    private Table table;
    private boolean visible = false;

    public PauseMenu(Stage stage, GameManager game) {
        this.stage = stage;
        this.game = game;
        createUI();
    }

    private void createUI() {
        Skin skin = createBasicSkin();

        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);

        // Semi-transparent background
        table.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));

        // Title
        Label titleLabel = new Label("GAME PAUSED", skin);
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2f);

        // Buttons
        TextButton resumeButton = new TextButton("RESUME", skin);
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        TextButton mainMenuButton = new TextButton("MAIN MENU", skin);
        TextButton exitButton = new TextButton("EXIT GAME", skin);

        // Button sizes
        resumeButton.getLabel().setFontScale(1.2f);
        optionsButton.getLabel().setFontScale(1.2f);
        mainMenuButton.getLabel().setFontScale(1.2f);
        exitButton.getLabel().setFontScale(1.2f);

        // Layout
        table.add(titleLabel).padBottom(60).row();
        table.add(resumeButton).width(250).height(50).padBottom(15).row();
        table.add(optionsButton).width(250).height(50).padBottom(15).row();
        table.add(mainMenuButton).width(250).height(50).padBottom(15).row();
        table.add(exitButton).width(250).height(50).padBottom(15).row();

        // Button listeners
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
                // Resume logic handled by GameScreen
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                com.badlogic.gdx.Gdx.app.exit();
            }
        });

        stage.addActor(table);
    }

    private Skin createBasicSkin() {
        Skin skin = new Skin();

        // Create a simple white pixel for drawables
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        return skin;
    }

    public void show() {
        visible = true;
        table.setVisible(true);
    }

    public void hide() {
        visible = false;
        table.setVisible(false);
    }

    public boolean isVisible() {
        return visible;
    }
}
