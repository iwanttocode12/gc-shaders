package w4160.game;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

import java.awt.HeadlessException;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import ar.com.hjg.pngj.*;
import w4160.engine.GameItem;
import w4160.engine.Window;
import w4160.engine.graphics.Camera;
import w4160.engine.graphics.DirectionalLight;
import w4160.engine.graphics.Mesh;
import w4160.engine.graphics.Material;
import w4160.engine.graphics.PointLight;
import w4160.engine.graphics.ShaderProgram;
import w4160.engine.graphics.Transformation;

/**
 * Renderer is what transforms the objects and parameters in the scene and
 * transforms it to an image on the screen. It takes into account the shader files
 * and material and lighting properties to create a ShaderProgram, used to render
 * GameItems and light sources using OpenGL.
 */
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private LinkedHashMap<String,ShaderProgram> shaderProgramList;

    private final float specularPower;

    private Window window;
        
    public Renderer() {
        transformation = new Transformation();
        specularPower = 4f;
        shaderProgramList = new LinkedHashMap<>();
    }

    // Example shader
    public ShaderProgram createPhongShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/phong_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/phong_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }
    

    // Skeleton shader
    public ShaderProgram createSkeletonShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/skeleton_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/skeleton_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("ambientLight");

        return shaderProgram;
    }

    /* Student code
    public ShaderProgram createMyShader() throws Exception {
        // ...
    }
    */

    public ShaderProgram createGouraudShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/gouraud_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/gouraud_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createCheckerboardShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/checkerboard_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/checkerboard_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }
    
    public ShaderProgram createTextureShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/texture_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/texture_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("texture_sampler");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createNormalShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/normal_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/normal_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("texture_sampler");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createCelShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/cel_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/cel_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("texture_sampler");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createGoochShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/gooch_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/gooch_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createUniform("uWarmColor");
        shaderProgram.createUniform("uCoolColor");
        shaderProgram.createUniform("uDiffuseWarm");
        shaderProgram.createUniform("uDiffuseCool");

        return shaderProgram;
    }

    public ShaderProgram createHatchShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/hatching_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/hatching_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        shaderProgram.createUniform("texture_sampler");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createSpinShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/spin_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/spin_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        shaderProgram.createUniform("currentTime");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    public ShaderProgram createChangeColorShader() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        shaderProgram.createVertexShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/changecolor_vertex.vs"))));
        shaderProgram.createFragmentShader(new String(Files.readAllBytes(Paths.get("src/resources/shaders/changecolor_fragment.fs"))));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");

        shaderProgram.createUniform("currentTime");

        // Create uniform for material
        shaderProgram.createMaterialUniform("material");

        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");

        return shaderProgram;
    }

    /**
     * Initialize the Renderer by setting up the shaders. You don't have to understand this 
     * method for now, just use it. You'll learn the shader programming later in this class, 
     * and we will dedicate programming assignment 2 for shader programming.  
     * 
     * @param window the current window
     * @throws Exception
     */
    public void init(Window window) throws Exception {
    	this.window = window;
    	
        // Create our example shader
        shaderProgramList.put("phong", createPhongShader());
        shaderProgramList.put("skeleton", createSkeletonShader());

        // Student code: add your shaders here
        // ...
        shaderProgramList.put("gouraud", createGouraudShader());      
        shaderProgramList.put("checkerboard", createCheckerboardShader()); 
        shaderProgramList.put("texture-modulated", createTextureShader());    
        shaderProgramList.put("normal", createNormalShader());
        shaderProgramList.put("cel", createCelShader()); 
        shaderProgramList.put("gooch", createGoochShader());    
        shaderProgramList.put("hatching", createHatchShader());
        shaderProgramList.put("spin", createSpinShader()); 
        shaderProgramList.put("changecolor", createChangeColorShader()); 
    }

    public int getNumShaders() { return shaderProgramList.size(); }

    public String getShaderName(int ind) { return new ArrayList<String>(shaderProgramList.keySet()).get(ind); }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight,
        PointLight pointLight, DirectionalLight directionalLight, String currentShader) {
        
    	clear();
        
        if ( window.isResized() ) {
        	// if needed, do sth. to respond to the window size change
        	window.setResized(false);
        	glViewport(0, 0, window.getBufferWidth(), window.getBufferHeight());
        }
        
        ShaderProgram shaderProgram = shaderProgramList.get(currentShader);
        shaderProgram.bind();

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);

        // Provided example: Phong shading
        if(currentShader.equals("phong")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }
        // Provided example: basic skeleton shader
        else if(currentShader.equals("skeleton")) {
        	// Here you need to set up the uniform variable values
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
        }
        /* Student code
        else if(currentShader.equals("my_shader")) {
            // ...
        }
        */
        else if(currentShader.equals("gouraud")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("checkerboard")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("texture-modulated")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("normal")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("cel")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("gooch")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);
            Vector3f uWarmColor = new Vector3f(0.8f,0.8f,0.0f);
            Vector3f uCoolColor = new Vector3f(0.0f,0.0f,1.0f);
            float uDiffuseWarm = 0.2f;
            float uDiffuseCool = 0.6f;
            shaderProgram.setUniform("uWarmColor", uWarmColor);
            shaderProgram.setUniform("uCoolColor", uCoolColor);
            shaderProgram.setUniform("uDiffuseWarm", uDiffuseWarm);
            shaderProgram.setUniform("uDiffuseCool", uDiffuseCool);

            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);
        }

        else if(currentShader.equals("hatching")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("spin")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);
            long time = java.lang.System.currentTimeMillis();
            shaderProgram.setUniform("currentTime", (int) time);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        else if(currentShader.equals("changecolor")) {
            shaderProgram.setUniform("projectionMatrix", projectionMatrix);
            long time = java.lang.System.currentTimeMillis();
            shaderProgram.setUniform("currentTime", (int) time);

            // Update Light Uniforms
            shaderProgram.setUniform("ambientLight", ambientLight);
            shaderProgram.setUniform("specularPower", specularPower);
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLight);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            shaderProgram.setUniform("pointLight", currPointLight);

            // Get a copy of the directional light object and transform its position to view coordinates
            DirectionalLight currDirLight = new DirectionalLight(directionalLight, 0.3f);
            Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
            dir.mul(viewMatrix);
            currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
            shaderProgram.setUniform("directionalLight", currDirLight);
        }

        // Render each gameItem
        for(GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix); 

            shaderProgram.setUniform("material", mesh.getMaterial());

            if (mesh.getMaterial().isTextured()){
                if(currentShader.equals("texture-modulated") || currentShader.equals("normal") || currentShader.equals("cel")) {
                    shaderProgram.setUniform("texture_sampler",  mesh.getMaterial().getTexture());
                }
            }
            // Render the mesh for this game item
            mesh.render();
        }

        shaderProgram.unbind();
    }

    public void cleanup() {
        for (ShaderProgram shaderProgram : shaderProgramList.values()) {
            if(shaderProgram != null) {
                shaderProgram.cleanup();
            }
        }
    }
    
    private static int imgcount = 0;

    public void writePNG() throws HeadlessException{
    	glPixelStorei(GL_PACK_ALIGNMENT, 1);
    	glReadBuffer(GL_FRONT);
    	
		final int width = window.getBufferWidth();
		final int height= window.getBufferHeight();

		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		ImageInfo imi = new ImageInfo(width, height, 8, false);
        PngWriter png = new PngWriter(new File("screenshot"+imgcount+".png"), imi , true);
        
        ImageLineInt iline = new ImageLineInt(imi);        
		for(int row = 0; row < imi.rows; row++){
	        for (int col = 0; col < imi.cols; col++) { // this line will be written to all rows
				int i = (col + (width * (imi.rows-row-1))) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
	            ImageLineHelper.setPixelRGB8(iline, col, r, g, b); 
	        }
	        png.writeRow(iline);
		}
        png.end();
    	imgcount=imgcount+1;
    }
}
