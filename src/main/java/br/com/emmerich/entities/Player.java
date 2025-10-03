package br.com.emmerich.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public class Player {

    private Camera camera;
    private Vector3 velocity;
    private boolean grounded;

    private float moveSpeed = 5f;
    private float jumpForce = 8f;
    private float gravity = 15f;

    public Player(Camera camera) {
        this.camera = camera;
        this.velocity = new Vector3();
        this.grounded = true;
    }

    public void update(float deltaTime) {
        // Apply gravity
        if (!grounded) {
            velocity.y -= gravity * deltaTime;
            camera.position.y += velocity.y * deltaTime;

            // Simple ground collision
            if (camera.position.y <= 2f) {
                camera.position.y = 2f;
                velocity.y = 0f;
                grounded = true;
            }
        }
    }

    public void move(Vector3 direction, float deltaTime) {
        if (direction.len2() > 0) {
            direction.nor().scl(moveSpeed * deltaTime);
            camera.position.add(direction);
        }
    }

    public void jump() {
        if (grounded) {
            velocity.y = jumpForce;
            grounded = false;
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public Vector3 getPosition() {
        return camera.position;
    }
}
