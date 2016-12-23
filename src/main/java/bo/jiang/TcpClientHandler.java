package bo.jiang;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.SynchronousQueue;


/**
 * @author shumpert.jiang
 */
public class TcpClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private static Logger logger = LogManager.getLogger(TcpClientHandler.class);
  private static Gson gson = new Gson();

  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    MsgType msgType = MsgType.getByIndex(msg.readByte());

    //获取消息
    byte[] bytes = new byte[msg.readableBytes()];
    msg.readBytes(bytes);
    String str = new String(bytes);
    Message message = gson.fromJson(str, Message.class);

    switch (msgType) {
      case REQUEST:
        logger.info("收到请求消息，消息id为{}", message.getHeader().getUuid());
        String serviceId = message.getHeader().getServiceId();
        ServiceContext serviceContext = ServiceContext.getInstance();
        Class<?> clazz = serviceContext.get(serviceId);
        if (clazz == null) {
          logger.error("这里没有这个服务{}", serviceId);
          throw new Exception("没有服务");
        }
        IService iService = (IService)clazz.newInstance();
        Message responseMsg = iService.execute(message);
        new JamesClient().sendReply(responseMsg);
        break;
      case RESPONSE:
        logger.info("收到返回消息，消息id为{}", message.getHeader().getUuid());
        SyncContext context = SyncContext.getInstance();
        SynchronousQueue<Message> syncQueue = context.get(message.getHeader().getUuid());
        syncQueue.offer(message);
        break;
      default:
        break;
    }


  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }
}
