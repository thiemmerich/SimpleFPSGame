package br.com.emmerich.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class ActionsHandler {

    private CustomCamera camera;

    public ActionsHandler(CustomCamera camera) {
        this.camera = camera;
    }

    public void handleActions() {
        // Shooting
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            shoot();
        }

        // Reloading
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            reload();
        }
    }

    private void shoot() {
        System.out.println("Bang! Shooting from: " + camera.getPosition());

        if (!Gdx.input.isCursorCatched()) {
            System.out.println("Mouse desativado, ativando...");
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
        }

        // TODO: Implement raycasting for hit detection
        // TODO: Add muzzle flash, sound effects, etc.
    }

    private void reload() {
        System.out.println("Reloading...");
        // TODO: Implement reloading logic
    }
}
