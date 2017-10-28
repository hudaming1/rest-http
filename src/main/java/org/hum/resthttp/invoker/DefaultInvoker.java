package org.hum.resthttp.invoker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hum.resthttp.invoker.bean.Result;
import org.hum.resthttp.invoker.enumtype.ResultCodeEum;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class DefaultInvoker extends AbstractInvoker {

	private final ClassPool CLASSPOOL = ClassPool.getDefault();
	
	public DefaultInvoker(Method method, Object instance) {
		super(method, instance);
	}

	@Override
	public Result invoke(Map<String, Object> parameters) {
		try {
			Object[] params = parseParamMap2ObjectArray(instance.getClass(), method.getName(), parameters);
			Object resultData = method.invoke(instance, params);
			return new Result(ResultCodeEum.SUCCESS, null, resultData);
		} catch (Exception ce) {
			return new Result(ResultCodeEum.ERROR, ce, null);
		}
	}

	private Object[] parseParamMap2ObjectArray(Class<?> instanceClass, String methodName, Map<String, Object> parameters) throws NotFoundException {
		List<Object> params = new ArrayList<>();

		CtClass ctClass = CLASSPOOL.get(instanceClass.getName());
		CtMethod cm = ctClass.getDeclaredMethod(methodName);
		MethodInfo methodInfo = cm.getMethodInfo();

		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

		/*
		 * JVM规范规定：非static方法，第一个隐式参数为this，第二个开始才是我们代码的参数
		 */
		int addLength = methodInfo.isStaticInitializer() ? 0 : 1;
		
		for (int i = addLength ; i < parameters.size() + addLength ;i ++ ) {
			String paramName = attr.variableName(i);
			params.add(parameters.get(paramName));
		}
		
		return params.toArray();
	}
}
