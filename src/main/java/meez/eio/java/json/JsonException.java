package meez.eio.java.json;

/**
 * Exception thrown reading/writing Json.
 * <p/>
 * We extend RuntimeException to simplify integration
 */
public class JsonException extends RuntimeException {

  // Public methods

  /** Create new JsonException */
  public JsonException(String msg) {
    super(msg);
  }

  /** Create new JsonException */
  public JsonException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
