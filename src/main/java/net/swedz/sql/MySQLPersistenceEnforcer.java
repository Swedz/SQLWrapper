package net.swedz.sql;

public class MySQLPersistenceEnforcer extends Thread {
	private static final long MAX_IDLE_TIME = 60 * 60 * 1000L;
	
	private final MySQLWrapper sql;
	
	private long lastQueryTime;
	
	public MySQLPersistenceEnforcer(MySQLWrapper sql) {
		super();
		this.sql = sql;
	}
	
	void setLastQueryTime() {
		lastQueryTime = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		while(!this.isInterrupted()) {
			try {
				if(System.currentTimeMillis() - lastQueryTime >= MAX_IDLE_TIME)
					sql.queryBuilder("select 1;").async(true).query();
				Thread.sleep(10 * 1000L);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}
