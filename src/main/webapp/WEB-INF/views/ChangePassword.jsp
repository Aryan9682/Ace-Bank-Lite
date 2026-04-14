<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<title>Change Password</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-black text-white flex justify-center items-center min-h-screen">

<div class="bg-white/10 p-8 rounded-xl w-96">
<h2 class="text-2xl mb-6 text-cyan-400">Change Password</h2>

<form action="ChangePassword" method="post">

    <input type="password" name="oldPassword"
           placeholder="Old Password"
           required
           class="w-full mb-3 p-3 rounded bg-black text-white">

    <input type="password" name="newPassword"
           placeholder="New Password"
           required
           class="w-full mb-3 p-3 rounded bg-black text-white">

    <button type="submit"
            class="w-full bg-cyan-500 p-3 rounded text-white font-bold">
        Update Password
    </button>

</form>

</div>
</body>
</html>