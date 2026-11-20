<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>LIBIS - Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">

<div class="card shadow p-4" style="width: 100%; max-width: 400px;">
  <h2 class="text-center mb-4 text-primary">LIBIS Login</h2>

  <c:if test="${param.error == '1'}">
    <div class="alert alert-danger text-center" role="alert">Invalid email or password.</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/j_security_check" method="post" autocomplete="on">
    <div class="mb-3">
      <label for="j_username" class="form-label">Email address</label>
      <input type="email" class="form-control" id="j_username" name="j_username" required>
    </div>
    <div class="mb-3">
      <label for="j_password" class="form-label">Password</label>
      <input type="password" class="form-control" id="j_password" name="j_password" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">Login</button>
  </form>
</div>

</body>
</html>