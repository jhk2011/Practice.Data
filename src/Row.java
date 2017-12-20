import java.util.HashMap;
import java.util.Map.Entry;

public class Row {
	
	HashMap<String,Object> map = new HashMap<String,Object> ();

	public boolean contains(String name) {
		return map.containsKey(name);
	}
	
	public Object get(String name) {
		Object value=map.get(name);
		return value;
	}
	
	public void set(String name,Object value) {
		map.put(name, value);
	}

	@Override
	public String toString() {
		String s="{";
		for(Entry<String, Object> entry:map.entrySet()) {
			s+="\""+entry.getKey()+"\"";
			s+="=";
			s+="\""+entry.getValue()+"\"";
			s+=",";
		}
		if(s.endsWith(",")) {
			s=s.substring(0,s.length()-1);
		}
		s+="}";
		return s;
	}
}
