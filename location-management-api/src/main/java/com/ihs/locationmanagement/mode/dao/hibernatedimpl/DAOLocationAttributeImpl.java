package com.ihs.locationmanagement.mode.dao.hibernatedimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import com.ihs.locationmanagement.api.model.LocationAttribute;
import com.ihs.locationmanagement.api.model.dao.DAOLocationAttribute;

public class DAOLocationAttributeImpl  extends DAOHibernateImpl implements DAOLocationAttribute {

	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	public DAOLocationAttributeImpl(Session session) {
		super(session);
		this.session = session;
	}

	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
	}

	
	public LocationAttribute findById(int locationAttributeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("locationAttributeId" ), locationAttributeId ) );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		//CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("locationAttributeId" ), locationAttributeId ) );
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttribute findByValue(String value, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		
//		criteria.select(root).where(builder.equal(root.get("value" ), value ) );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("value", value));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttribute findByTypeName(String typeName, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("typeName" ), typeName ) );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("typeName", typeName));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttribute findByTypeValue1(String typeValue1, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("typeValue1" ), typeValue1 ) );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("typeValue1", typeValue1));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public LocationAttribute findByTypeValue2(String typeValue2, boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("typeValue2" ), typeValue2 ) );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("typeValue2", typeValue2));
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	public List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaBuilder builder = session.getCriteriaBuilder();
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		criteria.from( LocationAttribute.class );
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttribute> list = criteria.list();
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
	}
	
	@SuppressWarnings({"deprecation", "unchecked"})
	public List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, String typeName, String typeValue1, String typeValue2, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize ,boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria cri=session.createCriteria(LocationAttribute.class);
		if(locationId!=null){
			cri.createAlias("location", "l").add(Restrictions.eq("l.locationId", locationId));
		}
		if(locationAttributeTypeId!=null){
			cri.createAlias("locationAttributeType", "lat").add(Restrictions.eq("lat.locationAttributeTypeId", locationAttributeTypeId));
		}
		if(locationAttributeId!=null){
			cri.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		}
		if((value!=null) && (!(value.equals("")))){
			cri.add(Restrictions.eq("value", value));
		}
		if((typeName!=null) && (!(typeName.equals("")))){
			cri.add(Restrictions.eq("typeName", typeName));
		}
		if((typeValue1!=null) && (!(typeValue1.equals("")))){
			cri.add(Restrictions.eq("typeValue1", typeValue1));
		}
		if((typeValue2!=null) && (!(typeValue2.equals("")))){
			cri.add(Restrictions.eq("typeValue2", typeValue2));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<LocationAttribute> list = cri.setReadOnly(isreadonly).setFirstResult(firstResult).setMaxResults(fetchsize).list();
//		List<LocationAttribute> list = cri.setReadOnly(isreadonly).list();
		return list;
	}
	
	
	public List<LocationAttribute> findByCriteria1(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize ,boolean isreadonly, String[] mappingsToJoin) {
		Criteria criteria = session.createCriteria(LocationAttribute.class);
//		CriteriaQuery<LocationAttribute> criteria = builder.createQuery(LocationAttribute.class);
//		Root<LocationAttribute> root = criteria.from( LocationAttribute.class );
//		criteria.select(root).where(builder.equal(root.get("locationAttributeId" ), locationAttributeId ) );
////		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).setFirstResult(firstResult).setMaxResults(fetchsize).getResultList();
//		List<LocationAttribute> list = session.createQuery( criteria ).setReadOnly(isreadonly).getResultList();
		List<LocationAttribute> list = (List<LocationAttribute>) criteria.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		
		setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
		
//		Criteria cri=session.createCriteria(LocationAttribute.class);
//		if(locationId!=null){
//			cri.createAlias("location", "l").add(Restrictions.eq("l.locationId", locationId));
//		}
//		if(locationAttributeTypeId!=null){
//			cri.createAlias("locationAttributeType", "lat").add(Restrictions.eq("lat.locationAttributeTypeId", locationAttributeTypeId));
//		}
//		if(locationAttributeId!=null){
//			cri.add(Restrictions.eq("locationAttributeId", locationAttributeId));
//		}
//		if((value!=null) && (!(value.equals("")))){
//			cri.add(Restrictions.eq("value", value));
//		}
//		
//		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
//		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		
//		if(mappingsToJoin != null)
//		for (String mapping : mappingsToJoin) {
//			cri.setFetchMode(mapping, FetchMode.JOIN);
//		}
//		
//		List<LocationAttribute> list = cri.setReadOnly(isreadonly).setFirstResult(firstResult).setMaxResults(fetchsize).list();
//		return list;
	}
}

/*
	@Override
	public LocationAttribute findById(int locationAttributeId, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttribute.class)
				.add(Restrictions.eq("locationAttributeId", locationAttributeId)).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}

		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttribute> list = cri.list();
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<LocationAttribute> getAll(boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(LocationAttribute.class).setReadOnly(isreadonly);
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<LocationAttribute> list = cri.addOrder(Order.asc("locationAttributeId")).list();
		return list;
	}
	
	@Override
	public List<LocationAttribute> findByCriteria(Integer locationAttributeId, String value, Integer locationId, Integer locationAttributeTypeId, int firstResult, int fetchsize ,boolean isreadonly, String[] mappingsToJoin) {
		
		Criteria cri=session.createCriteria(LocationAttribute.class);
		if(locationId!=null){
			cri.createAlias("location", "l").add(Restrictions.eq("l.locationId", locationId));
		}
		if(locationAttributeTypeId!=null){
			cri.createAlias("locationAttributeType", "lat").add(Restrictions.eq("lat.locationAttributeTypeId", locationAttributeTypeId));
		}
		if(locationAttributeId!=null){
			cri.add(Restrictions.eq("locationAttributeId", locationAttributeId));
		}
		if((value!=null) && (!(value.equals("")))){
			cri.add(Restrictions.eq("value", value));
		}
		
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		cri.setProjection(null).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if(mappingsToJoin != null)
		for (String mapping : mappingsToJoin) {
			cri.setFetchMode(mapping, FetchMode.JOIN);
		}
		List<LocationAttribute> list = cri.setReadOnly(isreadonly).setFirstResult(firstResult).setMaxResults(fetchsize).list();
		return list;
	}
*/