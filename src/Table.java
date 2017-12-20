import com.sun.deploy.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

public class Table<T> {
	String tableName;
	Class<T> cls;
	Database database;
	
	/**
	 * @param tableName
	 * @param cls
	 * @param database
	 */
	public Table(String tableName, Class<T> cls, Database database) {
		super();
		this.tableName = tableName;
		this.cls = cls;
		this.database = database;
		this.tableInfo = new TableInfo<T>(cls);
	}
	
	TableInfo<T> tableInfo;
	
	public List<T> getList() {
		return getList(null);
	}
	
	public List<T> getList(String condition,Object...params) {
		String sql = "select * from [" + tableName + "]";
		if(condition!=null && !condition.isEmpty()) {
			sql+=" where "+condition;
		}
		List<Row> rows= database.getList(sql,params);
        List<T> entities = new ArrayList<T>(rows.size());
        for(Row row:rows){
            entities.add(map(row));
        }
        return entities;
	}
	
	public int delete(Object... values) {
		String sql = "delete [" + tableName + "]"
					+" where " ;

        List<String> keys = tableInfo.keys;

		for(int i=0;i<keys.size();i++) {
			String key = keys.get(i);
			sql+= key+" = ?";
			if(i<keys.size()-1) {
				sql+=" and ";
			}
		}

        return database.execute(sql, values);
	}

    public int delete(T entity) {
        String sql = "delete [" + tableName + "]"
                +" where " ;

        List<String> keys = tableInfo.keys;

        for(int i=0;i<keys.size();i++) {
            String key = keys.get(i);
            sql+= key+" = ?";
            if(i<keys.size()-1) {
                sql+=" and ";
            }
        }

        Row row = map(entity);
        Object[] params = getValues(row,keys);

        return database.execute(sql, keys);
    }

    protected T map(Row row){
        T entity = null;
        try {
            entity = cls.newInstance();

            for(ColumnInfo column:tableInfo.getColumns()){
                Object value = row.get(column.name);
                column.setValue(entity,value);
            }
            return entity;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected Row map(T entity){
	    Row row = new Row();
        for(ColumnInfo column:tableInfo.getColumns()){
            Object value = column.getValue(entity);
            row.set(column.name,value);
        }
        return row;
    }

    protected Object[] getValues(Row row,List<String> names){
	    Object[] values = new Object[names.size()];
	    for (int i=0;i<names.size();i++){
	        values[i]=row.get(names.get(i));
        }
        return values;
    }

	public int add(T entity) {
		String sql = "insert into ["+tableName+"]";
		sql+="(";
		sql+=String.join(",", tableInfo.selects);
		sql+=")";
		sql+="values";
		sql+="(";

		List<String> columns = tableInfo.selects;

        for(int i=0;i<columns.size();i++) {
			sql+="?";
			if(i<columns.size()-1) {
				sql+=",";
			}
		}
		sql+=")";

        Row row = map(entity);

        Object[] params = getValues(row,columns);

		return database.execute(sql, params);
	}
	
	public int update(T entity) {
		String sql = "update ["+tableName+"]";
		sql+="(";
		sql+=String.join(",", tableInfo.selects);
		sql+=")";
		sql+="values";
		sql+="(";
		for(int i=0;i<tableInfo.selects.size();i++) {
			sql+="?";
			if(i<tableInfo.selects.size()-1) {
				sql+=",";
			}
		}
		sql+=")";
		
		Object[] params = new Object[tableInfo.selects.size()];
		
		for(int i=0;i<tableInfo.columns.size();i++) {
			params[i]=tableInfo.columns.get(i).getValue(entity);
		}
		
		return database.execute(sql, params);
	}
}
