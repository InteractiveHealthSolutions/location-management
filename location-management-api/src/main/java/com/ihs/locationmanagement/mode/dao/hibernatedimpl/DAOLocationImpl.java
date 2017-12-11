package com.ihs.locationmanagement.mode.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import com.ihs.locationmanagement.api.model.Location;
import com.ihs.locationmanagement.api.model.dao.DAOLocation;

public class DAOLocationImpl extends DAOHibernateImpl implements DAOLocation {
	
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationImpl(Session session) {
		super(session);
		this.session=session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	public List findById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class).add(Restrictions.eq("locationId", cityId));
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("locationId" ), cityId ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		
		setLAST_QUERY_TOTAL_ROW_COUNT(criteria.list().size());
		return (criteria.list().size() == 0 ? null : criteria.list());
	}

	@SuppressWarnings("unchecked")
	public List findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
	Criteria criteria = session.createCriteria(Location.class).add(Restrictions.eq("name", name));
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("name"), name ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		//List<Location> list = (List<Location>) session.createCriteria(Location.class).add(Restrictions.eq("name", name));
		setLAST_QUERY_TOTAL_ROW_COUNT(criteria.list().size());
		return (criteria.list().size() == 0 ? null : criteria.list());
	}

	public Location findByShortName(String shortName, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("shortName"), shortName ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("shortName", shortName));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public Location findByFullName(String fullName, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("fullName"), fullName ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("fullName", fullName));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public Location findByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("description"), description ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("description", description));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("otherIdentifier"), identifier ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("otherIdentifier", identifier));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public Location findByLatitude(String latitude, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("latitude"), latitude ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("latitude", latitude));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public Location findByLongitude(String longitude, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("longitude"), longitude ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("longitude", longitude));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	public List<Location> getChildLocations(Location location, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("longitude"), longitude ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("parentLocation", location));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list);
	}
	public List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(Location.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		criteria.from( Location.class );
//		criteria.distinct(true);
//		Root<Location> root = criteria.from( Location.class );
//		criteria.select(root).where(builder.equal(root.get("voided"), voided ) );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = (List<Location>) criteria.add(Restrictions.eq("voided", voided));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	public List<Location> getAll(boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria criteria = session.createCriteria(Location.class)
			/*	.setProjection(
					    Projections.distinct(Projections.projectionList()
					    .add(Projections.property("locationId"), "locationId")))*/.addOrder(Order.asc("locationId"));
		//CriteriaBuilder builder = session.getCriteriaBuilder();
	
//		CriteriaQuery<Location> criteria = builder.createQuery(Location.class);
//		criteria.from( Location.class );
//		List<Location> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<Location> list = criteria.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	
}

/*
	@Override
	public Location findById(int cityId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("locationId", cityId));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public Location findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("name", name));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<Location> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Location> list = cri.addOrder(Order.asc("locationId")).list();

		return list;
	}
*/
