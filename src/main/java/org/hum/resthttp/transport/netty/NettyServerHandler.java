package org.hum.resthttp.transport.netty;

import org.hum.resthttp.common.RestfulException;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.serialization.Serialization;
import org.hum.resthttp.transport.context.ServerContext;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class NettyServerHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {

	private ServerContext<HttpRequest> serverContext = new ServerContext<>();
	private NettyHandleCallback nettyCallable;
	private Serialization serialization;

	public NettyServerHandler(NettyHandleCallback nettyCallable, Serialization serialization) {
		this.serialization = serialization;
		this.nettyCallable = nettyCallable;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest request) throws Exception {
		
		// 0.初始化上下文
		serverContext.set(request);
		
		/*
		 * 1.创建Invocation
		 *  XXX bad smell: 这里设计的不好，如果method不符合定义要求，仍然会解析参数，发生调用，最好在这层能拦住(TcpServer也面临这个问题，该如何是好？)
		 */
		Invocation invocation = new Invocation(request.method().name(), NettyHttpUtils.getUrl(request), NettyHttpUtils.requestParams2Map(request));
		
		// 2.调用(这里交给NettySever来处理)
		Result result = nettyCallable.handler(invocation);
		
		// 3.返回结果
		ctx.writeAndFlush(wrapResult(result)).addListener(ChannelFutureListener.CLOSE);
	}

	private HttpResponse wrapResult(Result result) {
		String jsonString = serialization.serialize(result.getData());
		return new DefaultFullHttpResponse(serverContext.get().protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonString.getBytes()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		// TODO 1 这里设计的有问题，有时候接不住异常(DefaultInvokerHolder.vaildate没有catch住时问题可现)
		// TODO 2 这里可以考虑如何打出异常堆栈（堆栈输出依赖Stream，但NIO中却没有Stream的概念，这该如何输出）
		ByteBuf byteBuf = Unpooled.wrappedBuffer(cause.getMessage().getBytes());
		HttpResponse response = new DefaultFullHttpResponse(serverContext.get().protocolVersion(), HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf);
		
		// 如果抛出的是restful异常，则遵循HTTP错误码标准输出
		if (cause instanceof RestfulException) {
			response.setStatus(NettyHttpUtils.parseStatus(((RestfulException) cause).getHttpCode().getCode()));
		} 
		ctx.fireExceptionCaught(cause);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		serverContext.remove();
        ctx.fireChannelReadComplete();
    }
}
