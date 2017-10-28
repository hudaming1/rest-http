package org.hum.resthttp.transport.netty;

import java.util.concurrent.Future;

import org.hum.resthttp.common.RestfulException;
import org.hum.resthttp.invoker.bean.Invocation;
import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.holder.InvokerHolder;
import org.hum.resthttp.serialization.Serialization;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class NettyServerHandler extends SimpleChannelInboundHandler<DefaultHttpRequest> {

	private InvokerHolder invokerHolder;
	private Serialization serialization;

	public NettyServerHandler(InvokerHolder invokerHolder, Serialization serialization) {
		this.invokerHolder = invokerHolder;
		this.serialization = serialization;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest request) throws Exception {

		/*
		 * 1.创建Invocation
		 *  TODO 这里设计的不好，如果method不符合定义要求，仍然会解析参数，发生调用，最好在这层能拦住
		 */
		Invocation invocation = new Invocation(request.method().name(), NettyHttpUtils.getUrl(request), NettyHttpUtils.requestParams2Map(request));
		
		// 2.非阻塞调用
		Future<Result> result = invokerHolder.invoke(invocation);

		// 3.返回结果
		ctx.writeAndFlush(wrapResult(result.get())).addListener(ChannelFutureListener.CLOSE);
	}

	private HttpResponse wrapResult(Result result) {
		String jsonString = serialization.serialize(result.getData());
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonString.getBytes()));
		return response;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 这里可以考虑如何打出异常堆栈（堆栈输出依赖Stream，但NIO中却没有Stream的概念，这该如何输出）
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
		
		// 如果抛出的是restful异常，则遵循HTTP错误码标准输出
		if (cause instanceof RestfulException) {
			response.setStatus(NettyHttpUtils.parseStatus(((RestfulException) cause).getCode()));
		}
		ctx.fireExceptionCaught(cause);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
}
