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
        // Create background (optional - try to load image, fallback to color)
        try {
            backgroundTexture = new Texture(Gdx.files.internal("assets/ui/menu_bg.png"));
            backgroundImage = new Image(backgroundTexture);
            backgroundImage.setFillParent(true);
            stage.addActor(backgroundImage);
        } catch (Exception e) {
            System.out.println("No background image found for options screen, using solid color");
            // Create a solid color background
            Table backgroundTable = new Table();
            backgroundTable.setFillParent(true);
            backgroundTable.setBackground(createBasicSkin().newDrawable("white", new Color(0.1f, 0.1f, 0.2f, 1f)));
            stage.addActor(backgroundTable);
        }

        Skin skin = createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("OPTIONS", skin);
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2f);

        // Settings
        Label volumeLabel = new Label("Volume", skin);
        volumeLabel.setFontScale(1.2f);

        Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setValue(80); // Default volume

        // Volume value display
        Label volumeValue = new Label("80%", skin);
        volumeValue.setFontScale(1.1f);

        Label sensitivityLabel = new Label("Mouse Sensitivity", skin);
        sensitivityLabel.setFontScale(1.2f);

        Slider sensitivitySlider = new Slider(0.1f, 2f, 0.1f, false, skin);
        sensitivitySlider.setValue(1.0f); // Default sensitivity

        // Sensitivity value display
        Label sensitivityValue = new Label("1.0", skin);
        sensitivityValue.setFontScale(1.1f);

        CheckBox fullscreenCheckbox = new CheckBox(" Fullscreen", skin);
        fullscreenCheckbox.getLabel().setFontScale(1.2f);
        fullscreenCheckbox.setChecked(false);

        // Back button
        TextButton backButton = new TextButton("BACK", skin);
        backButton.getLabel().setFontScale(1.3f);

        // Layout
        table.add(titleLabel).padBottom(60).row();

        // Volume row
        Table volumeTable = new Table();
        volumeTable.add(volumeLabel).left().width(200).padRight(10);
        volumeTable.add(volumeSlider).width(200);
        volumeTable.add(volumeValue).width(60).padLeft(10);
        table.add(volumeTable).padBottom(30).row();

        // Sensitivity row
        Table sensitivityTable = new Table();
        sensitivityTable.add(sensitivityLabel).left().width(200).padRight(10);
        sensitivityTable.add(sensitivitySlider).width(200);
        sensitivityTable.add(sensitivityValue).width(60).padLeft(10);
        table.add(sensitivityTable).padBottom(30).row();

        table.add(fullscreenCheckbox).padBottom(40).row();
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
                int volume = (int) volumeSlider.getValue();
                volumeValue.setText(volume + "%");
                // TODO: Implement volume change
                System.out.println("Volume: " + volume + "%");
            }
        });

        sensitivitySlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float sensitivity = sensitivitySlider.getValue();
                sensitivityValue.setText(String.format("%.1f", sensitivity));
                // TODO: Implement sensitivity change
                System.out.println("Sensitivity: " + sensitivity);
            }
        });

        fullscreenCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean fullscreen = fullscreenCheckbox.isChecked();
                // TODO: Implement fullscreen toggle
                System.out.println("Fullscreen: " + fullscreen);
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

    @Override
    public void render(float delta) {
        clearScreen();
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
