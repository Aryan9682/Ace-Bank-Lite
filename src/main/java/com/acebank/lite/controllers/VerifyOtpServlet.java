package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");
        String newPassword = request.getParameter("newPassword");

        boolean success = bankService.verifyOtpAndReset(email, otp, newPassword);

        if(success){
            response.sendRedirect("login.jsp?msg=Password+Reset+Successful");
        } else {
            response.sendRedirect("verifyOtp.jsp?error=Invalid+OTP+or+Expired");
        }
    }
}