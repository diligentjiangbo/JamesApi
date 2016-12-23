package bo.jiang;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @author shumpert.jiang
 */
public class TcpEncoder extends MessageToByteEncoder<ByteBuf>{

  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    out.writeInt(msg.readableBytes());
    out.writeBytes(msg);
  }

}
