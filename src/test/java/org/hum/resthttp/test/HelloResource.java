package org.hum.resthttp.test;

import org.hum.resthttp.mapper.annotation.Get;
import org.hum.resthttp.mapper.annotation.Post;

public class HelloResource {

	@Get(url = "/hello/sayhello")
	public String sayHello(String name) {
		return "hello" + name;
	}
	
	@Post(url = "/hello/sayhi")
	public String sayHi(String name) {
		return "hi " + name + "!";
	}
}
