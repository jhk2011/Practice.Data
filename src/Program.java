import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Program {

	static class MyDatabase extends Database {

		public MyDatabase(String connectionString) {
			super(connectionString);
			units = getTable("TBUnit", TBUnit.class);
		}

		Table<TBUnit> units;

		public Table<TBUnit> getUnits() {
			return units;
		}

	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		String sDriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

		Class.forName(sDriverName);

		TableInfo<TBUnit> table = new TableInfo<TBUnit>(TBUnit.class);

		System.out.println(table.name);
		
		for (ColumnInfo column : table.getColumns()) {
			System.out.println(column.name + ":" + column.key);
		}


		test2();

		//test();
	}

	private static void test2() {
		String s = "jdbc:sqlserver://localhost:1433;" 
				+"databaseName=WMS_Developer;" 
				+ "user=sa;"
				+ "password=123456";

		MyDatabase db = new MyDatabase(s);

        List<Row> rows = db.getList("select 1 as a,2 as b union all select ?,?", "3", "4");
		
		rows=db.getList("use master;select name from sys.databases;select 1;");
		rows=db.getList("sp_helpdb");
		
		System.out.println(rows);
        System.out.println(rows.size());
		
		if(true)return;
	
		Table<TBUnit> tbUnit = db.getUnits();

		TBUnit unit = new TBUnit();
		
		
		Transaction trans = db.beginTransaction();
		
		unit.setUnitCode("000");
		unit.setUnitName("test");

		
		tbUnit.add(unit);
		
		trans.rollback();
		
		db.beginTransaction(Connection.TRANSACTION_SERIALIZABLE);
		

		List<TBUnit> units = tbUnit.getList("UnitName like ?", "%M%");

		for (TBUnit unit1 : units) {
			System.out.println(unit1.toString());
		}
		
		tbUnit.delete("000");
	}

	private static void test() throws SQLException {
		String s = "jdbc:sqlserver://localhost:1433;" 
				+ "databaseName=WMS_Developer;" 
				+ "user=sa;"
				+ "password=1234567";

		Connection conn = DriverManager.getConnection(s);

		PreparedStatement stmt = conn.prepareStatement("select top 5 * from TbProduct");
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("Name"));
		}
	}

}
