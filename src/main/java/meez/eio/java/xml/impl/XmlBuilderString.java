package meez.eio.java.xml.impl;

import meez.eio.java.xml.XmlException;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/** XmlBuilder that generates a string */
public class XmlBuilderString extends BaseXmlBuilderStream<String> {
  //
  // Public methods
  //

  /** Create new XmlBuilderString */
  public XmlBuilderString() {
    super(new ByteArrayOutputStream());
  }

  /** Return output */
  public String toOutput() {
    ByteArrayOutputStream bout = (ByteArrayOutputStream) this.out;

    try {
      return new String(bout.toByteArray(), "utf8");
    } catch (UnsupportedEncodingException e) {
      throw new XmlException("Unable to decode XML");
    }
  }
}
