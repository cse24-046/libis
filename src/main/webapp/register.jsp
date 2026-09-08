<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign Up - LIBIS</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }

        body {
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
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

        .card {
            background: rgba(15, 23, 42, 0.75);
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.5);
            border: 1px solid rgba(255, 255, 255, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .card h2 { margin-bottom: 20px; color: #ffffff; text-align: center; font-weight: 600; }
        .error { background-color: rgba(220, 38, 38, 0.2); color: #fca5a5; padding: 10px; border-radius: 6px; font-size: 14px; margin-bottom: 20px; border: 1px solid rgba(239, 68, 68, 0.4); text-align: center; }

        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; color: #94a3b8; font-size: 14px; font-weight: 600; }

        .form-group input {
            width: 100%;
            padding: 12px;
            background: rgba(30, 41, 59, 0.8);
            border: 1px solid #475569;
            border-radius: 6px;
            font-size: 14px;
            color: #ffffff;
            outline: none;
            transition: border-color 0.3s;
        }

        .form-group input:focus { border-color: #38bdf8; }

        .btn {
            width: 100%;
            padding: 12px;
            background: linear-gradient(135deg, #1e3a8a 0%, #0f172a 100%);
            color: #ffffff;
            border: 1px solid #3b82f6;
            border-radius: 6px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 4px 12px rgba(30, 58, 138, 0.4);
            transition: all 0.3s ease;
        }

        .btn:hover {
            background: linear-gradient(135deg, #2563eb 0%, #1e3a8a 100%);
            box-shadow: 0 6px 16px rgba(37, 99, 235, 0.6);
        }

        .footer-text { margin-top: 25px; text-align: center; font-size: 14px; color: #94a3b8; }
        .footer-text a { color: #38bdf8; text-decoration: none; font-weight: 600; }
        .footer-text a:hover { text-decoration: underline; }
    </style>
</head>
<body>

<div class="bg-overlay"></div>

<div class="card">
    <h2>Create Account</h2>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="register" method="post">
        <div class="form-group">
            <label for="fullName">Full Name</label>
            <input type="text" id="fullName" name="fullName" required />
        </div>

        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" required />
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required />
        </div>

        <button type="submit" class="btn">Sign Up</button>
    </form>

    <div class="footer-text">
        Already have an account? <a href="login">Log In</a>
    </div>
</div>

</body>
</html>