package br.com.emmerich.trace;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class BulletTraceLine {
    private Vector3 start;
    private Vector3 end;
    private float lifetime;
    private float maxLifetime;
    private ModelInstance lineModel;
    private final ModelBuilder modelBuilder;

    public BulletTraceLine(Vector3 start, Vector3 end, float duration, ModelBuilder modelBuilder) {
        this.start = new Vector3(start);
        this.end = new Vector3(end);
        this.maxLifetime = duration;
        this.lifetime = duration;
        this.modelBuilder = modelBuilder;

        createLineModel();
    }

    private void createLineModel() {
        // Calculate line direction and length
        Vector3 direction = new Vector3(end).sub(start);
        float length = direction.len();
        direction.nor();

        // Create a thin cylinder for the bullet trace
        float thickness = 0.01f; // Very thin line

        com.badlogic.gdx.graphics.g3d.Material material = new com.badlogic.gdx.graphics.g3d.Material();

        // Create a glowing effect with transparency
        Color traceColor = new Color(1f, 0.8f, 0.2f, 0.8f); // Orange-yellow glow
        material.set(ColorAttribute.createDiffuse(traceColor));
        material.set(ColorAttribute.createSpecular(traceColor));

        // Create cylinder model for the trace
        com.badlogic.gdx.graphics.g3d.Model lineModelData = modelBuilder.createCylinder(
                thickness, length, thickness, 8,
                material,
                com.badlogic.gdx.graphics.VertexAttributes.Usage.Position |
                        com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal
        );

        lineModel = new ModelInstance(lineModelData);

        // Position and rotate the cylinder to match the trace direction
        Vector3 center = new Vector3(start).add(end).scl(0.5f);
        lineModel.transform.setToTranslation(center);

        // Calculate rotation to align cylinder with direction
        Vector3 up = new Vector3(0, 1, 0);
        Vector3 axis = new Vector3(up).crs(direction);
        float angle = (float) Math.acos(up.dot(direction));
        lineModel.transform.rotate(axis, angle * com.badlogic.gdx.math.MathUtils.radiansToDegrees);
    }

    public void update(float deltaTime) {
        lifetime -= deltaTime;

        // Update transparency based on remaining lifetime
        if (lineModel != null) {
            float alpha = Math.max(0f, lifetime / maxLifetime);
            Color currentColor = new Color(1f, 0.8f, 0.2f, alpha * 0.8f);

            // Update material color
            com.badlogic.gdx.graphics.g3d.Material material = lineModel.materials.get(0);
            material.set(ColorAttribute.createDiffuse(currentColor));
            material.set(ColorAttribute.createSpecular(currentColor));
        }
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if (lineModel != null && lifetime > 0) {
            modelBatch.render(lineModel, environment);
        }
    }

    public boolean isExpired() {
        return lifetime <= 0;
    }

    public void dispose() {
        if (lineModel != null) {
            lineModel.model.dispose();
        }
    }
}
