package org.ird.unfepi.model.dao;

import java.io.Serializable;
import java.util.List;

public interface DAO {
	
	List<Object> executeSQL(String sql);
	
	Serializable save(Object objectinstance);

	void delete(Object objectinstance);

	Object merge(Object objectinstance);

	void update(Object objectinstance);
	
	void saveOrUpdate(Object objectinstance);
}
