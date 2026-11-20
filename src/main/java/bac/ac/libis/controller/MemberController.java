package bac.ac.libis.controller;

import bac.ac.libis.dao.LoanDao;
import bac.ac.libis.dao.MemberDao;
import bac.ac.libis.model.Member;
import bac.ac.libis.exception.LoanException;
import bac.ac.libis.service.LoanService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/member/loans", "/member/borrow", "/member/return"})
public class MemberController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/member/loans") || request.getRequestURI().endsWith("/member/loans")) {
            Member member = signedInMember(request);
            try {
                LoanDao loans = new LoanDao();
                request.setAttribute("openLoans", loans.findOpenByMember(member.getMemberId()));
                request.setAttribute("history", loans.findHistoryByMember(member.getMemberId()));
                request.setAttribute("openLoanCount", loans.countOpenByMember(member.getMemberId()));
                request.getRequestDispatcher("/WEB-INF/my-loans.jsp").forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Database error loading loans", e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member member = signedInMember(request);
        LoanService loanService = new LoanService();
        try {
            String path = request.getServletPath();
            if (path.equals("/member/borrow")) {
                int bookId = parseId(request.getParameter("bookId"));
                loanService.borrow(member.getMemberId(), bookId);
            } else if (path.equals("/member/return")) {
                int loanId = parseId(request.getParameter("loanId"));
                loanService.returnLoan(member.getMemberId(), loanId);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/member/loans");
        } catch (LoanException e) {
            request.setAttribute("loanMessage", e.getMessage());
            if (request.getServletPath().equals("/member/borrow")) {
                forwardCatalogue(request, response, member);
            } else {
                forwardMyLoans(request, response, member);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error processing loan", e);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
        }
    }

    private Member signedInMember(HttpServletRequest request) throws ServletException {
        String username = request.getRemoteUser();
        if (username == null) {
            throw new ServletException("No authenticated principal");
        }
        try {
            Member member = new MemberDao().findByEmail(username);
            if (member == null) {
                throw new ServletException("Authenticated member not found");
            }
            return member;
        } catch (SQLException e) {
            throw new ServletException("Database error loading member", e);
        }
    }

    private void forwardCatalogue(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            bac.ac.libis.dao.BookDao bookDao = new bac.ac.libis.dao.BookDao();
            request.setAttribute("member", member);
            request.setAttribute("bookList", bookDao.findAll());
            request.getRequestDispatcher("/WEB-INF/welcome.jsp").forward(request, response);
        } catch (java.sql.SQLException e) {
            throw new ServletException("Database error reloading catalogue", e);
        }
    }

    private void forwardMyLoans(HttpServletRequest request, HttpServletResponse response, Member member)
            throws ServletException, IOException {
        try {
            LoanDao loanDao = new LoanDao();
            request.setAttribute("openLoans", loanDao.findOpenByMember(member.getMemberId()));
            request.setAttribute("history", loanDao.findHistoryByMember(member.getMemberId()));
            request.setAttribute("openLoanCount", loanDao.countOpenByMember(member.getMemberId()));
            request.getRequestDispatcher("/WEB-INF/my-loans.jsp").forward(request, response);
        } catch (java.sql.SQLException e) {
            throw new ServletException("Database error reloading loans", e);
        }
    }

    private int parseId(String value) {
        if (value == null) throw new NumberFormatException("Missing ID");
        return Integer.parseInt(value);
    }
}
