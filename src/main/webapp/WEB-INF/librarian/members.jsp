<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${param.lang == 'tn' or param.lang == 'en'}"><c:set var="libisLang" value="${param.lang}" scope="session" /></c:if>
<c:set var="libisLang" value="${empty sessionScope.libisLang ? 'en' : sessionScope.libisLang}" scope="session" />
<fmt:setLocale value="${libisLang == 'tn' ? 'tn_BW' : 'en'}" scope="session" />
<fmt:setBundle basename="messages" />
<!DOCTYPE html><html><head><title>Manage Members</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><div class="container py-4">
<h2>Manage Members</h2>
<div class="mb-3">
  <c:choose>
    <c:when test="${libisLang == 'tn'}"><a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/librarian/members?lang=en"><fmt:message key="language.english" /></a></c:when>
    <c:otherwise><a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/librarian/members?lang=tn"><fmt:message key="language.setswana" /></a></c:otherwise>
  </c:choose>
</div>
<c:if test="${not empty param.message}"><div class="alert alert-success"><c:out value="${param.message}" /></div></c:if>
<form method="get" action="${pageContext.request.contextPath}/librarian/members" class="d-flex gap-2 mb-3"><input class="form-control" name="search" value="<c:out value='${search}' />" placeholder="Name or member number"><button class="btn btn-primary">Search</button><a class="btn btn-secondary" href="${pageContext.request.contextPath}/librarian/members">Clear</a></form>
<a href="${pageContext.request.contextPath}/librarian/members/add" class="btn btn-success mb-3">Add Member</a>
<table class="table table-striped"><thead><tr><th>Member ID</th><th>Name</th><th>Email / Username</th><th>Actions</th></tr></thead><tbody>
<c:forEach var="member" items="${members}"><tr><td>${member.memberId}</td><td><c:out value="${member.fullName}" /></td><td><c:out value="${member.email}" /></td><td><a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/librarian/members/edit?id=${member.memberId}">Edit</a> <a class="btn btn-sm btn-danger" href="${pageContext.request.contextPath}/librarian/members/confirm-delete?id=${member.memberId}">Delete</a></td></tr></c:forEach>
</tbody></table><a href="${pageContext.request.contextPath}/librarian/books" class="btn btn-secondary">Manage Books</a> <form method="post" action="${pageContext.request.contextPath}/logout" class="d-inline"><button class="btn btn-outline-secondary">Logout</button></form>
</div></body></html>
