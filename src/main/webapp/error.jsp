<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${errorTitle} | AceBank</title>

<script src="https://cdn.tailwindcss.com"></script>

</head>

<body class="bg-gradient-to-br from-blue-900 to-black min-h-screen flex items-center justify-center text-white">

<div class="bg-white/10 backdrop-blur-lg shadow-2xl rounded-2xl p-12 w-[500px] text-center">

    <!-- Error Code -->
    <h1 class="text-7xl font-bold text-red-400 mb-4">${errorCode}</h1>

    <!-- Title -->
    <h2 class="text-2xl font-semibold mb-4">${errorTitle}</h2>

    <!-- Message -->
    <p class="text-gray-300 mb-8">
        ${errorMessage}
    </p>

    <!-- Buttons -->
    <div class="space-x-4">
        <a href="${pageContext.request.contextPath}/home"
           class="bg-cyan-500 hover:bg-cyan-600 px-6 py-3 rounded-lg font-semibold">
            Go Dashboard
        </a>

        <a href="index.jsp"
           class="border border-cyan-400 px-6 py-3 rounded-lg hover:bg-cyan-500">
            Home Page
        </a>
    </div>

</div>

</body>
</html>