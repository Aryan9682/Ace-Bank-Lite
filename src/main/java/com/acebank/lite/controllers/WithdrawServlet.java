package com.acebank.lite.controllers;

import com.acebank.lite.service.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        HttpSession session = req.getSession(false);

        if(session == null || session.getAttribute("accountNumber") == null){
            res.sendRedirect("login.jsp");
            return;
        }

        try{
            int accNo = (int) session.getAttribute("accountNumber");

            String amtStr = req.getParameter("amount");

            if(amtStr == null || amtStr.isEmpty()){
                res.sendRedirect("home?error=Enter+valid+amount");
                return;
            }

            BigDecimal amount = new BigDecimal(amtStr);

            String result = bankService.withdraw(accNo, amount);

            if(result.equals("SUCCESS")){
                res.sendRedirect("home?msg=Withdraw+Success");
            }else{
                res.sendRedirect("home?error="+ java.net.URLEncoder.encode(result,"UTF-8"));
            }

        }catch(Exception e){
            e.printStackTrace();   // ⭐ console me real error dikhega
            res.sendRedirect("home?error=System+Error");
        }
    }
}