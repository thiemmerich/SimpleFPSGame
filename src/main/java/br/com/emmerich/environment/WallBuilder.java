package br.com.emmerich.environment;

import br.com.emmerich.utils.CollisionDetector;
import br.com.emmerich.utils.TextureManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class WallBuilder {
    private final CollisionDetector collisionDetector;
    private final ModelBuilder modelBuilder;
    private final TextureManager textureManager;

    public WallBuilder(ModelBuilder modelBuilder, CollisionDetector collisionDetector, TextureManager textureManager) {
        this.modelBuilder = modelBuilder;
        this.collisionDetector = collisionDetector;
        this.textureManager = textureManager;
    }

    public ModelInstance create(float x, float y, float z, float width, float height, float depth, String textureName) {
        Texture texture = textureManager.getTexture(textureName);

        Material wallMaterial = new Material();
        wallMaterial.set(TextureAttribute.createDiffuse(texture));

        Model wallModel = modelBuilder.createBox(width, height, depth, wallMaterial,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        // Add collision for wall
        collisionDetector.addCollisionBox(
                new BoundingBox(
                        new Vector3(-width / 2, -height / 2, -depth / 2),
                        new Vector3(width / 2, height / 2, depth / 2)
                ),
                new Vector3(x, y, z)
        );
        return new ModelInstance(wallModel, x, y, z);
    }

    // Overload for backward compatibility
    public ModelInstance create(float x, float y, float z, float width, float height, float depth, Color color) {
        Material wallMaterial = new Material(ColorAttribute.createDiffuse(color));

        Model wallModel = modelBuilder.createBox(width, height, depth, wallMaterial,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        collisionDetector.addCollisionBox(
                new BoundingBox(
                        new Vector3(-width / 2, -height / 2, -depth / 2),
                        new Vector3(width / 2, height / 2, depth / 2)
                ),
                new Vector3(x, y, z)
        );
        return new ModelInstance(wallModel, x, y, z);
    }
}
