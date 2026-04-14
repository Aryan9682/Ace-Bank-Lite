<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Loan Request | AceBank</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gradient-to-br from-blue-950 via-black to-blue-900 min-h-screen text-white flex items-center justify-center">

<div class="w-full max-w-4xl bg-white/10 backdrop-blur-lg p-10 rounded-2xl shadow-xl">

    <!-- Header -->
    <h2 class="text-3xl font-bold text-cyan-400 mb-2">Loan Application</h2>
    <p class="text-gray-400 mb-6">
        Dear ${sessionScope.firstName}, you are eligible for loans up to
        <span class="text-green-400 font-bold text-lg">₹ 5,00,000</span>
    </p>

    <!-- Success -->
    <c:if test="${not empty param.msg}">
        <div class="bg-green-500 text-white p-3 rounded mb-4 text-center">
            ${param.msg}
        </div>
    </c:if>

    <!-- Error -->
    <c:if test="${not empty param.error}">
        <div class="bg-red-500 text-white p-3 rounded mb-4 text-center">
            ${param.error}
        </div>
    </c:if>

    <form action="applyLoan" method="post" class="grid grid-cols-2 gap-6">

        <!-- Loan Type -->
        <div>
            <label class="block mb-2 text-gray-300">Loan Type</label>
            <select name="loanType"
                    class="w-full p-3 rounded-lg bg-black border border-gray-600"
                    required>
                <option value="">Select Loan Type</option>
                <option value="Personal Loan">Personal Loan</option>
                <option value="Home Loan">Home Loan</option>
                <option value="Education Loan">Education Loan</option>
                <option value="Business Loan">Business Loan</option>
            </select>
        </div>

        <!-- Age -->
        <div>
            <label class="block mb-2 text-gray-300">Your Age</label>
            <input type="number" name="age"
                   class="w-full p-3 rounded-lg bg-black border border-gray-600"
                   required>
        </div>

        <!-- Required Amount -->
        <div class="col-span-2">
            <label class="block mb-2 text-gray-300">Required Loan Amount</label>
            <input type="number" name="amount"
                   placeholder="Enter required amount"
                   class="w-full p-3 rounded-lg bg-black border border-gray-600"
                   required>
        </div>

        <!-- Submit -->
        <div class="col-span-2 mt-4">
            <button type="submit"
                    class="w-full bg-cyan-500 py-3 rounded-xl text-lg font-bold hover:bg-cyan-600">
                Submit Loan Request
            </button>
        </div>

    </form>

    <!-- Back Button -->
    <div class="mt-6 text-center">
        <a href="home"
           class="text-gray-400 hover:text-white underline">
            ← Back to Dashboard
        </a>
    </div>

</div>

</body>
</html>