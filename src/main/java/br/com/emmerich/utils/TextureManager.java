package br.com.emmerich.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private Map<String, Texture> textures;
    private Map<String, Material> materials;

    public TextureManager() {
        textures = new HashMap<>();
        materials = new HashMap<>();
        loadTextures();
    }

    private void loadTextures() {
        try {
            // Load texture files - they should be in core/assets/textures/
            loadTexture("grass", "assets/textures/grass04.png");
            loadTexture("brick", "assets/textures/darkbrown.png");
            loadTexture("wood", "assets/textures/woodTexture.png");
//            loadTexture("concrete", "assets/concrete.png");

            // Create materials from textures
            createMaterials();

        } catch (Exception e) {
            System.err.println("Error loading textures: " + e.getMessage());
            createFallbackMaterials();
        }
    }

    private void loadTexture(String name, String path) {
        try {
            System.out.println("Loading texture: " + path);
            Texture texture = new Texture(Gdx.files.internal(path));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            textures.put(name, texture);
            System.out.println("Successfully loaded texture: " + name);

        } catch (Exception e) {
            System.err.println("Failed to load texture: " + path + " - " + e.getMessage());
        }
    }

    private void createMaterials() {
        for (Map.Entry<String, Texture> entry : textures.entrySet()) {
            entry.getValue().getTextureData().prepare();
            Pixmap pixmap = entry.getValue().getTextureData().consumePixmap();
            String format = pixmap.getFormat().toString();
            System.out.println("Pixmap "+ entry.getKey()+ " format: " + format);

            Material material = new Material();
            material.set(TextureAttribute.createDiffuse(entry.getValue()));
            materials.put(entry.getKey(), material);
        }
    }

    private void createFallbackMaterials() {
        // Fallback colored materials if textures fail to load
        System.out.println("Using fallback colored materials");

        materials.put("grass", new Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(0.3f, 0.5f, 0.2f, 1f)));
        materials.put("brick", new Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(0.7f, 0.3f, 0.2f, 1f)));
        materials.put("wood", new Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(0.8f, 0.6f, 0.4f, 1f)));
        materials.put("concrete", new Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(0.6f, 0.6f, 0.6f, 1f)));
    }

    public Material getMaterial(String name) {
        Material material = materials.get(name);
        if (material == null) {
            System.err.println("Material not found: " + name + ", using fallback");
            return new Material(com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute.createDiffuse(1f, 0f, 1f, 1f)); // Magenta fallback
        }
        return material;
    }

    public Texture getTexture(String name) {
        return textures.get(name);
    }

    public void setTextureRepeat(String materialName, float repeatU, float repeatV) {
        Material material = materials.get(materialName);
        if (material != null && material.has(TextureAttribute.Diffuse)) {
            TextureAttribute texAttr = (TextureAttribute) material.get(TextureAttribute.Diffuse);
            texAttr.scaleU = repeatU;
            texAttr.scaleV = repeatV;
        }
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
        materials.clear();
    }
}
