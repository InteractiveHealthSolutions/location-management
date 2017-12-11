package com.ihs.locationmanagement.mode.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import com.ihs.locationmanagement.api.model.LocationAttributeType;
import com.ihs.locationmanagement.api.model.dao.DAOLocationAttributeType;

public class DAOLocationAttributeTypeImpl  extends DAOHibernateImpl implements DAOLocationAttributeType {

	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationAttributeTypeImpl(Session session) {
		super(session);
		this.session = session;
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	public LocationAttributeType findById(int locationAttributeTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		Root<LocationAttributeType> root = criteria.from( LocationAttributeType.class );
//		criteria.select(root).where(builder.equal(root.get("locationAttributeTypeId" ), locationAttributeTypeId ) );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = (List<LocationAttributeType>) criteria.add(Restrictions.eq("locationAttributeTypeId", locationAttributeTypeId));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		Root<LocationAttributeType> root = criteria.from( LocationAttributeType.class );
//		criteria.select(root).where(builder.equal(root.get("attributeName" ), name ) );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = (List<LocationAttributeType>) criteria.add(Restrictions.eq("attributeName", name));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttributeType findByDisplayName(String displayName, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		Root<LocationAttributeType> root = criteria.from( LocationAttributeType.class );
//		criteria.select(root).where(builder.equal(root.get("displayName" ), displayName ) );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = (List<LocationAttributeType>) criteria.add(Restrictions.eq("displayName", displayName));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttributeType findByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		Root<LocationAttributeType> root = criteria.from( LocationAttributeType.class );
//		criteria.select(root).where(builder.equal(root.get("description" ), description ) );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = (List<LocationAttributeType>) criteria.add(Restrictions.eq("description", description));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	public LocationAttributeType findByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		Root<LocationAttributeType> root = criteria.from( LocationAttributeType.class );
//		criteria.select(root).where(builder.equal(root.get("category" ), category ) );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = (List<LocationAttributeType>) criteria.add(Restrictions.eq("category", category));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public List<LocationAttributeType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttributeType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttributeType> criteria = builder.createQuery(LocationAttributeType.class);
//		criteria.from( LocationAttributeType.class );
//		List<LocationAttributeType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttributeType> list = criteria.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
}

/*

	@Override
	public LocationAttributeType findById(int locationAttributeTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("locationAttributeTypeId", locationAttributeTypeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public LocationAttributeType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("attributeName", name)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public LocationAttributeType findByCategory(String category, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class)
				.add(Restrictions.eq("category", category)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<LocationAttributeType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttributeType.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttributeType> list = cri.addOrder(Order.asc("locationAttributeTypeId")).list();
		return list;
	}
*/