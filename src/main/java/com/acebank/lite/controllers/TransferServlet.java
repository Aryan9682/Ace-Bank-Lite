package com.acebank.lite.controllers;

import com.acebank.lite.models.ServiceResponse;
import com.acebank.lite.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        HttpSession session = req.getSession(false);
        int fromAcc = (int) session.getAttribute("accountNumber");

        int toAcc = Integer.parseInt(req.getParameter("toAccount"));
        BigDecimal amount = new BigDecimal(req.getParameter("amount"));

        ServiceResponse response = bankService.processTransfer(fromAcc,toAcc,amount);

        if(response.success()){
            res.sendRedirect("home?msg="+response.message().replace(" ","+"));
        }else{
            res.sendRedirect("home?error="+response.message().replace(" ","+"));
        }
    }
}