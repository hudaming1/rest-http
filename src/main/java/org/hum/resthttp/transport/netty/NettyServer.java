package org.hum.resthttp.transport.netty;

import org.hum.resthttp.common.ServerException;
import org.hum.resthttp.transport.AbstractServer;
import org.hum.resthttp.transport.config.ServerConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyServer extends AbstractServer {
	
	final NettyServer nettyServer = this;

	@Override
	public void doOpen(ServerConfig serverConfig) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootStrap = new ServerBootstrap();
			bootStrap.group(bossGroup, workerGroup);
			bootStrap.channel(NioServerSocketChannel.class);
			bootStrap.childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new HttpResponseEncoder());
					ch.pipeline().addLast(new HttpRequestDecoder());
					ch.pipeline().addLast(new NettyServerHandler(nettyServer.invokerHolder, nettyServer.serialization));
				}
			});
			ChannelFuture future = bootStrap.bind(serverConfig.getPort()).sync();
			System.out.println("server listener on " + serverConfig.getPort());
			future.channel().closeFuture().sync();
		} catch (Exception ce) {
			throw new ServerException("netty server start occured exception", ce);
		} finally {
			// TODO 这里的异常应该怎么处理好？
			try {
				bossGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				workerGroup.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() {
		
	}
}
