package meez.eio.java.core;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.streams.WriteStream;

import java.io.IOException;
import java.io.OutputStream;

/** Map OutputStream to WriteStream */
public class WriteStreamOutputStream extends OutputStream {

  //
  // Instance variables
  //

  /** Output */
  private final WriteStream out;

  /** Buffer */
  private Buffer buf;

  //
  // Public methods
  //

  /** Create new WriteStreamOutputStream */
  public WriteStreamOutputStream(WriteStream out) {

    this.out = out;
    this.buf = null;
  }

  /** Return output */
  public WriteStream getOutput() throws IOException {

    // Auto-flush before returning
    flush();

    return this.out;
  }

  // OutputStream implementation

  /** Flush */
  @Override
  public void flush() throws IOException {
    if (this.buf == null)
      return;

    this.out.write(this.buf);
    this.buf = null;
  }

  /** Close */
  @Override
  public void close() throws IOException {
    flush();
  }

  /** Write single byte */
  @Override
  public void write(int b) throws IOException {
    ensureBuffer();

    this.buf.appendByte((byte) b);
  }

  /** Write array chunk */
  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    ensureBuffer();

    this.buf.appendBytes(b, off, len);
  }

  // Implementation

  /** Allocate buffer on demand */
  protected Buffer ensureBuffer() {
    if (this.buf == null)
      this.buf = new Buffer();

    return this.buf;
  }
}
