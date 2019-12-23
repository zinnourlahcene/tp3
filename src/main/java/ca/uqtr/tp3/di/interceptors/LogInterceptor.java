package ca.uqtr.tp3.di.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

public class LogInterceptor implements MethodInterceptor {
	@Inject Logger logger;
	
	public LogInterceptor() {}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("In {}", invocation.getMethod().getName());
		Object result;
		try {
			result = invocation.proceed();
			logger.info("Out {} with result {}", invocation.getMethod().getName(), result);
		} catch (Throwable e) {
			logger.catching(e);
			throw e;
		}
		
		return result;
	}
}
