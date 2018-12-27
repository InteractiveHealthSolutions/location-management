package org.ird.unfepi.model.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public interface DAO {
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();
	
	Criteria buildCriteria(boolean isreadonly, String[] mappingsToJoin); 

	Object buildResult(Criteria cri); 

	List<?> buildResultList(Criteria cri, Order... orders); 

	List<?> buildResultList(Criteria cri, String... orders);

	List<Object> executeSQL(String sql);
	
	Serializable save(Object objectinstance);

	void delete(Object objectinstance);

	Object merge(Object objectinstance);

	void update(Object objectinstance);
	
	void saveOrUpdate(Object objectinstance);

}
