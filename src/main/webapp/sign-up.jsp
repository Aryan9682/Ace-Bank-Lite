<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Create Account | AceBank</title>

<script src="https://cdn.tailwindcss.com"></script>

</head>

<body class="bg-gradient-to-br from-blue-900 to-black min-h-screen flex items-center justify-center">

<div class="bg-white/10 backdrop-blur-lg shadow-2xl rounded-2xl p-10 w-[420px] text-white">

    <!-- LOGO -->
    <h1 class="text-3xl font-bold text-center mb-6 text-cyan-400">AceBank</h1>

    <h2 class="text-xl text-center mb-6 font-semibold">Create Your Account</h2>

    <form action="signup" method="POST" class="space-y-4">

        <!-- First + Last Name -->
        <div class="flex gap-4">
            <input type="text" name="firstName"
                placeholder="First Name"
                class="w-1/2 p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                required>

            <input type="text" name="lastName"
                placeholder="Last Name"
                class="w-1/2 p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                required>
        </div>

        <!-- Aadhaar -->
        <input type="text" name="aadharNumber"
            placeholder="Aadhaar Number (12 digits)"
            pattern="\d{12}"
            class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
            required>

        <!-- Email -->
        <input type="email" name="email"
            placeholder="Email Address"
            class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
            required>

        <!-- Password -->
        <input type="password" name="password"
            placeholder="Password (Min 8 chars)"
            minlength="8"
            class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
            required>

            <input type="text" name="mobile"
            placeholder="Mobile Number"
            pattern="[0-9]{10}"
            maxlength="10"
            class="w-full p-3 rounded-lg bg-white/20 border border-gray-400 focus:outline-none focus:ring-2 focus:ring-cyan-400"
            required>

        <!-- Submit -->
        <button type="submit"
            class="w-full bg-cyan-500 hover:bg-cyan-600 p-3 rounded-lg font-bold text-lg">
            Create Account
        </button>

    </form>

    <p class="text-center mt-6 text-sm">
        Already have an account?
        <a href="login.jsp" class="text-cyan-400 hover:underline">Login</a>
    </p>

</div>

</body>
</html>