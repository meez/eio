package meez.eio.java.xml.impl;

import meez.eio.java.core.WriteStreamOutputStream;
import meez.eio.java.xml.XmlBuilder;
import meez.eio.java.xml.XmlException;
import org.vertx.java.core.streams.WriteStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

/** Base class for XmlBuilderStream */
public abstract class BaseXmlBuilderStream<T> implements XmlBuilder<T> {
  //
  // Definitions
  //

  /** TagBuilder */
  public class BaseTagBuilderStream<S> implements TagBuilder<S> {
    // Instance variables

    /** Parent stream */
    private BaseXmlBuilderStream<S> parent;

    // Public methods

    /** Create new BaseTagBuilderStream */
    public BaseTagBuilderStream(BaseXmlBuilderStream<S> parent) {
      this.parent = parent;
    }

    //
    // TagBuilder implementation
    //

    /** Write string attribute */
    public BaseTagBuilderStream<S> attribute(String name, String value) {
      try {
        if (value != null)
          parent.xml.writeAttribute(name, value);

        return this;
      } catch (XMLStreamException e) {
        throw new XmlException("Unable to encode", e);
      }
    }

    /** Close tag */
    public BaseXmlBuilderStream<S> close() throws XmlException {
      return this.parent;
    }
  }

  //
  // Instance variables
  //

  /** Lib */
  private static XMLOutputFactory xmlFactory = XMLOutputFactory.newFactory();

  /** Output */
  protected OutputStream out;

  /** TypedList */
  protected XMLStreamWriter xml;

  //
  // Public methods
  //

  /** Create new BaseXmlBuilderStream */
  public BaseXmlBuilderStream(OutputStream out) {
    this.out = out;

    try {
      this.xml = xmlFactory.createXMLStreamWriter(this.out, "utf-8");
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to create writer", e);
    }
  }

  /** Create new BaseXmlBuilderStream */
  public BaseXmlBuilderStream(WriteStream out) {
    this(new WriteStreamOutputStream(out));
  }

  //
  // XmlBuilder implementation
  //

  /** Return stream */
  public abstract T toOutput();

  /** Write string element */
  public XmlBuilder<T> element(String name, String value) throws XmlException {
    try {
      xml.writeStartElement(name);
      xml.writeCharacters(value);
      xml.writeEndElement();

      return this;
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to render element", e);
    }
  }

  /** Open tag */
  public TagBuilder<T> tag(String name) throws XmlException {
    try {
      xml.writeStartElement(name);
      return new BaseTagBuilderStream<>(this);
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to render element", e);
    }
  }

  /** Open element */
  public XmlBuilder<T> element(String name) throws XmlException {
    try {
      xml.writeStartElement(name);

      return this;
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to render element", e);
    }
  }

  /** Text */
  public XmlBuilder<T> text(String value) throws XmlException {
    try {
      if (value != null)
        xml.writeCharacters(value);

      return this;
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to render element", e);
    }
  }

  /** Render object */
  public <V> XmlBuilder<T> object(String name, Encoder<XmlBuilder> encoder, V value) {
    throw new RuntimeException("Not implemented");
  }

  /** Finish element */
  public XmlBuilder<T> end() throws XmlException {
    try {
      xml.writeEndElement();

      return this;
    } catch (XMLStreamException e) {
      throw new XmlException("Unable to render element", e);
    }
  }
}
