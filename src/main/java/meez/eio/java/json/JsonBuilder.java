package meez.eio.java.json;

import java.util.List;

/** Interface for creating a Json object / stream */
public interface JsonBuilder<T> {

  /** Encode */
  public interface Encoder<S> {
    void call(JsonBuilder builder, S value);
  }

  // Builder

  /** Return output */
  T toOutput() throws JsonException;

  /** Add string key */
  JsonBuilder<T> key(String name, String value) throws JsonException;

  /** Add int key */
  JsonBuilder<T> key(String name, Integer value) throws JsonException;

  /** Add long key */
  JsonBuilder<T> key(String name, Long value) throws JsonException;

  /** Add double key */
  JsonBuilder<T> key(String name, Double value) throws JsonException;

  /** Add boolean key */
  JsonBuilder<T> key(String name, Boolean value) throws JsonException;

  /** Start Object */
  JsonBuilder<T> object(String name) throws JsonException;

  /** Encode Object */
  <V> JsonBuilder<T> object(String name, Encoder<V> encoder, V value);

  /** Raw Object */
  JsonBuilder<T> object(String name, Object value) throws JsonException;

  /** Array Object */
  JsonBuilder<T> object() throws JsonException;

  /** Array Integer Element */
  JsonBuilder<T> element(Integer value) throws JsonException;

  /** Array String Element */
  JsonBuilder<T> element(String value) throws JsonException;

  /** Start Array */
  JsonBuilder<T> array(String name) throws JsonException;

  /** Complete array */
  JsonBuilder<T> array(String name, Iterable values);

  /** Encode array */
  <V> JsonBuilder<T> array(String s, Encoder<V> encoder, List<V> value);

  /** Sub-Array */
  JsonBuilder<T> array() throws JsonException;

  /** End */
  JsonBuilder<T> end() throws JsonException;
}
