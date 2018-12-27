package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Location;

public interface DAOLocation extends DAO {
	
	Location findById(int locationid, boolean isreadonly, String[] mappingsToJoin);

	Location findByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin);
	
	List<Location> getAll(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin, String[] orders);
}
