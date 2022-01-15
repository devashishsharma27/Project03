package in.co.sunrays.proj3.model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.RoleDTO;

public class RoleModelHibImp implements RoleModelInt {

	public long add(RoleDTO dto) throws DuplicateRecordException, ApplicationException {
		int pk=0;
		Session session=null;
		Transaction tx=null;
		RoleDTO dtoExists=null;
		try
		{
			dtoExists=findByName(dto);
			if(dtoExists!=null )
			{
				if( dtoExists.getName().equalsIgnoreCase(dto.getName()))
				{
				throw new DuplicateRecordException("Role Name is Already Exists");
				}
			}
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			session.save(dto);
			tx.commit();
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
			}
		   throw new ApplicationException("Exception in add of Role Model "+hb.getMessage() );	
		}
		finally{
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public void delete(RoleDTO dto) throws ApplicationException {
          
		Session session=null;
		Transaction tx=null;
		try
		{
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			session.delete(dto);
			tx.commit();
		}
		catch(HibernateException hb)
		{
			 hb.printStackTrace();
			if(tx!=null){
				tx.rollback();
			}
			throw new ApplicationException("Exception in delete of role Model "+hb.getMessage());
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
	}
        public void update(RoleDTO dto) throws ApplicationException {
		
		Session session=null;
		Transaction tx=null;
		try
		{
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			session.update(dto);
			tx.commit();
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
			}
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		
	}

	public RoleDTO findByPK(long pk) throws ApplicationException {
		
		RoleDTO  dto=null;
		Session session=null;
		try
		{
		  session=HibDataSource.getSession();
		  dto=(RoleDTO) session.get(RoleDTO.class, pk);
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			
			throw new ApplicationException("Exception in getting record by pk:" + hb.getMessage());
	
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		
		return dto;
	}

	public RoleDTO findByName(RoleDTO dto) throws ApplicationException {
		Session session=null;
		RoleDTO dto1=null;
		List list=null;
		try
		{
		    session=HibDataSource.getSession();
	        Criteria cr=session.createCriteria(RoleDTO.class);
	        cr.add(Restrictions.eq("name", dto.getName()));
	        list=cr.list();
	        Iterator it=list.iterator();
	        while(it.hasNext())
	        {
	        	dto1=(RoleDTO) it.next();
	        }
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return dto1;
	}

	public List search(RoleDTO dto, int pageNo, int pageSize) throws ApplicationException {
		List list=null;
		Session session=null;
		
		System.out.println(pageNo+"  "+pageSize);
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(RoleDTO.class);
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getName()!=null && dto.getName().trim().length()>0)
				{
					cr.add(Restrictions.like("name", dto.getName()+"%"));
				}
				if(dto.getDescription()!=null && dto.getDescription().trim().length()>0)
				{
					cr.add(Restrictions.like("description", dto.getDescription()+"%"));
				}
			}
			if(pageSize>0)
			{
				cr.setFirstResult(((pageNo-1)*pageSize));
				cr.setMaxResults(pageSize);
			}
			list=cr.list();
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(RoleDTO dto) throws ApplicationException {
		
		return search(dto,0,0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		
		Session session=null;
		List list=null;
		try
		{
		 session=HibDataSource.getSession();
		 Criteria cr=session.createCriteria(RoleDTO.class);
		 if(pageSize>0)
		 {
			 cr.setFirstResult(((pageNo-1)*pageSize));
			 cr.setMaxResults(pageSize);
		 }
		 list=cr.list();
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List list() throws ApplicationException {
     
		return list(0,0);	
	}

}
