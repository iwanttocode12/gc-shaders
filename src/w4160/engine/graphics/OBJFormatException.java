package w4160.engine.graphics;

public class OBJFormatException extends RuntimeException {
	private static final long serialVersionUID = -6951591201031552497L;

	/**
     * Constructs an OBJFormatException with no detail message.
     * A detail message is a String that describes this particular exception.
     */
    public OBJFormatException() {
        super();
    }
    
    /**
     * Constructs an OBJFormatException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     */
    public OBJFormatException(String s) {
        super(s);
    }
    
    public OBJFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public OBJFormatException(Throwable cause) {
        super(cause);
    }
}
