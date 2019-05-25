package org.eproject.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.File;
import java.util.logging.Logger;

public class GeoServer {
  private static final Logger logger = Logger.getLogger(GeoServer.class.getName());

  private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private EventLoopGroup workerGroup = new NioEventLoopGroup();
  private ChannelFuture channelFuture;

  /**
   * Starts the server
   *
   * @param port Port to bind the server
   */
  public void start(int port) throws Exception {
    start(port, 120, null, null);
  }
  /**
   * Starts the server
   * 
   * @param port Port to bind the server
   * @param timeout Idle timeout time in seconds
   * @param sslCertFile Path to SSL certificate in PEM format
   * @param sslKeyFile Path to SSL key certificate in PEM format
   */
  public void start(int port, int timeout, String sslCertFile, String sslKeyFile) throws Exception {

    SslContext sslContext = null;
    // Enable SSL when root certificate is set
    if (sslCertFile != null && sslKeyFile != null) {
      logger.info("Initialising SSL...");
      SelfSignedCertificate ssc = new SelfSignedCertificate();
      sslContext = SslContextBuilder.forServer(new File(sslCertFile), new File(sslKeyFile))
              .build();
    }

    ServerBootstrap boot = new ServerBootstrap();
    boot.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .handler(new LoggingHandler())
      .childHandler(new GeoChannelInitializer(sslContext, timeout))
      .option(ChannelOption.SO_BACKLOG, 128)
      .childOption(ChannelOption.SO_KEEPALIVE, true);

    channelFuture = boot.bind(port).sync();

    logger.info(String.format("Server is running on port %d...", port));
  }

  /**
   * Stops the server
   */
  public void stop() {
    logger.info("Shutting down the server...");
    try {
      bossGroup.shutdownGracefully().sync();
      workerGroup.shutdownGracefully().sync();
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws Exception {
    GeoServer server = new GeoServer();
    server.start(
            Integer.parseInt(System.getProperty("PORT", "5432")),
            Integer.parseInt(System.getProperty("TIMEOUT", "120")),
            System.getProperty("SSL_CERT", null),
            System.getProperty("SSL_KEY", null)
    );
  }
}

class GeoChannelInitializer extends ChannelInitializer<SocketChannel> {
  private SslContext sslContext;
  private int timeout;

  GeoChannelInitializer(SslContext sslContext, int timeout) {
    this.sslContext = sslContext;
    this.timeout = timeout;
  }

  @Override
  public void initChannel(SocketChannel ch) {
    if (sslContext != null) {
      ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
    }
    ch.pipeline().addLast(new IdleStateHandler(0, 0, timeout));
    ch.pipeline().addLast(new org.eproject.server.MessageDecoder());
    ch.pipeline().addLast(new org.eproject.server.MessageEncoder());
    ch.pipeline().addLast(new org.eproject.server.GeoClientHandler());
  }
}
