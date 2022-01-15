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
import in.co.sunrays.project3.dto.CollegeDTO;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.FacultyDTO;
import in.co.sunrays.project3.dto.MarksheetDTO;
import in.co.sunrays.project3.dto.SubjectDTO;

public class FacultyModelHibImp implements FacultyModelInt{

	public long add(FacultyDTO dto) throws DuplicateRecordException, ApplicationException {
		long pk=0;
		Session session=null;
		Transaction tx=null;
		try
		{
		   CollegeModelInt cModel=new CollegeModelHibImp();
		   CollegeDTO cDto=new CollegeDTO();
		   cDto=cModel.findByPk(dto.getCollegeId());
		   dto.setCollegeName(cDto.getName());
		   CourseModelInt crModel=new CourseModelHibImp();
		   CourseDTO crDto=crModel.findByPK(dto.getCourseId());
		   dto.setCourseName(crDto.getCourseName());
		   SubjectModelInt sModel=new SubjectModelHibImp();
		   SubjectDTO sDto=sModel.findByPK(dto.getSubjectId());
		   dto.setSubjectName(sDto.getSubjectName()); 
		 FacultyDTO dtoExists=findByEmail(dto.getEmailId());
		 if(dtoExists!=null)
		 {
			 throw new DuplicateRecordException("Record is already exists");
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
				throw new ApplicationException("Exception in Faculty Model add:" + hb.getMessage());
			}
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public void delete(FacultyDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in Faculty Model delete:" + hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		
	}
	public void update(FacultyDTO dto) throws ApplicationException {
		Session session=HibDataSource.getSession();
		Transaction tx=null;
		try
		{
			   CollegeModelInt cModel=new CollegeModelHibImp();
			   CollegeDTO cDto=new CollegeDTO();
			   cDto=cModel.findByPk(dto.getCollegeId());
			   dto.setCollegeName(cDto.getName());
			   CourseModelInt crModel=new CourseModelHibImp();
			   CourseDTO crDto=crModel.findByPK(dto.getCourseId());
			   dto.setCourseName(crDto.getCourseName());
			   SubjectModelInt sModel=new SubjectModelHibImp();
			   SubjectDTO sDto=sModel.findByPK(dto.getSubjectId());
			   dto.setSubjectName(sDto.getSubjectName()); 
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
			throw new ApplicationException("Exception in Faculty Model update:" + hb.getMessage());
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
	}

	public FacultyDTO findByPK(long pk) throws ApplicationException {
	    FacultyDTO dto=null;
	    Session session=null;
	    try
	    {
	      session=HibDataSource.getSession();
	      dto=(FacultyDTO) session.get(FacultyDTO.class, pk);
	    }catch(HibernateException hb)
	    {
	    	hb.printStackTrace();
			throw new ApplicationException("Exception in getting record by pk:" + hb.getMessage());
	    }finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public FacultyDTO findByEmail(String Email) throws ApplicationException {
        
		FacultyDTO dto=null;
		Session session=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(FacultyDTO.class);
			cr.add(Restrictions.eq("emailId", Email));
			List list=cr.list();
			Iterator it=list.iterator();
			while(it.hasNext())
			{
				dto=(FacultyDTO) it.next();
			}
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			throw new ApplicationException("Exception in find By Email of faculty Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}		
		return dto;
	}

	public List search(FacultyDTO dto, int pageNo, int pageSize) throws ApplicationException {
		 
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(FacultyDTO.class);
			if(dto!=null)
			{
				if(dto.getId()>0)
				{
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getFirstName()!=null && dto.getFirstName().trim().length()>0)
				{
					cr.add(Restrictions.like("firstName", dto.getFirstName()+"%"));
				}
				if(dto.getCollegeId()>0)
				{
					cr.add(Restrictions.eq("collegeId", dto.getCollegeId()));
					
				}
				if(dto.getCourseId()>0)
				{
					cr.add(Restrictions.eq("courseId", dto.getCourseId()));
				}
				if(dto.getEmailId()!=null && dto.getEmailId().trim().length()>0)
				{
					cr.add(Restrictions.like("emailId", dto.getEmailId()+"%"));
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
			throw new ApplicationException("Exception in search of faculty Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(FacultyDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto,0,0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(FacultyDTO.class);
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
			throw new ApplicationException("Exception in List of faculty Model "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0,0);
	}

}
