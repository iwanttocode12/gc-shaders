package w4160.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallback;

import w4160.engine.GameItem;
import w4160.engine.IControlLogic;
import w4160.engine.MouseInput;
import w4160.engine.Window;
import w4160.engine.graphics.Camera;
import w4160.engine.graphics.DirectionalLight;
import w4160.engine.graphics.Material;
import w4160.engine.graphics.Mesh;
import w4160.engine.graphics.OBJLoader;
import w4160.engine.graphics.PointLight;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A simple implementation of the {@link IControlLogic} interface. It defines 
 * the high-level logic of an interactive graphics application, including how
 * to initialize, how to response user input (mouse clicks and key strokes), and
 * how to display content.
 * 
 * @see IControlLogic
 */
public class SimpleGame implements IControlLogic {

    private static final float MOUSE_SENSITIVITY 	= 0.25f;
    
    /** Step size of scaling the displayed object */
    private static final float SCALE_STEP 			= 0.01f;

    /** Step size of translating the displayed object */
    private static final float TRANSLATE_STEP 		= 0.01f;
   
    /** Step size for rotating the displayed object */
    private static final float ROTATION_STEP 		= 0.3f;

    /** Direction of camera to move at each update. Set to (0,0,0) for stationary camera */
    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    /** Array of items in the game */
    private GameItem[] gameItems;

    private Vector3f ambientLight;

    private PointLight pointLight;

    private DirectionalLight directionalLight;

    private float lightAngle;

    /** Step size for moving the camera position */
    private static final float CAMERA_POS_STEP = 0.05f;
    
    private int currentObj;

    private int currentShaderIndex;

    private String[] meshFile = null;

    /** 
     * Constructor of SimpleGame using the default mesh, which is a Cube. 
     * 
     * @see Mesh
     */
    public SimpleGame() {
        renderer = new Renderer();
        camera = new Camera();
        camera.setPosition(0.f, 0.f, 0.6f);
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;
        currentObj=0;
        currentShaderIndex=0;
    }
    
    /** 
     * Constructor of SimpleGame by loading the mesh stored in an .obj file. 
     * 
     * @param meshFile .obj filename that stores the mesh
     * @see Mesh
     * @see OBJLoader
     */
    public SimpleGame(String meshFile) {
        String[] parts = meshFile.split(" ");
    	renderer = new Renderer();
        camera = new Camera();
        camera.setPosition(0.f, 0.f, 0.6f);
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        
        lightAngle = -90;
        currentObj=0;
        this.meshFile = parts;
    }
    
    
    @Override
    public void init(Window window) throws Exception {
        float reflectance = 1f;
        Mesh mesh;
        Material material;
        if (meshFile==null){
           mesh = new Mesh(); 
           material = new Material(new Vector3f(0.4f, 0.8f, 1f), reflectance);
        }
        else{
            mesh = OBJLoader.loadMesh(meshFile[0]);
            if (meshFile.length == 1){
                material = new Material(new Vector3f(0.4f, 0.8f, 1f), reflectance);
            }
            else{
                material = new Material(meshFile[1], reflectance);
            }
        }

        mesh.setMaterial(material);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0.1f, -0.5f, -2);
        gameItems = new GameItem[]{gameItem};

