package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationAttributeType;
import org.ird.unfepi.model.dao.DAOLocationAttributeType;

public class DAOLocationAttributeTypeImpl  extends DAOHibernateImpl<LocationAttributeType> implements DAOLocationAttributeType {

	public DAOLocationAttributeTypeImpl(Session session) {
		super(session);
	}

	@Override
	protected Class<?> getEntity() {
		return LocationAttributeType.class;
	}

	@Override
	public LocationAttributeType findById(int locationAttributeTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationAttributeTypeId", locationAttributeTypeId));
		
		return buildResult(cri);
	}

	@Override
	public LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("name", name));
		
		return buildResult(cri);
	}
	
	@Override
	public List<LocationAttributeType> findByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("category", category));
		
		return buildResultList(cri, Order.asc("name"));
	}
	
	@Override
	public List<LocationAttributeType> findByDisplayName(String displayname, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.ilike("displayName", displayname, MatchMode.ANYWHERE));
		
		return buildResultList(cri, Order.asc("displayName"));
	}

	@Override
	public List<LocationAttributeType> getAll(boolean includeVoided, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		if(!includeVoided) {
			cri.add(Restrictions.eq("voided", false));
		}
		
		return buildResultList(cri, Order.asc("name"));
	}
	
	// TODO add repeatable, dataType in search
}
