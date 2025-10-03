package br.com.emmerich.screens;

import br.com.emmerich.core.GameManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen extends BaseScreen {
    private Texture backgroundTexture;
    private Image backgroundImage;

    public MainMenuScreen(GameManager game) {
        super(game);
        createUI();
    }

    private void createUI() {
        // Create background (optional)
        try {
            backgroundTexture = new Texture(Gdx.files.internal("assets/ui/menu_bg.png"));
            backgroundImage = new Image(backgroundTexture);
            backgroundImage.setFillParent(true);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("No background image found, using solid color");
        }

        // Create skin
        Skin skin = createBasicSkin();

        // Create table for layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("FPS GAME", skin);
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2.5f);

        // Buttons
        TextButton playButton = new TextButton("PLAY", skin);
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        TextButton exitButton = new TextButton("EXIT", skin);

        // Button sizes
        playButton.getLabel().setFontScale(1.5f);
        optionsButton.getLabel().setFontScale(1.5f);
        exitButton.getLabel().setFontScale(1.5f);

        // Layout
        table.add(titleLabel).padBottom(80).row();
        table.add(playButton).width(300).height(60).padBottom(20).row();
        table.add(optionsButton).width(300).height(60).padBottom(20).row();
        table.add(exitButton).width(300).height(60).padBottom(20).row();

        // Button listeners
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(game));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private Skin createBasicSkin() {
        Skin skin = new Skin();

        // Create a simple white pixel for drawables
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Create font
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        // Create button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.fontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        // Create label style
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        return skin;
    }

    @Override
    public void render(float delta) {
        clearScreen();
        super.render(delta);
    }
}
