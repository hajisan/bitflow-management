package com.example.estimationtool.unitTest.service;

import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.toolbox.controllerAdvice.UserFriendlyException;
import com.example.estimationtool.toolbox.dto.UserRegistrationDTO;
import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.model.User;
import com.example.estimationtool.service.UserService;
import com.example.estimationtool.toolbox.dto.UserUpdateDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

// Mockito = generelt framework.
// Laver mock objekter af afhængigheder for at teste komponenter isoleret uden hele
// applikationen skal køres

    @Mock
    private IUserRepository iUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserViewDTO sessionAdmin;
    private UserViewDTO sessionProjectManager;
    private UserRegistrationDTO userRegistrationDTO;
    private UserUpdateDTO userUpdateDTO;


    // Til at mocke en hel user
    //User mockAdminUser = new User(1, "Admin", "LastNameAdmin", "admin@admin.com", "hashedAdminPassword", Role.ADMIN);
    //User mockProjectMangaerUser = new User(1, "ProjectManager", "LastNameProjectManager", "projectmanager@projectmanager.com", "hashedProjectManagerPassword", Role.PROJECT_MANAGER);
    //User mockDeveloperUser = new User(1, "Developer", "LastNameDeveloper", "developer@developer.com", "hashedDeveloperPassword", Role.DEVELOPER);

    /*

    // Tilføjer project manager, der er logget ind til session
    UserViewDTO projectManagerMockUserDTO = new UserViewDTO(
            2,
            "ProjectManager",
            "LastNameProjectManager",
            "projectmanager@projectmanager.com",
            Role.PROJECT_MANAGER
    );

       UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

   UserViewDTO sessionDeveloper = new UserViewDTO(
                3,
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                Role.DEVELOPER);

     */

    /* Skabelon:

    //--------- Arrange ---------

    //--------- Act -------------

    //--------- Assert ----------

     */

    //------------------------------------ Create() ------------------------------------

    @Test
    public void test_createUser() {

        // --------- Arrange ---------

        // Admin må gerne oprette en bruger
        sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN
        );

        userRegistrationDTO = new UserRegistrationDTO(
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                "notHashedDeveloperPassword",
                Role.DEVELOPER
        );

        String encodedPassword = "hashed_password";
        when(passwordEncoder.encode("notHashedDeveloperPassword")).thenReturn(encodedPassword);


        User expectedDeveloper = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                encodedPassword,
                Role.DEVELOPER
        );

        when(iUserRepository.create(any(User.class))).thenReturn(expectedDeveloper);

        //Fanger det præcise user-objekt, UserService giver til UserRepository
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);


        // --------- Act ---------
        // Opretter bruger med admin-session
        User createdUser = userService.createUser(sessionAdmin, userRegistrationDTO);

        // --------- Assert ---------


        // Sikrer at dette UserService returnerer dén User, som UserRepository returnerede (service må ikke ændre objektet)
        assertNotNull(createdUser);
        assertEquals("Developer", createdUser.getFirstName());
        assertEquals("LastNameDeveloper", createdUser.getLastName());
        assertEquals("developer@developer.com", createdUser.getEmail());
        assertEquals(encodedPassword, createdUser.getPasswordHash());
        assertEquals(Role.DEVELOPER, createdUser.getRole());


        // Sikrer at metoderne kaldes
        verify(passwordEncoder).encode("notHashedDeveloperPassword");
        verify(iUserRepository).create(userCaptor.capture()); //med det præcise user-object

        // ---- Sikrer at UserRepository returnerer det præcise user-objekt ----
        // Dvs. DTO'en konverterer det oprettede user-objekt korrekt (ellers mister vi data)
        User capturedUser = userCaptor.getValue();
        assertEquals("Developer", capturedUser.getFirstName());
        assertEquals("LastNameDeveloper", capturedUser.getLastName());
        assertEquals("developer@developer.com", capturedUser.getEmail());
        assertEquals(encodedPassword, capturedUser.getPasswordHash());
        assertEquals(Role.DEVELOPER, capturedUser.getRole());
    }


    // --------------- Negativ test for createUser() --------------


    @Test // Tjekker at kun admin må oprette en bruger
    void test_createUser_fails() {

        //--------- Arrange ---------

        // En Project Manager forsøger at oprette en bruger – skal fejle
        sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Project Manager vil oprette en developer
        userRegistrationDTO = new UserRegistrationDTO(
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                "notHashedDeveloperPassword",
                Role.DEVELOPER
        );

        // --------- Act + Assert ---------

        UserFriendlyException exception = assertThrows(UserFriendlyException.class, () -> {
            userService.createUser(sessionProjectManager, userRegistrationDTO);
        });

        assertEquals("Kun Admin har adgang til denne funktion", exception.getMessage());

        // Verificerer at hverken passwordEncoder eller repository bliver kaldt
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(iUserRepository);
    }

    //------------------------------------ Update() ------------------------------------

    @Test
    public void test_updateUser() {


        // --------- Arrange ---------

        // Admin må gerne opdaterer en bruger
        sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN
        );

        userUpdateDTO = new UserUpdateDTO(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "newPassword",
                Role.DEVELOPER
        );

        User existingUser = new User(
                3,
                "OldDev",
                "OldLastName",
                "olddev@developer.com",
                "oldHashedPassword",
                Role.DEVELOPER
        );

        when(iUserRepository.readById(3)).thenReturn(existingUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");

        User updatedUser = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "hashedNewPassword",
                Role.DEVELOPER
        );

        when(iUserRepository.update(any(User.class))).thenReturn(updatedUser);

        //Fanger det præcise user-objekt, UserService giver til UserRepository
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // --------- Act ---------
        User result = userService.updateUser(userUpdateDTO, sessionAdmin);

        // --------- Assert ---------

        // Sikrer at dette UserService returnerer dén User, som UserRepository returnerede (service må ikke ændre objektet)
        assertNotNull(result);
        assertEquals("Developer", result.getFirstName());
        assertEquals("newdeveloper@developer.com", result.getEmail());
        assertEquals("hashedNewPassword", result.getPasswordHash());

        verify(passwordEncoder).encode("newPassword");
        verify(iUserRepository).update(userCaptor.capture());

        // ---- Sikrer at UserRepository returnerer det præcise user-objekt ----
        // Dvs. DTO'en konverterer det oprettede user-objekt korrekt (ellers mister vi data)
        User capturedUser = userCaptor.getValue();
        assertEquals("Developer", capturedUser.getFirstName());
        assertEquals("LastNameDeveloper", capturedUser.getLastName());
        assertEquals("newdeveloper@developer.com", capturedUser.getEmail());
        assertEquals("hashedNewPassword", capturedUser.getPasswordHash());
        assertEquals(Role.DEVELOPER, capturedUser.getRole());
    }

    // --------------- Negativ test for updateUser() --------------


    @Test
    public void test_updateUser_fail() {
        // --------- Arrange ---------

        // Project Manager er logget ind
        sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Bruger som forsøges opdateret
        userUpdateDTO = new UserUpdateDTO(
                3,
                "DevNew",
                "DevLast",
                "dev@newmail.com",
                "somePassword",
                Role.DEVELOPER
        );

        // Simulerer at brugeren findes i databasen
        when(iUserRepository.readById(3)).thenReturn(new User(
                3,
                "OldDev",
                "OldLast",
                "old@dev.com",
                "oldHash",
                Role.DEVELOPER
        ));

        // --------- Act + Assert ---------
        UserFriendlyException exception =




    }
