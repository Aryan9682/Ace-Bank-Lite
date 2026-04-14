<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard | AceBank</title>
<script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gradient-to-br from-[#081428] via-[#07101f] to-black min-h-screen text-white">

<!-- ================= NAVBAR ================= -->

<nav class="flex justify-between items-center px-10 py-4 bg-[#0b1d3a] shadow-lg">

<div class="flex items-center gap-8">

<div class="flex items-center gap-2">
    <div class="bg-yellow-400 p-2 rounded-lg">🏦</div>
    <h1 class="text-xl font-bold text-yellow-400">Ace Bank</h1>
</div>

<div class="hidden md:flex gap-8 text-gray-300 font-semibold">
    <a href="home" class="flex items-center gap-2 hover:text-cyan-400">🏠 Dashboard</a>
    <a href="downloadStatement" class="flex items-center gap-2 hover:text-cyan-400">📄 Statement</a>
    <a href="loanPage" class="flex items-center gap-2 hover:text-cyan-400">🏦 Loan</a>
</div>

</div>

<div class="flex items-center gap-6">

<div class="relative cursor-pointer">
    <span class="text-2xl hover:text-yellow-400">🔔</span>
    <span class="absolute -top-1 -right-1 bg-red-500 text-xs px-1 rounded-full">3</span>
</div>

<div class="relative">
    <button onclick="toggleMenu()" class="flex items-center gap-2 bg-white/10 px-4 py-2 rounded-lg hover:bg-white/20">
        <div class="bg-yellow-500 w-9 h-9 rounded-full flex items-center justify-center text-black font-bold text-lg">
            ${sessionScope.firstName.substring(0,1)}
        </div>
        <span class="font-semibold">${sessionScope.firstName}</span>
    </button>


<div id="profileMenu"
     class="hidden absolute right-0 mt-3 w-52 bg-[#0b1d3a] rounded-xl shadow-xl border border-gray-700">

    <a href="home" class="block px-5 py-3 hover:bg-white/10 border-b border-gray-700">🏠 Dashboard</a>
    <a href="ChangePassword" class="block px-5 py-3 hover:bg-white/10 border-b border-gray-700">🔒 Change Password</a>
    <a href="loanPage" class="block px-5 py-3 hover:bg-white/10 border-b border-gray-700">🏦 Loan Request</a>
    <a href="Logout" class="block px-5 py-3 text-red-400 hover:bg-red-500/20">🚪 Logout</a>
</div>


</div>

</div>
</nav>

<script>
function toggleMenu(){
 document.getElementById("profileMenu").classList.toggle("hidden");
}
</script>

<!-- ================= MAIN ================= -->

<div class="max-w-7xl mx-auto px-10 py-10">

<c:if test="${not empty param.msg}">

<div class="bg-green-500 text-white p-3 rounded mb-5 text-center shadow-lg">
${param.msg}
</div>
</c:if>

<c:if test="${not empty param.error}">

<div class="bg-red-500 text-white p-3 rounded mb-5 text-center shadow-lg">
${param.error}
</div>
</c:if>

<!-- WELCOME CARD -->

<div class="bg-gradient-to-r from-[#102a52] to-[#0b1d3a] p-8 rounded-2xl shadow-lg mb-10">
    <h2 class="text-gray-300">Welcome back,</h2>
    <h1 class="text-3xl font-bold">${sessionScope.firstName}! 👋</h1>

<!-- 🔐 Account number hide/show -->
<p class="text-gray-400 mt-2 flex items-center gap-3">

    Account:
    <span id="accNumber" data-full="${sessionScope.accountNumber}">
        xxxx${sessionScope.accountNumber.toString().substring(sessionScope.accountNumber.toString().length()-4)}
    </span>

    <span onclick="toggleAcc()" class="cursor-pointer text-xl hover:text-cyan-400">👁</span>

</p>


</div>

<!-- BALANCE -->

<div class="bg-[#132a4a] p-7 rounded-2xl shadow-lg w-[350px] mb-12">
    <p class="text-gray-400">Total Balance</p>
    <h1 class="text-3xl font-bold text-cyan-400 mt-2">
        ₹ ${sessionScope.balance}
    </h1>
</div>

<!-- ACTIONS -->

<div class="grid grid-cols-1 md:grid-cols-4 gap-8">

<a href="depositPage.jsp" class="bg-[#132a4a] p-8 rounded-2xl shadow-lg hover:scale-105 hover:bg-[#173a63] transition text-center">
<div class="text-4xl mb-3">💰</div>
<h2 class="text-xl font-bold">Deposit</h2>
<p class="text-gray-400 text-sm">Add money</p>
</a>

<a href="withdrawPage.jsp" class="bg-[#132a4a] p-8 rounded-2xl shadow-lg hover:scale-105 hover:bg-[#173a63] transition text-center">
<div class="text-4xl mb-3">🏧</div>
<h2 class="text-xl font-bold">Withdraw</h2>
<p class="text-gray-400 text-sm">Cash out</p>
</a>

<a href="transferPage.jsp" class="bg-[#132a4a] p-8 rounded-2xl shadow-lg hover:scale-105 hover:bg-[#173a63] transition text-center">
<div class="text-4xl mb-3">💸</div>
<h2 class="text-xl font-bold">Transfer</h2>
<p class="text-gray-400 text-sm">Send money</p>
</a>

<a href="loanPage" class="bg-[#132a4a] p-8 rounded-2xl shadow-lg hover:scale-105 hover:bg-[#173a63] transition text-center">
<div class="text-4xl mb-3">🏦</div>
<h2 class="text-xl font-bold">Loan Request</h2>
<p class="text-gray-400 text-sm">Apply for loan</p>
</a>

</div>

<!-- ================= RECENT TRANSACTIONS ================= -->

<div class="bg-[#132a4a] p-8 rounded-2xl shadow-lg mt-12">


<h2 class="text-2xl font-bold text-cyan-400 mb-6">
    Recent Transactions
</h2>

<div class="overflow-x-auto">

    <table class="w-full text-left border-collapse">

        <thead>
            <tr class="bg-cyan-600 text-white">
                <th class="p-3">Date</th>
                <th class="p-3">Type</th>
                <th class="p-3">Remark</th>
                <th class="p-3 text-right">Amount</th>
            </tr>
        </thead>

        <tbody class="bg-[#081428]">

            <c:forEach var="tx" items="${sessionScope.transactionDetailsList}">
                <tr class="border-b border-gray-700 hover:bg-[#173a63]">

                    <td class="p-3">${tx.createdAt}</td>
                    <td class="p-3 font-semibold">${tx.txType}</td>
                    <td class="p-3">${tx.remark}</td>

                    <td class="p-3 text-right font-bold
                        <c:choose>
                            <c:when test="${tx.txType=='DEPOSIT'}">text-green-400</c:when>
                            <c:when test="${tx.txType=='TRANSFER'}">text-yellow-400</c:when>
                            <c:otherwise>text-red-400</c:otherwise>
                        </c:choose>">
                        ₹ ${tx.amount}
                    </td>

                </tr>
            </c:forEach>

        </tbody>

    </table>

</div>


</div>

</div>

<!-- 🔐 Toggle account number script -->

<script>
let visible=false;

function toggleAcc(){
    let span=document.getElementById("accNumber");
    let full=span.getAttribute("data-full");

    if(!visible){
        span.innerText=full;
        visible=true;
    }else{
        span.innerText="xxxx"+full.slice(-4);
        visible=false;
    }
}
</script>

</body>
</html>
