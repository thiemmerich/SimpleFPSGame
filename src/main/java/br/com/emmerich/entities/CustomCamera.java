package br.com.emmerich.entities;

import br.com.emmerich.utils.CollisionDetector;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class CustomCamera {

    private final PerspectiveCamera camera;
    private final CollisionDetector collisionDetector;
    private final Vector3 tempVector = new Vector3();
    private final Vector3 newPosition = new Vector3();

    // Movement
    private boolean grounded = true;
    private boolean isRunning = false;
    private final Vector3 velocity = new Vector3();
    private float moveSpeed = 5f;

    public CustomCamera(CollisionDetector collisionDetector, int fieldOfView, int width, int height) {
        this.collisionDetector = collisionDetector;
        camera = new PerspectiveCamera(fieldOfView, width, height);
        camera.position.set(0f, 2f, 0f); // Player height ~2 units
        camera.direction.set(0f, 0f, -1f); // Look forward
        camera.near = 0.1f;
        camera.far = 300f;
        camera.update();
    }

    public void handleMouseLook() {
        if (!Gdx.input.isCursorCatched()) return;

        // Input
        float mouseSensitivity = 0.2f;
        float deltaX = -Gdx.input.getDeltaX() * mouseSensitivity;
        float deltaY = -Gdx.input.getDeltaY() * mouseSensitivity;

        // Rotate camera horizontally (Y-axis)
        camera.direction.rotate(camera.up, deltaX);

        // Rotate camera vertically (X-axis) with limits
        tempVector.set(camera.direction).crs(camera.up).nor();

        // Simple pitch limiting based on current Y direction
        float newY = camera.direction.y + (deltaY * 0.01f); // Adjust sensitivity

        // Limit looking up/down (keep between -0.9 and 0.9 to prevent flipping)
        if (newY > -0.9f && newY < 0.9f) {
            camera.direction.rotate(tempVector, deltaY);
        }

        // Escape key to show cursor
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
        }
    }

    public void handleMovement(float deltaTime) {
        Vector3 moveDirection = new Vector3();
        float jumpForce = 8f;

        // Forward/Backward
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            moveDirection.add(getForwardVector());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            moveDirection.sub(getForwardVector());
        }

        // Left/Right
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            moveDirection.add(getRightVector());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            moveDirection.sub(getRightVector());
        }

        // Apply movement with collision detection
        if (moveDirection.len2() > 0) {
            moveDirection.nor().scl(moveSpeed * deltaTime);
            newPosition.set(camera.position).add(moveDirection);

            // Check collision and resolve
            Vector3 resolvedPosition = collisionDetector.resolveCollision(
                    camera.position, newPosition
            );

            camera.position.set(resolvedPosition);
        }

        // Jumping with collision detection
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && grounded) {
            velocity.y = jumpForce;
            grounded = false;
        }

        // Apply gravity with collision detection
        if (!grounded) {
            velocity.y -= 15f * deltaTime;
            newPosition.set(camera.position.x, camera.position.y + velocity.y * deltaTime, camera.position.z);

            // Check ground collision
            if (!collisionDetector.checkCollision(new Vector3(newPosition.x, newPosition.y - 1f, newPosition.z))) {
                camera.position.y = newPosition.y;
            } else {
                // Hit ground
                camera.position.y = Math.round(camera.position.y); // Snap to ground
                velocity.y = 0f;
                grounded = true;
            }

            // Additional check to prevent falling through floor
            if (camera.position.y < 1f) {
                camera.position.y = 1f;
                velocity.y = 0f;
                grounded = true;
            }
        } else {
            // Check if we're still grounded
            Vector3 groundCheck = new Vector3(camera.position.x, camera.position.y - 1.1f, camera.position.z);
            grounded = collisionDetector.checkCollision(groundCheck);
        }
    }

    public void increaseSpeedAtRunning() {
        if (!isRunning) {
            moveSpeed = 10f;
            isRunning = true;
        } else {
            moveSpeed = 5f;
            isRunning = false;
        }
    }

    private Vector3 getForwardVector() {
        Vector3 forward = new Vector3(camera.direction.x, 0, camera.direction.z);
        return forward.nor();
    }

    private Vector3 getRightVector() {
        Vector3 right = new Vector3(camera.direction.z, 0, -camera.direction.x);
        return right.nor();
    }

    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    public void update() {
        camera.update();
    }

    public PerspectiveCamera getPerspectiveCamera() {
        return camera;
    }

    public Vector3 getPosition() {
        return camera.position;
    }
}
