package bac.ac.libis.dao;

import bac.ac.libis.model.Loan;
import bac.ac.libis.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoanDao {

    private static final String SELECT =
            "SELECT l.loan_id, l.member_id, l.book_id, l.borrowed_on, l.returned_on, b.title "
                    + "FROM loans l JOIN books b ON b.book_id = l.book_id";

    /** records a new open loan and returns its id */
    public int create(int memberId, int bookId) throws SQLException {
        String sql = "INSERT INTO loans (member_id, book_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, memberId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    /** this one sets returned_on to now returns false if the loan doesn't exist (or was already returned) */
    public boolean markReturned(int loanId) throws SQLException {
        String sql = "UPDATE loans SET returned_on = NOW() WHERE loan_id = ? AND returned_on IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            return ps.executeUpdate() > 0;
        }
    }

    /** number of books a member has out */
    public int countOpenByMember(int memberId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND returned_on IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /** loans that haven't been returned */
    public List<Loan> findOpenByMember(int memberId) throws SQLException {
        return query(SELECT + " WHERE l.member_id = ? AND l.returned_on IS NULL ORDER BY l.borrowed_on DESC",
                memberId);
    }

    /**borrowing history */
    public List<Loan> findHistoryByMember(int memberId) throws SQLException {
        return query(SELECT + " WHERE l.member_id = ? AND l.returned_on IS NOT NULL ORDER BY l.returned_on DESC",
                memberId);
    }

    public boolean hasOpenLoan(int bookId) throws SQLException {
        String sql = "SELECT 1 FROM loans WHERE book_id = ? AND returned_on IS NULL LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // these are helpers!(don't forget!)

    private List<Loan> query(String sql, Object... params) throws SQLException {
        List<Loan> loans = new ArrayList<>();


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Loan loan = new Loan();
                    loan.setLoanId(rs.getInt("loan_id"));
                    loan.setMemberId(rs.getInt("member_id"));
                    loan.setBookId(rs.getInt("book_id"));
                    loan.setBorrowedOn(rs.getTimestamp("borrowed_on"));
                    loan.setReturnedOn(rs.getTimestamp("returned_on"));
                    loan.setBookTitle(rs.getString("title"));
                    loans.add(loan);
                }
            }
        }
        return loans;
    }
}