package bo.jiang;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.SynchronousQueue;


/**
 * @author shumpert.jiang
 */
public class JamesClient {
  private static Logger logger = LogManager.getLogger(JamesClient.class);

  private static final Gson gson = new Gson();

  /**
   * 连接消息处理端
   * @param ip
   * @param port
   */
  public void connect(String ip, int port) {
    TcpClient.getInstance().init(ip, port);
  }

  /**
   * 发送服务请求
   * @param message
   * @return
   */
  public Message sendRequest(Message message) throws InterruptedException {
    String uuid = UUID.randomUUID().toString();
    message.getHeader().setUuid(uuid);

    String str = gson.toJson(message);
    ByteBuf byteBuf = Unpooled.buffer();
    byteBuf.writeByte((byte)MsgType.REQUEST.getIndex());
    byteBuf.writeBytes(str.getBytes());
    TcpClient.getInstance().send(byteBuf);

    SynchronousQueue<Message> syncQueue = new SynchronousQueue<Message>();
    SyncContext context = SyncContext.getInstance();
    context.put(uuid, syncQueue);
    Message returnMsg = syncQueue.take();
    context.remove(uuid);
    return returnMsg;
  }

  /**
   * 发送服务请求
   * @param message
   * @return
   */
  public void sendReply(Message message) throws InterruptedException {
    String str = gson.toJson(message);
    ByteBuf byteBuf = Unpooled.buffer();
    byteBuf.writeByte((byte)MsgType.RESPONSE.getIndex());
    byteBuf.writeBytes(str.getBytes());
    TcpClient.getInstance().send(byteBuf);
  }

  /**
   * 注册服务
   * @param service
   * @return
   */
  public boolean registerService(Service service) throws InterruptedException {
    String uuid = UUID.randomUUID().toString();
    Message message = new Message();
    message.getHeader().setServiceId(service.getServiceId());
    message.getHeader().setUuid(uuid);

    String str = gson.toJson(message);
    ByteBuf byteBuf = Unpooled.buffer();
    byteBuf.writeByte((byte)MsgType.SERVICE.getIndex());
    byteBuf.writeBytes(str.getBytes());
    TcpClient.getInstance().send(byteBuf);

    SynchronousQueue<Message> syncQueue = new SynchronousQueue<Message>();
    SyncContext context = SyncContext.getInstance();
    context.put(uuid, syncQueue);
    Message returnMsg = syncQueue.take();
    context.remove(uuid);
    if (returnMsg.getHeader().getReturnCode() == ReturnCode.SUCCESS) {
      ServiceContext.getInstance().put(service.getServiceId(), service.getClazz());
      return true;
    } else {
      return false;
    }
  }


}
