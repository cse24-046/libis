package bac.ac.libis.controller;

import bac.ac.libis.model.DataProvider;
import bac.ac.libis.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet({"/login", "/loginProcess"})
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Member authenticatedMember = null;

        for (Member member : DataProvider.getMembers()) {
            if (member.getEmail().equalsIgnoreCase(email) && member.getPassword().equals(password)) {
                authenticatedMember = member;
                break;
            }
        }

        if (authenticatedMember != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loggedMember", authenticatedMember);
            request.setAttribute("member", authenticatedMember);
            request.setAttribute("bookList", DataProvider.getBooks());
            request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
            return;
        }

        request.setAttribute("errorMessage", "Invalid email or password.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
