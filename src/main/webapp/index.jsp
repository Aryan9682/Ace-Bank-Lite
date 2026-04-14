<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>AceBank - Digital Banking</title>

<script src="https://cdn.tailwindcss.com"></script>

</head>

<body class="bg-gradient-to-br from-blue-900 via-black to-blue-950 min-h-screen text-white">

<!-- NAVBAR -->
<nav class="flex justify-between items-center px-10 py-6">
    <h1 class="text-3xl font-bold text-cyan-400">AceBank</h1>

    <div class="space-x-6">
        <a href="login.jsp" class="hover:text-cyan-400 text-lg">Login</a>
        <a href="sign-up.jsp"
           class="bg-cyan-500 px-6 py-2 rounded-lg hover:bg-cyan-600 text-lg font-semibold">
           Sign Up
        </a>
    </div>
</nav>

<!-- HERO SECTION -->
<div class="flex flex-col items-center justify-center text-center mt-20 px-4">

    <p class="text-cyan-400 mb-4 text-lg">Trusted by 2M+ Users</p>

    <h2 class="text-5xl font-bold mb-6 leading-tight">
        Banking Made <span class="text-cyan-400">Simple & Secure</span>
    </h2>

    <p class="text-gray-300 max-w-xl mb-8 text-lg">
        Manage your money, transfer funds, and track transactions with
        next-generation digital banking. Fast, secure and reliable.
    </p>

    <div class="space-x-4">
        <a href="sign-up.jsp"
           class="bg-cyan-500 px-8 py-3 rounded-lg text-lg font-semibold hover:bg-cyan-600 shadow-lg">
           Open Account
        </a>

        <a href="login.jsp"
           class="border border-cyan-400 px-8 py-3 rounded-lg text-lg hover:bg-cyan-500">
           Login Now
        </a>
    </div>
</div>

<!-- FEATURES -->
<div class="grid md:grid-cols-3 gap-8 px-16 mt-24">

    <div class="bg-white/10 backdrop-blur-lg p-8 rounded-xl text-center">
        <h3 class="text-xl font-bold mb-2 text-cyan-400">Secure Banking</h3>
        <p class="text-gray-300">Your money and data are protected with high-level encryption.</p>
    </div>

    <div class="bg-white/10 backdrop-blur-lg p-8 rounded-xl text-center">
        <h3 class="text-xl font-bold mb-2 text-cyan-400">Instant Transfer</h3>
        <p class="text-gray-300">Send and receive money instantly anytime, anywhere.</p>
    </div>

    <div class="bg-white/10 backdrop-blur-lg p-8 rounded-xl text-center">
        <h3 class="text-xl font-bold mb-2 text-cyan-400">24/7 Access</h3>
        <p class="text-gray-300">Access your account and transactions anytime online.</p>
    </div>

</div>

<!-- FOOTER -->
<footer class="text-center mt-20 pb-6 text-gray-400">
    © 2026 AceBank. All rights reserved.
</footer>

</body>
</html>