package com.example.estimationtool.unitTest.controller;


import com.example.estimationtool.model.*;
import com.example.estimationtool.model.enums.Status;
import com.example.estimationtool.toolbox.controllerAdvice.UserFriendlyException;
import com.example.estimationtool.toolbox.dto.*;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.controller.UserController;
import com.example.estimationtool.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;

import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // DEAKTIVERER SPRING SECURITY, så tests ikke fejler pga. adgangskontrol
public class UserControllerTest {

// MockMVC er et Spring Boot framework: simulerer HTTP-forespørgsler og verificerer, hvordan controlleren
//håndterer dem.


    @Autowired
    private MockMvc mockMvc;


    // For at kunne kalde metoder fra service
    @MockBean
    private UserService userService; //Mocker @Service = injiceres i @UserController


    //--------------------------------- Efter login -----------------------------------

    @Test
    void test_getFrontPage() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        // --------- Act + Assert ---------
        mockMvc.perform(get("/users/profile")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk())
                .andExpect(view().name("user/front-page"));
    }


    //--------------------------------- Hent Create() ----------------------------------
    @Test
    void test_showCreateUser() throws Exception {

        // Tilføjer en bruger, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);


        mockMvc.perform(get("/users/create")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Forventer status 200
                .andExpect(view().name("user/create-user")) // Forventer dette view-navn
                .andExpect(model().attributeExists("user")); //Forventer at modellen "user" sendes med til Thymeleaf
    }

    //------------------------------------ Create() ------------------------------------

    @Test
    void test_createUser() throws Exception {

        //--------- Arrange ---------

        // Tilføjer admin, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);


        // Admin opretter project manager
        UserRegistrationDTO registreProjectManager = new UserRegistrationDTO(
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                "notHashedProjectManagerPassword",
                Role.PROJECT_MANAGER
        );


        //--------- Act ------------- Opretter en Projektleder

        mockMvc.perform(post("/users/create")
                        .param("firstName", "ProjectManager") //Sætter parametrene fra UserRegistrationDTO
                        .param("lastName", "LastNameProjectManager")
                        .param("email", "projectmanager@projectmanager.com")
                        .param("password", "hashedProjectManagerPassword")
                        .sessionAttr("currentUser", sessionAdmin)) // Admin opretter bruger
                .andExpect(status().is3xxRedirection()) // Assert - Tjekker at den redirecter
                .andExpect(redirectedUrl("/users/list")) // Tjekker at den redirecter til user-list
                .andExpect(flash().attributeExists("success")); // Tjekker at succes-besked findes

        //Tjekker at createUser() kaldes fra @UserService når admin er i session
        verify(userService).createUser(eq(sessionAdmin), any(UserRegistrationDTO.class));

        // eq = metoden kaldes med præcis admin-objektet

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).createUser(sessionAdmin,registreProjectManager);
    }

    // --------------------- Negativ test for createUser -----------------------

    @Test
    void test_createUser_fail() throws Exception {
        // --------- Arrange ---------

        // En Project Manager forsøger at oprette en bruger – skal fejle
        UserViewDTO sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Simuler at service nægter adgang og smider UserFriendlyException
        doThrow(new UserFriendlyException("Kun Admin har adgang til denne funktion", "/users/profile"))
                .when(userService).createUser(eq(sessionProjectManager), any(UserRegistrationDTO.class));

        // --------- Act + Assert ---------

        mockMvc.perform(post("/users/create")
                        .param("firstName", "Developer")
                        .param("lastName", "LastNameDeveloper")
                        .param("email", "developer@developer.com")
                        .param("password", "hashedDeveloperPassword")
                        .sessionAttr("currentUser", sessionProjectManager))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Kun Admin har adgang til denne funktion"));


    }


        //------------------------------------ ReadAll() -----------------------------------

    @Test
    void test_showAllUsers() throws Exception {

        //--------- Arrange ---------

        // Tilføjer admin, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        // Henter en liste af brugere
        List<UserViewDTO> userViewDTOMockList = List.of(
                new UserViewDTO(2, "ProjectManager", "LastNameProjectManager", "projectmanager@projectmanager.com", Role.PROJECT_MANAGER),
                new UserViewDTO(3, "Developer", "LastNameDeveloper", "developer@developer.com", Role.DEVELOPER)
        );

        when(userService.readAll()).thenReturn(userViewDTOMockList);


        //--------- Act -------------

        mockMvc.perform(get("/users/list")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attributeExists("users")) //Tjekker om users findes i modellen
                .andExpect(model().attribute("users", userViewDTOMockList)); // Tjekker at users har den rigtige værdi

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAll();

    }
    //------------------------------------ Read() --------------------------------------

    @Test
    void test_showUser() throws Exception {

        //--------- Arrange ---------

        // Tilføjer admin, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        // Brugeren der skal hentes
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );


        // Mock service til at returnere brugeren
        when(userService.readById(2)).thenReturn(projectManagerMockDTO);


        //--------- Act -------------
        mockMvc.perform(get("/users/2")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-detail"))
                .andExpect(model().attributeExists("user")) //Tjekker om user findes i modellen
                .andExpect(model().attribute("user", projectManagerMockDTO)); // Tjekker at user har den rigtige værdi


        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readById(2);
    }


    //------------------------------------ Hent Update() -------------------------------

    @Test
    void test_showEditUser() throws Exception {

    //--------- Arrange ---------
    // Tilføjer admin, der er logget ind til sessionen
    UserViewDTO sessionAdmin = new UserViewDTO(
            1,
            "Admin",
            "LastNameAdmin",
            "admin@admin.com",
            Role.ADMIN);

    // Henter update-formularen for brugeren, der skal redigeres
    UserUpdateDTO userUpdateDTO = new UserUpdateDTO(
            2,
            "ProjectManager",
            "LastNameProjectManager",
            "projectmanager@projectmanager.com",
            "hashedProjectManagerPassword",
            Role.PROJECT_MANAGER);

        when(userService.getUserUpdateDTOById(2)).thenReturn(userUpdateDTO);


    // --------- Act ---------

    mockMvc.perform(get("/users/edit/2")
            .sessionAttr("currentUser", sessionAdmin))
            .andExpect(status().isOk()) // Assert
            .andExpect(view().name("user/edit-user"))
            .andExpect(model().attributeExists("userUpdateDTO"))
            .andExpect(model().attribute("userUpdateDTO", userUpdateDTO));
}

    // --------------- Negativ test for showEditUser() --------------
    @Test
    void test_showEditUser_fail() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        int userIdToEdit = 3;

        // Simuler at service nægter adgang og smider UserFriendlyException
        doThrow(new UserFriendlyException("Kun Admin har adgang til denne funktion", "/users/profile"))
                .when(userService).getUserUpdateDTOById(userIdToEdit);

        // --------- Act + Assert ---------
        mockMvc.perform(get("/users/edit/" + userIdToEdit)
                        .sessionAttr("currentUser", sessionProjectManager))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Kun Admin har adgang til denne funktion"));
    }


    //------------------------------------ Update() ------------------------------------

    @Test
    void test_updateUser() throws Exception {
        //--------- Arrange ---------
        // Tilføjer admin, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);


        // --------- Act ---------

        // Admin vil redigere project manager
        mockMvc.perform(post("/users/update")
                        .sessionAttr("currentUser", sessionAdmin)
                        .param("userId", "2")
                        .param("firstName", "ProjectManager")
                        .param("lastName", "LastNameProjectManager")
                        .param("email", "projectmanagerNewEmail@projectmanagerNewEmail.com")
                        .param("password", "hashedProjectManagerPassword")
                        .param("role", "PROJECT_MANAGER"))
                .andExpect(status().is3xxRedirection()) // Assert
                .andExpect(redirectedUrl("/users/2"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(flash().attribute("success", "Brugeren blev opdateret."));

        // Tjekker at når admin er i session, kaldes update-formularen
        verify(userService).updateUser(any(UserUpdateDTO.class), eq(sessionAdmin));

        // eq = metoden kaldes med præcis admin-objektet

    }

    // --------------- Negativ test for updateUser() --------------

    @Test
    void test_updateUser_fail() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionProjectManager = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Simuler adgangsnægtelse
        doThrow(new UserFriendlyException("Kun Admin har adgang til denne funktion", "/users/profile"))
                .when(userService).updateUser(any(UserUpdateDTO.class), eq(sessionProjectManager));

        // --------- Act + Assert ---------
        mockMvc.perform(post("/users/update")
                        .sessionAttr("currentUser", sessionProjectManager)
                        .param("userId", "3")
                        .param("firstName", "Developer")
                        .param("lastName", "LastNameDeveloper")
                        .param("email", "developer@developer.com")
                        .param("role", "DEVELOPER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"))
                .andExpect(flash().attributeExists("error"))
                .andExpect(flash().attribute("error", "Kun Admin har adgang til denne funktion"));
    }



    //------------------------------------ Delete() ------------------------------------

    @Test
    void test_deleteUser() throws Exception {

        //--------- Arrange ---------

        // Tilføjer admin, der er logget ind til sessionen
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userIdToDelete = 2; // Project Manager skal slettes


        //--------- Act -------------

        mockMvc.perform(post("/users/delete/" + userIdToDelete)
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().is3xxRedirection()) // Assert
                .andExpect(redirectedUrl("/users/list"))
                .andExpect(flash().attributeExists("success"))
                .andExpect(flash().attribute("success", "Brugeren blev slettet."));

        // Verificér at service blev kaldt korrekt med præcis admin-objekt
        verify(userService).deleteById(eq(userIdToDelete), eq(sessionAdmin));


        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).deleteById(2,sessionAdmin);

    }

    // --------------- Negativ test for deleteUser() --------------


    @Test
    void test_deleteUser_fail() throws Exception {

        // --------- Arrange ---------

        int userIdToDelete = 3; // Developer skal slettes

        // Project Manager er IKKE admin, så det skal fejle
        UserViewDTO sessionProjectManager= new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Simulerer, at service smider exception pga. manglende admin-rettigheder
        doThrow(new UserFriendlyException("Kun Admin har adgang til denne funktion", "/users/profile"))
                .when(userService).deleteById(userIdToDelete, sessionProjectManager);


        // --------- Act + Assert ---------
        mockMvc.perform(post("/users/delete/" + userIdToDelete)
                        .sessionAttr("currentUser", sessionProjectManager))
                .andExpect(status().is3xxRedirection()) // Forventer redirect
                .andExpect(redirectedUrl("/users/profile")) // Redirect fra exception
                .andExpect(flash().attributeExists("error")) // Flash message med fejl
                .andExpect(flash().attribute("error", "Kun Admin har adgang til denne funktion"));
    }


    // -------------------- Viser brugerens tilknyttede projekter ----------------------


    @Test
    void test_showUserWithProjects() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userId = 2; // ID'et vi vil hente projekterne på


        // Brugeren, hvis projekter vi vil hente
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Liste af brugerens projekter
        List<Project> mockProjects = List.of(
                new Project(
                        10,
                        100,
                        40,
                        "Projekt A",
                        "Beskrivelse af Projekt A",
                        LocalDate.of(2025, 6, 1),
                        Status.ACTIVE
                ),
                new Project(
                        20,
                        80,
                        20,
                        "Projekt B",
                        "Beskrivelse af Projekt B",
                        LocalDate.of(2025, 7, 15),
                        Status.INACTIVE
                ));

        UserWithProjectsDTO userWithProjectsDTO = new UserWithProjectsDTO(projectManagerMockDTO, mockProjects);
        when(userService.readAllProjectsByUserId(userId)).thenReturn(userWithProjectsDTO);


        // --------- Act ---------
        mockMvc.perform(get("/users/2/projects")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) //Assert
                .andExpect(view().name("user/user-with-projects"))
                .andExpect(model().attributeExists("userWithProjects"))
                .andExpect(model().attribute("userWithProjects", userWithProjectsDTO));


        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAllProjectsByUserId(2);
    }

    // -------------------- Viser brugerens tilknyttede subprojekter --------------------


    @Test
    void test_showUserWithSubProjects() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userId = 2; // ID'et vi vil hente subprojekter for

        // Brugeren hvis subprojekter vi vil hente
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

       // Liste af brugerens subprojekter
        List<SubProject> mockSubProjects = List.of(
                new SubProject(
                        110,
                        10, // projectId
                        50,
                        20,
                        "Subprojekt A",
                        "Beskrivelse af Subprojekt A",
                        LocalDate.of(2025, 6, 10),
                        Status.ACTIVE
                ),
                new SubProject(
                        120,
                        10, // projectId
                        40,
                        30,
                        "Subprojekt B",
                        "Beskrivelse af Subprojekt B",
                        LocalDate.of(2025, 7, 20),
                        Status.INACTIVE
                )
        );

        UserWithSubProjectsDTO userWithSubProjectsDTO = new UserWithSubProjectsDTO(projectManagerMockDTO, mockSubProjects);
        when(userService.readAllSubProjectsByUserId(userId)).thenReturn(userWithSubProjectsDTO);

        // --------- Act ---------
        mockMvc.perform(get("/users/2/subprojects")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-with-subprojects"))
                .andExpect(model().attributeExists("userWithSubProjects"))
                .andExpect(model().attribute("userWithSubProjects", userWithSubProjectsDTO));

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAllSubProjectsByUserId(2);
    }

    // -------------------- Viser brugerens tilknyttede tasks ------------------------------

    @Test
    void test_showUserWithTasks() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userId = 2; // ID'et vi vil hente tasks for

        // Brugeren hvis tasks vi vil hente
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Liste af brugerens tasks
        List<Task> mockTasks = List.of(
                new Task(
                        110, // subProjectId
                        210, // taskId
                        20,
                        10,
                        "Task A",
                        "Beskrivelse af Task A",
                        LocalDate.of(2025, 6, 10),
                        Status.ACTIVE
                ),
                new Task(
                        110, // subProjectId
                        220,
                        30,
                        25,
                        "Task B",
                        "Beskrivelse af Task B",
                        LocalDate.of(2025, 7, 5),
                        Status.INACTIVE
                )
        );

        UserWithTasksDTO userWithTasksDTO = new UserWithTasksDTO(projectManagerMockDTO, mockTasks);
        when(userService.readAllTasksByUserId(userId)).thenReturn(userWithTasksDTO);

        // --------- Act ---------
        mockMvc.perform(get("/users/2/tasks")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-with-tasks"))
                .andExpect(model().attributeExists("userWithTasks"))
                .andExpect(model().attribute("userWithTasks", userWithTasksDTO));

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAllTasksByUserId(2);
    }

    // ------------------ Viser brugerens tilknyttede subtasks ----------------------------

    @Test
    void test_showUserWithSubTasks() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userId = 2; // ID'et vi vil hente subtasks for

        // Brugeren hvis subtasks vi vil hente
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Liste af brugerens subtasks
        List<SubTask> mockSubTasks = List.of(
                new SubTask(
                        210, // taskId
                        310, // subTaskId
                        10,
                        5,
                        "SubTask A",
                        "Beskrivelse af SubTask A",
                        LocalDate.of(2025, 6, 15),
                        Status.ACTIVE
                ),
                new SubTask(
                        210, // taskId
                        320,
                        15,
                        10,
                        "SubTask B",
                        "Beskrivelse af SubTask B",
                        LocalDate.of(2025, 7, 10),
                        Status.INACTIVE
                )
        );

        UserWithSubTasksDTO userWithSubTasksDTO = new UserWithSubTasksDTO(projectManagerMockDTO, mockSubTasks);
        when(userService.readAllSubTasksByUserId(userId)).thenReturn(userWithSubTasksDTO);

        // --------- Act ---------
        mockMvc.perform(get("/users/2/subtasks")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-with-subtasks"))
                .andExpect(model().attributeExists("userWithSubTasks"))
                .andExpect(model().attribute("userWithSubTasks", userWithSubTasksDTO));

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAllSubTasksByUserId(2);
    }

    // ------------------ Viser brugerens tilknyttede timeEntries -------------------------
    @Test
    void test_showUserWithTimeEntries() throws Exception {

        // --------- Arrange ---------
        UserViewDTO sessionAdmin = new UserViewDTO(
                1,
                "Admin",
                "LastNameAdmin",
                "admin@admin.com",
                Role.ADMIN);

        int userId = 2; // ID på brugeren, vi henter time entries for

        // Brugeren hvis time entries vi vil hente
        UserViewDTO projectManagerMockDTO = new UserViewDTO(
                2,
                "ProjectManager",
                "LastNameProjectManager",
                "projectmanager@projectmanager.com",
                Role.PROJECT_MANAGER
        );

        // Liste af time entries
        List<TimeEntry> mockTimeEntries = List.of(
                new TimeEntry(
                        410, // timeEntryId
                        2, // userId
                        210, // taskId
                        310, // subTaskId
                        2, // hoursSpent
                        LocalDate.of(2025, 5, 10)
                ),
                new TimeEntry(
                        420, // timeEntryId
                        2, // userId
                        220, // taskId
                        320, // subTaskId
                        3, // hoursSpent
                        LocalDate.of(2025, 5, 10)
                )
        );

        UserWithTimeEntriesDTO userWithTimeEntriesDTO = new UserWithTimeEntriesDTO(projectManagerMockDTO, mockTimeEntries);
        when(userService.readAllTimeEntriesByUserId(userId)).thenReturn(userWithTimeEntriesDTO);

        // --------- Act ---------
        mockMvc.perform(get("/users/2/timeentries")
                        .sessionAttr("currentUser", sessionAdmin))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-with-timeentries"))
                .andExpect(model().attributeExists("userWithTimeEntries"))
                .andExpect(model().attribute("userWithTimeEntries", userWithTimeEntriesDTO));

        // Tjekker at metoden kaldes én gang
        verify(userService, times(1)).readAllTimeEntriesByUserId(2);
    }


    //---------------------------------- Session Redirect ----------------------------------

        @Test
        void test_showUser_redirectsWhenNotLoggedIn () throws Exception {
            mockMvc.perform(get("/users/2"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/login"))
                    .andExpect(flash().attributeExists("error"));
        }
    }

