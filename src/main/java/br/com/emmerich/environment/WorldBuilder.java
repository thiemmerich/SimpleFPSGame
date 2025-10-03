package br.com.emmerich.environment;

import br.com.emmerich.utils.CollisionDetector;
import br.com.emmerich.utils.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class WorldBuilder {

    private final Array<ModelInstance> instances;
    private final CollisionDetector collisionDetector;
    private final ModelBuilder modelBuilder;
    private final WallBuilder wallBuilder;
    private final CrateBuilder crateBuilder;
    private final TextureManager textureManager;

    public WorldBuilder(CollisionDetector collisionDetector, TextureManager textureManager) {
        this.collisionDetector = collisionDetector;
        this.instances = new Array<>();
        this.modelBuilder = new ModelBuilder();
        this.wallBuilder = new WallBuilder(modelBuilder, collisionDetector, textureManager);
        this.crateBuilder = new CrateBuilder(modelBuilder, collisionDetector, textureManager);
        this.textureManager = textureManager;
    }

    public Array<ModelInstance> getInstances() {
        return instances;
    }

    public void setupWorld() {
        this.createTexturizedFloor();

        // Add floor collision
        collisionDetector.addCollisionBox(
                new BoundingBox(new Vector3(-25f, -1f, -25f), new Vector3(25f, 0f, 25f)),
                new Vector3(0, 0, 0)
        );

        // Create walls with brick texture
        instances.add(wallBuilder.create(8f, 1f, 0f, 5f, 4f, 1f, "brick"));
        instances.add(wallBuilder.create(-8f, 1f, 0f, 5f, 4f, 1f, "brick"));
        instances.add(wallBuilder.create(0f, 1f, 8f, 1f, 4f, 5f, "brick"));
        instances.add(wallBuilder.create(0f, 1f, -8f, 1f, 4f, 5f, "brick"));

        // Create crates with wood texture
        instances.add(crateBuilder.create(4f, 0f, 4f, "wood"));
        instances.add(crateBuilder.create(-4f, 0f, 6f, "wood"));
        instances.add(crateBuilder.create(6f, 0f, -4f, "wood"));
        instances.add(crateBuilder.create(-5f, 0f, -5f, "wood"));
    }

    private void createTexturizedFloor() {
        try {
            // Load texture directly
            Texture grassTexture = textureManager.getTexture("grass");

            Material groundMaterial = new Material();
            groundMaterial.set(TextureAttribute.createDiffuse(grassTexture));

            // Create a thin box (0.1f height) for the ground
            Model groundModel = modelBuilder.createBox(50f, 0.1f, 50f, groundMaterial,
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

            ModelInstance groundInstance = new ModelInstance(groundModel, 0f, -1f, 0f);
            instances.add(groundInstance);

            System.out.println("Simple textured box floor created");

        } catch (Exception e) {
            System.err.println("Direct texture loading failed: " + e.getMessage());
            // Create a bright yellow floor as fallback
            Material yellowMaterial = new Material(ColorAttribute.createDiffuse(Color.YELLOW));
            Model yellowModel = modelBuilder.createBox(50f, 1f, 50f, yellowMaterial,
                    VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
            instances.add(new ModelInstance(yellowModel, 0f, -1f, 0f));
            System.out.println("Added BRIGHT YELLOW fallback floor");
        }
    }

    public void dispose() {
        for (ModelInstance instance : instances) {
            instance.model.dispose();
        }
    }
}
