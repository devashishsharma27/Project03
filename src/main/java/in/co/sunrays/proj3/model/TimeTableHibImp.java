package in.co.sunrays.proj3.model;

import java.util.Date;
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

import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.project3.dto.CourseDTO;
import in.co.sunrays.project3.dto.SubjectDTO;
import in.co.sunrays.project3.dto.TimetableDTO;

public class TimeTableHibImp implements TimeTableModelInt {

	public long add(TimetableDTO dto) throws ApplicationException, DuplicateRecordException, RecordNotFoundException {
		long pk = 0;
		Session session = null;
		Transaction tx = null;
		CourseDTO cDto = null;
		SubjectDTO sDto = null;
		CourseModelHibImp cModel = new CourseModelHibImp();
		SubjectModelHibImp sModel = new SubjectModelHibImp();
		try {

			cDto = cModel.findByPK(dto.getCourseId());
			sDto = sModel.findByPK(dto.getSubjectId());
			if (sDto != null) {
				if (sDto.getCourseId() != dto.getCourseId()) {
					throw new DuplicateRecordException("This Subject is not available for this course");
				}
			}
			dto.setCourseName(cDto.getCourseName());
			dto.setSubjectName(sDto.getSubjectName());
			semChecker(cDto, dto);
			TimetableDTO duplicatename = timeTableDuplicacy(dto.getCourseId(), dto.getSemester(), dto.getExamDate());

			TimetableDTO duplicatename1 = timeTableDuplicacy(dto.getCourseId(), dto.getSemester(), dto.getSubjectId());

			TimetableDTO duplicatename2 = timeTableDuplicacy(dto.getCourseId(), dto.getSubjectId(), dto.getExamDate());
			if (duplicatename1 != null || duplicatename != null || duplicatename2 != null) {
				throw new DuplicateRecordException("Time Table already exist");

			}

			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		} catch (HibernateException hb) {
			hb.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			HibDataSource.closeSession(session);
		}
		return pk;

	}

