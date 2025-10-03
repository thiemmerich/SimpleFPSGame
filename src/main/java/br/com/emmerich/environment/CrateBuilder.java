package br.com.emmerich.environment;

import br.com.emmerich.utils.CollisionDetector;
import br.com.emmerich.utils.TextureManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class CrateBuilder {
    private final CollisionDetector collisionDetector;
    private final ModelBuilder modelBuilder;
    private final TextureManager textureManager;

    public CrateBuilder(ModelBuilder modelBuilder, CollisionDetector collisionDetector, TextureManager textureManager) {
        this.modelBuilder = modelBuilder;
        this.collisionDetector = collisionDetector;
        this.textureManager = textureManager;
    }

    public ModelInstance create(float x, float y, float z, String textureName) {
        float size = 2f;
        Model crateModel = modelBuilder.createBox(
                size, size, size,
                textureManager.getMaterial(textureName),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        // Add collision for crate
        collisionDetector.addCollisionBox(
                new BoundingBox(
                        new Vector3(-size / 2, -size / 2, -size / 2),
                        new Vector3(size / 2, size / 2, size / 2)
                ),
                new Vector3(x, y + size / 2, z)
        );

        return new ModelInstance(crateModel, x, y + size / 2, z);
    }

    // Overload for backward compatibility
    public ModelInstance create(float x, float y, float z, Color color) {
        float size = 2f;
        Color woodColor = colorVariation(color);
        Material crateMaterial = new Material(ColorAttribute.createDiffuse(woodColor));

        Model crateModel = modelBuilder.createBox(size, size, size, crateMaterial,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        collisionDetector.addCollisionBox(
                new BoundingBox(
                        new Vector3(-size / 2, -size / 2, -size / 2),
                        new Vector3(size / 2, size / 2, size / 2)
                ),
                new Vector3(x, y + size / 2, z)
        );

        return new ModelInstance(crateModel, x, y + size / 2, z);
    }

    private Color colorVariation(Color color) {
        return new Color(
                color.r * (0.8f + (float) Math.random() * 0.4f),
                color.g * (0.8f + (float) Math.random() * 0.4f),
                color.b * (0.8f + (float) Math.random() * 0.4f),
                1f
        );
    }
}
