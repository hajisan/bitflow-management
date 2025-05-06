package com.example.estimationtool.unitTest.controller;


import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.model.enums.Role;
import com.example.estimationtool.model.User;
import com.example.estimationtool.controller.UserController;
import com.example.estimationtool.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // DEAKTIVERER SPRING SECURITY, så tests ikke fejler pga. adgangskontrol
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

   // @MockBean
    private UserService userService; //Mocker @Service = injiceres i @UserController


      /* Skabelon:

    //--------- Arrange ---------

    //--------- Act -------------

    //--------- Assert ----------

     */


    //--------------------------------- Hent Create() ----------------------------------
    @Test
    void test_showCreateUser() throws Exception {

        // Tilføjer en bruger, der er logget ind til sessionen
        User mockUser = new User(99, "Admin", "User", "admin@admin.com", "hashedPassword", Role.ADMIN);

        mockMvc.perform(get("/users/create")
                        .sessionAttr("currentUser", mockUser))
                .andExpect(status().isOk()) // Forventer status 200
                .andExpect(view().name("user/create-user")) // Forventer dette view-navn
                .andExpect(model().attributeExists("user")); //Forventer at modellen "user" sendes med til Thymeleaf
    }

    //------------------------------------ Create() ------------------------------------

    @Test
    void test_createUser() throws Exception {

        //--------- Arrange ---------
        User adminUser = new User(
                1,
                "Sofie",
                "Rytter",
                "rytterriet@gmail.com",
                "rytterKoden",
                Role.ADMIN
        );

        //--------- Act -------------

        mockMvc.perform(post("/users/create")
                .param("firstName", "Sofie") //Sætter parametrene fra UserRegistrationDTO
                .param("lastName", "Rytter")
                .param("email", "rytterriet@gmail.com" )
                .param("password", "rytterKoden")
                .sessionAttr("currentUser", adminUser))
                .andExpect(status().is3xxRedirection()) // Assert - Tjekker at den redirecter
                .andExpect(redirectedUrl("/users")) // Tjekker at den redirecter til user-list
                .andExpect(flash().attributeExists("succes")); // Tjekker at succes-besked findes

        // Tjekker at createUser() kaldes fra @UserService
       // verify(userService).createUser(any(User.class), any(UserRegistrationDTO.class));


    }

    //------------------------------------ Read() --------------------------------------

    @Test
    void showAllUsers() throws Exception {

        //--------- Arrange ---------

        List<UserViewDTO> users = List.of(
                new UserViewDTO(1, "Bob", "Marley", "marley@example.com", Role.DEVELOPER),
                new UserViewDTO(2, "Tut", "Jensen", "tut@example.com", Role.PROJECT_MANAGER)
        );

        Mockito.when(userService.readAll()).thenReturn(users);

        // Tilføjer en bruger, der er logget ind til sessionen
        User mockUser = new User(99, "Admin", "User", "admin@admin.com", "hashedPassword", Role.ADMIN);


        //--------- Act -------------

        mockMvc.perform(get("/users")
                        .sessionAttr("currentUser", mockUser))
                .andExpect(status().isOk()) // Assert
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", users)); // Sammenligner hele listen
    }


    @Test
    void showUser() throws Exception {

        //--------- Arrange ---------

        UserViewDTO expectedUserDTO = new UserViewDTO(1, "Bob", "Marley", "marley@gmail.com", Role.DEVELOPER);
        Mockito.when(userService.readById(1)).thenReturn(expectedUserDTO);

        // Tilføjer en bruger, der er logget ind til sessionen
        User mockUser = new User(99, "Admin", "User", "admin@admin.com", "hashedPassword", Role.ADMIN);

        //--------- Act -------------
        mockMvc.perform(get("/users/1")
                        .sessionAttr("currentUser", mockUser))
                .andExpect(status().isOk()) //Assert
                .andExpect(view().name("user/user-details"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", expectedUserDTO));

    }


    //------------------------------------ Hent Update() -------------------------------


    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
