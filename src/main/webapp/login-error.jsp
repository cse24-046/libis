<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>LIBIS - Login Failed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">

<div class="card shadow p-4" style="width: 100%; max-width: 400px;">
    <h2 class="text-center mb-3 text-danger">Login failed</h2>
    <p class="text-center">Invalid email or password.</p>
    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary w-100">Try again</a>
</div>

</body>
</html>