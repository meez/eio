package meez.eio.java.core;

import junit.framework.TestCase;
import org.junit.Test;
import org.vertx.java.core.streams.WriteStream;

import java.io.IOException;

import static org.mockito.Mockito.*;

/** Unit-test for WriteOutputStream */
public class TestWriteStreamOutputStream extends TestCase {

  /** Test Encode */
  @Test
  public void testEncode() throws IOException {
    WriteStream target=mock(WriteStream.class);

    WriteStreamOutputStream out=new WriteStreamOutputStream(target);

    byte[] data=new byte[] { (byte)0xff, 0x0, 0xf, 0x1 };

    out.write(0xff);
    out.write(0x0);
    out.write(0xf);
    out.write(0x1);

    out.write(data);

    out.write(data,2,2);
    out.write(data,0,2);

    out.getOutput();
  }
}