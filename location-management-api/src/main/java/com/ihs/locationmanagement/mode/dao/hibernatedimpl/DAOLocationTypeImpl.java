package com.ihs.locationmanagement.mode.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;

import com.ihs.locationmanagement.api.model.LocationAttributeType;
import com.ihs.locationmanagement.api.model.LocationType;
import com.ihs.locationmanagement.api.model.dao.DAOLocationType;;

public class DAOLocationTypeImpl extends DAOHibernateImpl implements DAOLocationType{
	
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationTypeImpl(Session session) {
		super(session);
		this.session = session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}

	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	public LocationType findById(int locationTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationType> criteria = builder.createQuery(LocationType.class);
//		Root<LocationType> root = criteria.from( LocationType.class );
//		criteria.select(root).where(builder.equal(root.get("locationTypeId" ), locationTypeId ) );
//		List<LocationType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationType> list = (List<LocationType>) criteria.add(Restrictions.eq("locationTypeId", locationTypeId));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	public LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationType> criteria = builder.createQuery(LocationType.class);
//		Root<LocationType> root = criteria.from( LocationType.class );
//		criteria.select(root).where(builder.equal(root.get("typeName" ), name ) );
//		List<LocationType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationType> list = (List<LocationType>) criteria.add(Restrictions.eq("typeName", name));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	public LocationType findByLevel(int level, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationType> criteria = builder.createQuery(LocationType.class);
//		Root<LocationType> root = criteria.from( LocationType.class );
//		criteria.select(root).where(builder.equal(root.get("level" ), level ) );
//		List<LocationType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationType> list = (List<LocationType>) criteria.add(Restrictions.eq("level", level));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	public LocationType findByDescription(String description, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationType> criteria = builder.createQuery(LocationType.class);
//		Root<LocationType> root = criteria.from( LocationType.class );
//		criteria.select(root).where(builder.equal(root.get("description" ), description ) );
//		List<LocationType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationType> list = (List<LocationType>) criteria.add(Restrictions.eq("description", description));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationType.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationType> criteria = builder.createQuery(LocationType.class);
//		criteria.from( LocationType.class );
//		List<LocationType> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationType> list = criteria.list(); 
	    setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
}

/*
	@Override
	public LocationType findById(int locationTypeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("locationTypeId", locationTypeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public LocationType findByName(String name, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class)
				.add(Restrictions.eq("typeName", name)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}
	
	@Override
	public List<LocationType> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationType.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationType> list = cri.addOrder(Order.asc("locationTypeId")).list();
		return list;
	}
*/