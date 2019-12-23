package ca.uqtr.tp3.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	/**
	 * Get a connection (session) with a specific database. 
	 * from a default config file postgres.properties available in the classpath.
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public static Connection getConnection() throws IOException, SQLException {
		Properties connProps = new Properties();
		InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("config/postgres.properties");
		connProps.load(in);
		in.close();
		
		return DriverManager.getConnection(connProps.getProperty("url"), connProps);
	}
	
	/**
	 * Get a connection (session) with a specific database. 
	 * @param url the jdbc url to the database
	 * @param user the username credential
	 * @param password the passwort credential
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String url, String user, String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
