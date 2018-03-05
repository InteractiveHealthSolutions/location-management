/**
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Location;

/**
 * @author Safwan
 *
 */
public interface DAOLocation extends DAO {
	
	Location findById(int locationid, boolean isreadonly, String[] mappingsToJoin);

	Number LAST_QUERY_TOTAL_ROW_COUNT();

	Location findByName(String name, boolean isreadonly, String[] mappingsToJoin);
	
	Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin);
	
	List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin);

	List<Location> getAll(boolean isreadonly, String[] mappingsToJoin);
	
	List<Location> findByHQL(int locId, boolean isreadonly, String[] mappingsToJoin);
}
