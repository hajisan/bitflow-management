package com.example.estimationtool.interfaces;

import com.example.estimationtool.user.User;

import java.util.List;

public interface IUserRepository extends IRepository<User, Integer> {

    User create(User user);

    List<User> readAll();

    User readById(int id);

    User update(User user);

    void deleteById(int id);

    User readByEmail(String email);

}
