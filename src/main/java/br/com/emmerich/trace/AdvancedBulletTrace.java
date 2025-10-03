package br.com.emmerich.trace;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class AdvancedBulletTrace {
    private final Array<BulletTraceEffect> effects;
    private final ModelBuilder modelBuilder;

    public AdvancedBulletTrace() {
        effects = new Array<>();
        modelBuilder = new ModelBuilder();
    }

    public void createBulletTrace(Vector3 start, Vector3 end, boolean hitTarget) {
        effects.add(new BulletTraceEffect(start, end, hitTarget));
    }

    public void createBulletTrace(Vector3 start, Vector3 direction, float maxDistance, boolean hitTarget) {
        Vector3 end = new Vector3(start).add(direction.scl(maxDistance));
        createBulletTrace(start, end, hitTarget);
    }

    public void update(float deltaTime) {
        for (int i = effects.size - 1; i >= 0; i--) {
            BulletTraceEffect effect = effects.get(i);
            effect.update(deltaTime);

            if (effect.isExpired()) {
                effects.removeIndex(i);
                effect.dispose();
            }
        }
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if (effects.size == 0) return;

        com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
        com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (BulletTraceEffect effect : effects) {
            effect.render(modelBatch, environment);
        }

        com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        for (BulletTraceEffect effect : effects) {
            effect.dispose();
        }
        effects.clear();
    }

    private class BulletTraceEffect {
        private Vector3 start;
        private Vector3 end;
        private boolean hitTarget;
        private float lifetime;
        private float maxLifetime = 0.4f;
        private ModelInstance lineModel;
        private ModelInstance hitEffectModel;

        public BulletTraceEffect(Vector3 start, Vector3 end, boolean hitTarget) {
            this.start = new Vector3(start);
            this.end = new Vector3(end);
            this.hitTarget = hitTarget;
            this.lifetime = maxLifetime;

            createTraceLine();
            if (hitTarget) {
                createHitEffect();
            }
        }

        private void createTraceLine() {
            Vector3 direction = new Vector3(end).sub(start);
            float length = direction.len();
            direction.nor();

            float thickness = 0.015f;

            com.badlogic.gdx.graphics.g3d.Material material = new com.badlogic.gdx.graphics.g3d.Material();
            Color lineColor = hitTarget ? new Color(1f, 0.3f, 0.1f, 0.9f) : // Red-orange for hits
                    new Color(1f, 0.8f, 0.2f, 0.7f);  // Yellow for misses
            material.set(ColorAttribute.createDiffuse(lineColor));
            material.set(ColorAttribute.createSpecular(lineColor));

            com.badlogic.gdx.graphics.g3d.Model lineModelData = modelBuilder.createCylinder(
                    thickness, length, thickness, 6, material,
                    com.badlogic.gdx.graphics.VertexAttributes.Usage.Position |
                            com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal
            );

            lineModel = new ModelInstance(lineModelData);

            Vector3 center = new Vector3(start).add(end).scl(0.5f);
            lineModel.transform.setToTranslation(center);

            Vector3 up = new Vector3(0, 1, 0);
            Vector3 axis = new Vector3(up).crs(direction);
            float angle = (float) Math.acos(up.dot(direction));
            lineModel.transform.rotate(axis, angle * com.badlogic.gdx.math.MathUtils.radiansToDegrees);
        }

        private void createHitEffect() {
            // Create a small spark/sphere at the hit point
            com.badlogic.gdx.graphics.g3d.Material material = new com.badlogic.gdx.graphics.g3d.Material();
            Color hitColor = new Color(1f, 0.6f, 0.1f, 1f); // Bright orange
            material.set(ColorAttribute.createDiffuse(hitColor));
            material.set(ColorAttribute.createSpecular(hitColor));

            com.badlogic.gdx.graphics.g3d.Model hitModel = modelBuilder.createSphere(
                    0.08f, 0.08f, 0.08f, 8, 8, material,
                    com.badlogic.gdx.graphics.VertexAttributes.Usage.Position |
                            com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal
            );

            hitEffectModel = new ModelInstance(hitModel, end);
        }

        public void update(float deltaTime) {
            lifetime -= deltaTime;
            float alpha = Math.max(0f, lifetime / maxLifetime);

            // Update line transparency
            if (lineModel != null) {
                Color currentColor = hitTarget ?
                        new Color(1f, 0.3f, 0.1f, alpha * 0.9f) :
                        new Color(1f, 0.8f, 0.2f, alpha * 0.7f);

                com.badlogic.gdx.graphics.g3d.Material material = lineModel.materials.get(0);
                material.set(ColorAttribute.createDiffuse(currentColor));
                material.set(ColorAttribute.createSpecular(currentColor));
            }

            // Update hit effect (grows then shrinks)
            if (hitEffectModel != null) {
                float scale = 1.0f + (1.0f - alpha) * 2.0f; // Grow effect
                hitEffectModel.transform.setToTranslation(end);
                hitEffectModel.transform.scale(scale, scale, scale);

                Color hitColor = new Color(1f, 0.6f, 0.1f, alpha);
                com.badlogic.gdx.graphics.g3d.Material material = hitEffectModel.materials.get(0);
                material.set(ColorAttribute.createDiffuse(hitColor));
                material.set(ColorAttribute.createSpecular(hitColor));
            }
        }

        public void render(ModelBatch modelBatch, Environment environment) {
            if (lineModel != null && lifetime > 0) {
                modelBatch.render(lineModel, environment);
            }
            if (hitEffectModel != null && lifetime > 0) {
                modelBatch.render(hitEffectModel, environment);
            }
        }

        public boolean isExpired() {
            return lifetime <= 0;
        }

        public void dispose() {
            if (lineModel != null) {
                lineModel.model.dispose();
            }
            if (hitEffectModel != null) {
                hitEffectModel.model.dispose();
            }
        }
    }
}
