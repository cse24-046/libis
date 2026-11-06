package bac.ac.libis.controller;

import bac.ac.libis.model.Book;
import bac.ac.libis.model.Member;
import bac.ac.libis.dao.MemberDao;
import bac.ac.libis.dao.BookDao;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/catalogue")
public class CatalogueController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Book> matchedBooks;
        Member member;

        try {
            BookDao bookDao = new BookDao();
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                matchedBooks = bookDao.search(searchTerm.trim());
            } else {
                matchedBooks = bookDao.findAll();
            }
            member = new MemberDao().findByEmail(request.getRemoteUser());
        } catch (SQLException e) {
            throw new ServletException("Database error loading catalogue", e);
        }

        request.setAttribute("member", member);
        request.setAttribute("bookList", matchedBooks);
        request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
    }
}
