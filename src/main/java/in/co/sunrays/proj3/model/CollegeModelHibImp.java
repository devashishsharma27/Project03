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
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.CollegeDTO;

public class CollegeModelHibImp implements CollegeModelInt {

	public long add(CollegeDTO dto) throws ApplicationException, RecordNotFoundException, DuplicateRecordException {
		long pk=0;
		Session session=HibDataSource.getSession();
		Transaction tx=null;
		try
		{
			CollegeDTO DtoExits=findByName(dto.getName());
			if(DtoExits!=null)
			{
				throw new DuplicateRecordException("College Name is Already Exists");
			}
			tx=session.beginTransaction();
			session.save(dto);
			
			tx.commit();
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
				throw new ApplicationException("Exception in College Model add:" + hb.getMessage());
			}
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public void delete(CollegeDTO dto) throws ApplicationException {
		
		Session session=HibDataSource.getSession();
		Transaction tx=null;
		try
		{
			tx=session.beginTransaction();
			session.delete(dto);
			tx.commit();
		}
		catch(HibernateException hb)
		{
			if(tx!=null)
			{
				tx.rollback();
			}
			throw new ApplicationException("Exception in College Model delete:" + hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		
	}

	public void update(CollegeDTO dto) throws ApplicationException {
		
		Session session=HibDataSource.getSession();
		Transaction tx=null;
		try
		{
			tx=session.beginTransaction();
			session.update(dto);
			tx.commit();
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
			 tx.rollback();	
			}
			throw new ApplicationException("Exception in College Model update:" + hb.getMessage());
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
	}

	public CollegeDTO findByPk(long pk) throws ApplicationException {
		 Session session=null;
		 CollegeDTO dto=null;
		 
		 try
		 {
			 session=HibDataSource.getSession();
			 dto=(CollegeDTO) session.get(CollegeDTO.class, pk);
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

	public CollegeDTO findByName(String name) throws ApplicationException {

      Session session=null;
      CollegeDTO dto=null;
      try
      {
    	session=HibDataSource.getSession();
    	Criteria cr=session.createCriteria(CollegeDTO.class);
    	cr.add(Restrictions.eq("name", name));
    	List list=cr.list();
    	Iterator itr=list.iterator();
    	while(itr.hasNext())
    	{
    		dto=(CollegeDTO) itr.next();
    	}
    	
      }
      catch(HibernateException hb)
      {
    	  hb.printStackTrace();
    	  throw new ApplicationException("Exception in College FindByLogin" + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public List search(CollegeDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session=null;
		Transaction tx=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(CollegeDTO.class);
			
			System.out.println("id is  "+dto.getId());
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
				if(dto.getAddress()!=null && dto.getAddress().trim().length()>0)
				{
					cr.add(Restrictions.like("address", dto.getAddress()+"%"));
				}
				if(dto.getCity()!=null && dto.getCity().trim().length()>0){
					cr.add(Restrictions.like("city", dto.getCity()+"%"));
				}
				if(dto.getState()!=null && dto.getState().trim().length()>0)
				{
					cr.add(Restrictions.like("state",dto.getState()+"%"));
				}
			}
			
			if(pageSize>0)
			{
				cr.setFirstResult(((pageNo-1)*pageSize));
				cr.setMaxResults(pageSize);
				
			
			}
			list=cr.list();
		}catch(HibernateException hb)
		{
			throw new ApplicationException("Exception in college search");
		} finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search( CollegeDTO dto) throws ApplicationException {
		return search(dto, 0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
        
		Session session=null;
		List list=null;
	    try
	    {
	    	session =HibDataSource.getSession();
	    	Criteria cr=session.createCriteria(CollegeDTO.class);
	    	if(pageSize>0)
	    	{
	    		cr.setFirstResult(((pageNo-1)*pageSize));
	    		cr.setMaxResults(pageSize);
	    		
	    	}
	    	list=cr.list();
	    }catch(HibernateException hb)
	    {
	    	
	    	throw new ApplicationException("Exception : Exception in  College list");
		} finally {
			HibDataSource.closeSession(session);
		}

		return list;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

}
