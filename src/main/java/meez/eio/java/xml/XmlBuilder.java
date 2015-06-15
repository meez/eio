package meez.eio.java.xml;

/** Interface for building XML objects, similar to JsonBuilder */
public interface XmlBuilder<T> {

  /** Encode */
  public interface Encoder<T> {
    void call(XmlBuilder builder, T value);
  }

  /** Nested Tag Builder */
  public interface TagBuilder<T> {
    /** Attribute */
    TagBuilder<T> attribute(String name, String value);

    /** Close current tag */
    XmlBuilder<T> close() throws XmlException;
  }

  /** Return output */
  T toOutput() throws XmlException;

  /** Simple */
  XmlBuilder<T> element(String name, String value) throws XmlException;

  /** Start tag */
  TagBuilder<T> tag(String name) throws XmlException;

  /** Start element */
  XmlBuilder<T> element(String name) throws XmlException;

  /** Text */
  XmlBuilder<T> text(String value) throws XmlException;

  /** Nested encoder */
  <V> XmlBuilder<T> object(String name, Encoder<XmlBuilder> encoder, V value);

  /** End current element */
  XmlBuilder<T> end() throws XmlException;
}
