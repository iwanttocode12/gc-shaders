package w4160.engine;

import org.joml.Vector3f;

import w4160.engine.graphics.Mesh;

/**
 * GameItem represents each item rendered in the scene.
 * It contains the {@link Mesh} object to specify the shape, as well as several parameters used when
 * rendering the object on the screen, such as rotation, scale, and position.
 */
public class GameItem {

    private final Mesh mesh;
    
    private final Vector3f position;
    
    private float scale;

    private final Vector3f rotation;

    public GameItem(Mesh mesh) {
        this.mesh = mesh;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the Game Item about itself.
     * @param x Angle to rotate x in degrees
     * @param y Angle to rotate y in degrees
     * @param z Angle to rotate z in degrees
     */
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
}
