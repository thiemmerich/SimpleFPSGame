package br.com.emmerich.entities;

public class Weapon {
    private String name;
    private int currentAmmo;
    private int totalAmmo;
    private int maxAmmo;
    private float reloadTime;
    private float reloadTimer;
    private boolean reloading;

    public Weapon(String name, int maxAmmo, int totalAmmo, float reloadTime) {
        this.name = name;
        this.maxAmmo = maxAmmo;
        this.currentAmmo = maxAmmo;
        this.totalAmmo = totalAmmo;
        this.reloadTime = reloadTime;
    }

    public boolean canShoot() {
        return !reloading && currentAmmo > 0;
    }

    public void shoot() {
        if (canShoot()) {
            currentAmmo--;
        }
    }

    public boolean canReload() {
        return !reloading && currentAmmo < maxAmmo && totalAmmo > 0;
    }

    public void reload() {
        if (canReload()) {
            reloading = true;
            reloadTimer = 0f;
        }
    }

    public void update(float delta) {
        if (reloading) {
            reloadTimer += delta;
            if (reloadTimer >= reloadTime) {
                int ammoNeeded = maxAmmo - currentAmmo;
                int ammoToReload = Math.min(ammoNeeded, totalAmmo);

                currentAmmo += ammoToReload;
                totalAmmo -= ammoToReload;
                reloading = false;
            }
        }
    }

    // Getters
    public boolean isReloading() {
        return reloading;
    }

    public float getReloadProgress() {
        return reloadTimer / reloadTime;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getTotalAmmo() {
        return totalAmmo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public String getName() {
        return this.name;
    }
}
