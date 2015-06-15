package meez.eio.java.xml.impl;

import meez.eio.java.xml.XmlBuilder;

/** XmlSupport */
public class XmlSupport {

  /** String builder */
  public static XmlBuilder<String> string() {
    return new XmlBuilderString();
  }
}
