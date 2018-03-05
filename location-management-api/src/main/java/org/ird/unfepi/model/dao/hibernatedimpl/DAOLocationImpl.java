/**
 * 
 */
package org.ird.unfepi.model.dao.hibernatedimpl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ird.unfepi.model.Location;
import org.ird.unfepi.model.dao.DAOLocation;

/**
 * @author Safwan
 *
 */

@SuppressWarnings({"unchecked"})
public class DAOLocationImpl extends DAOHibernateImpl implements DAOLocation {
	
	/** The session. */
	private Session session ;
	private Number LAST_QUERY_TOTAL_ROW_COUNT;

	/**
	 * Instantiates a new dAO address impl.
	 *
	 * @param session the session
	 */
	public DAOLocationImpl(Session session) {
		super(session);
		this.session=session;
	}

	@Override
	public Location findById(int locationid, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("locationId", locationid));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		setLAST_QUERY_TOTAL_ROW_COUNT((Number) cri.setProjection(Projections.rowCount()).uniqueResult());
		return (list.size() == 0 ? null : list.get(0));
	}
	
	private void setLAST_QUERY_TOTAL_ROW_COUNT(Number LAST_QUERY_TOTAL_ROW_COUNT) {
		this.LAST_QUERY_TOTAL_ROW_COUNT = LAST_QUERY_TOTAL_ROW_COUNT;
	}
	
	@Override
	public Number LAST_QUERY_TOTAL_ROW_COUNT() {
		return LAST_QUERY_TOTAL_ROW_COUNT;
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
	public Location findByIdentifier(String identifier, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("identifier", identifier));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return (list.size() == 0 ? null : list.get(0));
	}

	@Override
	public List<Location> findLocationByVoided(boolean voided, boolean isreadonly, String[] mappingsToJoin) {
		Criteria cri = session.createCriteria(Location.class).setReadOnly(isreadonly)
				.add(Restrictions.eq("voided", voided));
		
		if(mappingsToJoin != null)
			for (String mapping : mappingsToJoin) {
				cri.setFetchMode(mapping, FetchMode.JOIN);
			}
		
		List<Location> list = cri.list();
		//setLAST_QUERY_TOTAL_ROW_COUNT(list.size());
		return list;
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
	
	@Override
	public List<Location> findByHQL(int locId, boolean isreadonly, String[] mappingsToJoin) {
		String hql = "select  GetTree("+locId+") from location l where locationId ="+ locId;
		SQLQuery query = session.createSQLQuery(hql);
		
		List list = query.list();
		return list;	
	}
}
