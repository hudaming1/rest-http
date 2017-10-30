package org.hum.resthttp.test;

import javax.annotation.Resource;

import org.hum.resthttp.mapper.annotation.Get;
import org.hum.resthttp.mapper.annotation.Post;

public class HelloResource {
	
	@Resource
	private HelloService helloService;

	@Get(url = "/hello/sayhello")
	public String sayHello(String name) {
		if (helloService != null) {
			return helloService.sayHello(name);
		}
		return "hello" + name;
	}
	
	@Post(url = "/hello/sayhi")
	public String sayHi(String name) {
		return "hi " + name + "!";
	}
}
