import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

	String connectionString;

	public Database(String connectionString) {
		super();
		this.connectionString = connectionString;
	}

	Connection connection;
	
	public Connection getConnection() {
		if(connection==null) {
			try {
				connection=DriverManager.getConnection(this.connectionString);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return connection;
	}
	
	public void close() {
		if(connection!=null) {
			try {
				connection.close();
				connection=null;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	Transaction transaction;
	
	public Transaction beginTransaction() {
	    transaction = new Transaction(this.getConnection(),Connection.TRANSACTION_NONE);
		return transaction;
	}
	
	public Transaction beginTransaction(int level) {
	    transaction = new Transaction(this.getConnection(),level);
		return transaction;
	}
	
	public List<Row> getList(String sql,Object...params){

		try {
			System.out.println(sql);

			Connection conn = getConnection();

			PreparedStatement stmt = conn.prepareStatement(sql);

			int i = 1;

			for (Object param : params) {
				System.out.println(i + ":" + param);
				stmt.setObject(i, param);
				i++;
			}

			ResultSet rs = stmt.executeQuery();

			ArrayList<Row> rows = new ArrayList<Row>();

			while (rs.next()) {
				Row row = new Row();
				ResultSetMetaData metadata=rs.getMetaData();

				System.out.println("ColumnCount="+metadata.getColumnCount());
				
				for(int column=1;column<=metadata.getColumnCount();column++) {
					String name = metadata.getColumnName(column);
					Object value = rs.getObject(name);
					row.set(name, value);
				}
				rows.add(row);
			}
			System.out.println(rows.size()+" rows");
			return rows;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int execute(String sql, Object... params) {

		try {

			System.out.println(sql);

			Connection conn = getConnection();

			PreparedStatement stmt = conn.prepareStatement(sql);

			int i = 1;

			for (Object param : params) {
				System.out.println(i + "=" + param);
				stmt.setObject(i, param);
				i++;
			}
			int n= stmt.executeUpdate();
			
			System.out.println(n+" rows affected");
			
			return n;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected <T> Table<T> getTable(String name, Class<T> cls) {
		return new Table<T>(name, cls, this);
	}
}
