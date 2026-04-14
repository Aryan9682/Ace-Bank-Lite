<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<title>Withdraw | AceBank</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-[#081428] text-white min-h-screen flex justify-center items-center">

<div class="bg-[#0b1d3a] p-10 rounded-2xl shadow-xl w-[400px]">

<h2 class="text-2xl font-bold mb-6 text-red-400 text-center">Withdraw Money</h2>

<form action="withdraw" method="post">

<input type="number" name="amount" placeholder="Enter Amount"
class="w-full p-3 rounded bg-black mb-4" required>

<button class="w-full bg-red-500 py-3 rounded-lg font-bold hover:bg-red-600">
Withdraw
</button>

</form>

<div class="text-center mt-5">
<a href="home" class="text-gray-400 hover:text-white">← Back to Dashboard</a>
</div>

</div>
</body>
</html>