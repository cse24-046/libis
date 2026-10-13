package bac.ac.libis.controller;

import bac.ac.libis.model.DataProvider;
import bac.ac.libis.model.Member;
import bac.ac.libis.model.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/loginProcess")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String inputEmail = request.getParameter("email");
        String inputPassword = request.getParameter("password");

        Member authenticatedMember = null;

        // loop through member list to check credentials
        List<Member> members = DataProvider.getMembers();
        for (Member m : members) {
            if (m.getEmail().equalsIgnoreCase(inputEmail) && m.getPassword().equals(inputPassword)) {
                authenticatedMember = m;
                break;
            }
        }

        // this one branches logic based on success or failure (i think)
        if (authenticatedMember != null) {

            // saves member identity
            HttpSession session = request.getSession();
            session.setAttribute("loggedMember", authenticatedMember);

            List<Book> books = DataProvider.getBooks();
            session.setAttribute("bookList", books);

            response.sendRedirect("welcome.jsp");

        } else {
            // reject and redirect back to login
            response.sendRedirect("login.jsp?error=true");
        }
    }
}