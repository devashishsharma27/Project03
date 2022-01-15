package in.co.sunrays.proj3.model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.MarksheetDTO;
import in.co.sunrays.project3.dto.StudentDTO;

public class MarksheetModelHibImp implements MarksheetModelInt {

	
	public long add(MarksheetDTO dto)throws ApplicationException,DuplicateRecordException
	{
	    long pk=0;
	    Session session=null;
	    Transaction tx=null;
	    MarksheetDTO dtoExists=null;
	    StudentDTO sDto=null;
	    try
	    {
	    	dtoExists=findByRollNo(dto.getRollNo());
	    	if(dtoExists!=null)
	    	{
	    		throw new  DuplicateRecordException("RollNO is Already Exists");
	    	}
	    	session=HibDataSource.getSession();
	    	sDto=(StudentDTO) session.get(StudentDTO.class, dto.getStudentId());
	        dto.setName(sDto.getFirstName()+" "+sDto.getLastName());
	    	tx=session.beginTransaction();
	    	pk=(Long) session.save(dto);
	    	tx.commit();
	    }
	    catch(HibernateException hb)
	    {
	    	hb.printStackTrace();
	    	if(tx!=null)
	    	{
	    		tx.rollback();
	    	}
	    	throw new ApplicationException("Exception in add of Marksheet Model "+hb.getMessage());
	    }
	    finally {
			HibDataSource.closeSession(session);
		}
		return pk;
	}
	public MarksheetDTO findByPK(long pk) throws ApplicationException
	{
		Session session=null;
		MarksheetDTO dto=null;
		try
		{
			session=HibDataSource.getSession();
			dto=(MarksheetDTO) session.get(MarksheetDTO.class, pk);
		}
		catch(HibernateException hb)
		{
		   hb.printStackTrace();
		   throw new ApplicationException("Exception in find by pk of Marksheet Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}
	public MarksheetDTO findByRollNo(String rollNo) throws ApplicationException
	{
		Session session=null;
		MarksheetDTO dto=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(MarksheetDTO.class);
			cr.add(Restrictions.eq("rollNo", rollNo));
			list=cr.list();
			Iterator it=list.iterator();
			while(it.hasNext())
			{
				dto=(MarksheetDTO) it.next();
			}
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			throw new ApplicationException("Exception in find by rollno in marksheet Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}
	public void delete(MarksheetDTO dto) throws ApplicationException
	{
		Session session=null;
		Transaction tx=null;
		try
		{
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			session.delete(dto);
			tx.commit();
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
			}
			throw new ApplicationException("Exception in delete of marksheet Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
	}
	public void update(MarksheetDTO dto) throws ApplicationException
	{
		Session session=null;
		Transaction tx=null;
		try{
			session=HibDataSource.getSession();
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
			throw new ApplicationException("Exception in update of marksheet Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
	}
	public List search(MarksheetDTO dto,int pageNo,int pageSize) throws ApplicationException
	{
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(MarksheetDTO.class);
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getStudentId()>0)
				{
					cr.add(Restrictions.eq("studentId", dto.getStudentId()));
				}
				if(dto.getName()!=null&&dto.getName().trim().length()>0)
				{
					cr.add(Restrictions.like("name", dto.getName()+"%"));
				}
				if(dto.getRollNo()!=null&&dto.getRollNo().trim().length()>0)
				{
					cr.add(Restrictions.eq("rollNo", dto.getRollNo()));
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
			throw new ApplicationException("Exception in search of marksheet model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}
	public List search(MarksheetDTO dto) throws ApplicationException
	{
		return search(dto,0,0);
	}
	public List list(int pageNo,int pageSize)throws ApplicationException
	{
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(MarksheetDTO.class);
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
			throw new ApplicationException("Exception in list of marksheet model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}
	public List list() throws ApplicationException
	{
		return list(0,0);
	}
	
	public List getMeritList(int pageNo,int PageSize)throws ApplicationException
	{
		List list=null;
		 
		Session session=null;
		try
		{
			session=HibDataSource.getSession();
			Query q=session.createSQLQuery("SELECT `ID`,`ROLLNO`, `NAME`, `PHYSICS`, `CHEMISTRY`, `MATHS` , (PHYSICS + CHEMISTRY + MATHS) as total from ST_MARKSHEET where physics>=35 and chemistry>=35 and maths>=35  order by total desc limit 0,10");
			System.out.println("heloo");
			list=q.list();
			
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
		}
		finally {
			session.close();
		}
		return list;
		
	}
}
