package meez.eio.java.json;

import java.util.List;

/** Interface for navigating a JSON object */
public interface JsonCursor<T> {

  // Definitions

  /** Mapper */
  public interface Mapper<S> {
    S call(JsonCursor in);
  }

  /** Iteration Handler */
  public interface Iterate<K, V> {
    void call(K key, V val);
  }

  // Tree

  /** Object */
  JsonCursor<T> object(String name) throws JsonException;

  /** Array */
  JsonCursor<T> array(String name) throws JsonException;

  /** End */
  JsonCursor<T> end();

  // Tests

  /** Return true if child object exists */
  boolean hasChild(String name);

  // Values

  /** String */
  String string(String name);

  /** String */
  String string(String name, String defaultValue);

  /** Number */
  Number number(String name);

  /** Number */
  Number number(String name, Number defaultValue);

  // Arrays

  /** First */
  JsonCursor<T> first();

  /** Iterator */
  JsonCursor<T> foreach(Iterate<String, T> f);

  /** Collect array into a list */
  JsonCursor<T> collect(String name, List l);

  /** Collect array into a list with map */
  <V> JsonCursor<T> map(String name, List<V> l, Mapper<V> tx);
}
