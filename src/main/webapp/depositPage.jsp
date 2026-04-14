<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<title>Deposit | AceBank</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-[#081428] text-white min-h-screen flex justify-center items-center">

<div class="bg-[#0b1d3a] p-10 rounded-2xl shadow-xl w-[400px]">

<h2 class="text-2xl font-bold mb-6 text-cyan-400 text-center">Deposit Money</h2>

<form action="deposit" method="post">

<input type="number" name="amount" placeholder="Enter Amount"
class="w-full p-3 rounded bg-black mb-4" required>

<button class="w-full bg-cyan-500 py-3 rounded-lg font-bold hover:bg-cyan-600">
Deposit Now
</button>

</form>

<div class="text-center mt-5">
<a href="home" class="text-gray-400 hover:text-white">← Back to Dashboard</a>
</div>

</div>
</body>
</html>