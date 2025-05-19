package com.example.estimationtool.controller;

import com.example.estimationtool.toolbox.dto.TaskWithSubTasksDTO;
import com.example.estimationtool.toolbox.dto.TaskWithTimeEntriesDTO;
import com.example.estimationtool.toolbox.dto.TaskWithUsersDTO;
import com.example.estimationtool.toolbox.dto.UserViewDTO;
import com.example.estimationtool.model.Task;
import com.example.estimationtool.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("tasks")
public class TaskController {

    private final TaskService taskService;

    // Dependency Injection af TaskService i constructoren
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    private UserViewDTO getCurrentUser(HttpSession session) {
        return (UserViewDTO) session.getAttribute("currentUser");
    }

    //------------------------------------ Hent Create() -------------------------------

    // TODO - DONE

    @GetMapping("/create")
    public String showCreateTask(@RequestParam int subProjectId,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en task.");
            return "redirect:/login";
        }

        Task task = new Task();
        task.setSubProjectId(subProjectId);  // Forbinder task med det givne subprojekt

        model.addAttribute("task", task);
        return "task/create-task";
    }

    //------------------------------------ Create() ------------------------------------

    // TODO - DONE
    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") Task task,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at oprette en ny task.");
            return "redirect:/login";
        }

        taskService.createTask(currentUser, task);

        redirectAttributes.addFlashAttribute("success", "Opgaven blev oprettet");

        return "redirect:/subprojects/" + task.getSubProjectId() + "/tasks";

        }

    //------------------------------------ Read() --------------------------------------

    // TODO - DONE
    @GetMapping("/list")
    public String showAllTasks(Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se alle tasks.");
            return "redirect:/login";
        }

        List<Task> taskList = taskService.readAll();

        model.addAttribute("tasks", taskList);

        return "task/task-list";

    }
    // TODO - DONE

    @GetMapping("/{taskId}")
    public String showTask(@PathVariable int taskId,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes
                           ) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om brugeren er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at se en task.");
            return "redirect:/login";
        }

        Task task = taskService.readById(taskId);
        model.addAttribute("task", task);

        return "task/task-detail";

    }
    //------------------------------------ Hent Update() -------------------------------
// TODO - DONE
    @GetMapping("/edit/{taskId}")
    public String showEditTask(@PathVariable int taskId,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at redigere en task.");
            return "redirect:/login";
        }

        Task task = taskService.readById(taskId);

        model.addAttribute("task", task);

        return "task/edit-task";

    }
    //------------------------------------ Update() ------------------------------------

    // TODO - DONE
    @PostMapping("/update")
    public String updateTask(@ModelAttribute("task") Task task,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjekker om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Log ind for at opdatere en task.");
            return "redirect:/login";
        }

        taskService.updateTask(task);

        // Tilføj succesbesked som flash-attribut (vises efter redirect)
        redirectAttributes.addFlashAttribute("success", "Task blev opdateret.");

        return "redirect:/tasks/" + task.getTaskId(); // Redirect til task-detail

    }
    //------------------------------------ Delete() ------------------------------------

    // TODO - DONE

    @PostMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable int taskId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne slette en task.");
            return "redirect:/login";
        }

        // Henter task for at hente dennes subprojectID til redirect
        Task task = taskService.readById(taskId);
        int subProjectId = task.getSubProjectId();

        taskService.deleteById(taskId);

        redirectAttributes.addFlashAttribute("success", "Task blev slettet.");

        // Redirect: subproject-with-tasks.html
        return "redirect:/subprojects/" + subProjectId + "/tasks";

    }

    //---------------------------------- DTO read() ------------------------------------

    // -------------------- Viser en task's tilknyttede brugere ------------------------

    // TODO - DONE
    @GetMapping("/{taskId}/users")
    public String showTaskWithUsers(@PathVariable int taskId,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        // Tjek om bruger er logget ind
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at se brugere tilknyttet en task.");
            return "redirect:/login";
        }

        // Henter task + brugere
        TaskWithUsersDTO taskWithUsersDTO = taskService.readAllUsersByTaskId(taskId);


        // Viser ikke-tilknyttede brugere (til POST-formularen)
        List<UserViewDTO> unassignedUsers = taskService.readAllUnAssignedUsers(taskId);

        // Tilføjer til model
        model.addAttribute("taskWithUsers", taskWithUsersDTO);
        model.addAttribute("unassignedUsers", unassignedUsers);

        // Returnerer HTML-side
        return "task/task-with-users";
    }

    // -------------------- Viser en task's tilknyttede subtasks ---------------------

    // TODO - DONE
    @GetMapping("/{taskId}/subtasks")
    public String showTaskWithSubTasks(@PathVariable int taskId,
                                       HttpSession session,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {


        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at se en task's tilknyttede subtasks.");
            return "redirect:/login";
        }

        TaskWithSubTasksDTO taskWithSubTasksDTO = taskService.readAllSubTasksByTaskId(taskId);
        model.addAttribute("taskWithSubTasks", taskWithSubTasksDTO);

        return "task/task-with-subtasks";
    }

    // -------------------- Viser en task's tilknyttede timeEntries  ------------------

    @GetMapping("/{taskId}/timeentries")
    public String showTaskWithTimeEntries(@PathVariable int taskId,
                                          HttpSession session,
                                          Model model,
                                          RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at se tidsregistreringer for en task.");
            return "redirect:/login";
        }

        TaskWithTimeEntriesDTO dto = taskService.readAllTimeEntriesByTaskId(taskId);
        model.addAttribute("taskWithTimeEntries", dto);

        return "task/task-with-timeentries";
    }


    //---------------------------- POST Assign User to Task ---------------------------

    @PostMapping("/tasks/{taskId}/assignusers")
    public String assignUsersToTask(@PathVariable int taskId,
                                    @RequestParam("userIds") List<Integer> userIds,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        // Henter og sætter session for Thymeleaf
        UserViewDTO currentUser = getCurrentUser(session);
        session.setAttribute("currentUser", currentUser);

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "Du skal være logget ind for at kunne tildele brugere en task.");
            return "redirect:/login";
        }

        taskService.assignUsersToTask(currentUser, userIds, taskId);

        redirectAttributes.addFlashAttribute("success", "Brugere blev tildelt en task.");
        return "redirect:/tasks/" + taskId + "/users";
    }



}




