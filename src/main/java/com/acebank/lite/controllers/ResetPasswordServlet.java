package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        request.setAttribute("token", token);

        request.getRequestDispatcher("/reset.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");

        boolean success = bankService.resetPassword(token, newPassword);

        if(success){
            response.sendRedirect("login.jsp?msg=Password Reset Successful");
        } else {
            response.sendRedirect("login.jsp?msg=Invalid or Expired Link");
        }
    }
}