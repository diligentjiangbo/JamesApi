package bo.jiang;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author shumpert.jiang
 */
public class TcpDecoder extends LengthFieldBasedFrameDecoder {
  public TcpDecoder(int maxLength) {
    super(maxLength, 0, 4, 0, 4);
  }

}
