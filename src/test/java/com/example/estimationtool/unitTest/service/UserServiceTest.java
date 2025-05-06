package com.example.estimationtool.unitTest.service;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.toolbox.dto.UserRegistrationDTO;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.model.User;
import com.example.estimationtool.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)  //For at kunne bruge @Mock og @InjectMock
public class UserServiceTest {

    @Mock
    private IUserRepository iUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User adminUser;



    /* Skabelon:

    //--------- Arrange ---------

    //--------- Act -------------

    //--------- Assert ----------

     */

    //------------------------------------ Create() ------------------------------------


    @Test // Tjekker at kun admin må oprette en bruger
    void test_userRegistrationDTO_throwsSecurityException() {

        //--------- Arrange ---------
        User nonAdminUser = new User(
                2,
                "IkkeAdmin",
                "Bruger",
                "dev@example.com",
                "hash",
                Role.DEVELOPER
        );

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "Ny",
                "Bruger",
                "ny@bruger.dk",
                "kode123",
                Role.DEVELOPER
        );

        // Act + Assert
        assertThrows(SecurityException.class, () -> { //Fanger undtagelsen fra metoden createUser()
         //   userService.createUser(nonAdminUser, userRegistrationDTO); // Skal fejle pga. rolle
        });
    }


    @Test // Tjekker at admin kan oprette en bruger
    void test_UserRegistrationWithDTO() {

        //--------- Arrange ---------


        // Admin-bruger
        User adminUser = new User(
                1,
                "Sofie",
                "Rytter",
                "rytterriet@gmail.com",
                "rytterKoden",
                Role.ADMIN
        );

        // Dev-bruger
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "Bo",
                "Maynerd",
                "maynerd@gmail.com",
                "kode123",
                Role.DEVELOPER
        );

        when(passwordEncoder.encode("kode123")).thenReturn("hashedKode123");

        // Dev-bruger med hashed password
        User expectedUser = new User(
                0,
                "Bo",
                "Maynerd",
                "maynerd@gmail.com",
                "hashedKode123",
                Role.DEVELOPER
        );


        // Tjekker at passwordet hashes
        when(iUserRepository.create(any(User.class))).thenReturn(expectedUser);

        //--------- Act -------------

        // User result = userService.createUser(adminUser, userRegistrationDTO);

        //--------- Assert ----------

        // Tjekker at felter i UserRegistrationDTO mappes korrekt
//        assertEquals("Bo", result.getFirstName());
//        assertEquals("Maynerd", result.getLastName());
//        assertEquals("maynerd@gmail.com", result.getEmail());
//        assertEquals("hashedKode123", result.getPasswordHash());
//        assertEquals(Role.DEVELOPER, result.getRole());

        // Tjekker at create() kaldes
        verify(iUserRepository, times(1)).create(any(User.class));

    }

}
