package ca.uqtr.tp3.di;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import ca.uqtr.tp3.di.annotation.Transactional;
import ca.uqtr.tp3.di.interceptors.LogInterceptor;
import ca.uqtr.tp3.di.interceptors.TransactionInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import ca.uqtr.tp3.di.annotation.Log;
import ca.uqtr.tp3.persistence.ConnectionFactory;
import ca.uqtr.tp3.persistence.PersistenceManager;
import ca.uqtr.tp3.persistence.interfaces.EntityManager;

public class PersistenceModule extends AbstractModule{

	public PersistenceModule() {}

	@Override
	protected void configure() {
		bind(EntityManager.class).to(PersistenceManager.class);
		
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		requestInjection(transactionInterceptor);
		LogInterceptor logInterceptor = new LogInterceptor();
		requestInjection(logInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Log.class), logInterceptor);
	}
	
	@Provides
	Connection provideConnection() throws IOException, SQLException {
		return ConnectionFactory.getConnection();
	}
	
	@Provides
	@Singleton
	Logger provideLogger() {
		return LogManager.getRootLogger();
	}
}
