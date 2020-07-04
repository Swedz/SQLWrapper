package net.swedz.sql;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLWrapper extends SQLWrapper {
	private final boolean persistent;
	
	private MySQLPersistenceEnforcer persistenceEnforcer;
	
	public MySQLWrapper(String ip, int port, String database, String username, String password, boolean persistent) {
		super(ip, port, database, username, password);
		this.persistent = persistent;
	}
	
	@Override
	public MySQLWrapper connect() {
		try {
			MysqlDataSource source = new MysqlDataSource();
			source.setDatabaseName(database);
			source.setUser(username);
			source.setPassword(password);
			source.setServerName(ip);
			source.setPort(port);
			source.setUseSSL(false);
			source.setAllowPublicKeyRetrieval(true);
			source.setAutoReconnect(true);
			source.setAllowMultiQueries(true);
			conn = source.getConnection();
			if(persistent) {
				persistenceEnforcer = new MySQLPersistenceEnforcer(this);
				persistenceEnforcer.start();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this;
	}
	
	@Override
	protected ResultSet execute(QueryBuilder query) throws SQLException {
		persistenceEnforcer.setLastQueryTime();
		return super.execute(query);
	}
}
