package bac.ac.libis.controller;

import bac.ac.libis.model.Book;
import bac.ac.libis.model.DataProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/catalogue")
public class CatalogueController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        List<Book> books = DataProvider.getBooks();
        List<Book> matchedBooks = new ArrayList<>();

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String term = searchTerm.trim().toLowerCase();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(term)) {
                    matchedBooks.add(book);
                }
            }
        } else {
            matchedBooks.addAll(books);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            request.setAttribute("member", session.getAttribute("loggedMember"));
        }
        request.setAttribute("bookList", matchedBooks);
        request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
    }
}
