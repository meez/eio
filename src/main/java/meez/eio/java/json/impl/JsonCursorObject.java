package meez.eio.java.json.impl;

import meez.eio.java.json.JsonCursor;
import meez.eio.java.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

import java.util.LinkedList;
import java.util.List;

/** JsonCursor
 *
 * Class for navigating JSON objects.
 *
 */
public class JsonCursorObject implements JsonCursor<JsonObject> {

  // Instance variables

  /** Logging */
  protected final static Logger log = LoggerFactory.getLogger(JsonCursorObject.class);

  /** Stack */
  private LinkedList<JsonElement> stack;

  // Public methods

  /** Create new JsonCursor */
  public JsonCursorObject(JsonObject in) {
    this.stack = new LinkedList<JsonElement>();
    this.stack.push(in);
  }

  /** Object */
  public JsonObject asObject() {
    return peekObject();
  }

  /** Current */
  public JsonArray asArray() {
    return peekArray();
  }

  //
  // JsonCursor implementation
  //

  /** Object */
  public JsonCursor object(String name) throws JsonException {
    JsonObject obj = peekObject().getObject(name);
    if (obj == null)
      throw new JsonException(String.format("Object does not contain child object '%s'", name));

    this.stack.push(obj);

    return this;
  }

  /** Array */
  public JsonCursor array(String name) throws JsonException {
    JsonArray arr = peekObject().getArray(name);
    if (arr == null)
      throw new JsonException(String.format("Object does not contain child array '%s'", name));

    this.stack.push(arr);

    return this;
  }

  /** Unwind */
  public JsonCursor end() {
    this.stack.pop();

    return this;
  }

  // Tests

  /** Return true if child object exists */
  public boolean hasChild(String name) {
    return peekObject().getFieldNames().contains(name);
  }

  // Property accessors

  /** String */
  public String string(String name) {
    return peekObject().getString(name);
  }

  /** String */
  public String string(String name, String defaultValue) {
    String value = peekObject().getString(name);

    return (value != null) ? value : defaultValue;
  }

  /** Number */
  public Number number(String name) {
    return peekObject().getNumber(name);
  }

  /** Number */
  public Number number(String name, Number defaultValue) {
    Number value = peekObject().getNumber(name);

    return (value != null) ? value : defaultValue;
  }

  // Array accessors

  /** Array start */
  public JsonCursor first() {
    JsonArray res = peekArray();

    assert res.size() > 0 && res.get(0) instanceof JsonObject;

    this.stack.push((JsonObject) res.get(0));

    return this;
  }

  /** Cursor */
  public JsonCursor foreach(Iterate f) {
    if (isArray()) {
      JsonArray res = peekArray();
      for (int i = 0; i < res.size(); i++) {
        f.call(Integer.toString(i), res.get(i));
      }
    } else {
      JsonObject obj = peekObject();
      for (String name : obj.getFieldNames()) {
        f.call(name, obj.getField(name));
      }
    }

    return this;
  }

  /** Collect array into a list */
  public JsonCursor collect(String name, List l) {
    // Check if array exists
    JsonArray res = peekObject().getArray(name);
    if (res == null)
      return this;

    for (int i = 0; i < res.size(); i++) {
      l.add(res.get(i));
    }

    return this;
  }

  /** Collect array into a list with map */
  public <T> JsonCursor map(String name, List<T> l, Mapper<T> tx) {
    // Check if array exists
    JsonArray res = peekObject().getArray(name);
    if (res == null)
      return this;

    for (int i = 0; i < res.size(); i++) {
      JsonObject cur=res.get(i);
      this.stack.push(cur);
      try {
        T val = tx.call(this);
        if (val != null)
          l.add(val);
      } catch (Exception e) {
        log.warn("Unable to transform list element "+cur+" - ignoring");
      }

      this.stack.pop();
    }

    return this;
  }

  // Implementation

  /** Return true if array */
  private boolean isArray() {
    return this.stack.peek() instanceof JsonArray;
  }

  /** Peek object */
  private JsonObject peekObject() {
    return (JsonObject) this.stack.peek();
  }

  /** Peek array */
  private JsonArray peekArray() {
    return (JsonArray) this.stack.peek();
  }
}
