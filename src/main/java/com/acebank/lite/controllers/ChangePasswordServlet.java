package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/ChangePassword")
public class ChangePasswordServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    // 🟢 OPEN PAGE
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // ✅ correct path (tumhari file yahi hai)
        request.getRequestDispatcher("/WEB-INF/views/ChangePassword.jsp")
                .forward(request, response);
    }

    // 🟢 UPDATE PASSWORD
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int accNo = (int) session.getAttribute("accountNumber");

        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");

        try {

            boolean success = bankService.changePassword(accNo, oldPass, newPass);

            if (success) {

                // 🔥 logout user after change
                session.invalidate();

                response.sendRedirect("login.jsp?msg=Password+Changed+Login+Again");

            } else {
                response.sendRedirect("ChangePassword?error=Wrong+Old+Password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ChangePassword?error=System+Error");
        }
    }
}