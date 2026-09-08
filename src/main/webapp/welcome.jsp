<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="bac.ac.libis.Member" %>
<%@ page import="bac.ac.libis.LibraryData" %>
<%@ page import="bac.ac.libis.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    Member member = (Member) session.getAttribute("member");
    if (member == null) {
        response.sendRedirect("login");
        return;
    }

    // Process Search Query
    String searchQuery = request.getParameter("query");
    List<Book> filteredBooks = new ArrayList<>();

    if (searchQuery != null && !searchQuery.trim().isEmpty()) {
        String queryLower = searchQuery.trim().toLowerCase();
        for (Book book : LibraryData.books) {
            if (book.getTitle().toLowerCase().contains(queryLower) ||
                    book.getAuthor().toLowerCase().contains(queryLower)) {
                filteredBooks.add(book);
            }
        }
    } else {
        filteredBooks = LibraryData.books;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Welcome - LIBIS</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }

        body {
            min-height: 100vh;
            padding: 40px;
            position: relative;
            color: #e2e8f0;
        }

        .bg-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: url('bg.png') no-repeat center center fixed;
            background-size: cover;
            filter: blur(8px) brightness(0.6);
            transform: scale(1.05);
            z-index: -1;
        }

        .container {
            max-width: 950px;
            margin: 0 auto;
            background: rgba(15, 23, 42, 0.8);
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.5);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #334155;
            padding-bottom: 15px;
            margin-bottom: 25px;
        }

        .greeting-box h1 { color: #ffffff; font-size: 24px; margin-bottom: 5px; }
        .user-info { color: #94a3b8; font-size: 14px; }

        .btn-logout {
            padding: 8px 16px;
            background: linear-gradient(135deg, #991b1b 0%, #7f1d1d 100%);
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
            font-weight: 600;
            font-size: 14px;
            border: 1px solid #ef4444;
            transition: all 0.3s ease;
        }

        .btn-logout:hover {
            background: linear-gradient(135deg, #dc2626 0%, #991b1b 100%);
        }

        /* Search Section Controls */
        .catalog-controls {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .catalog-controls h2 { color: #cbd5e1; font-size: 18px; }

        .search-container {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .sr-only {
            position: absolute;
            width: 1px;
            height: 1px;
            padding: 0;
            margin: -1px;
            overflow: hidden;
            clip: rect(0, 0, 0, 0);
            white-space: nowrap;
            border: 0;
        }

        .search-input {
            padding: 8px 12px;
            background: rgba(30, 41, 59, 0.8);
            border: 1px solid #475569;
            border-radius: 6px;
            font-size: 14px;
            color: #ffffff;
            outline: none;
            width: 250px;
            transition: border-color 0.3s;
        }

        .search-input:focus {
            border-color: #38bdf8;
        }

        .btn-search {
            padding: 8px 16px;
            background: linear-gradient(135deg, #1e3a8a 0%, #0f172a 100%);
            color: #ffffff;
            border: 1px solid #3b82f6;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-search:hover {
            background: linear-gradient(135deg, #2563eb 0%, #1e3a8a 100%);
        }

        .btn-reset {
            padding: 8px 12px;
            background: #475569;
            color: #ffffff;
            text-decoration: none;
            border-radius: 6px;
            font-size: 13px;
        }

        /* Table Styles */
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #334155; }

        th {
            background: linear-gradient(135deg, #1e3a8a 0%, #0f172a 100%);
            color: #ffffff;
            font-weight: 600;
            border-bottom: 2px solid #3b82f6;
        }

        tr:hover { background-color: rgba(51, 65, 85, 0.4); }

        .badge { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
        .available { background-color: rgba(22, 101, 52, 0.4); color: #4ade80; border: 1px solid #22c55e; }
        .borrowed { background-color: rgba(153, 27, 27, 0.4); color: #fca5a5; border: 1px solid #ef4444; }

        .action-links a { color: #38bdf8; text-decoration: none; font-weight: 600; font-size: 13px; margin-right: 6px; }
        .action-links a:hover { text-decoration: underline; color: #60a5fa; }
    </style>
</head>
<body>

<div class="bg-overlay"></div>

<div class="container">
    <div class="header">
        <div class="greeting-box">
            <h1>Welcome, <%= member.getFullName() %>!</h1>
            <div class="user-info">Logged in as: <strong><%= member.getEmail() %></strong></div>
        </div>
        <a href="login" class="btn-logout">Logout</a>
    </div>

    <div class="catalog-controls">
        <h2>Library Book Catalog</h2>
        <form action="welcome.jsp" method="get" class="search-container">
            <label for="catalogSearch" class="sr-only">Search Library Catalog</label>
            <input type="text" id="catalogSearch" name="query"
                   value="<%= searchQuery != null ? searchQuery : "" %>"
                   placeholder="Search by title or author..." class="search-input" />
            <button type="submit" class="btn-search">Search</button>
            <% if (searchQuery != null && !searchQuery.trim().isEmpty()) { %>
            <a href="welcome.jsp" class="btn-reset">Reset</a>
            <% } %>
        </form>
    </div>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Author</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            if (!filteredBooks.isEmpty()) {
                for (Book book : filteredBooks) {
        %>
        <tr>
            <td><%= book.getId() %></td>
            <td><strong><%= book.getTitle() %></strong></td>
            <td><%= book.getAuthor() %></td>
            <td>
                <% if (book.isAvailable()) { %>
                <span class="badge available">Available</span>
                <% } else { %>
                <span class="badge borrowed">Borrowed</span>
                <% } %>
            </td>
            <td class="action-links">
                <a href="#">Borrow</a> |
                <a href="#">Reserve</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="5" style="text-align:center;">No books found matching your search criteria.</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>

</body>
</html>