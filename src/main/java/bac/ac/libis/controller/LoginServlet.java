package bac.ac.libis.controller;

import bac.ac.libis.dao.MemberDao;
import bac.ac.libis.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            return;
        }

        try {
            Member member = new MemberDao().findByEmail(email);

            if (member != null && password.equals(member.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("member", member);

                response.sendRedirect(request.getContextPath() + "/catalogue");
                return;
            }
        } catch (Exception e) {
            throw new ServletException("Login failed", e);
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
    }
}