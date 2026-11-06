package bac.ac.libis.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.logout();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();                      // not sure but i think it destroys the session
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
}