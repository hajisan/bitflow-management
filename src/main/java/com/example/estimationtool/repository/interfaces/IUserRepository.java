package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.User;

import java.util.List;

public interface IUserRepository extends IRepository<User, Integer> {

    User create(User user);

    List<User> readAll();

    User readById(Integer id);

    User update(User user);

    void deleteById(Integer id);

    User readByEmail(String email);

    List<User> readByProjectId(Integer id);

}
