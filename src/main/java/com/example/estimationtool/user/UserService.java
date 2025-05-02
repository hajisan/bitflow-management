package com.example.estimationtool.user;

import com.example.estimationtool.interfaces.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository iUserRepository;


    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public User createUser(User user) {
        return iUserRepository.create(user);
    }

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
