package com.example.estimationtool.unitTest.controller;


import com.example.estimationtool.user.UserController;
import com.example.estimationtool.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.ui.Model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // DEAKTIVERER SPRING SECURITY, så tests ikke fejler pga. adgangskontrol
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    //--------------------------------- Hent Create() ----------------------------------
    @Test
    void test_showCreateUser() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk()) // Forventer status 200
                .andExpect(view().name("user/create-user")) // Forventer dette view-navn
                .andExpect(model().attributeExists("user")); //Forventer at modellen "user" sendes med til Thymeleaf
    }

    //------------------------------------ Create() ------------------------------------

    //------------------------------------ Read() --------------------------------------

    //------------------------------------ Hent Update() -------------------------------

    //------------------------------------ Update() ------------------------------------

    //------------------------------------ Delete() ------------------------------------
}
