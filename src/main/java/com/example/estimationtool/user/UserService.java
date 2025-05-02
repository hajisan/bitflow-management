package com.example.estimationtool.user;

import com.example.estimationtool.interfaces.IUserRepository;
import com.example.estimationtool.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final IUserRepository iUserRepository;


    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public User createUser(User currentUser, User newUser) {

        RoleCheck.ensureAdmin(currentUser.getRole()); // Adgangskontrol
        return iUserRepository.create(newUser);
    }

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
