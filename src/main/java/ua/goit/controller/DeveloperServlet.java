package ua.goit.controller;

import lombok.SneakyThrows;
import ua.goit.model.Developer;
import ua.goit.repository.CrudRepository;
import ua.goit.repository.RepositoryFactory;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/developer/*")
public class DeveloperServlet extends Servlet {

    private CrudRepository developerRepository;


    @Override
    public void init() {
        developerRepository = RepositoryFactory.of(Developer.class);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getPathInfo();
        if (action.startsWith("/findDeveloper")) {
            req.getRequestDispatcher("/view/developer/find_developer.jsp").forward(req, resp);
        }
        if (action.startsWith("/createDeveloper")) {
            req.getRequestDispatcher("/view/developer/create_developer.jsp").forward(req, resp);
        }
        if (action.startsWith("/updateDeveloper")) {
            req.getRequestDispatcher("/view/developer/update_developer.jsp").forward(req, resp);
        }
        if (action.startsWith("/deleteDeveloper")) {
            req.getRequestDispatcher("/view/developer/delete_developer.jsp").forward(req, resp);
        }
        if (action.startsWith("/allDeveloper")) {
            List<Developer> developerList = developerRepository.findAll();
            req.setAttribute("developers", developerList);
            req.getRequestDispatcher("/view/developer/all_developers.jsp").forward(req, resp);
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getPathInfo();
        if (action.startsWith("/createDeveloper")) {
            createDeveloper(req, resp);
        }
        if (action.startsWith("/findDeveloper")) {
            findDeveloper(req, resp);
        }
        if (action.startsWith("/deleteDeveloper")) {
            deleteDeveloper(req, resp);
        }
        if (action.startsWith("/updateDeveloper")) {
            updateDeveloper(req, resp);
        }
    }

    @SneakyThrows
    private void createDeveloper(HttpServletRequest req, HttpServletResponse resp) {
        Developer developer = buildDeveloper(req);
        req.getRequestDispatcher("/view/developer/create_developer.jsp").forward(req, resp);
        developerRepository.create(developer);
        req.setAttribute("message", "Developer create successful: " + developer);
    }

    @SneakyThrows
    private void findDeveloper(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("id").trim());
        Optional<Developer> developerOptional = developerRepository.findById(id);
        if (!developerOptional.isPresent()) {
            req.setAttribute("message", "Request developer not found");
            req.getRequestDispatcher("/view/developer/find_developer.jsp").forward(req, resp);
        } else {
            req.setAttribute("message", String.format("Developer found: %s", developerOptional));
            req.getRequestDispatcher("/view/developer/find_developer.jsp").forward(req, resp);
        }
    }

    @SneakyThrows
    private void deleteDeveloper(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf((req.getParameter("id")));
        Optional<Developer> developer = developerRepository.findById(id);
        if (!developer.isPresent()) {
            req.setAttribute("message", "Request developer not found");
        } else {
            developerRepository.deleteById(id);
            req.setAttribute("message", "Delete developer successful");
        }
        req.getRequestDispatcher("/view/developer/delete_developer.jsp").forward(req, resp);
    }

   @SneakyThrows
    private void updateDeveloper(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf((req.getParameter("id")));
        Optional<Developer> developer = developerRepository.findById(id);
        String newSalary = req.getParameter("salary");
        developer.get().setSalary(Long.valueOf(newSalary));
        developerRepository.update(developer.get());
        req.setAttribute("message", "Developer updated");
        req.getRequestDispatcher("/view/developer/update_developer.jsp").forward(req, resp);
    }

    private Developer buildDeveloper(HttpServletRequest request) {
        return Developer.builder()
                .id(Long.valueOf(request.getParameter("id")))
                .name(request.getParameter("name"))
                .age(Integer.valueOf(request.getParameter("age")))
                .gender(request.getParameter("gender"))
                .salary(Long.valueOf(request.getParameter("salary")))
                .build();
    }
}
