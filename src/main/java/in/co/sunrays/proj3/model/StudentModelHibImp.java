package in.co.sunrays.proj3.model;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.ui.Model;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DataBaseException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.CollegeDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.StudentDTO;

public class StudentModelHibImp implements StudentModelInt{

	public long add(StudentDTO dto) throws ApplicationException, DuplicateRecordException {
		long pk=0;
		Session session=null;
		Transaction tx=null;
		StudentDTO sDto=null;
		try
		{ 
			CollegeModelInt cModel=ModelFactory.getInstance().getCollegeModel();
			CollegeDTO cDto=cModel.findByPk(dto.getCollegeId());
			dto.setCollegeName(cDto.getName());
			CourseModelInt crModel=ModelFactory.getInstance().getCourseModel();
			CourseDTO crDto=crModel.findByPK(dto.getCourseId());
			dto.setCourseName(crDto.getCourseName());
		
			session=HibDataSource.getSession();
			tx=session.beginTransaction();
			pk=(Long) session.save(dto);
			tx.commit();
		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			if(tx!=null)
			{
				tx.rollback();
				throw new ApplicationException("Exception in add of StudentModel "+hb.getMessage());
			}
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public StudentDTO findByPK(long pk) throws ApplicationException
	{
		Session session=null;
		StudentDTO dto=null;
		try
		{
			session=HibDataSource.getSession();
			dto=(StudentDTO) session.get(StudentDTO.class, pk);

		}catch(HibernateException hb)
		{
			hb.printStackTrace();
			throw new ApplicationException("Exception in findByPK of StudentModel "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return dto;
		
	}
	public StudentDTO findByName(String name) throws ApplicationException
	{
		Session session=null;
		StudentDTO dto=null;
		List list=null;
		try
		{
		   session=HibDataSource.getSession();
		   Criteria cr=session.createCriteria(StudentDTO.class);
		   cr.add(Restrictions.eq("Student_name", name));
		   list=cr.list();
		   Iterator itr=list.iterator();
		   while(itr.hasNext())
		   {
			   dto=(StudentDTO) itr.next();
		   }
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			throw new ApplicationException("Exception in findBy Name of Student Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return dto;
		
	}

	public void update(StudentDTO dto) throws ApplicationException {
	
		Session session=null;
		Transaction tx=null;
		try
		{
			CollegeModelInt cModel=ModelFactory.getInstance().getCollegeModel();
			CollegeDTO cDto=cModel.findByPk(dto.getCollegeId());
			dto.setCollegeName(cDto.getName());
			CourseModelInt crModel=ModelFactory.getInstance().getCourseModel();
			CourseDTO crDto=crModel.findByPK(dto.getCourseId());
			dto.setCourseName(crDto.getCourseName());
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
			throw new ApplicationException("Exception in update of StudentModel "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		
	}

	public void delete(StudentDTO dto) throws ApplicationException {
		
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
			throw new ApplicationException("Exception in delete of Student Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		
	}

	public List list() throws ApplicationException {
		
		return list(0,0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(StudentDTO.class);
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
			throw new ApplicationException("Exception in gettion list of Student of Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(StudentDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		List list=null;
		Session session=null;
		try
		{
		     session=HibDataSource.getSession();
		     Criteria cr=session.createCriteria(StudentDTO.class);
		     if(dto!=null)
		     {
		    	 if(dto.getId()>0)
		    	 {
		           cr.add(Restrictions.eq("id", dto.getId()));
		    	 }
		    	 if(dto.getCollegeId()>0)
		    	 {
		    		 cr.add(Restrictions.eq("collegeId", dto.getCollegeId())); 
		    	 }
		    	 if(dto.getFirstName()!=null && dto.getFirstName().trim().length()>0)
		    	 {
		    		 cr.add(Restrictions.like("firstName", dto.getFirstName()+"%"));
		    	 }
		    	 if(dto.getLastName()!=null && dto.getLastName().trim().length()>0)
		    	 {
		    		 cr.add(Restrictions.like("lastName", dto.getLastName()+"%"));
		    	 }
		    	 if(dto.getEmail()!=null && dto.getEmail().trim().length()>0)
		    	 {
		    		 cr.add(Restrictions.like("email", dto.getEmail()+"%")); 
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
		   throw new ApplicationException("Exception in search of Student Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(StudentDTO dto) throws ApplicationException {
		
		return search(dto,0,0);
	}

}
