package org.ird.unfepi.model.dao.hibernatedimpl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationRelation;
import org.ird.unfepi.model.dao.DAOLocationRelation;

public class DAOLocationRelationImpl extends DAOHibernateImpl<LocationRelation> implements DAOLocationRelation {
	
	public DAOLocationRelationImpl(Session session) {
		super(session);
	}

	@Override
	protected Class<?> getEntity() {
		return LocationRelation.class;
	}
	
	@Override
	public List<LocationRelation> findById(int locationId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationId", locationId));
		
		return buildResultList(cri, "level");
	}

	@Override
	public List<LocationRelation> findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("name", name));
		
		return buildResultList(cri, "level");
	}

	@Override
	public List<LocationRelation> findByRelativeName(String relativename,  boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("relativeName", relativename));
		
		return buildResultList(cri, "level");
	}

	@Override
	public List<LocationRelation> findByRelativeId(int relativeid, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("relative", relativeid));
		
		return buildResultList(cri, "level");
	}

	@Override
	public List<LocationRelation> findByLocationType(int locationtype, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("locationType", locationtype));
		
		return buildResultList(cri, "name");
	}

	@Override
	public List<LocationRelation> findByRelativeLocationType(int relativetype, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin)
				.add(Restrictions.eq("relativeLocationType", relativetype));
		
		return buildResultList(cri, "name");
	}

	@Override
	public List<LocationRelation> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		return buildResultList(cri, "name");
	}

	@Override
	public List<LocationRelation> findByCriteria(Integer locationId, String name, Integer locationtype, Integer relativeid,
			String relativename, Integer relativetype, boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria cri = buildCriteria(isreadonly, mappingsToJoin);
		
		if(locationId !=null){
			cri.add(Restrictions.eq("locationId", locationId));
		}
		
		if(name !=null){
			cri.add(Restrictions.eq("name", name));
		}
		
		if(locationtype !=null){
			cri.add(Restrictions.eq("locationType", locationtype));
		}
		
		if(relativeid != null){
			cri.add(Restrictions.eq("relativeId", relativeid));
		}
		
		if(relativename != null){
			cri.add(Restrictions.eq("relativeName", relativename));
		}
		if(relativetype !=null){
			cri.add(Restrictions.eq("relativeLocationType", relativetype));
		}
		
		return buildResultList(cri, "name");
	}
}
