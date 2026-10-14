<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    jakarta.servlet.http.HttpSession currentSession = request.getSession(false);
    if (currentSession != null) {
        currentSession.invalidate();
    }
    response.sendRedirect(request.getContextPath() + "/login");
%>
<!-- not sure if by action means jsp file of java file -->