package com.example.estimationtool.service;

import com.example.estimationtool.dto.UserRegistrationDTO;

import com.example.estimationtool.dto.UserUpdateDTO;
import com.example.estimationtool.dto.UserViewDTO;
import com.example.estimationtool.enums.Role;
import com.example.estimationtool.interfaces.IUserRepository;
import com.example.estimationtool.roleCheck.RoleCheck;
import com.example.estimationtool.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// DATABASEFEJL BOBLER OP til @ControllerAdvice pga. JdbcTemplate fordi det er en unchecked exception
//VALIDERING, eks. login-fejl, KASTER VI EXCEPTION (throws) for at signalere en fejl

@Service
public class UserService {

    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(IUserRepository iUserRepository, PasswordEncoder passwordEncoder) {
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //------------------------------------ Create() ------------------------------------

    public User createUser(UserViewDTO currentUser, UserRegistrationDTO userDTO) {

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

    public UserViewDTO readById(int id) {
        User user = iUserRepository.readById(id);
        if (user == null) {
            throw new NoSuchElementException("Bruger med ID " + id + " eksisterer ikke.");
        }

        return new UserViewDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

    }

    //------------------------------------ Update() ------------------------------------

    public User updateUser(UserUpdateDTO userUpdateDTO, UserViewDTO currentUser) {

        User existingUser = iUserRepository.readById(userUpdateDTO.getUserId());

        // Tjekker om bruger findes
        if (existingUser == null) {
            throw new RuntimeException("Bruger med ID: " + userUpdateDTO.getUserId() + " eksisterer ikke.");
        }

        // Håndterer rolle
        Role role;

        if (currentUser.getRole() == Role.ADMIN) {
            role = existingUser.getRole(); // Kun admin må ændre rolle
        } else {
            if (!currentUser.getRole().equals(existingUser.getRole())) {
                throw new SecurityException("Du har ikke tilladelse til ændre en brugers rolle.");
            }

            role = existingUser.getRole(); // Beholder nuværende rolle
        }

        // Håndterer password
        String passwordHash;

        if (userUpdateDTO.getPassword() == null || userUpdateDTO.getPassword().isBlank()) {
            // Hvis intet password er angivet, behold eksisterende hash
            passwordHash = existingUser.getPasswordHash();
        } else if (passwordEncoder.matches(userUpdateDTO.getPassword(), existingUser.getPasswordHash())) {
            // Hvis brugeren indtaster det samme password = behold det
            passwordHash = existingUser.getPasswordHash();
        } else {
            // Hash opdaterede password
            passwordHash = passwordEncoder.encode(userUpdateDTO.getPassword());
        }

        // Mapper UserUpdateDTO til User-objekt med opdateret bruger
        User updatedUser = new User(
                userUpdateDTO.getUserId(),
                userUpdateDTO.getFirstName(),
                userUpdateDTO.getLastName(),
                userUpdateDTO.getEmail(),
                passwordHash,
                role
        );

        return iUserRepository.update(updatedUser);

    }

    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id, UserViewDTO currentUser) {

        RoleCheck.ensureAdmin(currentUser.getRole());

        iUserRepository.deleteById(id);

    }


    //------------------------------------ Login() -------------------------------------

    public UserViewDTO login(String email, String inputPassword) {

        User user = iUserRepository.readByEmail(email);

        if (passwordEncoder.matches(inputPassword, user.getPasswordHash())) {
            return new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
        }
        throw new BadCredentialsException("Adgangskoden er forkert.");
    }
}
