package br.com.emmerich.hud;

import br.com.emmerich.entities.Weapon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HUDManager {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    // HUD elements state
    private int health = 100;
    private int maxHealth = 100;
    private int ammo = 30;
    private int maxAmmo = 30;
    private int totalAmmo = 90;
    private String weaponName = "NONE";
    private boolean isReloading = false;
    private float reloadProgress = 0f;
    private String message = "";
    private float messageTimer = 0f;

    // Crosshair
    private final Crosshair crosshair;

    public HUDManager() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
        crosshair = new Crosshair();


        // Set initial HUD state
        this.setWeapon("NONE", 0);
        this.setAmmo(0, 0);
        this.setHealth(100);

        // Set font scale for better readability
        font.getData().setScale(1.2f);
    }

    public void render() {
        // Enable blending for transparency
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Render crosshair first (it uses its own ShapeRenderer)
        crosshair.render();

        // Render all ShapeRenderer elements first
        renderShapeElements();

        // Then render all text elements in a single batch
        batch.begin();
        renderTextElements();
        batch.end();

        // Disable blending
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderShapeElements() {
        // Health bar
        renderHealthBar();

        // Ammo indicator
        renderAmmoIndicator();

        // Reload indicator
        if (isReloading) {
            renderReloadIndicator();
        }

        // Mini-map
        //renderMiniMap();
    }

    private void renderTextElements() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Health text
        font.setColor(Color.WHITE);
        font.draw(batch, "HEALTH: " + health + "/" + maxHealth, 25, screenHeight - 30);

        // Ammo counter
        String ammoText = ammo + " / " + totalAmmo;
        glyphLayout.setText(font, ammoText);
        font.draw(batch, ammoText, screenWidth - glyphLayout.width - 20, 40);

        // Weapon name
        font.setColor(Color.YELLOW);
        glyphLayout.setText(font, weaponName);
        font.draw(batch, weaponName, screenWidth - glyphLayout.width - 20, 80);

        // Reload text
        if (isReloading) {
            font.setColor(Color.WHITE);
            String reloadText = "RELOADING " + (int)(reloadProgress * 100) + "%";
            glyphLayout.setText(font, reloadText);
            float textWidth = glyphLayout.width;
            font.draw(batch, reloadText, screenWidth / 2 - textWidth / 2, screenHeight / 2 + 65);
        }

        // Message
        if (messageTimer > 0) {
            font.setColor(1, 1, 1, Math.min(1.0f, messageTimer));
            glyphLayout.setText(font, message);
            float textWidth = glyphLayout.width;
            font.draw(batch, message, screenWidth / 2 - textWidth / 2, screenHeight - 100);
        }

        // Mini-map label
//        font.setColor(Color.WHITE);
//        font.getData().setScale(0.8f);
//        font.draw(batch, "MAP", 25, 185);
//        font.getData().setScale(1.2f); // Reset scale
    }

    private void renderHealthBar() {
//        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Health bar background
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        shapeRenderer.rect(20, screenHeight - 50, 200, 25);

        // Health bar fill
        float healthPercent = (float) health / maxHealth;
        if (healthPercent > 0.6f) {
            shapeRenderer.setColor(0.2f, 0.8f, 0.2f, 0.9f); // Green
        } else if (healthPercent > 0.3f) {
            shapeRenderer.setColor(0.8f, 0.8f, 0.2f, 0.9f); // Yellow
        } else {
            shapeRenderer.setColor(0.8f, 0.2f, 0.2f, 0.9f); // Red
        }
        shapeRenderer.rect(20, screenHeight - 50, 200 * healthPercent, 25);

        shapeRenderer.end();

        // Health bar border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 0.5f);
        shapeRenderer.rect(20, screenHeight - 50, 200, 25);
        shapeRenderer.end();
    }

    private void renderAmmoIndicator() {
        int screenWidth = Gdx.graphics.getWidth();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        shapeRenderer.rect(screenWidth - 120, 50, 100, 15);

        // Ammo fill
        float ammoPercent = (float) ammo / maxAmmo;
        if (ammoPercent > 0.3f) {
            shapeRenderer.setColor(0.9f, 0.9f, 0.3f, 0.9f); // Yellow
        } else if (ammoPercent > 0) {
            shapeRenderer.setColor(0.9f, 0.3f, 0.3f, 0.9f); // Red
        } else {
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.9f); // Gray (empty)
        }
        shapeRenderer.rect(screenWidth - 120, 50, 100 * ammoPercent, 15);

        shapeRenderer.end();

        // Border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 0.5f);
        shapeRenderer.rect(screenWidth - 120, 50, 100, 15);
        shapeRenderer.end();
    }

    private void renderReloadIndicator() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Reload background
        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.9f);
        shapeRenderer.rect(screenWidth / 2 - 100, screenHeight / 2 + 50, 200, 20);

        // Reload progress
        shapeRenderer.setColor(0.9f, 0.6f, 0.1f, 0.9f);
        shapeRenderer.rect(screenWidth / 2 - 100, screenHeight / 2 + 50, 200 * reloadProgress, 20);

        shapeRenderer.end();

        // Border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 0.8f);
        shapeRenderer.rect(screenWidth / 2 - 100, screenHeight / 2 + 50, 200, 20);
        shapeRenderer.end();
    }

    private void renderMiniMap() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Mini-map background
        shapeRenderer.setColor(0.1f, 0.1f, 0.2f, 0.7f);
        shapeRenderer.rect(20, 20, 150, 150);

        // Player position (center of mini-map)
        shapeRenderer.setColor(0, 1, 0, 1); // Green dot for player
        shapeRenderer.circle(20 + 75, 20 + 75, 3);

        shapeRenderer.end();

        // Mini-map border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.3f, 0.3f, 0.8f, 0.9f);
        shapeRenderer.rect(20, 20, 150, 150);
        shapeRenderer.end();
    }

    // Public methods to update HUD state
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void setAmmo(int ammo, int totalAmmo) {
        this.ammo = ammo;
        this.totalAmmo = totalAmmo;
    }

    public void setWeapon(Weapon weapon) {
        this.ammo = weapon.getCurrentAmmo();
        this.totalAmmo = weapon.getTotalAmmo();
        this.weaponName = weapon.getName();
        this.maxAmmo = weapon.getMaxAmmo();
    }

    public void setWeapon(String name, int maxAmmo) {
        this.weaponName = name;
        this.maxAmmo = maxAmmo;
    }

    public void setReloading(boolean reloading, float progress) {
        this.isReloading = reloading;
        this.reloadProgress = progress;
    }

    public void showMessage(String message, float duration) {
        this.message = message;
        this.messageTimer = duration;
    }

    public void triggerHit() {
        crosshair.triggerHit();
    }

    public void update(float delta) {
        crosshair.update(delta);

        // Update message timer
        if (messageTimer > 0) {
            messageTimer -= delta;
        }
    }

    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        crosshair.resize(width, height);
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        crosshair.dispose();
    }
}