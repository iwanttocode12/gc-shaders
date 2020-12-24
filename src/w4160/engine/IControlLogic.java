package w4160.engine;

/**
 * Interface of the basic operations of controlling an OpenGL display.
 *
 */
public interface IControlLogic {

    void init(Window window) throws Exception;
    
    void respond_key_action(long window, int key, int scancode, int action, int mods);
    
    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);
    
    void render(Window window);
    
    void cleanup();
}
