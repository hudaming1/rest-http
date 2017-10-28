### 基于Netty框架实现的一个Restful框架 <br />
   1.通信层使用Netty <br />
   2.序列化使用FastJson <br />
   3.示例化Resource使用项目中的AnnotationMapper <br />

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
