Project overview
This project is a Java web application for a small library system. It uses:
•Java 17
•Maven + Tomcat
•MySQL database
•Jakarta Servlet/JSP
•JNDI DataSource for DB connection

Main purpose:
•Member login using email/password
•View book catalogue
•Search books by title/author
•Check book availability
•Track loans (borrowed / returned)

Project structure
src/main/java/bac/ac/libis/
•controller/
◦LoginServlet.java
◦CatalogueController.java
◦LogoutController.java

•dao/
◦BookDao.java
◦MemberDao.java
◦LoanDao.java

•model/
◦Book.java
◦Member.java
◦Loan.java

•util/
◦DBConnection.java

Key configuration
Database connection is configured in: src/main/webapp/META-INF/context.xml
Important snippet:
<Resource name="jdbc/libis"
          auth="Container"
          type="javax.sql.DataSource"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/libis?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
          username="root"
          password="YOUR_PASSWORD"
          maxTotal="20"
          maxIdle="5"/>
This is accessed with a JNDI lookup in:
package bac.ac.libis.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static final DataSource DATA_SOURCE;

    static {
        try {
            DATA_SOURCE = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/libis");
        } catch (NamingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}

App flow
1.User opens login.jsp
2.Login form submits to /login
3.LoginServlet checks email and password in database
4.If valid, session is created and redirect to /catalogue
5.CatalogueController loads all books or filtered search results
6.JSP displays the catalogue with availability status

Key login logic
File: LoginServlet.java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
            return;
        }

        try {
            Member member = new MemberDao().findByEmail(email);

            if (member != null && password.equals(member.getPassword())) {
                HttpSession session = request.getSession(true);
                session.setAttribute("member", member);
                response.sendRedirect(request.getContextPath() + "/catalogue");
                return;
            }
        } catch (Exception e) {
            throw new ServletException("Login failed", e);
        }

        response.sendRedirect(request.getContextPath() + "/login.jsp?error=1");
    }
}

Catalogue logic
File: CatalogueController.java
@WebServlet("/catalogue")
public class CatalogueController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

Important note:
•The login sets member in the session, but in the catalogue controller it uses request.getRemoteUser().
•In this setup, it is safer and more consistent to read from the session instead:
HttpSession session = request.getSession(false);
Member member = (Member) session.getAttribute("member");
This is a good practical point to mention if the app fails to load the user name.

Book DAO
File: BookDao.java Main feature: add, list, search, update, delete books.
Important snippet:
private static final String SELECT =
        "SELECT b.book_id, b.title, b.author, b.genre, b.synopsis, "
                + "NOT EXISTS (SELECT 1 FROM loans l WHERE l.book_id = b.book_id AND l.returned_on IS NULL) AS available "
                + "FROM books b";
Create book:
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

Search by title/author:
public List<Book> search(String term) throws SQLException {
    String like = "%" + term.toLowerCase() + "%";
    return query(SELECT + " WHERE LOWER(b.title) LIKE ? OR LOWER(b.author) LIKE ? ORDER BY b.book_id",
            like, like);
}

Member DAO
File: MemberDao.java Main feature: find member by email, create/update/delete member.
Important snippet:
public Member findByEmail(String email) throws SQLException {
    return first(query(SELECT + " WHERE LOWER(email) = LOWER(?)", email));
}

public int create(Member m) throws SQLException {
    String sql = "INSERT INTO members (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, m.getFirstName());
        ps.setString(2, m.getLastName());
        ps.setString(3, m.getEmail());
        ps.setString(4, m.getPassword());
        ps.executeUpdate();

        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) {
                m.setMemberId(keys.getInt(1));
            }
        }
    }
    return m.getMemberId();
}

Loan DAO
File: LoanDao.java Main feature: create loan, check active loans, mark book as returned.
Insert loan:
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

Return book:
public boolean markReturned(int loanId) throws SQLException {
    String sql = "UPDATE loans SET returned_on = NOW() WHERE loan_id = ? AND returned_on IS NULL";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, loanId);
        return ps.executeUpdate() > 0;
    }
}

Check active loans:
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

SQL queries used by the DB layer
These are the main queries used by the app.
1.Get all members
SELECT member_id, first_name, last_name, email, password
FROM members
ORDER BY member_id;

