package w4160.engine;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

/**
 * This class defines an OpenGL display window.
 *
 */
public class Window {

    private final String title;

    private int width;

    private int height;
    
    private int scaleFactorW = 1;
    private int scaleFactorH = 1;

    private long windowHandle;

    private boolean resized;

    private boolean vSync;
    
    /**
     * Window constructor.
     * 
     * @param title displayed title of the window
     * @param width width of the window in pixels
     * @param height height of the window in pixels
     * @param vSync indicating if the vertical sync in GLFW should be enabled
     */
    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;

    }
    
    /**
     * Initialize GLFW settings for the window.
     * 
     * @see <a href="https://www.glfw.org/docs/latest/window_guide.html">GLFW Window guide</a>
     */
    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        /*
         * Compute the scale factor between display buffer resolution and screen resolution.
         * This is a fix for Mac OS retina screen. It has a scale factor of 2
         */
        try (MemoryStack stack = MemoryStack.stackPush()) {
        	IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetFramebufferSize(windowHandle, pWidth, pHeight);
			scaleFactorW = pWidth.get(0) / width;
			scaleFactorH = pHeight.get(0) / height;
		}
        
        // Setup resize callback
        glfwSetWindowSizeCallback(windowHandle, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.this.width = width;
                Window.this.height = height;
                Window.this.setResized(true);
            }
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }


        //glfwSetInputMode(windowHandle, GLFW_STICKY_KEYS, GLFW_TRUE);
        
        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
    }

    /**
     * @return return the handle of current window 
     */
    public long getWindowHandle() {
        return windowHandle;
    }

    /**
     * Set the OpenGL background color.
     * 
     * @param r red channel 
     * @param g green channel
     * @param b blue channel 
     * @param alpha alpha channel
     */
    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    /**
     * This function is to check the state of the given key. Return True if 
     * it's being pressed.
     * 
     * @param keyCode
     * @return
     */
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    /**
     * @return the current window title
     */
    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getBufferWidth() {
    	return width * scaleFactorW;
    }
    
    public int getBufferHeight() {
    	return height * scaleFactorH;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
       this.resized = resized;
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    /*
     * Run the OpenGL update to refresh the current display buffer.
     */
    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }
}
