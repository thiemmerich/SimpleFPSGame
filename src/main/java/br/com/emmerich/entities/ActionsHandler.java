package br.com.emmerich.entities;

import br.com.emmerich.hud.HUDManager;
import br.com.emmerich.trace.BulletTrace;
import br.com.emmerich.utils.CollisionDetector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public class ActionsHandler {

    private final CustomCamera camera;
    private final HUDManager hudManager;
    private final BulletTrace bulletTrace;
    private final CollisionDetector collisionDetector;
    private final Player player;

    public ActionsHandler(CustomCamera camera, HUDManager hudManager, CollisionDetector collisionDetector, Player player) {
        this.camera = camera;
        this.hudManager = hudManager;
        this.collisionDetector = collisionDetector;
        this.player = player;
        this.bulletTrace = new BulletTrace(); // Initialize bullet trace
    }

    public void handleActions() {
        Weapon currentWeapon = player.getCurrentWeapon();
        // Shooting
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            shoot(currentWeapon);
        }

        // Reloading
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            reload(currentWeapon);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
            camera.increaseSpeedAtRunning();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            player.changeCurrentWeapon(1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            player.changeCurrentWeapon(2);
        }

        // Update reload progress if reloading
        if (currentWeapon.isReloading()) {
            currentWeapon.update(Gdx.graphics.getDeltaTime());
            hudManager.setReloading(true, currentWeapon.getReloadProgress());

            if (!currentWeapon.isReloading()) {
                // Reload finished
                hudManager.setAmmo(currentWeapon.getCurrentAmmo(), currentWeapon.getTotalAmmo());
                hudManager.setReloading(false, 0f);
                hudManager.showMessage("RELOADED", 2f);
            }
        }

        // Update bullet traces
        bulletTrace.update(Gdx.graphics.getDeltaTime());
    }

    private void shoot(Weapon currentWeapon) {
        if (currentWeapon.canShoot()) {
            currentWeapon.shoot();
            hudManager.setAmmo(currentWeapon.getCurrentAmmo(), currentWeapon.getTotalAmmo());
            hudManager.triggerHit();

            // Create bullet trace
            createBulletTrace();

            if (currentWeapon.getCurrentAmmo() == 0) {
                hudManager.showMessage("OUT OF AMMO", 2f);
            }
        } else if (currentWeapon.getCurrentAmmo() == 0) {
            hudManager.showMessage("PRESS R TO RELOAD", 2f);
        }
    }

    private void createBulletTrace() {
        Vector3 start = camera.getPosition();
        Vector3 direction = camera.getPerspectiveCamera().direction.cpy().nor();

        // Raycast to find where the bullet hits
        float maxDistance = 300f; // Maximum bullet range
        Vector3 end = new Vector3(start).add(direction.scl(maxDistance));

        // Simple collision detection - check if we hit anything
        boolean hitTarget = collisionDetector.checkCollision(end);

        // If we hit something, adjust the end point
        if (hitTarget) {
            // For now, we'll use a simple approach - you can enhance this with proper raycasting
            end.set(start).add(direction.scl(maxDistance * 0.7f)); // Shorten the trace if hit
        }

        // Create the bullet trace
        bulletTrace.createTrace(start, end);

        System.out.println("Bullet trace from " + start + " to " + end + (hitTarget ? " [HIT]" : " [MISS]"));
    }

    // Add this method to render bullet traces
    public void renderBulletTraces(ModelBatch modelBatch, Environment environment) {
        bulletTrace.render(modelBatch, environment);
    }

    private void reload(Weapon currentWeapon) {
        if (currentWeapon.canReload()) {
            currentWeapon.reload();
            hudManager.showMessage("RELOADING...", 1f);
        }
    }
}
