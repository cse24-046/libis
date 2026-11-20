<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${param.lang == 'tn' or param.lang == 'en'}">
  <c:set var="libisLang" value="${param.lang}" scope="session" />
</c:if>
<c:set var="libisLang" value="${empty sessionScope.libisLang ? 'en' : sessionScope.libisLang}" scope="session" />
<fmt:setLocale value="${libisLang == 'tn' ? 'tn_BW' : 'en'}" scope="session" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html>
<html>
<head>
  <title><fmt:message key="catalogue.title" /></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<nav class="navbar navbar-dark bg-primary shadow-sm mb-4">
  <div class="container">
    <span class="navbar-brand mb-0 h1"><fmt:message key="catalogue.heading" /></span>
    <div class="d-flex align-items-center gap-2">
      <form action="${pageContext.request.contextPath}/catalogue" method="get" class="d-flex align-items-center gap-2">
        <input type="text" class="form-control form-control-sm" name="searchTerm" value="<c:out value='${param.searchTerm}' />" placeholder="<fmt:message key='search.placeholder' />">
        <button type="submit" class="btn btn-outline-light btn-sm"><fmt:message key="search" /></button>
      </form>
      <c:choose>
        <c:when test="${libisLang == 'tn'}">
          <a href="${pageContext.request.contextPath}/catalogue?lang=en&amp;searchTerm=${param.searchTerm}" class="btn btn-outline-light btn-sm"><fmt:message key="language.english" /></a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/catalogue?lang=tn&amp;searchTerm=${param.searchTerm}" class="btn btn-outline-light btn-sm"><fmt:message key="language.setswana" /></a>
        </c:otherwise>
      </c:choose>
      <c:if test="${pageContext.request.userPrincipal != null and pageContext.request.isUserInRole('member')}">
        <a href="${pageContext.request.contextPath}/member/loans" class="btn btn-outline-light btn-sm"><fmt:message key="nav.myLoans" /></a>
      </c:if>
      <c:if test="${pageContext.request.userPrincipal != null and pageContext.request.isUserInRole('librarian')}">
        <a href="${pageContext.request.contextPath}/librarian/members" class="btn btn-outline-light btn-sm"><fmt:message key="nav.manage" /></a>
      </c:if>
      <form method="post" action="${pageContext.request.contextPath}/logout">
        <button type="submit" class="btn btn-outline-light btn-sm"><fmt:message key="nav.logout" /></button>
      </form>
    </div>
  </div>
</nav>

<div class="container">
  <c:if test="${not empty loanMessage}">
    <div class="alert alert-danger" role="alert"><c:out value="${loanMessage}" /></div>
  </c:if>

  <div class="mb-4">
    <h3><fmt:message key="catalogue.welcome" /> <span class="text-primary"><c:out value="${member.firstName}" /> <c:out value="${member.lastName}" /></span>!</h3>
    <p class="text-muted"><fmt:message key="catalogue.introduction" /></p>
  </div>

  <div class="card shadow-sm">
    <div class="card-body p-0">
      <table class="table table-striped table-hover mb-0 align-middle">
        <thead class="table-dark">
        <tr>
          <th><fmt:message key="table.title" /></th>
          <th><fmt:message key="table.author" /></th>
          <th><fmt:message key="table.genre" /></th>
          <th><fmt:message key="table.status" /></th>
          <th class="text-center"><fmt:message key="table.actions" /></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${bookList}">
          <tr>
            <td class="fw-bold"><c:out value="${book.title}" /></td>
            <td><c:out value="${book.author}" /></td>
            <td><c:out value="${book.genre}" /></td>
            <td>
              <c:choose>
                <c:when test="${book.available}">
                  <span class="badge bg-success"><fmt:message key="status.available" /></span>
                </c:when>
                <c:otherwise>
                  <span class="badge bg-secondary"><fmt:message key="status.checkedOut" /></span>
                </c:otherwise>
              </c:choose>
            </td>
            <td class="text-center">
              <c:if test="${pageContext.request.isUserInRole('member') and book.available}">
                <form method="post" action="${pageContext.request.contextPath}/member/borrow">
                  <input type="hidden" name="bookId" value="${book.bookId}">
                  <button type="submit" class="btn btn-sm btn-outline-primary"><fmt:message key="action.borrow" /></button>
                </form>
              </c:if>
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
