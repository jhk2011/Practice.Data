import java.lang.reflect.Field;

public class ColumnInfo {
	String name;
	Field field;
	boolean key;
	/**
	 * @param field
	 */
	public ColumnInfo(Field field) {
		super();
		this.field = field;
		this.name = field.getName();
		Key key=field.getAnnotation(Key.class);
		this.key = key!=null;
	}
	
	public Object getValue(Object entity) {
		try {
			return this.field.get(entity);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

    public void setValue(Object entity, Object value) {
		try {
			this.field.set(entity,value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
