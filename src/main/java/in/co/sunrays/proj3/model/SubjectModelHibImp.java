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
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.StudentDTO;
import in.co.sunrays.project3.dto.SubjectDTO;
import in.co.sunrays.project3.dto.TimetableDTO;

public class SubjectModelHibImp implements SubjectModelInt {

	
	public SubjectDTO findByPK(long pk)throws ApplicationException
    	{
		Session session=null;
		SubjectDTO dto=null;
		List list=null;
		try
		{
		  session=HibDataSource.getSession();
		  dto=(SubjectDTO) session.get(SubjectDTO.class, pk);
		}catch(HibernateException hb)
		{
		 hb.printStackTrace();
		 throw new ApplicationException("Exception in find By pk of SubajectModel "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
	  return dto;	
	}
	
	public SubjectDTO findByName(String name) throws ApplicationException
	{
		Session session=null;
		SubjectDTO dto=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(SubjectDTO.class);
			cr.add(Restrictions.eq("subjectName", name));
			list=cr.list();
			Iterator itr=list.iterator();
			while(itr.hasNext())
			{
				dto=(SubjectDTO)itr.next();
			}
		}
		catch(HibernateException hb)
		{
			hb.printStackTrace();
			throw new ApplicationException("Exceptiion in findByName of SubjectModel "+hb.getMessage());
		}
		return dto;
		
	}

	public long add(SubjectDTO dto) throws ApplicationException, DuplicateRecordException {
		int pk=0;
		Session session=null;
		 Transaction tx=null;
		 SubjectDTO sDto=null;
		 CourseDTO cDto=null;
		 try
		 { 
			 sDto=findByName(dto.getSubjectName()); 
			 if(sDto!=null && sDto.getSubjectName().equalsIgnoreCase(dto.getSubjectName()))
			 {
				 throw new DuplicateRecordException("Subject Name is Already Exists");
			 }
			 session=HibDataSource.getSession();
			 cDto=(CourseDTO) session.get(CourseDTO.class, dto.getCourseId());
			 dto.setCourseName(cDto.getCourseName());
			 tx=session.beginTransaction();
			 session.save(dto);
			 tx.commit();
		 }catch(HibernateException hb)
		 {
			 hb.printStackTrace();
			 if(tx!=null)
			 {
				 tx.rollback();
					throw new ApplicationException("Exception in Subject Model add:" + hb.getMessage());
			 }
			 
		 }
		 finally
			{
				HibDataSource.closeSession(session);
			}
		return pk;
	}

	public void delete(SubjectDTO dto) throws ApplicationException {
     
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
			throw new ApplicationException("Exception in Subject Model delete:" + hb.getMessage());
			
		}finally {
			HibDataSource.closeSession(session);
		}
		
	}

	public void update(SubjectDTO dto) throws ApplicationException {
     
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
			throw new ApplicationException("Exception in Subject Model update:" + hb.getMessage());
		}finally
		{
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
			Criteria cr=session.createCriteria(SubjectDTO.class);
			if(pageSize>0)
			{
				cr.setFirstResult(((pageNo-1)*pageSize));
				cr.setMaxResults(pageSize);
			}
			list=cr.list();
		}catch(HibernateException hb)
		{
			throw new ApplicationException("Exception : Exception in  Subject list");
		}
		finally
		{
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(SubjectDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		
		System.out.println("subjectname........."+dto.getSubjectName()+"cid........"+dto.getCourseId());
		Session session=null;
		List list=null;
		try
		{
			session=HibDataSource.getSession();
			Criteria cr=session.createCriteria(SubjectDTO.class);
			if(dto!=null){
				if(dto.getId()>0)
				{
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if(dto.getSubjectName()!=null &&dto.getSubjectName().trim().length()>0)
				{
					cr.add(Restrictions.like("subjectName", dto.getSubjectName()));
				}
				if(dto.getCourseId()>0)
				{
					cr.add(Restrictions.eq("courseId", dto.getCourseId()));
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
			throw new ApplicationException("Exception in searching of subjectModel "+hb.getMessage());
		}
		finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(SubjectDTO dto) throws ApplicationException {
		
		return search(dto,0,0);
	}

}
