package org.hum.resthttp.test;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

	public String sayHello(String name) {
		return "[HelloService] hello " + name;
	}
}
