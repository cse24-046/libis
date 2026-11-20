<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html><html><head><title>Book Form</title><link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"></head><body class="bg-light"><div class="container py-4" style="max-width:700px"><h2>${empty book.bookId ? 'Add Book' : 'Edit Book'}</h2>
<form method="post" action="${pageContext.request.contextPath}/librarian/${formAction}"><c:if test="${not empty book.bookId}"><input type="hidden" name="bookId" value="<c:out value='${book.bookId}' />"></c:if>
<div class="mb-3"><label class="form-label">Title</label><input class="form-control" name="title" value="<c:out value='${book.title}' />"><c:if test="${not empty errors.title}"><div class="text-danger"><c:out value="${errors.title}" /></div></c:if></div>
<div class="mb-3"><label class="form-label">Author</label><input class="form-control" name="author" value="<c:out value='${book.author}' />"><c:if test="${not empty errors.author}"><div class="text-danger"><c:out value="${errors.author}" /></div></c:if></div>
<div class="mb-3"><label class="form-label">Genre</label><input class="form-control" name="genre" value="<c:out value='${book.genre}' />"></div>
<div class="mb-3"><label class="form-label">Synopsis</label><textarea class="form-control" name="synopsis"><c:out value="${book.synopsis}" /></textarea></div><button class="btn btn-primary">Save</button> <a class="btn btn-secondary" href="${pageContext.request.contextPath}/librarian/books">Cancel</a></form></div></body></html>
