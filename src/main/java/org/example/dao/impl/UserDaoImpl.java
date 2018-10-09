package org.example.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.model.UserStatus;
import org.example.model.UsersReport;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * DAO for User
 */
@Stateless
@Slf4j
public class UserDaoImpl implements UserDao {
    @PersistenceContext(unitName = "wildfly-rest-persistence-unit")
    private EntityManager em;

    @Override
    public User create(User user) {
        em.persist(user);
        em.flush();
        log.info("User {} {} created Successfully.", user.getFirstname(), user.getSurname());
        return user;
    }

    @Override
    public void deleteById(Long id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
        log.info("User deleted Successfully.");
    }

    @Override
    public void delete(User user) {
        em.remove(user);
        log.info("User deleted Successfully.");
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public User update(User user) {
        em.merge(user);
        log.info("User {} {} updated Successfully.", user.getFirstname(), user.getSurname());
        return user;
    }

    @Override
    public List<User> listAll(Integer startPosition, Integer maxResult) {
        TypedQuery<User> findAllQuery = em.createQuery(
                "SELECT DISTINCT u FROM User u ORDER BY u.userId", User.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }
        return findAllQuery.getResultList();
    }

    @Override
    public UsersReport getUsersReport() {
        long activeUsers = getUsersCountByStatus(UserStatus.ACTIVE);
        long inactiveUsers = getUsersCountByStatus(UserStatus.INACTIVE);
        long deletedUsers = getUsersCountByStatus(UserStatus.DELETED);
        return new UsersReport(activeUsers, inactiveUsers, deletedUsers);
    }

    private Long getUsersCountByStatus(UserStatus status) {
        return em.createQuery("SELECT count(u.userId) FROM User u where u.status = :status", Long.class)
                .setParameter("status", status.toString())
                .getSingleResult();
    }
}