2.Get member by email
SELECT member_id, first_name, last_name, email, password
FROM members
WHERE LOWER(email) = LOWER(?);

3.Insert new member
INSERT INTO members (first_name, last_name, email, password)
VALUES (?, ?, ?, ?);

4.Update member
UPDATE members
SET first_name = ?, last_name = ?, email = ?, password = ?
WHERE member_id = ?;

5.Delete member
DELETE FROM members
WHERE member_id = ?;

6.Get all books and availability
SELECT b.book_id, b.title, b.author, b.genre, b.synopsis,
       NOT EXISTS (
           SELECT 1
           FROM loans l
           WHERE l.book_id = b.book_id
             AND l.returned_on IS NULL
       ) AS available
FROM books b
ORDER BY b.book_id;

7.Search books by title or author
SELECT b.book_id, b.title, b.author, b.genre, b.synopsis,
       NOT EXISTS (
           SELECT 1 FROM loans l
           WHERE l.book_id = b.book_id
             AND l.returned_on IS NULL
       ) AS available
FROM books b
WHERE LOWER(b.title) LIKE ? OR LOWER(b.author) LIKE ?
ORDER BY b.book_id;

8.Insert book
INSERT INTO books (title, author, genre, synopsis)
VALUES (?, ?, ?, ?);

9.Update book
UPDATE books
SET title = ?, author = ?, genre = ?, synopsis = ?
WHERE book_id = ?;

10.Delete book
DELETE FROM books
WHERE book_id = ?;

11.Create loan
INSERT INTO loans (member_id, book_id)
VALUES (?, ?);

12.Mark loan as returned
UPDATE loans
SET returned_on = NOW()
WHERE loan_id = ? AND returned_on IS NULL;

13.Count active loans for a member
SELECT COUNT(*)
FROM loans
WHERE member_id = ? AND returned_on IS NULL;

14.Check if a specific book is currently on loan
SELECT 1
FROM loans
WHERE book_id = ? AND returned_on IS NULL
LIMIT 1;
Database schema assumed by the project
This is the schema the DAO code is built around.
CREATE DATABASE libis;
USE libis;

CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'member'
);

CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(200) NOT NULL,
    genre VARCHAR(100),
    synopsis TEXT
);

CREATE TABLE loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    borrowed_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    returned_on TIMESTAMP NULL,
    FOREIGN KEY (member_id) REFERENCES members(member_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);

Sample inserts
INSERT INTO members (first_name, last_name, email, password, role)
VALUES ('Jane', 'Doe', 'jane@example.com', '1234', 'member');

INSERT INTO books (title, author, genre, synopsis)
VALUES ('Clean Code', 'Robert C. Martin', 'Programming', 'A handbook of agile software craftsmanship.');

INSERT INTO loans (member_id, book_id)
VALUES (1, 1);

Important notes for practicals / viva
•This is a library catalogue app, not a full production system.
•Passwords are stored and compared as plain strings in the current version:
if (member != null && password.equals(member.getPassword()))
For real systems, use hashed passwords (BCrypt/Argon2).
•The front-end JSP has Borrow and Reserve buttons but no actual servlet action behind them yet:
<a href="#" class="btn btn-sm btn-outline-primary me-1">Borrow</a>
<a href="#" class="btn btn-sm btn-outline-secondary">Reserve</a>

They are placeholders.
•The app uses MySQL and Tomcat container-managed DataSource. Make sure the DB is running and the Tomcat context.xml password is correct.
•The project uses DataSourceRealm in context.xml, so it expects members table with email and password fields. This matters for auth-related practicals.
Quick startup checklist
1.Start MySQL
2.Create database libis
3.Create tables above
4.Insert sample data
5.Update context.xml password
6.Run Maven build:
mvn clean package
7.Deploy WAR to Tomcat
8.Open:
http://localhost:8080/libis-app/
or the generated app name depending on your Tomcat setup

Short summary
This project is a simple but solid Java library system using:
•JSP pages
•Servlet controllers
•DAO classes
•MySQL database
•JNDI connection pool
It is very useful as a code vault and practical reference for:
•database connectivity
•CRUD operations
•search/filter queries
•MVC flow in Java web apps
•login/session handling
•library management logic
