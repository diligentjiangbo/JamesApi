package bo.jiang;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author shumpert.jiang
 */
public class TcpClient {
  private static Logger logger = LogManager.getLogger(TcpClient.class);

  private Channel channel;

  private static TcpClient tcpClient;

  private TcpClient() {}

  public static TcpClient getInstance()  {
    if (tcpClient == null) {
      synchronized (TcpClient.class) {
        if (tcpClient == null) {
          tcpClient = new TcpClient();
        }
      }
    }
    return tcpClient;
  }

  public void init(String ip, int port) {
    EventLoopGroup group = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    try {
      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline()
                  .addLast(new TcpDecoder(65535))
                  .addLast(new TcpEncoder())
                  .addLast(new TcpClientHandler());
            }
          });

      channel = bootstrap.connect(ip, port).sync().channel();
      logger.info("连接成功");
    } catch (InterruptedException e) {
      logger.error("中断", e);
    }
  }

  public void send(ByteBuf byteBuf) {
    channel.writeAndFlush(byteBuf);
  }

  public void close() {
    channel.close();

  }
}
