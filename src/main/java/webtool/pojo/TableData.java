package webtool.pojo;

import java.util.ArrayList;
import java.util.List;

public class TableData<T> {

	public List<T> data = new ArrayList<T>();
	
	public TableData(List<T> t) {
		data = t;
	}

	public TableData() {
		super();
	}
	
	
}
