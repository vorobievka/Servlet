package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class MainServlet extends HttpServlet {
    private PostController controller;

    public enum Method {
        GET,
        POST,
        DELETE
    }

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    final Set<String> allowedMethods = Set.of(GET, POST, DELETE);

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            System.out.println(method);
            if (allowedMethods.contains(method)) {
                switch (method) {
                    case GET:
                        if (path.equals("/api/posts")) {
                            controller.all(resp);
                            return;
                        } else if (path.matches("/api/posts/\\d+")) {
                            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                            System.out.println("id= " + id);
                            controller.getById(id, resp);
                            return;
                        } else {
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            resp.setContentType("text/plain");
                            resp.getWriter().println("Hello from MainServlet!");
                        }
                    case POST:
                        if (path.equals("/api/posts")) {
                            controller.save(req.getReader(), resp);
                            return;
                        }
                    case DELETE:
                        if (path.matches("/api/posts/\\d+")) {
                            final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                            System.out.println("id for remove: " + id);
                            controller.removeById(id, resp);
                            return;
                        }
                }

            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
