package br.com.emmerich.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Crosshair {

    private final ShapeRenderer shapeRenderer;

    private float pulseTimer = 0f;
    private boolean isHit = false;
    private float hitTimer = 0f;
    private int screenWidth, screenHeight;

    public Crosshair() {
        shapeRenderer = new ShapeRenderer();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
    }

    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Choose color based on state
        Color crosshairColor;
        if (isHit && hitTimer > 0) {
            crosshairColor = new Color(1, 0, 0, 0.8f); // Red when hit
        } else {
            // Pulsing effect
            float pulse = (MathUtils.sin(pulseTimer * 8f) + 1f) / 2f * 0.3f + 0.7f;
            crosshairColor = new Color(pulse, pulse, pulse, 0.9f);
        }

        shapeRenderer.setColor(crosshairColor);

        // Draw crosshair (simple + shape)
        int size = 8;
        int thickness = 2;
        int gap = 4;

        // Horizontal line
        shapeRenderer.rect(centerX - size, centerY - thickness / 2, size - gap, thickness);
        shapeRenderer.rect(centerX + gap, centerY - thickness / 2, size - gap, thickness);

        // Vertical line
        shapeRenderer.rect(centerX - thickness / 2, centerY - size, thickness, size - gap);
        shapeRenderer.rect(centerX - thickness / 2, centerY + gap, thickness, size - gap);

        // Center dot
        shapeRenderer.circle(centerX, centerY, 1);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void update(float delta) {
        pulseTimer += delta;

        if (isHit && hitTimer > 0) {
            hitTimer -= delta;
            if (hitTimer <= 0) {
                isHit = false;
            }
        }
    }

    public void triggerHit() {
        isHit = true;
        hitTimer = 0.3f; // Show hit effect for 0.3 seconds
    }

    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