        renderer.init(window);
        
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        lightPosition = new Vector3f(-1, 0, 0);
        lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window.getWindowHandle(), new GLFWKeyCallback() {
            @Override
            public void invoke(long windowHandle, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(windowHandle, true);
                } else {
                	respond_key_action(windowHandle, key, scancode, action, mods);
                }
            }
        });
    }

    /**
     * This function responds to the state changes of keys. Different from the {@link #input(Window, MouseInput) input} method,
     * this function is triggered when there is a key state change---for example, press a key or release a key. 
     * When the key is holding, this function won't be triggered.
     * 
     * @see #input(Window, MouseInput)
     */
    @Override
    public void respond_key_action(long window, int key, int scancode, int action, int mods) {
    	if ( action == GLFW_RELEASE ) {
    		
	    	if( key == GLFW_KEY_SPACE ) {
	            // select current shader
	            currentShaderIndex = currentShaderIndex + 1;
	            currentShaderIndex = currentShaderIndex % renderer.getNumShaders();
	            System.out.println("selected shader: " + renderer.getShaderName(currentShaderIndex));
	        } else if( key == GLFW_KEY_Q ){
	    		//select current object
	    		currentObj = currentObj + 1;
	    		currentObj = currentObj % gameItems.length;
	    		System.out.println("currently selected object ID: " + currentObj);
	    	}
	    	else if( key == GLFW_KEY_W ) {
	    		//select current object
	    		currentObj = currentObj - 1;
	    		currentObj = currentObj % gameItems.length;
	    		System.out.println("currently selected object ID: " + currentObj);
	    	} else if( key == GLFW_KEY_1 ) {
	    		//get screenshot
	    		renderer.writePNG();
	    	}
	    	else if( key == GLFW_KEY_7 ) {
	    		//reflection by manipulating mesh
	    		gameItems[currentObj].getMesh().reflectMesh(new Vector3f(0f,0f,0f), new Vector3f(0f, 0f, 1f));
	    	}
    	}
    } // end respond_key_action

    /**
     * This function responds keys "continuously". For example, when the user is pressing the key,
     * this function will respond to the key pressing at each game loop. Effectively, holding the key
     * down will keep triggering the key response implemented in this function.
     * 
     * <p>
     * This differs from the {@link #respond_key_action(long, int, int, int, int) respond_key_action} method,
     * which is triggered only when the key states are changed.
     * </p>
     * 
     * @see #respond_key_action(long, int, int, int, int)
     */
    @Override
    public void input(Window window, MouseInput mouseInput) {
 
        if(window.isKeyPressed(GLFW_KEY_E)){
    		//scale object
    		float curr = gameItems[currentObj].getScale();
    		gameItems[currentObj].setScale(curr+SCALE_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_R)){
    		//scale object
    		float curr = gameItems[currentObj].getScale();
    		gameItems[currentObj].setScale(curr-SCALE_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_T)){
    		//move object x by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x+TRANSLATE_STEP, curr.y, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_Y)){
    		//move object x by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x-TRANSLATE_STEP, curr.y, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_U)){
    		//move object y by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x, curr.y+TRANSLATE_STEP, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_I)){
    		//move object y by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x, curr.y-TRANSLATE_STEP, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_O)){
    		//move object z by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x, curr.y, curr.z+TRANSLATE_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_P)){
    		//move object z by step
    		Vector3f curr = gameItems[currentObj].getPosition();
    		gameItems[currentObj].setPosition(curr.x, curr.y, curr.z-TRANSLATE_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_A)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x+ROTATION_STEP, curr.y, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_S)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x-ROTATION_STEP, curr.y, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_D)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x, curr.y+ROTATION_STEP, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_F)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x, curr.y-ROTATION_STEP, curr.z);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_G)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x, curr.y, curr.z+ROTATION_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_H)){
    		//rotate object at x axis
    		Vector3f curr = gameItems[currentObj].getRotation();
    		gameItems[currentObj].setRotation(curr.x, curr.y, curr.z-ROTATION_STEP);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_0)){
    		//translation by manipulating mesh
    		gameItems[currentObj].getMesh().translateMesh(new Vector3f(0f,0.05f,0.01f));
    	}
    	else if(window.isKeyPressed(GLFW_KEY_9)){
    		//rotation by manipulating mesh
    		gameItems[currentObj].getMesh().rotateMesh(new Vector3f(1,1,1), 2);
    	}
    	else if(window.isKeyPressed(GLFW_KEY_8)){
    		//scale by manipulating mesh
    		gameItems[currentObj].getMesh().scaleMesh(1.001f,1.0f,1.0f);
    	}
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            Vector3f curr = gameItems[0].getRotation();
            gameItems[0].setRotation(curr.x-rotVec.x * MOUSE_SENSITIVITY, curr.y-rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update directional light direction, intensity and color
        lightAngle += 1.1f;
       
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 90) {
                lightAngle = -90;
            }
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, ambientLight, pointLight, directionalLight, renderer.getShaderName(currentShaderIndex));
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
