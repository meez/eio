package meez.eio.java.xml.impl;

import meez.eio.java.core.WriteStreamOutputStream;
import meez.eio.java.xml.XmlException;
import org.vertx.java.core.streams.WriteStream;

import java.io.IOException;

/** XmlBuilderStream */
public class XmlBuilderStream extends BaseXmlBuilderStream<WriteStream> {

  // Public methods

  /** Create new XmlBuilderStream */
  public XmlBuilderStream(WriteStream out) {
    super(out);
  }

  // XmlBuilder implementation

  /** Return output */
  public WriteStream toOutput() throws XmlException {
    try {
      WriteStreamOutputStream wout = (WriteStreamOutputStream) this.out;

      return wout.getOutput();
    } catch (IOException e) {
      throw new XmlException("Unable to extract stream", e);
    }
  }
}
