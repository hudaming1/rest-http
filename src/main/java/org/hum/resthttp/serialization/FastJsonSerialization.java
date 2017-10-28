package org.hum.resthttp.serialization;

import com.alibaba.fastjson.JSON;

public class FastJsonSerialization implements Serialization {

	@Override
	public String serialize(Object object) {
		return JSON.toJSONString(object);
	}
}
