<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Reset Password</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-black text-white flex justify-center items-center min-h-screen">

<div class="bg-white/10 p-8 rounded-xl w-96">

<h2 class="text-2xl mb-6 text-cyan-400 text-center">Reset Password</h2>

<form action="reset-password" method="post">

<input type="hidden" name="token" value="${token}" />

<input type="password" name="newPassword"
placeholder="Enter New Password"
class="w-full p-3 mb-4 rounded bg-black/40"
required />

<button class="bg-cyan-500 w-full p-3 rounded">
Update Password
</button>

</form>

</div>

</body>
</html>