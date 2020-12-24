package w4160.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import w4160.engine.GameItem;

/**
 * This class contains methods to calculate the several matrices needed to transform all objects
 * in a scene to the display window.
 */
public class Transformation {

    private final Matrix4f projectionMatrix;
    
    private final Matrix4f viewMatrix;
    
    private final Matrix4f modelMatrix;

    public Transformation() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
    }

    /**
     * Calculates the Projection Matrix which transforms vectors in camera space to homogeneous space.
     *
     * @param fov The vertical field of view in radians
     * @param width The width of my window
     * @param height The height of my window
     * @param zNear Near clipping plane
     * @param zFar Far clipping plane
     * @return The Projection Matrix
     */
    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
    	float aspectRatio = width / height;        
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    /**
     * Returns a View Matrix which transforms vertices from world space to
     * camera space. The information about the camera is stored in the input {@link Camera} instance.
     *
     * @param camera The camera contains information about the camera space
     * @return The View Matrix
     * @see Camera
     */
    public Matrix4f getViewMatrix(Camera camera) {
    	Vector3f cameraPos = camera.getPosition();
    	Vector3f cameraTarget = camera.getTarget();
    	Vector3f up = camera.getUp();
    	
    	// student code
    	viewMatrix.identity();
        viewMatrix.lookAt(cameraPos, cameraTarget, up);
        
        return viewMatrix;
    }

    /**
     * Returns the Model Matrix which transforms model space to world space by applying
     * the transformation (translation, rotation, scale) parameters of the given {@link GameItem}.
     * 
     * <p>
     * <b>Note:</b> The returned ModelMatrix is to transform the specific {@link GameItem}.
     * </p>
     *
     * @param gameItem The object to generate the model matrix from
     * @return The Model Matrix
     * @see GameItem
     */
    public Matrix4f getModelMatrix(GameItem gameItem){
        Vector3f rotation = gameItem.getRotation();
        modelMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float)Math.toRadians(-rotation.x)).
                rotateY((float)Math.toRadians(-rotation.y)).
                rotateZ((float)Math.toRadians(-rotation.z)).
                scale(gameItem.getScale());
        return modelMatrix;
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        Matrix4f viewCurr = new Matrix4f(viewMatrix);
        return viewCurr.mul(getModelMatrix(gameItem));
    }
}
