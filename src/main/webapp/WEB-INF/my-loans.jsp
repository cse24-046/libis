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
<html><head><title><fmt:message key="loans.title" /></title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head>
<body class="bg-light"><div class="container py-4"><h2><fmt:message key="loans.heading" /></h2>
<div class="mb-3">
  <c:choose>
    <c:when test="${libisLang == 'tn'}"><a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/member/loans?lang=en"><fmt:message key="language.english" /></a></c:when>
    <c:otherwise><a class="btn btn-outline-primary btn-sm" href="${pageContext.request.contextPath}/member/loans?lang=tn"><fmt:message key="language.setswana" /></a></c:otherwise>
  </c:choose>
</div>
<c:if test="${not empty loanMessage}"><div class="alert alert-danger" role="alert"><c:out value="${loanMessage}" /></div></c:if>
<p class="text-muted"><fmt:message key="loans.count"><fmt:param value="${openLoanCount}" /></fmt:message></p>
<h4 class="mt-4"><fmt:message key="loans.current" /></h4>
<table class="table table-striped"><thead><tr><th><fmt:message key="table.book" /></th><th><fmt:message key="table.borrowed" /></th><th><fmt:message key="table.action" /></th></tr></thead><tbody>
<c:forEach var="loan" items="${openLoans}"><tr><td><c:out value="${loan.bookTitle}" /></td><td>${loan.borrowedOn}</td><td><form method="post" action="${pageContext.request.contextPath}/member/return"><input type="hidden" name="loanId" value="${loan.loanId}"><button class="btn btn-sm btn-primary"><fmt:message key="action.return" /></button></form></td></tr></c:forEach>
</tbody></table>
<h4 class="mt-4"><fmt:message key="loans.history" /></h4>
<table class="table table-striped"><thead><tr><th><fmt:message key="table.book" /></th><th><fmt:message key="table.borrowed" /></th><th><fmt:message key="table.returned" /></th></tr></thead><tbody>
<c:forEach var="loan" items="${history}"><tr><td><c:out value="${loan.bookTitle}" /></td><td>${loan.borrowedOn}</td><td>${loan.returnedOn}</td></tr></c:forEach>
</tbody></table>
<a class="btn btn-secondary" href="${pageContext.request.contextPath}/catalogue"><fmt:message key="nav.catalogue" /></a> <form method="post" action="${pageContext.request.contextPath}/logout" class="d-inline"><button class="btn btn-outline-secondary"><fmt:message key="nav.logout" /></button></form>
</div></body></html>
