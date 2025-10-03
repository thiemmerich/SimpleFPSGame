package br.com.emmerich.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private Weapon currentWeapon;
    private Map<Integer, Weapon> equipedWeapons;
    private CustomCamera camera;
    private Vector3 velocity;
    private boolean grounded;

    private float moveSpeed = 5f;
    private float jumpForce = 8f;
    private float gravity = 15f;

    public Player(CustomCamera camera) {
        this.camera = camera;
        this.velocity = new Vector3();
        this.grounded = true;
        equipWeapons();
    }

    private void equipWeapons() {
        equipedWeapons = new HashMap<>();
        equipedWeapons.put(1, new Weapon("ASSAULT RIFLE", 30, 90, 2.0f));
        equipedWeapons.put(2, new Weapon("PISTOL", 12, 90, 1.0f));
        currentWeapon = equipedWeapons.get(1);
    }

    public Weapon getCurrentWeapon() {
        return this.currentWeapon;
    }

    public void changeCurrentWeapon(Integer newWeapon) {
        System.out.println("Alterando arma equipada para " + newWeapon);
        currentWeapon = equipedWeapons.get(newWeapon);
    }

    public void update(float deltaTime) {
        // Apply gravity
//        if (!grounded) {
//            velocity.y -= gravity * deltaTime;
//            camera.position.y += velocity.y * deltaTime;
//
//            // Simple ground collision
//            if (camera.position.y <= 2f) {
//                camera.position.y = 2f;
//                velocity.y = 0f;
//                grounded = true;
//            }
//        }
    }

//    public void move(Vector3 direction, float deltaTime) {
//        if (direction.len2() > 0) {
//            direction.nor().scl(moveSpeed * deltaTime);
//            camera.position.add(direction);
//        }
//    }
//
//    public void jump() {
//        if (grounded) {
//            velocity.y = jumpForce;
//            grounded = false;
//        }
//    }
//
//    public Camera getCamera() {
//        return camera;
//    }
//
//    public boolean isGrounded() {
//        return grounded;
//    }
//
//    public Vector3 getPosition() {
//        return camera.position;
//    }
}
