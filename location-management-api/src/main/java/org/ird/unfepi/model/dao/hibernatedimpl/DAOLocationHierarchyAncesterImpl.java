package org.ird.unfepi.model.dao.hibernatedimpl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.LocationHierarchyAncester;
import org.ird.unfepi.model.dao.DAOLocationHierarchyAncester;

@SuppressWarnings({"unchecked"})
public class DAOLocationHierarchyAncesterImpl extends DAOHibernateImpl implements DAOLocationHierarchyAncester {
	
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationHierarchyAncesterImpl(Session session) {
		super(session);
		this.session=session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	@Override
	public List<LocationHierarchyAncester> findById(int locationId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("locationId", locationId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list);
	}

	@Override
	public List<LocationHierarchyAncester> findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("name", name));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<LocationHierarchyAncester> findByRelativeName(String relativename,  boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("relativeName", relativename));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<LocationHierarchyAncester> findByRelativeId(int relativeid, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("relative", relativeid));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list.get(0));	
	}

	@Override
	public List<LocationHierarchyAncester> findByLocationType(int locationtype, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("locationType", locationtype));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list);
	}

	@Override
	public List<LocationHierarchyAncester> findByRelativeLocationType(int relativetype, boolean isreadonly,
			String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("relativeLocationType", relativetype));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list);	
	}

	@Override
	public List<LocationHierarchyAncester> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationHierarchyAncester.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<LocationHierarchyAncester> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (List<LocationHierarchyAncester>) (list.size() == 0 ? null : list);
	}

	@Override
	public List<LocationHierarchyAncester> findByCriteria(Integer locationId, String name, Integer locationtype, Integer relativeid,
			String relativename, Integer relativetype, boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria cri=session.createCriteria(LocationHierarchyAncester.class);
		
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
			cri.add(Restrictions.eq("relative", relativeid));
		}
		
		if(relativename != null){
			cri.add(Restrictions.eq("relativeName", relativename));
		}
		if(relativetype !=null){
			cri.add(Restrictions.eq("relativeLocationType", relativetype));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<LocationHierarchyAncester> list = cri.list();
		return list;
	}
}
