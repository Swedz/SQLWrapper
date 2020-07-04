package net.swedz.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetWrapper {
	private final ResultSet resultSet;
	
	ResultSetWrapper(ResultSet resultSet) {
		this.resultSet = resultSet;
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
			while(resultSet.next())
				if(consumer != null)
					consumer.accept(resultSet);
			if(finish != null)
				finish.run();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
