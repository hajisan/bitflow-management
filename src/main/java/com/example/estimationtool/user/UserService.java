package com.example.estimationtool.user;

import com.example.estimationtool.dto.UserRegistrationDTO;

import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.interfaces.IUserRepository;
import com.example.estimationtool.roleCheck.RoleCheck;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(IUserRepository iUserRepository, PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //------------------------------------ Create() ------------------------------------

    public User createUser(User currentUser, UserRegistrationDTO userDTO) {

        RoleCheck.ensureAdmin(currentUser.getRole()); // Adgangskontrol

        // Mapper DTO til User-objekt med hashet password
        User newUser = new User(
                0,
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()), // hashing
                userDTO.getRole()
        );

        return iUserRepository.create(newUser);
    }


    //------------------------------------ Read() --------------------------------------

    public List<UserViewDTO> readAll() {
        List<User> userList = iUserRepository.readAll();
        List<UserViewDTO> userViewDTOList = new ArrayList<>();

        for (User user : userList) {
            UserViewDTO userViewDTO = new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            userViewDTOList.add(userViewDTO);
        }
        return userViewDTOList;

    }

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
