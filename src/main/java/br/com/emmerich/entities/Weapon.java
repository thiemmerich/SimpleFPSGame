package br.com.emmerich.entities;

public class Weapon {

    private String name;
    private int maxAmmo;
    private int currentAmmo;
    private int damage;
    private float fireRate;
    private float reloadTime;

    private float timeSinceLastShot = 0f;
    private boolean isReloading = false;
    private float reloadTimer = 0f;

    public Weapon(String name, int maxAmmo, int damage, float fireRate, float reloadTime) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.damage = damage;
        this.fireRate = fireRate;
        this.reloadTime = reloadTime;
    }

    public boolean canShoot(float deltaTime) {
        timeSinceLastShot += deltaTime;

        if (isReloading) {
            reloadTimer += deltaTime;
            if (reloadTimer >= reloadTime) {
                isReloading = false;
                reloadTimer = 0f;
                currentAmmo = maxAmmo;
            }
            return false;
        }

        return currentAmmo > 0 && timeSinceLastShot >= 1f / fireRate;
    }

    public void shoot() {
        if (canShoot(0)) { // We check time separately
            currentAmmo--;
            timeSinceLastShot = 0f;
            System.out.println(name + " fired! Ammo: " + currentAmmo + "/" + maxAmmo);
        }
    }

    public void reload() {
        if (!isReloading && currentAmmo < maxAmmo) {
            isReloading = true;
            System.out.println("Reloading " + name + "...");
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public float getReloadProgress() {
        return reloadTimer / reloadTime;
    }
}
