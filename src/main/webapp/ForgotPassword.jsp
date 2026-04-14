<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>
<head>
    <title>Forgot Password | AceBank</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gradient-to-br from-blue-900 to-black min-h-screen flex justify-center items-center text-white">

<div class="bg-white/10 backdrop-blur-lg p-8 rounded-xl w-96 shadow-lg">


<h2 class="text-2xl mb-6 text-cyan-400 font-bold text-center">
    Forgot Password (OTP Verification)
</h2>

<!-- Success Message -->
<%
    String msg = request.getParameter("msg");
    if(msg != null){
%>
    <div class="bg-green-500 p-3 rounded mb-3 text-center"><%=msg%></div>
<% } %>

<!-- Error -->
<%
    String error = request.getParameter("error");
    if(error != null){
%>
    <div class="bg-red-500 p-3 rounded mb-3 text-center"><%=error%></div>
<% } %>

<!-- EMAIL FORM -->
<form action="send-otp" method="post">

    <input type="email"
           name="email"
           placeholder="Enter your registered email"
           class="w-full p-3 mb-4 rounded bg-black/40 outline-none focus:ring-2 focus:ring-cyan-400"
           required>

    <button type="submit"
            class="bg-cyan-500 w-full p-3 rounded hover:bg-cyan-600 font-semibold">
        Send OTP
    </button>

</form>

<p class="text-sm text-center mt-4 text-gray-300">
    <a href="login.jsp" class="hover:text-cyan-400">Back to Login</a>
</p>

</div>

</body>
</html>
