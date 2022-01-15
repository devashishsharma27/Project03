package in.co.sunrays.proj3.model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.CollegeDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import sun.net.www.http.Hurryable;

public class CourseModelHibImp implements CourseModelInt {

	public long add(CourseDTO dto) throws DataBaseException, ApplicationException, DuplicateRecordException {
		long pk=0;
		Session session=HibDataSource.getSession();
		Transaction tx=null;
		try
		{
			CourseDTO dtoExist=findByName(dto.getCourseName());
			if(dtoExist!=null && dtoExist.getCourseName().equalsIgnoreCase(dto.getCourseName()))
			{
				throw new DuplicateRecordException("Record already exists");
			}
			tx=session.beginTransaction();
			session.save(dto);
			tx.commit();
			
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
				throw new ApplicationException("Exception in Course Model add:" + hb.getMessage());
			}
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public void delete(CourseDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Course Model delete:" + hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		
	}

	public void update(CourseDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Course Model update:" + hb.getMessage());
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
	}

	public CourseDTO findByName(String name) throws ApplicationException {
		 Session session=null;
	      CourseDTO dto=null;
	      try
	      {
	    	session=HibDataSource.getSession();
	    	Criteria cr=session.createCriteria(CourseDTO.class);
	    	cr.add(Restrictions.eq("courseName", name));
	    	List list=cr.list();
	    	Iterator itr=list.iterator();
	    	while(itr.hasNext())
	    	{
	    		dto=(CourseDTO) itr.next();
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


	public CourseDTO findByPK(long pk) throws ApplicationException {
		Session session=null;
		 CourseDTO dto=null;
		 
		 try
		 {
			 session=HibDataSource.getSession();
			 dto=(CourseDTO) session.get(CourseDTO.class, pk);
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

	public List search(CourseDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(CourseDTO.class);
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getCourseName()!=null &&dto.getCourseName().trim().length()>0)
				{
					cr.add(Restrictions.like("CourseName", dto.getCourseName()+"%"));
				}
				if(dto.getDuration()!=null && dto.getDuration().trim().length()>0)
				{
					cr.add(Restrictions.like("duration", dto.getDuration()+"%"));
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
			hb.printStackTrace();
			throw new ApplicationException("Exception in course search");
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(CourseDTO dto) throws ApplicationException {
		return search(dto,0,0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(CourseDTO.class);
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
			throw new ApplicationException("Exception in course list");
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
