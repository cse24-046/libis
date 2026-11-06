package bac.ac.libis.dao;

import bac.ac.libis.model.Book;
import bac.ac.libis.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    private static final String SELECT =
            "SELECT b.book_id, b.title, b.author, b.genre, b.synopsis, "
                    + "NOT EXISTS (SELECT 1 FROM loans l WHERE l.book_id = b.book_id AND l.returned_on IS NULL) AS available "
                    + "FROM books b";

    // read!

    public List<Book> findAll() throws SQLException {
        return query(SELECT + " ORDER BY b.book_id");
    }

    /** the book id is also the book's identifying number. is it the same as isbn though?? (ask sir) */
    public Book findById(int bookId) throws SQLException {
        return first(query(SELECT + " WHERE b.book_id = ?", bookId));
    }

    /** matches part of the title or the author ignoring case */
    public List<Book> search(String term) throws SQLException {
        String like = "%" + term.toLowerCase() + "%";
        return query(SELECT + " WHERE LOWER(b.title) LIKE ? OR LOWER(b.author) LIKE ? ORDER BY b.book_id",
                like, like);
    }

    // write!

    /** inserts the book, sets the new id on the object and returns */
    public int create(Book b) throws SQLException {
        String sql = "INSERT INTO books (title, author, genre, synopsis) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getGenre());
            ps.setString(4, b.getSynopsis());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    b.setBookId(keys.getInt(1));
                }
            }
        }
        return b.getBookId();
    }

    /** returns true if a row was changed */
    public boolean update(Book b) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, synopsis = ? WHERE book_id = ?";
        return execute(sql, b.getTitle(), b.getAuthor(), b.getGenre(), b.getSynopsis(), b.getBookId()) > 0;
    }

    /** Returns true if a book was deleted */
    public boolean delete(int bookId) throws SQLException {
        return execute("DELETE FROM books WHERE book_id = ?", bookId) > 0;
    }

    // these are helpers

    private List<Book> query(String sql, Object... params) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        }
        return books;
    }

    private int execute(String sql, Object... params) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }

    private Book first(List<Book> list) {
        return list.isEmpty() ? null : list.get(0);
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getString("synopsis"),
                rs.getBoolean("available"));
    }
}
