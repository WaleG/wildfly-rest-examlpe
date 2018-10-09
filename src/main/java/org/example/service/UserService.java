package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.example.model.UserStatus;
import org.example.model.UsersReport;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Valentyn.Moliakov
 */
@Stateless
public class UserService {

    @Inject
    private UserDao userDao;

    public User create(User entity) {
        return userDao.create(entity);
    }

    public User findById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new WebApplicationException("User with id " + id + " not found!", Response.Status.NOT_FOUND);
        }
        return user;
    }

    public void deleteById(Long id) {
        User user = this.findById(id);
        userDao.delete(user);
    }

    public List<User> listAll(Integer startPosition, Integer maxResult) {
        return userDao.listAll(startPosition, maxResult);
    }

    public User update(Long id, User user) {
        if (user == null) {
            throw new WebApplicationException("Update info is not provided!", Response.Status.BAD_REQUEST);
        }
        if (id == null) {
            throw new WebApplicationException("User id is not provided!", Response.Status.BAD_REQUEST);
        }
        if (!id.equals(user.getUserId())) {
            throw new WebApplicationException("Provided id is not the same as updated user id!", Response.Status.CONFLICT);
        }
        if (userDao.findById(id) == null) {
            throw new WebApplicationException("User with id " + id + " not found!", Response.Status.NOT_FOUND);
        }
        try {
            return userDao.update(user);
        } catch (OptimisticLockException e) {
            throw new WebApplicationException("Cannot update user with id = " + id + "!", Response.Status.CONFLICT);
        }
    }

    public User changeStatus(Long id, UserStatus status) {
        User user = this.findById(id);
        user.setStatus(status);
        try {
            return userDao.update(user);
        } catch (OptimisticLockException e) {
            throw new WebApplicationException("Cannot update user with id = " + id + "!", Response.Status.CONFLICT);
        }
    }

    public UsersReport getUsersReport() {
        return userDao.getUsersReport();
    }
}
