<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>
<head>
<title>Verify OTP | AceBank</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gradient-to-br from-blue-900 to-black min-h-screen flex justify-center items-center text-white">

<div class="bg-white/10 backdrop-blur-lg p-8 rounded-xl w-96 shadow-lg">

<h2 class="text-2xl mb-6 text-cyan-400 font-bold text-center">
    Verify OTP & Reset Password
</h2>

<!-- ERROR -->

<%
String error = request.getParameter("error");
if(error!=null){
%>

<div class="bg-red-500 p-3 rounded mb-3 text-center"><%=error%></div>
<% } %>

<form action="verify-otp" method="post">

<input type="hidden" name="email" value="<%=request.getAttribute("email")%>">

<input type="text" name="otp"
placeholder="Enter OTP"
class="w-full p-3 mb-3 rounded bg-black/40"
required>

<input type="password" name="newPassword"
placeholder="Enter New Password"
class="w-full p-3 mb-4 rounded bg-black/40"
required>

<button class="bg-cyan-500 w-full p-3 rounded hover:bg-cyan-600 font-semibold">
Reset Password
</button>

</form>

</div>
</body>
</html>
