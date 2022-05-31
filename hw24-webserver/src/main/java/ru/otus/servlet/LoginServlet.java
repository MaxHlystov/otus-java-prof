package ru.otus.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static ru.otus.servlet.AuthorizationFilter.LOGIN_REDIRECT_URI;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String REDIRECT_URL = "redirect";

    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";


    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> props = new HashMap<>();
        var from = request.getParameter(LOGIN_REDIRECT_URI);
        props.put(REDIRECT_URL, from);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, props));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        String redirect_url = request.getParameter(REDIRECT_URL);
        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect(redirect_url == null ? "/clients" : redirect_url);
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }

    }

}
