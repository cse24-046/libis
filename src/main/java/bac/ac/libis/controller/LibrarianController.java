package bac.ac.libis.controller;

import bac.ac.libis.dao.BookDao;
import bac.ac.libis.dao.MemberDao;
import bac.ac.libis.model.Book;
import bac.ac.libis.model.Member;
import bac.ac.libis.service.BookService;
import bac.ac.libis.service.MemberService;
import bac.ac.libis.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/librarian/*")
public class LibrarianController extends HttpServlet {
    private final MemberDao memberDao = new MemberDao();
    private final BookDao bookDao = new BookDao();
    private final MemberService memberService = new MemberService();
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if (path == null || "/".equals(path) || "/members".equals(path)) {
                String search = request.getParameter("search");
                request.setAttribute("members", search == null || search.isBlank() ? memberDao.findAll() : memberDao.search(search.trim()));
                request.setAttribute("search", search);
                request.getRequestDispatcher("/WEB-INF/librarian/members.jsp").forward(request, response);
            } else if ("/members/add".equals(path)) {
                request.setAttribute("formAction", "members/create");
                request.getRequestDispatcher("/WEB-INF/librarian/member-form.jsp").forward(request, response);
            } else if ("/members/edit".equals(path)) {
                Member member = memberDao.findById(Integer.parseInt(request.getParameter("id")));
                if (member == null) { response.sendError(404); return; }
                request.setAttribute("member", member);
                request.setAttribute("formAction", "members/update");
                request.getRequestDispatcher("/WEB-INF/librarian/member-form.jsp").forward(request, response);
            } else if ("/members/confirm-delete".equals(path)) {
                Member member = memberDao.findById(Integer.parseInt(request.getParameter("id")));
                if (member == null) { response.sendError(404); return; }
                request.setAttribute("member", member);
                request.getRequestDispatcher("/WEB-INF/librarian/member-confirm-delete.jsp").forward(request, response);
            } else if ("/books".equals(path)) {
                String search = request.getParameter("search");
                request.setAttribute("books", search == null || search.isBlank() ? bookDao.findAll() : bookDao.search(search.trim()));
                request.setAttribute("search", search);
                request.getRequestDispatcher("/WEB-INF/librarian/books.jsp").forward(request, response);
            } else if ("/books/add".equals(path)) {
                request.setAttribute("formAction", "books/create");
                request.getRequestDispatcher("/WEB-INF/librarian/book-form.jsp").forward(request, response);
            } else if ("/books/edit".equals(path)) {
                Book book = bookDao.findById(Integer.parseInt(request.getParameter("id")));
                if (book == null) { response.sendError(404); return; }
                request.setAttribute("book", book);
                request.setAttribute("formAction", "books/update");
                request.getRequestDispatcher("/WEB-INF/librarian/book-form.jsp").forward(request, response);
            } else if ("/books/confirm-delete".equals(path)) {
                Book book = bookDao.findById(Integer.parseInt(request.getParameter("id")));
                if (book == null) { response.sendError(404); return; }
                request.setAttribute("book", book);
                request.getRequestDispatcher("/WEB-INF/librarian/book-confirm-delete.jsp").forward(request, response);
            } else response.sendError(404);
        } catch (SQLException e) { throw new ServletException("Database error loading librarian data", e); }
        catch (NumberFormatException e) { response.sendError(400, "Invalid ID"); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        try {
            if ("/members/create".equals(path) || "/members/update".equals(path)) {
                Member member = memberFromRequest(request);
                if ("/members/update".equals(path)) {
                    member.setMemberId(Integer.parseInt(request.getParameter("memberId")));
                }
                try {
                    if ("/members/create".equals(path)) memberService.create(member);
                    else memberService.update(member);
                    response.sendRedirect(request.getContextPath() + "/librarian/members");
                } catch (ValidationException e) {
                    forwardMemberForm(request, response, member, e, "/members/update".equals(path) ? "members/update" : "members/create");
                }
            } else if ("/members/delete".equals(path)) {
                int id = Integer.parseInt(request.getParameter("memberId"));
                Member member = memberDao.findById(id);
                Member signedIn = memberDao.findByEmail(request.getRemoteUser());
                try {
                    memberService.delete(id, signedIn.getMemberId());
                    response.sendRedirect(request.getContextPath() + "/librarian/members?message=" + java.net.URLEncoder.encode("Member " + member.getMemberId() + " (" + member.getFullName() + ") and their borrowing history have been deleted.", "UTF-8"));
                } catch (ValidationException e) {
                    request.setAttribute("errors", e.getErrors());
                    request.setAttribute("member", member);
                    request.getRequestDispatcher("/WEB-INF/librarian/member-confirm-delete.jsp").forward(request, response);
                }
            } else if ("/books/create".equals(path) || "/books/update".equals(path)) {
                Book book = bookFromRequest(request);
                if ("/books/update".equals(path)) book.setBookId(Integer.parseInt(request.getParameter("bookId")));
                try {
                    if ("/books/create".equals(path)) bookService.create(book);
                    else bookService.update(book);
                    response.sendRedirect(request.getContextPath() + "/librarian/books");
                } catch (ValidationException e) {
                    forwardBookForm(request, response, book, e, "/books/update".equals(path) ? "books/update" : "books/create");
                }
            } else if ("/books/delete".equals(path)) {
                int id = Integer.parseInt(request.getParameter("bookId"));
                Book book = bookDao.findById(id);
                try {
                    bookService.delete(book);
                    response.sendRedirect(request.getContextPath() + "/librarian/books?message=" + java.net.URLEncoder.encode(book.getTitle() + " has been deleted.", "UTF-8"));
                } catch (ValidationException e) {
                    request.setAttribute("errors", e.getErrors());
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/WEB-INF/librarian/book-confirm-delete.jsp").forward(request, response);
                }
            } else response.sendError(404);
        } catch (SQLException e) { throw new ServletException("Database error updating librarian data", e); }
        catch (NumberFormatException e) { response.sendError(400, "Invalid ID"); }
    }

    private Member memberFromRequest(HttpServletRequest request) {
        Member member = new Member();
        member.setFirstName(request.getParameter("firstName"));
        member.setLastName(request.getParameter("lastName"));
        member.setEmail(request.getParameter("email"));
        member.setPassword(request.getParameter("password"));
        member.setRole(request.getParameter("role") == null ? "member" : request.getParameter("role"));
        return member;
    }

    private Book bookFromRequest(HttpServletRequest request) {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setGenre(request.getParameter("genre"));
        book.setSynopsis(request.getParameter("synopsis"));
        return book;
    }

    private void forwardMemberForm(HttpServletRequest request, HttpServletResponse response, Member member, ValidationException e, String action) throws ServletException, IOException {
        request.setAttribute("errors", e.getErrors());
        request.setAttribute("member", member);
        request.setAttribute("formAction", action);
        request.getRequestDispatcher("/WEB-INF/librarian/member-form.jsp").forward(request, response);
    }

    private void forwardBookForm(HttpServletRequest request, HttpServletResponse response, Book book, ValidationException e, String action) throws ServletException, IOException {
        request.setAttribute("errors", e.getErrors());
        request.setAttribute("book", book);
        request.setAttribute("formAction", action);
        request.getRequestDispatcher("/WEB-INF/librarian/book-form.jsp").forward(request, response);
    }
}
