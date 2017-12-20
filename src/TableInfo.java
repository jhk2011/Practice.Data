import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TableInfo<T> {
	
	Class<T> cls;
	
	List<String> selects;
	List<String> keys;
	/**
	 * @param cls
	 */
	public TableInfo(Class<T> cls) {
		super();
		this.cls = cls;
		this.name = cls.getName();
		this.columns = new ArrayList<ColumnInfo>();
		
		for (Field field : cls.getDeclaredFields()) {
			ColumnInfo column = new ColumnInfo(field);
			columns.add(column);
		}
		keys = new ArrayList<String>();
		selects = new ArrayList<String>();
		for(ColumnInfo column:columns) {
			if(column.key) {
				keys.add(column.name);
			}
			selects.add(column.name);
		}
	}

	String name;
	
	public String getName() {
		return name;
	}

	public List<ColumnInfo> getColumns() {
		return columns;
	}

	List<ColumnInfo> columns;
}
