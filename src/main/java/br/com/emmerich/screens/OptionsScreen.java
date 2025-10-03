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

public class OptionsScreen extends BaseScreen {

    private Texture backgroundTexture;
    private Image backgroundImage;

    public OptionsScreen(GameManager game) {
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

        Skin skin = createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("OPTIONS", skin);
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2.5f);

        // Settings
        Label volumeLabel = new Label("Volume", skin);
        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setValue(80); // Default volume

        Label sensitivityLabel = new Label("Mouse Sensitivity", skin);
        Slider sensitivitySlider = new Slider(0.1f, 2f, 0.1f, false, skin);
        sensitivitySlider.setValue(1.0f); // Default sensitivity

        CheckBox fullscreenCheckbox = new CheckBox(" Fullscreen", skin);
        fullscreenCheckbox.setChecked(false);

        // Back button
        TextButton backButton = new TextButton("BACK", skin);

        // Layout
        table.add(titleLabel).padBottom(60).row();
        table.add(volumeLabel).left().padBottom(5).row();
        table.add(volumeSlider).width(300).padBottom(30).row();
        table.add(sensitivityLabel).left().padBottom(5).row();
        table.add(sensitivitySlider).width(300).padBottom(30).row();
        table.add(fullscreenCheckbox).padBottom(30).row();
        table.add(backButton).width(200).height(50).row();

        // Listeners
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // TODO: Implement volume change
                System.out.println("Volume: " + volumeSlider.getValue());
            }
        });
    }

    // ... createBasicSkin() same as in MainMenuScreen ...
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

        // Slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        sliderStyle.knob = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default-horizontal", sliderStyle);

        // Checkbox style
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOn = skin.newDrawable("white", Color.GREEN);
        checkBoxStyle.checkboxOff = skin.newDrawable("white", Color.DARK_GRAY);
        skin.add("default", checkBoxStyle);

        return skin;
    }
}
