package com.acebank.lite.controllers;

import com.acebank.lite.models.Transaction;
import com.acebank.lite.dao.BankUserDao;
import com.acebank.lite.dao.BankUserDaoImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/downloadStatement")
public class StatementPdfServlet extends HttpServlet {

    private final BankUserDao userDao = new BankUserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountNumber") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int accNo = (int) session.getAttribute("accountNumber");

        try {
            List<Transaction> transactions = userDao.getStatement(accNo);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=AceBank_Statement.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("AceBank - Account Statement", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Account Number: " + accNo));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell("Date");
            table.addCell("Type");
            table.addCell("Remark");
            table.addCell("Amount");

            for (Transaction tx : transactions) {
                table.addCell(String.valueOf(tx.createdAt()));
                table.addCell(tx.txType());
                table.addCell(tx.remark());
                table.addCell(String.valueOf(tx.amount()));
            }

            document.add(table);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}