	public TimetableDTO findByPK(long pk) throws ApplicationException {
		TimetableDTO dto = null;
		Session session = null;
		try {
			session = HibDataSource.getSession();
			dto = (TimetableDTO) session.get(TimetableDTO.class, pk);
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in findByPk of TimeTableModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public void update(TimetableDTO dto) throws ApplicationException, DuplicateRecordException, RecordNotFoundException {
		Session session = null;
		Transaction tx = null;
		CourseDTO cDto = null;
		SubjectDTO sDto = null;
		CourseModelHibImp cModel = new CourseModelHibImp();
		SubjectModelHibImp sModel = new SubjectModelHibImp();
		try {

			cDto = cModel.findByPK(dto.getCourseId());
			sDto = sModel.findByPK(dto.getSubjectId());
			if (sDto != null) {
				if (sDto.getCourseId() != dto.getCourseId()) {
					throw new DuplicateRecordException("This Subject is not available for this course");
				}
			}
			dto.setCourseName(cDto.getCourseName());
			dto.setSubjectName(sDto.getSubjectName());
			semChecker(cDto, dto);
			TimetableDTO duplicatename = timeTableDuplicacy(dto.getCourseId(), dto.getSemester(), dto.getExamDate());

			TimetableDTO duplicatename1 = timeTableDuplicacy(dto.getCourseId(), dto.getSemester(), dto.getSubjectId());

			TimetableDTO duplicatename2 = timeTableDuplicacy(dto.getCourseId(), dto.getSubjectId(), dto.getExamDate());
			if (duplicatename1 != null || duplicatename != null || duplicatename2 != null) {
				throw new DuplicateRecordException("Time Table already exist");

			}

			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();

		} catch (HibernateException hb) {
			hb.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in update of timetable Model " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
	}

	public void delete(TimetableDTO dto) throws ApplicationException {

		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();

		} catch (HibernateException hb) {
			hb.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in delete of time table Model " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}

	}

	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(TimetableDTO.class);
			if (pageSize > 0) {
				cr.setFirstResult(((pageNo - 1) * pageSize));
				cr.setMaxResults(pageSize);

			}
			list = cr.list();

		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Excpetion in getting List " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(TimetableDTO dto, int pageNo, int pageSize) throws ApplicationException {

		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(TimetableDTO.class);

			if (dto != null) {
				if (dto.getId() > 0) {
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getCourseId() > 0) {
					cr.add(Restrictions.eq("courseId", dto.getCourseId()));
				}
				if (dto.getSubjectId() > 0) {
					cr.add(Restrictions.eq("subjectId", dto.getSubjectId()));
				}
				if (dto.getExamDate() != null) {
					System.out.println("in date");
					cr.add(Restrictions.eq("examDate", dto.getExamDate()));
				}
				if (dto.getSemester() != null && dto.getSemester().trim().length() > 0) {
					cr.add(Restrictions.eq("semester", dto.getSemester()));
				}
			
			}
			if (pageSize > 0) {
				cr.setFirstResult(((pageNo - 1) * pageSize));
				cr.setMaxResults(pageSize);
			}
			list = cr.list();
		} catch (HibernateException hb) {

			System.out.println(hb.getMessage());
			hb.printStackTrace();
			throw new ApplicationException("Exception in searching of TimeTable Model");
		} finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(TimetableDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

	public void semChecker(CourseDTO cDto, TimetableDTO tDto) throws RecordNotFoundException {
		String duration = cDto.getDuration();

		if (duration.equalsIgnoreCase("3 Years")
				&& (tDto.getSemester().equalsIgnoreCase("VII") || tDto.getSemester().equalsIgnoreCase("VIII"))) {

			throw new RecordNotFoundException("This Semester is not available for this course");

		} else if (duration.equalsIgnoreCase("2 Years")
				&& (tDto.getSemester().equalsIgnoreCase("V") || tDto.getSemester().equalsIgnoreCase("VI")
						|| tDto.getSemester().equalsIgnoreCase("VII") || tDto.getSemester().equalsIgnoreCase("VIII"))) {
			throw new RecordNotFoundException("This Semester is not available for this course");

		} else {
		}
	}

	public TimetableDTO timeTableDuplicacy(long courseId, String semester, Date examDate) throws ApplicationException {
		Session session = null;
		TimetableDTO dto = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(TimetableDTO.class);
			cr.add(Restrictions.eq("courseId", courseId));
			cr.add(Restrictions.eq("semester", semester));
			cr.add(Restrictions.eq("examDate", examDate));
			list = cr.list();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				dto = (TimetableDTO) itr.next();
			}
            System.out.println(dto);
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in TimeTableDuplicacy of Timetable Model " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public TimetableDTO timeTableDuplicacy(long courseId, long subjectId, Date examDate) throws ApplicationException {
		Session session = null;
		TimetableDTO dto = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(TimetableDTO.class);
			cr.add(Restrictions.eq("courseId", courseId));
			cr.add(Restrictions.eq("subjectId", subjectId));
			cr.add(Restrictions.eq("examDate", examDate));
			list = cr.list();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				dto = (TimetableDTO) itr.next();
			}
			  System.out.println(dto);
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in TimeTableDuplicacy of Timetable Model " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public TimetableDTO timeTableDuplicacy(long courseId, String semester, long subjectId) throws ApplicationException {
		Session session = null;
		TimetableDTO dto = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(TimetableDTO.class);
			cr.add(Restrictions.eq("courseId", courseId));
			cr.add(Restrictions.eq("semester", semester));
			cr.add(Restrictions.eq("subjectId", subjectId));
			list = cr.list();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				dto = (TimetableDTO) itr.next();
			}
			  System.out.println("hello"+dto);
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in TimeTableDuplicacy of Timetable Model " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

}
