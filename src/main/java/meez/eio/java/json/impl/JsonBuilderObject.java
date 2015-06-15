package meez.eio.java.json.impl;

import org.vertx.java.core.json.JsonElement;
import org.vertx.java.core.json.JsonObject;

/** JsonObject builder */
public class JsonBuilderObject extends BaseJsonBuilderObject<JsonObject> {
  //
  // Public methods
  //

  /** Create new JsonBuilder */
  public JsonBuilderObject() {
    this(new JsonObject());
  }

  /** Create new JsonBuilder */
  public JsonBuilderObject(JsonElement root) {
    super(root);

    this.stack.push(this.root);
  }

  //
  // JsonBuilder implementation
  //

  /** Return object */
  public JsonObject toOutput() {
    assert this.root.isObject();

    return (JsonObject) this.root;
  }
}
