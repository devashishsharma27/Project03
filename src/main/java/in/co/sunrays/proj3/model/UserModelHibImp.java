package in.co.sunrays.proj3.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.sunrays.proj3.exception.ApplicationException;
import in.co.sunrays.proj3.exception.DuplicateRecordException;
import in.co.sunrays.proj3.exception.RecordNotFoundException;
import in.co.sunrays.proj3.util.EmailBuilder;
import in.co.sunrays.proj3.util.EmailMessage;
import in.co.sunrays.proj3.util.EmailUtility;
import in.co.sunrays.proj3.util.HibDataSource;
import in.co.sunrays.project3.dto.RoleDTO;
import in.co.sunrays.project3.dto.UserDTO;

public class UserModelHibImp implements UserModelInt {

	public long add(UserDTO dto) throws DuplicateRecordException, ApplicationException {

		long pk = 0;
		Session session = null;
		Transaction tx = null;
		UserDTO dtoExits = null;
		RoleDTO rDto=null;
		try {

			dtoExits = findByLogin(dto.getLoginId());
			if (dtoExits != null) {
				throw new DuplicateRecordException("Login Id is already Exists");
			}
   
			session = HibDataSource.getSession();
            rDto=(RoleDTO) session.get(RoleDTO.class, dto.getRoleId());
            dto.setRoleName(rDto.getName());
			tx = session.beginTransaction();
			pk = (Long) session.save(dto);
			System.out.println("id"+dto.getId());
			tx.commit();

		} catch (HibernateException hb) {
			hb.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in add of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return pk;
	}

	public UserDTO findByLogin(String login) throws ApplicationException {
		Session session = null;
		UserDTO dto = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(UserDTO.class);
			cr.add(Restrictions.eq("loginId", login));
			list = cr.list();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				dto = (UserDTO) itr.next();
			}
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in findByLogin of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;

	}

	public UserDTO findByPK(long pk) throws ApplicationException {
		Session session = null;
		UserDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (UserDTO) session.get(UserDTO.class, pk);
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in findByPk of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public void delete(UserDTO dto) throws ApplicationException {
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
			throw new ApplicationException("Exception in delete of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
	}

	public void update(UserDTO dto) throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.update(dto);
			tx.commit();
		} catch (HibernateException hb) {
			hb.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in update of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
	}

	public UserDTO authenticate(String login, String pass) throws ApplicationException {
		Session session = null;
		UserDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(UserDTO.class);
			cr.add(Restrictions.eq("loginId", login));
			cr.add(Restrictions.eq("password", pass));
			List list = cr.list();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				dto = (UserDTO) itr.next();
			}

		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in aunthenticate of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return dto;
	}

	public List search(UserDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(UserDTO.class);
			if (dto != null) {
				if (dto.getId() > 0) {
					cr.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getLoginId() != null && dto.getLoginId().trim().length() > 0) {
					cr.add(Restrictions.like("loginId", dto.getLoginId() + "%"));
				}
				if (dto.getRoleId() > 0) {
					cr.add(Restrictions.eq("roleId", dto.getRoleId()));
				}
				if (dto.getFirstName() != null && dto.getFirstName().trim().length() > 0) {
					cr.add(Restrictions.like("firstName", dto.getFirstName() + "%"));
				}
			}
			if (pageSize > 0) {
				cr.setFirstResult(((pageNo - 1) * pageSize));
				cr.setMaxResults(pageSize);
			}
			list = cr.list();
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in search of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return list;
	}

	public List search(UserDTO dto) throws ApplicationException {

		return search(dto, 0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria cr = session.createCriteria(UserDTO.class);

			if (pageSize > 0) {
				cr.setFirstResult(((pageNo - 1) * pageSize));
				cr.setMaxResults(pageSize);
			}
			list = cr.list();
		} catch (HibernateException hb) {
			hb.printStackTrace();
			throw new ApplicationException("Exception in list of UserModel " + hb.getMessage());
		} finally {
			HibDataSource.closeSession(session);
		}
		return list;

	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public long registerUser(UserDTO bean) throws ApplicationException, DuplicateRecordException, MessagingException {

		long pk = add(bean);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", bean.getLoginId());
		map.put("password", bean.getPassword());

		String message = EmailBuilder.getUserRegistrationMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(bean.getLoginId());
		msg.setSubject("Registration is successful for ORS Project SunilOS");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);
		return pk;
	}

	public boolean changePassword(long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, ApplicationException, MessagingException {
		boolean flag = false;
		UserDTO dtoExist = null;

		dtoExist = findByPK(id);
		if (dtoExist != null && dtoExist.getPassword().equals(oldPassword)) {
			dtoExist.setPassword(newPassword);
			try {
				update(dtoExist);
			} catch (Exception e) {

				throw new ApplicationException("LoginId is already exist");
			}
			flag = true;
		} else {
			throw new RecordNotFoundException("Login not exist");
		}

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("login", dtoExist.getLoginId());
		map.put("password", dtoExist.getPassword());
		map.put("firstName", dtoExist.getFirstName());
		map.put("lastName", dtoExist.getLastName());

		String message = EmailBuilder.getChangePasswordMessage(map);

		EmailMessage msg = new EmailMessage();

		msg.setTo(dtoExist.getLoginId());
		msg.setSubject("SUNARYS ORS Password has been changed Successfully.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);

		EmailUtility.sendMail(msg);

		return flag;

	}

	public boolean forgetPassword(String login)
			throws ApplicationException, RecordNotFoundException, MessagingException {
		UserDTO userData = findByLogin(login);
		boolean flag = false;

		if (userData == null) {
			throw new RecordNotFoundException("Email ID does not exists !");

		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLoginId());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());
		String message = EmailBuilder.getForgetPasswordMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(login);
		msg.setSubject("SUNARYS ORS Password reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		flag = true;

		return flag;
	}

}
