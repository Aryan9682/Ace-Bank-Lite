<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login | AceBank</title>

<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gradient-to-br from-blue-900 to-black min-h-screen flex items-center justify-center">

<div class="bg-white/10 backdrop-blur-lg shadow-2xl rounded-2xl p-10 w-[380px] text-white">

    <h1 class="text-3xl font-bold text-center mb-6 text-cyan-400">AceBank</h1>
    <h2 class="text-xl text-center mb-6 font-semibold">Welcome Back</h2>

    <!-- 🔴 ERROR MESSAGE ONLY WHEN WRONG LOGIN -->
    <c:if test="${param.error != null && param.error != ''}">
        <div class="bg-red-500 text-white p-3 rounded mb-4 text-center font-semibold">
            ${param.error}
        </div>
    </c:if>

    <!-- LOGIN FORM -->
    <form action="Login" method="post" autocomplete="off" class="space-y-4">

        <div>
            <label class="block mb-1 text-sm">Account Number</label>
            <input type="text" name="accountNumber"
                class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                placeholder="Enter Account Number" required>
        </div>

        <div>
            <label class="block mb-1 text-sm">Password</label>
            <input type="password" name="password"
                class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                placeholder="Enter Password" required>
        </div>

        <div class="flex justify-between items-center text-sm">
            <label>
                <input type="checkbox" name="rememberMe"> Remember Me
            </label>

            <a href="ForgotPassword.jsp" class="text-cyan-400 hover:underline">
                Forgot?
            </a>
        </div>

        <button type="submit"
            class="w-full bg-cyan-500 hover:bg-cyan-600 p-3 rounded-lg font-bold text-lg">
            Login
        </button>

    </form>

    <p class="text-center mt-6 text-sm">
        New user?
        <a href="sign-up.jsp" class="text-cyan-400 hover:underline">Create account</a>
    </p>

</div>
</body>
</html>