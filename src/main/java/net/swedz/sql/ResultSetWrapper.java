package net.swedz.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetWrapper {
	private final Statement statement;
	private final ResultSet resultSet;
	
	ResultSetWrapper(Statement statement, ResultSet resultSet) {
		this.statement = statement;
		this.resultSet = resultSet;
	}
	
	public Statement getStatement() {
		return statement;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public void ifRowExists(SQLConsumer<ResultSet> valid, Runnable invalid) {
		try {
			if(resultSet.next()) {
				if(valid != null)
					valid.accept(resultSet);
			}
			else if(invalid != null)
				invalid.run();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void forEachRow(SQLConsumer<ResultSet> consumer, Runnable finish) {
		try {
			while(resultSet.next()) {
				if(consumer != null)
					consumer.accept(resultSet);
			}
			if(finish != null)
				finish.run();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
