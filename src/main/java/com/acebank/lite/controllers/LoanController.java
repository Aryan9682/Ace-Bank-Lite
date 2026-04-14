package com.acebank.lite.controllers;

import com.acebank.lite.service.BankService;
import com.acebank.lite.service.BankServiceImpl;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/applyLoan")
public class LoanController extends HttpServlet {

    private final BankService bankService = new BankServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if(session == null){
            response.sendRedirect("login.jsp");
            return;
        }

        int accNo = (int) session.getAttribute("accountNumber");
        String name = (String) session.getAttribute("firstName");
        String email = (String) session.getAttribute("email");

        String loanType = request.getParameter("loanType");
        String ageStr = request.getParameter("age");
        String amountStr = request.getParameter("amount");

        System.out.println("LoanType: "+loanType);
        System.out.println("Age: "+ageStr);
        System.out.println("Amount: "+amountStr);

        try{
            int age = Integer.parseInt(ageStr);
            double amount = Double.parseDouble(amountStr);

            boolean success = bankService.applyLoan(accNo,name,email,loanType,age,amount);

            if(success){
                response.sendRedirect("home?msg=Loan+Request+Submitted+Successfully");
            }else{
                response.sendRedirect("home?error=Loan+Amount+Exceeds+PreApproved+Limit");
            }

        }catch(Exception e){
            e.printStackTrace();   // 🔥 real error dikhega console me
            response.sendRedirect("home?error=System+Error");
        }
    }
}