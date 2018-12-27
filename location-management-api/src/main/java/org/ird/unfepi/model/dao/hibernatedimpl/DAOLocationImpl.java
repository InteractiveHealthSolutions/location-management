package org.ird.unfepi.model.dao.hibernatedimpl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.dao.DAOLocation;

public class DAOLocationImpl extends DAOHibernateImpl<Location> implements DAOLocation {
	
	public DAOLocationImpl(Session session) {
		super(session);
	}
	
	@Override
	protected Class<?> getEntity() {
		return Location.class;
	}

	@Override
	public Location findById(int locationid, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationId", locationid));
		
		return buildResult(cri);
	}
	
	@Override
	public Location findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("name", name));
		
		return buildResult(cri);
	}

	@Override
	public Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("otherIdentifier", identifier));
		
		return buildResult(cri);
	}

	@Override
	public List<Location> getAll(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin, String[] orders) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		if(!includeVoided) {
			cri.add(Restrictions.eq("voided", false));
		}
		
		return buildResultList(cri, orders);
	}
	
	// TODO locations of certain types, level, under a specific parent, active filter
	
}
