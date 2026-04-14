package com.acebank.lite.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.acebank.lite.dao.BankUserDao;
import com.acebank.lite.dao.BankUserDaoImpl;
import com.acebank.lite.models.LoginResult;
import com.acebank.lite.models.Transaction;
import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;

@Log
@WebServlet("/Login")
public class Login extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();
    private final BankUserDao userDao = new BankUserDaoImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accStr = request.getParameter("accountNumber");
        String password = request.getParameter("password");

        try {
            int accountNo = Integer.parseInt(accStr);

            Optional<LoginResult> loginResultOpt = bankService.authenticate(accountNo, password);

            // ❌ WRONG LOGIN
            if (loginResultOpt.isEmpty()) {

                // 🔥 destroy any old session
                HttpSession old = request.getSession(false);
                if (old != null) old.invalidate();

                response.sendRedirect("login.jsp?error=Invalid+Credentials");
                return;
            }

            // ✅ CORRECT LOGIN
            LoginResult details = loginResultOpt.get();

            // 🔥 ALWAYS destroy old session
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // 🔥 create fresh session
            HttpSession session = request.getSession(true);

            session.setAttribute("accountNumber", accountNo);
            session.setAttribute("firstName", details.firstName());
            session.setAttribute("lastName", details.lastName());
            session.setAttribute("email", details.email());
            session.setAttribute("balance", details.balance());

            List<Transaction> statement = userDao.getStatement(accountNo);
            session.setAttribute("transactionDetailsList", statement);

            response.sendRedirect(request.getContextPath() + "/home");

        } catch (Exception e) {
            log.severe("Login error: " + e.getMessage());
            response.sendRedirect("login.jsp?error=System+Error");
        }
    }
}