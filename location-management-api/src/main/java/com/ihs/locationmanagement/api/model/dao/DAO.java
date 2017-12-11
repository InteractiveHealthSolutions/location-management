package com.ihs.locationmanagement.api.model.dao;

import java.io.Serializable;

public interface DAO {
	
	Serializable save(Object objectinstance);

	void delete(Object objectinstance);

	Object merge(Object objectinstance);

	void update(Object objectinstance);
	
	void saveOrUpdate(Object objectinstance);
}
