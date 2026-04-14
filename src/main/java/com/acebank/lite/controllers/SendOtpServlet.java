package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/send-otp")
public class SendOtpServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        boolean sent = bankService.sendOtp(email);

        if(sent){
            request.setAttribute("email", email);
            request.getRequestDispatcher("verifyOtp.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect("ForgotPassword.jsp?error=Email+Not+Found");
        }
    }
}