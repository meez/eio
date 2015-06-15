package meez.eio.java.json.impl;

import meez.eio.java.core.WriteStreamOutputStream;
import meez.eio.java.json.JsonBuilder;
import meez.eio.java.json.JsonException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.streams.WriteStream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/** JsonBuilderStream */
public class JsonBuilderStream implements JsonBuilder<WriteStream> {
  //
  // Definitions
  //

  /** Tokens */
  private enum State {
    OBJECT, ARRAY
  }

  ;

  //
  // Instance variables
  //

  /** Output */
  protected final WriteStreamOutputStream out;

  /** Generator */
  protected JsonGenerator json;

  /** State */
  protected LinkedList<State> stack;

  //
  // Public methods
  //

  /** Create new JsonBuilderStream */
  public JsonBuilderStream(WriteStream out) throws IOException {
    this.out = new WriteStreamOutputStream(out);

    this.json = new JsonFactory().createJsonGenerator(this.out);
    this.json.writeStartObject();

    this.stack = new LinkedList<State>();
    this.stack.push(State.OBJECT);
  }

  //
  // JsonBuilder implementation
  //

  /** Output */
  public WriteStream toOutput() throws JsonException {
    try {
      return this.out.getOutput();
    } catch (IOException e) {
      throw new JsonException("Unable to write", e);
    }
  }

  /** Add string key */
  public JsonBuilderStream key(String name, String value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeStringField(name, value);
      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Add int key */
  public JsonBuilderStream key(String name, Integer value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeNumberField(name, value);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode " + name + "'", e);
    }
  }

  /** Add long key */
  public JsonBuilderStream key(String name, Long value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeNumberField(name, value);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode " + name + "'", e);
    }
  }

  /** Add double key */
  public JsonBuilderStream key(String name, Double value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeNumberField(name, value);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Add boolean key */
  public JsonBuilder key(String name, Boolean value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeBooleanField(name, value);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Object */
  public JsonBuilderStream object(String name) throws JsonException {
    assert !this.stack.isEmpty();

    try {
      this.json.writeObjectFieldStart(name);
      this.stack.push(State.OBJECT);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Start object */
  public JsonBuilder object() {
    assert !this.stack.isEmpty();

    return this;
  }

  /** Nested encoder */
  public <V> JsonBuilder object(String name, Encoder<V> encoder, V value) {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    object(name);
    encoder.call(this, value);
    end();

    return this;
  }

  /**
   * Object value
   * <p/>
   * Only JsonObject is supported.
   */
  public JsonBuilderStream object(String name, Object value) throws JsonException {
    assert !this.stack.isEmpty();
    assert JsonObject.class.isInstance(value);

    try {
      // TODO: Optimize
      if (value != null) {
        this.json.writeFieldName(name);
        this.json.writeRaw(":");
        this.json.writeRaw(value.toString());
      } else {
        this.json.writeFieldName(name);
        this.json.writeRaw(":null");
      }

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** String element */
  public JsonBuilder element(String value) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.ARRAY;

    try {
      this.json.writeString(value);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode array string element", e);
    }
  }

  /** Array */
  public JsonBuilderStream array(String name) throws JsonException {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeArrayFieldStart(name);
      this.stack.push(State.ARRAY);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Complete array */
  public JsonBuilderStream array(String name, List<String> values) {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeArrayFieldStart(name);

      // Write the completed array
      for (int i = 0; i < values.size(); i++)
        this.json.writeString(values.get(i));

      this.json.writeEndArray();

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Complete array */
  public JsonBuilderStream array(String name, Iterable values) {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeArrayFieldStart(name);

      // Write the completed array
      for (Object el : values)
      {
        this.json.writeObject(el);
      }

      this.json.writeEndArray();

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Array encoder */
  public <V> JsonBuilder array(String name, Encoder<V> encoder, List<V> values) {
    assert !this.stack.isEmpty() && this.stack.peek() == State.OBJECT;

    try {
      this.json.writeArrayFieldStart(name);

      // Write the completed array
      for (int i = 0; i < values.size(); i++)
        encoder.call(this, values.get(i));

      this.json.writeEndArray();

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode '" + name + "'", e);
    }
  }

  /** Array */
  public JsonBuilder array() throws JsonException {
    try {
      this.json.writeStartArray();
      this.stack.push(State.ARRAY);

      return this;
    } catch (IOException e) {
      throw new JsonException("Unable to encode array start", e);
    }
  }

  /** End */
  public JsonBuilderStream end() throws JsonException {
    assert !this.stack.isEmpty();

    try {
      switch (this.stack.pop()) {
        case OBJECT:
          this.json.writeEndObject();
          break;
        case ARRAY:
          this.json.writeEndArray();
          break;
      }
    } catch (IOException e) {
      throw new JsonException("Unable to encode end", e);
    }

    if (this.stack.isEmpty()) {
      finish();
    }

    return this;
  }

  /** Finish */
  public void finish() throws JsonException {
    try {
      this.json.close();
    } catch (IOException e) {
      throw new JsonException("Unable to encode finish", e);
    }
  }
}
