package net.swedz.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryBuilder {
	private final String query;
	
	private SQLWrapper        sql;
	private PreparedStatement statement;
	
	private boolean update, async;
	
	QueryBuilder(SQLWrapper sql, String...queries) {
		this.sql = sql;
		this.query = String.join("; ", queries)
				.replace(";;", "; ")
				.replace("  ", " ")
				.trim();
		try {
			this.statement = sql.getConnection().prepareStatement(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public QueryBuilder withParams(SQLConsumer<PreparedStatement> consumer) {
		try {
			consumer.accept(statement);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return this;
	}
	
	public QueryBuilder update(boolean update) {
		this.update = update;
		return this;
	}
	
	public QueryBuilder async(boolean async) {
		this.async = async;
		return this;
	}
	
	String getRawQuery() {
		return query;
	}
	
	SQLWrapper getSQL() {
		return sql;
	}
	
	PreparedStatement getStatement() {
		return statement;
	}
	
	public boolean isUpdate() {
		return update;
	}
	
	public boolean isAsync() {
		return async;
	}
	
	public void query(SQLConsumer<ResultSetWrapper> consumer) {
		sql.query(this, (r) -> {
			if(consumer != null)
				consumer.accept(new ResultSetWrapper(r));
		});
	}
	
	public ResultSetWrapper query() {
		if(async) {
			sql.query(this, null);
			return null;
		}
		return new ResultSetWrapper(sql.query(this));
	}
}
