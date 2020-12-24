package w4160.engine.graphics;

public class GLSLShaderException extends RuntimeException {
	
	private static final long serialVersionUID = -6951591201031552500L;;

	/**
     * Constructs an GLSLShaderException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public GLSLShaderException() {
        super();
    }
    
    /**
     * Constructs an GLSLShaderException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public GLSLShaderException(String s) {
        super(s);
    }
    
    public GLSLShaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public GLSLShaderException(Throwable cause) {
        super(cause);
    }
}