package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationType;
import org.ird.unfepi.model.dao.DAOLocationType;

public class DAOLocationTypeImpl extends DAOHibernateImpl<LocationType> implements DAOLocationType{
	
	public DAOLocationTypeImpl(Session session) {
		super(session);
	}
	
	@Override
	protected Class<?> getEntity() {
		return LocationType.class;
	}

	@Override
	public LocationType findById(int locationTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationTypeId", locationTypeId));
		
		return buildResult(cri);
	}

	@Override
	public LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("name", name));
		
		return buildResult(cri);
	}
	
	@Override
	public List<LocationType> findByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("level", level));
		
		return buildResultList(cri, Order.asc("name"));
	}

	@Override
	public List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		return buildResultList(cri, Order.asc("level"));
	}
}
