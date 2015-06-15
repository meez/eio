package meez.eio.java.json.impl;

import meez.eio.java.json.JsonBuilder;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/** JsonSupport */
public class JsonSupport {

  // Definitions

  /** Empty list */
  public final static JsonArray EMPTY_ARRAY = new JsonArray();

  // Helpers

  /** Build */
  public static JsonBuilder<JsonObject> build() {
    return new JsonBuilderObject();
  }

  // Codec

  /** Convert string to long */
  public static long asLong(String value, long defaultValue) {
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /** Convert string to integer */
  public static int asInt(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  // Accessors

  /** Return array or default */
  public static JsonArray getOrDefault(JsonObject obj, String name, JsonArray defaultValue) {
    JsonArray value = obj.getArray(name);
    return (value != null) ? value : defaultValue;
  }

  /** Return Integer or default */
  public static int getOrDefault(JsonObject obj, String name, int defaultValue) {
    return obj.getNumber(name, defaultValue).intValue();
  }
}
