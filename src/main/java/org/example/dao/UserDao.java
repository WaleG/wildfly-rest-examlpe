package org.example.dao;

import org.example.model.User;
import org.example.model.UsersReport;

import java.util.List;

/**
 * @author Valentyn.Moliakov
 */
public interface UserDao {
    User create(User user);

    void deleteById(Long id);

    void delete(User user);

    User findById(Long id);

    User update(User user);

    List<User> listAll(Integer startPosition, Integer maxResult);

    UsersReport getUsersReport();
}
