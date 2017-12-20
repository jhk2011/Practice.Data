import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
	Connection connection;

	/**
	 * @param connection
	 */
	public Transaction(Connection connection,int level) {
		super();
		this.connection = connection;
		try {
			this.connection.setAutoCommit(false);
			if(level!=Connection.TRANSACTION_NONE) {
				this.connection.setTransactionIsolation(level);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void commit() {
		try {
			this.connection.commit();
		} catch (SQLException e) {
			
		}finally {
			try {
				this.connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void rollback() {
		try {
			this.connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
			try {
				this.connection.setAutoCommit(true);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
