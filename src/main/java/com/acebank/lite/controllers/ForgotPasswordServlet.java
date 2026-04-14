package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/ForgotPassword")
public class ForgotPasswordServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");

        boolean sent = bankService.sendResetLink(email);

        if(sent){
            response.sendRedirect("login.jsp?msg=Account details sent to email");
        }else{
            response.sendRedirect("ForgotPassword.jsp?msg=Email not found");
        }
    }
}