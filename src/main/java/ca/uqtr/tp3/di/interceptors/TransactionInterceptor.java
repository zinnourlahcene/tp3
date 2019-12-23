package ca.uqtr.tp3.di.interceptors;

import java.sql.Connection;
import java.sql.Savepoint;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

public class TransactionInterceptor implements MethodInterceptor {
	private static int transactions = 0;
	@Inject
	private Connection conn;
	@Inject
	private Logger logger;

	public TransactionInterceptor() {
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("Disabling connection autoCommit!");
		conn.setAutoCommit(false);
		transactions++;

		String name = invocation.getMethod().getName();
		Savepoint savePoint = conn.setSavepoint(name);
		logger.info("Transaction set savepoint to name: {}", savePoint.getSavepointName());
		Object result;

		try {
			logger.info("Transaction started for savepoint name: {}!",  savePoint.getSavepointName());
			result = invocation.proceed();
			logger.info("Transaction commit for savepoint name: {}!", savePoint.getSavepointName());
			conn.commit();
			transactions--;
			return result;
		} catch (Throwable e) {
			logger.error("Transaction failed for savePoint name: {}! Rollback!", savePoint.getSavepointName());
			logger.catching(e);
			conn.rollback(savePoint);
			transactions--;
			throw e;
		} finally {
			if (transactions == 0) {
				logger.info("All transactions done!");
				conn.setAutoCommit(true);
				logger.info("Re-enabling connection autoCommit!");
				if (!conn.isClosed()) {
					logger.info("Clossing connection!", conn);
					conn.close();
				}
			}
		}
	}

}
