package br.com.emmerich.utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

    public static class CollisionBox {
        public BoundingBox bounds;
        public Vector3 position;

        public CollisionBox(BoundingBox bounds, Vector3 position) {
            this.bounds = bounds;
            this.position = position;
        }
    }

    private List<CollisionBox> collisionObjects;

    public CollisionDetector() {
        collisionObjects = new ArrayList<>();
    }

    public void addCollisionBox(BoundingBox bounds, Vector3 position) {
        collisionObjects.add(new CollisionBox(bounds, position));
    }

    public boolean checkCollision(Vector3 point) {
        for (CollisionBox obj : collisionObjects) {
            BoundingBox worldBounds = new BoundingBox(
                    obj.bounds.min.cpy().add(obj.position),
                    obj.bounds.max.cpy().add(obj.position)
            );
            if (worldBounds.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollision(BoundingBox bounds, Vector3 position) {
        BoundingBox worldBounds = new BoundingBox(
                bounds.min.cpy().add(position),
                bounds.max.cpy().add(position)
        );

        for (CollisionBox obj : collisionObjects) {
            BoundingBox objWorldBounds = new BoundingBox(
                    obj.bounds.min.cpy().add(obj.position),
                    obj.bounds.max.cpy().add(obj.position)
            );

            if (worldBounds.intersects(objWorldBounds)) {
                return true;
            }
        }
        return false;
    }

    public Vector3 resolveCollision(Vector3 start, Vector3 end) {
        Vector3 result = new Vector3(end);

        // Simple sphere collision
        float radius = 0.3f; // Player radius
        for (CollisionBox obj : collisionObjects) {
            BoundingBox worldBounds = new BoundingBox(
                    obj.bounds.min.cpy().add(obj.position),
                    obj.bounds.max.cpy().add(obj.position)
            );

            // Expand bounds by player radius
            BoundingBox expandedBounds = new BoundingBox(
                    worldBounds.min.cpy().sub(radius, radius, radius),
                    worldBounds.max.cpy().add(radius, radius, radius)
            );

            if (expandedBounds.contains(result)) {
                // Push back to start position if collision detected
                result.set(start);
                break;
            }
        }

        return result;
    }

    public void clear() {
        collisionObjects.clear();
    }
}
