package com.example.estimationtool.unitTest.controller;


import com.example.estimationtool.dto.UserRegistrationDTO;
import com.example.estimationtool.enums.Role;
import com.example.estimationtool.user.User;
import com.example.estimationtool.user.UserController;
import com.example.estimationtool.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // DEAKTIVERER SPRING SECURITY, så tests ikke fejler pga. adgangskontrol
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; //Mocker @Service = injiceres i @UserController


      /* Skabelon:

    //--------- Arrange ---------

    //--------- Act -------------

    //--------- Assert ----------

     */


    //--------------------------------- Hent Create() ----------------------------------
    @Test
    void test_showCreateUser() throws Exception {
        mockMvc.perform(get("/users/create"))
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
                .sessionAttr("adminUser", adminUser))
                .andExpect(status().is3xxRedirection()) // Assert - Tjekker at den redirecter
                .andExpect(redirectedUrl("/user-list")) // Tjekker at den redirecter til user-list
                .andExpect(flash().attributeExists("succes")); // Tjekker at succes-besked findes

        // Tjekker at createUser() kaldes fra @UserService
        verify(userService).createUser(any(User.class), any(UserRegistrationDTO.class));





        //--------- Assert ----------



    }

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
