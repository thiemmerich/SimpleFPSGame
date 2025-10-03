package br.com.emmerich.trace;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BulletTrace {
    private final Array<BulletTraceLine> traces;
    private final ModelBuilder modelBuilder;

    public BulletTrace() {
        traces = new Array<>();
        modelBuilder = new ModelBuilder();
    }

    public void createTrace(Vector3 start, Vector3 end) {
        // How long traces stay visible
        float traceDuration = 0.3f;
        traces.add(new BulletTraceLine(start, end, traceDuration, modelBuilder));
    }

    public void createTrace(Vector3 start, Vector3 direction, float maxDistance) {
        Vector3 end = new Vector3(start).add(direction.scl(maxDistance));
        createTrace(start, end);
    }

    public void update(float deltaTime) {
        // Update all traces and remove expired ones
        for (int i = traces.size - 1; i >= 0; i--) {
            BulletTraceLine trace = traces.get(i);
            trace.update(deltaTime);

            if (trace.isExpired()) {
                traces.removeIndex(i);
                trace.dispose();
            }
        }
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        if (traces.size == 0) return;

        // Enable blending for transparency
        com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
        com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (BulletTraceLine trace : traces) {
            trace.render(modelBatch, environment);
        }

        com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        for (BulletTraceLine trace : traces) {
            trace.dispose();
        }
        traces.clear();
    }
}
