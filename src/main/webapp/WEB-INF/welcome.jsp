<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>LIBIS - Book Catalogue</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-primary shadow-sm mb-4">
  <div class="container">
    <span class="navbar-brand mb-0 h1">LIBIS Book Catalogue</span>
    <div class="d-flex align-items-center gap-2">
      <form action="${pageContext.request.contextPath}/catalogue" method="get" class="d-flex align-items-center gap-2">
        <input type="text" class="form-control form-control-sm" name="searchTerm" value="${param.searchTerm}" placeholder="Search">
        <button type="submit" class="btn btn-outline-light btn-sm">Search</button>
      </form>
      <a href="${pageContext.request.contextPath}/logout.jsp" class="btn btn-outline-light btn-sm">Logout</a>
    </div>
  </div>
</nav>

<div class="container">
  <div class="mb-4">
    <h3>Welcome, <span class="text-primary">${member.firstName} ${member.lastName}</span>!</h3>
    <p class="text-muted">Here is the available library book catalogue:</p>
  </div>

  <div class="card shadow-sm">
    <div class="card-body p-0">
      <table class="table table-striped table-hover mb-0 align-middle">
        <thead class="table-dark">
        <tr>
          <th>Title</th>
          <th>Author</th>
          <th>Genre</th>
          <th>Status</th>
          <th class="text-center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${bookList}">
          <tr>
            <td class="fw-bold">${book.title}</td>
            <td>${book.author}</td>
            <td>${book.genre}</td>
            <td>
              <c:choose>
                <c:when test="${book.available}">
                  <span class="badge bg-success">Available</span>
                </c:when>
                <c:otherwise>
                  <span class="badge bg-secondary">Checked Out</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="text-center">
              <a href="#" class="btn btn-sm btn-outline-primary me-1">Borrow</a>
              <a href="#" class="btn btn-sm btn-outline-secondary">Reserve</a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

</body>
</html>
