package org.ird.unfepi.model.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationAttribute;
import org.ird.unfepi.model.dao.DAOLocationAttribute;

public class DAOLocationAttributeImpl  extends DAOHibernateImpl<LocationAttribute> implements DAOLocationAttribute {

	public DAOLocationAttributeImpl(Session session) {
		super(session);
	}
	
	@Override
	protected Class<?> getEntity() {
		return LocationAttribute.class;
	}

	@Override
	public LocationAttribute findById(int locationAttributeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		
		return buildResult(cri);
	}

	@Override
	public List<LocationAttribute> findByLocation(int locationId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("location.locationId", locationId));
		
		return buildResultList(cri);
	}

	@Override
	public List<LocationAttribute> findByCriteria(Integer locationId, Integer locationAttributeTypeId, 
			String rangeValue1, String rangeValue2, String value, boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		if (locationId != null) {
			cri.add(Restrictions.eq("location.locationId", locationId));
		}
		if (locationAttributeTypeId != null) {
			cri.add(Restrictions.eq("locationAttributeType.locationAttributeTypeId", locationAttributeTypeId));
		}
		if (rangeValue1 != null){
			cri.add(Restrictions.eq("rangeValue1", rangeValue1));
		}
		if (rangeValue2 != null){
			cri.add(Restrictions.eq("rangeValue2", rangeValue2));
		}
		
		if (value != null) {
			cri.add(Restrictions.eq("value", value));
		}
		
		return buildResultList(cri);
	}
}
