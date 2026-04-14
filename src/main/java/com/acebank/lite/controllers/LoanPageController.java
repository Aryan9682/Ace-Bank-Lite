package com.acebank.lite.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

@WebServlet("/loanPage")
public class LoanPageController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/LoanPage.jsp")
                .forward(request, response);
    }
}