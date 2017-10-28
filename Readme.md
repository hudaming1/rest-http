### 基于Netty框架实现的一个Restful框架 <br />
   1.通信层使用Netty <br />
   2.序列化使用FastJson <br />
   3.示例化Resource使用项目中的AnnotationMapper <br />

### 框架介绍 <br />

框架设计初衷是为了学习Netty而开始，开发过程中也是在不断的参照SpringMVC所提供的功能编写代码。与SpringMVC不同的是，SpringMVC基于Servlet容器实现的Http协议，Spring更侧重于通信上层的实现。

rest-http是个人练习作品，自己内置简单的Http容器（不依赖Servlet容器）。Http在实现上分为Netty实现和Jdk自带的Tcp实现。

当然，该框架无法投入生产环境，但作为学习入门是一个不错的Demo，如果发现问题非常希望大家能够指出不足，一起讨论。

### Quick Start: <br />
   1.Run: org.hum.resthttp.test.Demo.java <br />
   2.access url: http://localhost:9080/hello/sayhello?name=world <br />

### How to export <br />
	@Get(url = "/hello/sayhello")
	public String sayHello(String name) {
	   return "hello" + name;
	}
	
	@Post(url = "/hello/sayhi")
	public String sayHi(String name) {
	   return "hi " + name + "!";
	}
