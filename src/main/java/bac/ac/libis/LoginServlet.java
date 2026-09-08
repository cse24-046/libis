package bac.ac.libis;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String identifier = request.getParameter("identifier");
        String password = request.getParameter("password");

        Member loggedInMember = null;
        for (Member member : LibraryData.members) {
            if (member.getEmail().equalsIgnoreCase(identifier)
                    && member.getPassword().equals(password)) {
                loggedInMember = member;
                break;
            }
        }

        if (loggedInMember != null) {
            request.getSession().setAttribute("member", loggedInMember);
            response.sendRedirect("welcome.jsp");
            return;
        }

        // Common error handling extracted outside condition
        request.setAttribute("error", "Invalid email or password.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}