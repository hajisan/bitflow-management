package com.example.estimationtool.service;


import com.example.estimationtool.model.User;
import com.example.estimationtool.repository.interfaces.ITaskRepository;
import com.example.estimationtool.model.Task;

import java.util.ArrayList;
import java.util.List;

import com.example.estimationtool.repository.interfaces.IUserRepository;
import com.example.estimationtool.toolbox.dto.SubProjectWithUsersDTO;
import com.example.estimationtool.toolbox.dto.TaskWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.toolbox.roleCheck.RoleCheck;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final ITaskRepository iTaskRepository;
    private final IUserRepository iUserRepository;

    public TaskService(ITaskRepository iTaskRepository, IUserRepository iUserRepository) {
        this.iTaskRepository = iTaskRepository;
        this.iUserRepository = iUserRepository;
    }

    //------------------------------------ Create() ------------------------------------

    public Task createTask(Task task) {
        return iTaskRepository.create(task);

    }

    //------------------------------------ Read() --------------------------------------

    public List<Task> readAll() {
        return iTaskRepository.readAll();
    }

    public Task readById(int id) {
        return iTaskRepository.readById(id);
    }

    //------------------------------------ Update() ------------------------------------

    public Task updateTask(Task task) {
        return iTaskRepository.update(task);
    }
    //------------------------------------ Delete() ------------------------------------

    public void deleteById(int id) {
        iTaskRepository.deleteById(id);
    }

    //------------------------------------ DTO'er -------------------------------------

    // --- Henter tasks ud fra brugerID for UserService ---

    public List<Task> readAllTasksByUserId(int userId) {

        return iTaskRepository.readAllByUserId(userId);
    }

    // --- Henter brugere ud fra taskID ---

    public TaskWithUsersDTO readAllUsersByTaskId(int taskId) {

        // Læser task ud fra taskID
        Task task = iTaskRepository.readById(taskId);

        // Læser tilknyttede brugere
        List<User> users = iUserRepository.readAllByTaskId(taskId);

        // Konverter til UserViewDTO
        List<UserViewDTO> userViewDTOList = new ArrayList<>();
        for (User user : users) {
            UserViewDTO userViewDTO = new UserViewDTO(
                    user.getUserId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole()
            );
            userViewDTOList.add(userViewDTO); // Tilføjer hver UserDTO til listen
        }
        // Returnerer task + liste af UserViewDTO
        return new TaskWithUsersDTO(task, userViewDTOList);

    }

    //---------------------------------- Assign User --------------------------------

    // ----------------- Task tildeles en bruger efter oprettelse -------------------

    public void assignUserToTask(UserViewDTO currentUser, int userId, int taskId) {
        RoleCheck.ensureAdminOrProjectManager(currentUser.getRole());
        iTaskRepository.assignUserToTask(userId, taskId);
    }

}
