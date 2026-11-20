<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html><html><head><title>Member Form</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head><body class="bg-light"><div class="container py-4" style="max-width:700px"><h2>${empty member.memberId ? 'Add Member' : 'Edit Member'}</h2>
<form method="post" action="${pageContext.request.contextPath}/librarian/${formAction}">
<c:if test="${not empty member.memberId}"><input type="hidden" name="memberId" value="<c:out value='${member.memberId}' />"></c:if>
<div class="row"><div class="col mb-3"><label class="form-label">First name</label><input class="form-control" name="firstName" value="<c:out value='${member.firstName}' />"></div><div class="col mb-3"><label class="form-label">Last name</label><input class="form-control" name="lastName" value="<c:out value='${member.lastName}' />"></div></div>
<c:if test="${empty member.memberId or empty member.fullName}"><div class="mb-3"><label class="form-label">Full name</label><input class="form-control" name="fullName" value="<c:out value='${param.fullName}' />"></div></c:if>
<c:if test="${not empty errors.fullName}"><div class="text-danger mb-3"><c:out value="${errors.fullName}" /></div></c:if>
<div class="mb-3"><label class="form-label">Email</label><input class="form-control" type="email" name="email" value="<c:out value='${member.email}' />"><c:if test="${not empty errors.email}"><div class="text-danger"><c:out value="${errors.email}" /></div></c:if></div>
<div class="mb-3"><label class="form-label">Password<c:if test="${not empty member.memberId}"> (leave blank to keep existing)</c:if></label><input class="form-control" type="password" name="password"><c:if test="${not empty errors.password}"><div class="text-danger"><c:out value="${errors.password}" /></div></c:if></div>
<div class="mb-3"><label class="form-label">Role</label><select class="form-select" name="role"><option value="member" ${member.role eq 'member' ? 'selected' : ''}>member</option><option value="librarian" ${member.role eq 'librarian' ? 'selected' : ''}>librarian</option></select></div>
<button class="btn btn-primary">Save</button> <a class="btn btn-secondary" href="${pageContext.request.contextPath}/librarian/members">Cancel</a>
</form></div></body></html>
