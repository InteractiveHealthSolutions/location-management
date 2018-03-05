/**
 * 
 */
package org.ird.unfepi.model.dao;

import java.util.List;

import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.LocationHierarchyAncester;

public interface DAOLocationHierarchyAncester extends DAO {
	
	Number LAST_QUERY_TOTAL_ROW_COUNT();

	List<LocationHierarchyAncester> findById(int locationId, boolean isreadonly, String[] mappingsToJoin);
	List<LocationHierarchyAncester> findByName(String name, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findByRelativeName (String relativename, boolean isreadonly, String[] mappingsToJoin);
	List<LocationHierarchyAncester> findByRelativeId (int relativeid, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> findByLocationType (int locationtype, boolean isreadonly, String[] mappingsToJoin);
	List<LocationHierarchyAncester> findByRelativeLocationType (int relativetype, boolean isreadonly, String[] mappingsToJoin);

	List<LocationHierarchyAncester> getAll( boolean isreadonly, String[] mappingsToJoin);
	
	List<LocationHierarchyAncester> findByCriteria(Integer locationId, String name, Integer locationtype,
								Integer relativeid, String relativename, Integer relativetype,  
								boolean isreadonly, String[] mappingsToJoin);
}
