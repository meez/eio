package meez.eio.java.json.impl;

import meez.eio.java.json.JsonBuilder;
import meez.eio.java.json.JsonException;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

import java.util.LinkedList;
import java.util.List;

/** Base-class for JsonObject JsonBuilder */
public abstract class BaseJsonBuilderObject<T> implements JsonBuilder<T> {

  // Instance variables

  /** Output */
  protected final JsonElement root;

  /** Stack */
  protected LinkedList<JsonElement> stack;

  // Public methods

  /** Create new BaseJsonBuilderObject */
  public BaseJsonBuilderObject(JsonElement root) {
    this.root = root;

    this.stack = new LinkedList<>();
    this.stack.push(this.root);
  }

  // JsonBuilder implementation

  /** As object */
  public abstract T toOutput();

  /** Set string key */
  public JsonBuilder<T> key(String name, String value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value == null)
      return this;

    ((JsonObject) this.stack.peek()).putString(name, value);

    return this;
  }

  /** Set int key */
  public JsonBuilder<T> key(String name, Integer value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value == null)
      return this;

    ((JsonObject) this.stack.peek()).putNumber(name, value);

    return this;
  }

  /** Set long key */
  public JsonBuilder<T> key(String name, Long value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value == null)
      return this;

    ((JsonObject) this.stack.peek()).putNumber(name, value);

    return this;
  }

  /** Set double key */
  public JsonBuilder<T> key(String name, Double value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value == null)
      return this;

    ((JsonObject) this.stack.peek()).putNumber(name, value);

    return this;
  }

  /** Set boolean key */
  public JsonBuilder<T> key(String name, Boolean value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value == null)
      return this;

    ((JsonObject)this.stack.peek()).putBoolean(name, value);

    return this;
  }

  /** Start object */
  public JsonBuilder<T> object(String name) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    pushElement(name, new JsonObject());

    return this;
  }

  /** Start object */
  public JsonBuilder<T> object() {
    assert !this.stack.isEmpty() && this.stack.peek().isArray();

    pushElement("", new JsonObject());

    return this;
  }

  /** Nested encoder */
  public <V> JsonBuilder<T> object(String name, Encoder<V> encoder, V value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    object(name);
    encoder.call(this, value);
    end();

    return this;
  }

  /** Insert object */
  public JsonBuilder<T> object(String name, Object value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();
    assert JsonObject.class.isInstance(value);

    ((JsonObject)this.stack.peek()).putObject(name, (JsonObject) value);

    return this;
  }

  /** Add array number element */
  public JsonBuilder<T> element(Integer value) {
    assert !this.stack.isEmpty() && this.stack.peek().isArray();

    ((JsonArray) this.stack.peek()).addNumber(value);

    return this;
  }

  /** Add array string element */
  public JsonBuilder<T> element(String value) {
    assert !this.stack.isEmpty() && this.stack.peek().isArray();

    ((JsonArray) this.stack.peek()).addString(value);

    return this;
  }

  /** Start array */
  public JsonBuilder<T> array(String name) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    pushElement(name, new JsonArray());

    return this;
  }

  /** Array encoder */
  public <V> JsonBuilder<T> array(String name, Encoder<V> encoder, List<V> values) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    pushElement(name, new JsonArray());

    // Write the completed array
    for (V o : values) {
      object();
      encoder.call(this,o);
      end();
    }

    this.stack.pop();

    return this;
  }

  /** Complete array */
  public JsonBuilder<T> array(String name, Iterable value) {
    assert !this.stack.isEmpty() && this.stack.peek().isObject();

    if (value instanceof JsonArray)
    {
      ((JsonObject)this.stack.peek()).putArray(name, (JsonArray)value);
    }
    else
    {
      assert !this.stack.isEmpty() && this.stack.peek().isObject();

      // Create the completed array
      JsonArray arr = new JsonArray();
      for (Object o : value)
        arr.add(o.toString());

      ((JsonObject)this.stack.peek()).putArray(name, arr);

      return this;
    }

    return this;
  }

  /** Start array */
  public JsonBuilder<T> array() {
    assert !this.stack.isEmpty() && this.stack.peek().isArray();

    pushElement("", new JsonArray());

    return this;
  }

  /** Finish element */
  public JsonBuilder<T> end() throws JsonException {
    assert !this.stack.isEmpty();

    this.stack.pop();

    return this;
  }

  // Implementation

  /** Push element */
  protected void pushElement(String name, JsonElement el) {
    assert !this.stack.isEmpty();

    if (this.stack.peek().isObject()) {
      if (el.isObject()) {
        ((JsonObject)this.stack.peek()).putObject(name, (JsonObject) el);
      } else {
        ((JsonObject)this.stack.peek()).putArray(name, (JsonArray) el);
      }
    } else {
      ((JsonArray)this.stack.peek()).add(el);
    }

    this.stack.push(el);
  }
}
