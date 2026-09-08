package bac.ac.libis;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean exists = false;
        for (Member member : LibraryData.members) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            Member newMember = new Member(fullName, email, password);
            LibraryData.members.add(newMember);
            response.sendRedirect("login");
            return;
        }

        // Common error path extracted outside the if block
        request.setAttribute("error", "An account with this email already exists.");
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}