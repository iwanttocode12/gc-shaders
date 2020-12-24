package w4160.engine.graphics;

import org.joml.Vector3f;

/**
 * This class specifies the Material properties of an object. The values
 * in this class are used when rendering the object.
 */
public class Material {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f colour = DEFAULT_COLOUR;
    
    private float reflectance = 1;

    private Texture texture = null;
    
    public Material() { }
    
    public Material(String texFile, float reflectance) throws Exception {
    	this.texture = new Texture(texFile); //
    	this.reflectance = reflectance;
    }
    
    public Material(Vector3f colour, float reflectance) {
        this();
        this.colour = colour;
        this.reflectance = reflectance;
    }

    public Vector3f getColour() {
        return colour;
    }

    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
    	return texture;
    }
    
    public boolean isTextured() {
        return texture != null;
    }

}
