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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;


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


    @Test
        // Tjekker at kun admin må oprette en bruger
    public void test_createUser_fails() {

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
        assertEquals("/users/profile", exception.getRedirectPath());

        // Verificerer at hverken passwordEncoder eller repository bliver kaldt
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(iUserRepository);
    }
    //------------------------------------ ReadAll() -----------------------------------

    @Test
    public void test_readAll() {

        // --------- Arrange ---------

        // Vi forbereder en liste af User-objekter, som databasen (repository) ville returnere
        List<User> userListFromDb = List.of(
                new User(2, "ProjectManager", "LastNameProjectManager", "projectmanager@projectmanager.com", "hashedProjectManagerPassword", Role.PROJECT_MANAGER),
                new User(3, "Developer", "LastNameDeveloper", "developer@developer.com", "hashedDeveloperPassword", Role.DEVELOPER)
        );

        //Når iUserRepository.readAll() kaldes, skal den returnere mock-dataen ovenfor
        when(iUserRepository.readAll()).thenReturn(userListFromDb);

        // --------- Act ---------

        // Kalder service-metoden vi tester
        List<UserViewDTO> result = userService.readAll();

        // --------- Assert ---------

        // Vi forventer præcis 2 brugere i den returnerede liste
        assertEquals(2, result.size());

        // Tjekker indholdet af første bruger (Project Manager)
        UserViewDTO projectManager = result.get(0);
        assertEquals(2, projectManager.getUserId());
        assertEquals("ProjectManager", projectManager.getFirstName());
        assertEquals("LastNameProjectManager", projectManager.getLastName());
        assertEquals("projectmanager@projectmanager.com", projectManager.getEmail());
        assertEquals(Role.PROJECT_MANAGER, projectManager.getRole());

        // Tjekker indholdet af anden bruger (Developer)
        UserViewDTO developer = result.get(1);
        assertEquals(3, developer.getUserId());
        assertEquals("Developer", developer.getFirstName());
        assertEquals("LastNameDeveloper", developer.getLastName());
        assertEquals("developer@developer.com", developer.getEmail());
        assertEquals(Role.DEVELOPER, developer.getRole());

        // Verificerer at repository faktisk blev kaldt
        verify(iUserRepository).readAll();

    }

    //------------------------------------ ReadById() -----------------------------------

    @Test
    public void test_readById() {

        // --------- Arrange ---------

        // Mocked Project Manager, som findes i databasen
        User projectManager = new User(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                "hashedProjectManagerPassword",
                Role.PROJECT_MANAGER
        );

        // Når repository bliver bedt om at hente ID 2, returneres mock-ProjectManager
        when(iUserRepository.readById(2)).thenReturn(projectManager);

        // --------- Act ---------

        // Kald metoden, vi tester
        UserViewDTO result = userService.readById(2);

        // --------- Assert ---------

        // Vi tjekker at det returnerede DTO matcher den mockede bruger
        assertNotNull(result);
        assertEquals(2, result.getUserId());
        assertEquals("ProjectManager", result.getFirstName());
        assertEquals("LastNameProjectManager", result.getLastName());
        assertEquals("projectmanager@projectmanager.com", result.getEmail());
        assertEquals(Role.PROJECT_MANAGER, result.getRole());

        // Tjek at repository blev kaldt korrekt
        verify(iUserRepository).readById(2);
    }


    //------------------------------------ Update() ------------------------------------

    @Test
    public void test_updateUser() {


        // --------- Arrange ---------

        // Admin må gerne opdatere en bruger
        sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN
        );

        // Admin opdaterer developers email og password
        userUpdateDTO = new UserUpdateDTO(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "newNotHashedPassword",
                Role.DEVELOPER
        );

        // Den gamle developer-bruger, der hentes fra databasen
        User existingDeveloperUser = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                "hashedDeveloperPassword",
                Role.DEVELOPER
        );

        // Henter bruger med userId = 3
        when(iUserRepository.readById(3)).thenReturn(existingDeveloperUser);
        when(passwordEncoder.encode("newNotHashedPassword")).thenReturn("hashedNewPassword");

        // Opretter dén developer-bruger vi forventer at få retur fra UserRepository
        User updatedDeveloperUser = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "hashedNewPassword",
                Role.DEVELOPER
        );

        // Vi forventer at denne bruger kommer retur fra UserRepository inkl. hashed password
        when(iUserRepository.update(any(User.class))).thenReturn(updatedDeveloperUser);

        //Fanger det præcise user-objekt, UserService sender videre til UserRepository
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // --------- Act ---------
        User result = userService.updateUser(userUpdateDTO, sessionAdmin);

        // --------- Assert ---------

        // Sikrer at UserService returnerer dén User, som UserRepository returnerede (service må ikke ændre objektet)
        assertNotNull(result);
        assertEquals("Developer", result.getFirstName());
        assertEquals("newdeveloper@developer.com", result.getEmail());
        assertEquals("hashedNewPassword", result.getPasswordHash());

        // Tjekker at passwordet blev hashed
        verify(passwordEncoder).encode("newNotHashedPassword");
        //Fanger det præcise objekt, som UserService sendte til databasen (UserRepository)
        verify(iUserRepository).update(userCaptor.capture());

        // Tjekker at DTO'en konverterer det oprettede user-objekt korrekt (ellers mister vi data)
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

        // Project Manager vil opdatere developers email og password
        userUpdateDTO = new UserUpdateDTO(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "newNotHashedPassword",
                Role.DEVELOPER
        );

        // Opretter dén developer-bruger vi forventer at få retur fra UserRepository
        User updatedDeveloperUser = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "newdeveloper@developer.com",
                "hashedNewPassword",
                Role.DEVELOPER
        );

        when(iUserRepository.readById(3)).thenReturn(updatedDeveloperUser);

        // --------- Act + Assert ---------

        // Forvent at metoden kaster en exception
        UserFriendlyException exception = assertThrows(UserFriendlyException.class, () ->
                userService.updateUser(userUpdateDTO, sessionProjectManager)
        );

        assertEquals("Kun Admin har adgang til denne funktion", exception.getMessage());
        assertEquals("/users/profile", exception.getRedirectPath());



        // --------- Verify ---------

        // Project manager må gerne hente én bruger
        verify(iUserRepository).readById(3);
        // Ingen update-kald til repository må ske
        verify(iUserRepository, never()).update(any());
        // Hashing må ikke ske
        verifyNoInteractions(passwordEncoder);

    }

    //------------------------------------ Delete() ------------------------------------

    @Test
    public void test_deleteById() {

        // --------- Arrange ---------

        // Admin må gerne slette en bruger
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN
        );

        int userIdToDelete = 2; // Vil slette en developer

        // --------- Act ---------
        userService.deleteById(userIdToDelete, sessionAdmin);

        // --------- Assert ---------
        verify(iUserRepository).deleteById(userIdToDelete);
    }


    // --------------- Negativ test for deleteById() ---------

    @Test
    public void test_deleteById_fails() {

        // --------- Arrange ---------

        // Project manager må ikke slette en bruger
        UserViewDTO sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "pm@pm.com",
                Role.PROJECT_MANAGER
        );

        int userIdToDelete = 3;

        // --------- Act + Assert ---------
        UserFriendlyException exception = assertThrows(UserFriendlyException.class, () ->
                userService.deleteById(userIdToDelete, sessionProjectManager)
        );

        assertEquals("Kun Admin har adgang til denne funktion", exception.getMessage());
        assertEquals("/users/profile", exception.getRedirectPath());

        // --------- Verify ---------

        // Ingen kald til repository må ske
        verifyNoInteractions(iUserRepository);
    }

    //------------------------------------- Login() -------------------------------------

    @Test
    public void test_login() {

        // --------- Arrange ---------


        // Email og password, som admin indtaster (password er ikke hashed endnu)
        String email = "admin@admin.com";
        String rawPassword = "notHashedAdminPassword";
        String hashedPassword = "hashedAdminPassword";

        // Simuler en bruger fra databasen – brugeren har et hashed password
        User adminUser = new User(
                1,
                "Admin",
                "LastNameAdmin",
                email,
                hashedPassword,
                Role.ADMIN
        );

        // Mock: Når der søges efter e-mailen, returnér den oprettede bruger (admin)
        when(iUserRepository.readByEmail(email)).thenReturn(adminUser);

        // Mock: Når input-password sammenlignes med det hashede password, returnér true (match)
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        // --------- Act ---------

        // Kald login-metoden – den burde returnere en UserViewDTO, da brugeren findes og password matcher
        UserViewDTO adminDTO = userService.login(email, rawPassword);

        // --------- Assert ---------

        // Tjek at login ikke returnerer null (bruger blev fundet og godkendt)
        assertNotNull(adminDTO);
        // Tjek at e-mailen matcher den, vi loggede ind med
        assertEquals(email, adminDTO.getEmail());
        // Tjek at rollen også matcher den oprindelige bruger
        assertEquals(Role.ADMIN, adminDTO.getRole());

        // Verificér at repository blev kaldt med den korrekte e-mail
        verify(iUserRepository).readByEmail(email);

        // Verificér at passwordEncoder blev brugt til at matche input med hash
        verify(passwordEncoder).matches(rawPassword, hashedPassword);
    }
    // --------------- Negativ test for Login() --------------
    @Test
    public void test_login_fails() {

        // Tester at project manager ikke kan logge ind med forkert adgangskode

        // --------- Arrange ---------

        // Email og forkert password som Project Manager forsøger at logge ind med
        String email = "pm@project.com";
        String wrongPassword = "wrongPassword";
        String correctHashedPassword = "hashedCorrectPassword";

        // Simuler en Project Manager fra databasen
        User projectManager = new User(
                2,
                "Project",
                "Manager",
                email,
                correctHashedPassword,
                Role.PROJECT_MANAGER
        );

        // Mock: Brugeren findes i databasen
        when(iUserRepository.readByEmail(email)).thenReturn(projectManager);

        // Mock: Password matcher ikke (brugeren indtaster forkert adgangskode)
        when(passwordEncoder.matches(wrongPassword, correctHashedPassword)).thenReturn(false);

        // --------- Act + Assert ---------

        // Forvent at login-metoden kaster en UserFriendlyException med korrekt besked og redirect
        UserFriendlyException exception = assertThrows(UserFriendlyException.class, () ->
                userService.login(email, wrongPassword)
        );

        // Tjek at besked og redirect path matcher den du har i login-metoden
        assertEquals("Adgangskoden er forkert.", exception.getMessage());
        assertEquals("/login", exception.getRedirectPath());

        // --------- Verify ---------

        // Verificér at brugeren blev slået op
        verify(iUserRepository).readByEmail(email);

        // Verificér at password blev forsøgt matchet
        verify(passwordEncoder).matches(wrongPassword, correctHashedPassword);
    }

    //----------------- Henter UserUpdateDTO for GET-mapping: showEditUser() ------------

    @Test
    public void test_getUserUpdateDTOById() {

        // Tester at metoden henter en bruger i samme UserService-klasse og konverterer denne til en
        // update-formular til weblaget


        // --------- Arrange ---------

        int userId = 2;

        // Vi mocker brugeren, som readById() skal returnere
        UserViewDTO mockUserViewDTO = new UserViewDTO(
                userId,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Når readById kaldes, returneres mockUserDTO
        // Brug spy for at verificere kald til metode i samme klasse
        UserService spyService = Mockito.spy(userService);
        doReturn(mockUserViewDTO).when(spyService).readById(userId);

        // --------- Act ---------
        UserUpdateDTO result = spyService.getUserUpdateDTOById(userId);

        // --------- Assert ---------
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("ProjectManager", result.getFirstName());
        assertEquals("LastNameProjectManager", result.getLastName());
        assertEquals("projectmanager@projectmanager.com", result.getEmail());
        assertEquals("", result.getPassword()); // password skal være tomt
        assertEquals(Role.PROJECT_MANAGER, result.getRole());

        // --------- Verify ---------
        verify(spyService).readById(userId); // sikre at den faktisk blev kaldt
    }

    //------------------------------ Henter/håndterer PasswordHash ----------------------
    @Test
    public void test_getPasswordHash() {

        // --------- Fælles Arrange ---------

        // Admin er logget ind og har rettigheder
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN
        );

        // Eksisterende bruger i databasen med hashed password
        User existingDeveloper = new User(
                3,
                "Developer",
                "LastNameDeveloper",
                "developer@developer.com",
                "hashedDeveloperPassword",
                Role.DEVELOPER
        );

        // Når UserService kalder readById(3), returneres eksisterende bruger
        lenient().when(iUserRepository.readById(3)).thenReturn(existingDeveloper);

        // ArgumentCaptor fanger det User-objekt, der sendes til update
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);


        // ==================== CASE 1 ====================
        // Password = null → behold eksisterende hash

        UserUpdateDTO updateNullPassword = new UserUpdateDTO(
                3, "Developer", "LastNameDeveloper", "developer@developer.com", null, Role.DEVELOPER
        );

        userService.updateUser(updateNullPassword, sessionAdmin);
        verify(iUserRepository).update(userCaptor.capture());
        assertEquals("hashedDeveloperPassword", userCaptor.getValue().getPasswordHash());

        // Ryd mocks
        reset(iUserRepository, passwordEncoder);
        lenient().when(iUserRepository.readById(3)).thenReturn(existingDeveloper);


        // ==================== CASE 2 ====================
        // Password = " " → behold eksisterende hash

        UserUpdateDTO updateBlankPassword = new UserUpdateDTO(
                3, "Developer", "LastNameDeveloper", "developer@developer.com", "   ", Role.DEVELOPER
        );

        userService.updateUser(updateBlankPassword, sessionAdmin);
        verify(iUserRepository).update(userCaptor.capture());
        assertEquals("hashedDeveloperPassword", userCaptor.getValue().getPasswordHash());

        reset(iUserRepository, passwordEncoder);
        lenient().when(iUserRepository.readById(3)).thenReturn(existingDeveloper);


        // ==================== CASE 3 ====================
        // Password = samme som eksisterende hash → behold eksisterende hash

        when(passwordEncoder.matches("samePassword", "hashedDeveloperPassword")).thenReturn(true);

        UserUpdateDTO updateSamePassword = new UserUpdateDTO(
                3, "Developer", "LastNameDeveloper", "developer@developer.com", "samePassword", Role.DEVELOPER
        );

        userService.updateUser(updateSamePassword, sessionAdmin);
        verify(iUserRepository).update(userCaptor.capture());
        assertEquals("hashedDeveloperPassword", userCaptor.getValue().getPasswordHash());

        reset(iUserRepository, passwordEncoder);
        lenient().when(iUserRepository.readById(3)).thenReturn(existingDeveloper);


        // ==================== CASE 4 ====================
        // Password = nyt → skal hashes på ny

        when(passwordEncoder.matches("newPassword", "hashedDeveloperPassword")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNewPassword");

        UserUpdateDTO updateNewPassword = new UserUpdateDTO(
                3, "Developer", "LastNameDeveloper", "developer@developer.com", "newPassword", Role.DEVELOPER
        );

        userService.updateUser(updateNewPassword, sessionAdmin);
        verify(iUserRepository).update(userCaptor.capture());
        assertEquals("hashedNewPassword", userCaptor.getValue().getPasswordHash());

        // --------- Verify hashing blev kaldt korrekt ---------
        verify(passwordEncoder).encode("newPassword");
    }



                //------------------------------------ DTO-Mappings ---------------------------------
    // --- Henter projekter ud fra brugerID ---
    // --- Henter subprojekter ud fra brugerID ---
    // --- Henter tasks ud fra brugerID ---
    // --- Henter subtasks ud fra brugerID ---
    // --- Henter time entries ud fra brugerID ---

}
