package ua.goit.controller;


import lombok.SneakyThrows;
import ua.goit.model.Project;
import ua.goit.repository.CrudRepository;
import ua.goit.repository.RepositoryFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/project/*")
public class ProjectServlet extends HttpServlet {

    private CrudRepository<Project, Long> projectRepository;

    @Override
    public void init() {
        projectRepository = RepositoryFactory.of(Project.class);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getPathInfo();
        if (action.startsWith("/findProject")) {
            req.getRequestDispatcher("/view/project/find_project.jsp").forward(req, resp);
        }
        if (action.startsWith("/createProject")) {
            req.getRequestDispatcher("/view/project/create_project.jsp").forward(req, resp);
        }
        if (action.startsWith("/updateProject")) {
            req.getRequestDispatcher("/view/project/update_project.jsp").forward(req, resp);
        }
        if (action.startsWith("/deleteProject")) {
            req.getRequestDispatcher("/view/project/delete_project.jsp").forward(req, resp);
        }
        if (action.startsWith("/allProject")) {
            List<Project> projectList = projectRepository.findAll();
            req.setAttribute("projects", projectList);
            req.getRequestDispatcher("/view/project/all_projects.jsp").forward(req, resp);
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getPathInfo();
        if (action.startsWith("/createProject")) {
            createProject(req, resp);
        }
        if (action.startsWith("/findProject")) {
            findProject(req, resp);
        }
        if (action.startsWith("/deleteProject")) {
            deleteProject(req, resp);
        }
        if (action.startsWith("/updateProject")) {
            updateProject(req, resp);
        }
    }

    @SneakyThrows
    private void createProject(HttpServletRequest req, HttpServletResponse resp) {
        Project project = buildProject(req);
        req.getRequestDispatcher("/view/project/create_project.jsp").forward(req, resp);
        projectRepository.create(project);
        req.setAttribute("message", "Project created successful: " + project);
    }

    @SneakyThrows
    private void findProject(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("id"));
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            req.setAttribute("message", "Request project not found");
            req.getRequestDispatcher("/view/project/find_project.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", String.format("Project by id is found: %s", project));
            req.getRequestDispatcher("/view/project/find_project.jsp").forward(req, resp);
        }
    }

    @SneakyThrows
    private void deleteProject(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf((req.getParameter("id")));
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            req.setAttribute("message", "Request project not found");
        } else {
            projectRepository.deleteById(id);
            req.setAttribute("message", "Delete project successful");
        }
        req.getRequestDispatcher("/view/project/delete_project.jsp").forward(req, resp);
    }

    @SneakyThrows
    private void updateProject(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf((req.getParameter("id")));
        Optional<Project> project = projectRepository.findById(id);
        String newCost = req.getParameter("cost");
        project.get().setCost(Long.valueOf(newCost));
        projectRepository.update(project.get());
        req.setAttribute("message", "Project updated");
        req.getRequestDispatcher("/view/project/update_project.jsp").forward(req, resp);
    }

    private Project buildProject(HttpServletRequest request) {
        return Project.builder()
                .id(Long.valueOf(request.getParameter("id")))
                .name(request.getParameter("name"))
                .field(request.getParameter("field"))
                .cost(Long.valueOf(request.getParameter("cost")))
                .createData(Date.valueOf(request.getParameter("create_date")))
                .build();
    }
}
