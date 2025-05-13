package com.example.estimationtool.repository.interfaces;

import com.example.estimationtool.model.User;

import java.util.List;

public interface IUserRepository extends IRepository<User, Integer> {

    // CRUDS

    User create(User user);

    List<User> readAll();

    User readById(Integer id);

    User update(User user);

    void deleteById(Integer id);

    // CRUDS for DTO'er

    User readByEmail(String email);

    List<User> readAllByProjectId(Integer projectId);

    List<User> readAllBySubProjectId(Integer subProjectId);

    List<User> readAllByTaskId(Integer taskId);

    User readUserBySubTaskId(Integer subTaskId);
}
