package net.swedz.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public abstract class SQLWrapper {
	protected final String ip, database, username, password;
	protected final int    port;
	
	protected Connection conn;
	
	public SQLWrapper(String ip, int port, String database, String username, String password) {
		this.ip = ip;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}
	
	public abstract SQLWrapper connect();
	
	public boolean disconnect() {
		if(!this.isConnected())
			throw new IllegalStateException("Cannot close non-connected sql instance");
		try {
			conn.close();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public boolean isConnected() {
		try {
			return conn != null && !conn.isClosed();
		} catch (SQLException ex) {
			return false;
		}
	}
	
	public QueryBuilder queryBuilder(String...queries) {
		return new QueryBuilder(this, queries);
	}
	
	protected ResultSet execute(QueryBuilder query) throws SQLException {
		ResultSet resultSet = null;
		PreparedStatement statement = query.getStatement();
		if(query.isUpdate())
			statement.executeUpdate();
		else
			resultSet = statement.executeQuery();
		return resultSet;
	}
	
	ResultSet query(QueryBuilder query) {
		if(query.isAsync())
			throw new IllegalArgumentException("Cannot supply async query for sync query method");
		if(!this.isConnected())
			throw new IllegalStateException("Cannot make call to non-connected sql instance");
		try {
			return this.execute(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	void query(QueryBuilder query, SQLConsumer<ResultSet> result) {
		Runnable runnable = () -> {
			try {
				ResultSet resultSet = this.execute(query);
				if(result != null)
					result.accept(resultSet);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		};
		if(query.isAsync())
			CompletableFuture.runAsync(runnable);
		else
			runnable.run();
	}
}
