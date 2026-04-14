package com.acebank.lite.service;



import com.acebank.lite.models.LoginResult;
import com.acebank.lite.models.ServiceResponse;
import com.acebank.lite.models.Transaction;
import com.acebank.lite.models.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BankService {
    Optional<LoginResult> authenticate(int accountNo, String password) throws SQLException;

    boolean changePassword(int accountNo, String oldPlain, String newPlain) throws SQLException;

    boolean processDeposit(int accountNo, BigDecimal amount);

    BigDecimal getBalance(int accountNo);

    List<Transaction> getTransactionHistory(int accountNo);

    ServiceResponse processTransfer(int fromAcc, int toAcc, BigDecimal amount);

    String withdraw(int accountNo, BigDecimal amount);

    Optional<LoginResult> registerUser(User user);

    public boolean recoverAccount(String email);

    public boolean applyForLoan(String firstName, String email, String loanType);

    boolean sendResetLink(String email);

    boolean resetPassword(String token, String newPassword);

    boolean applyLoan(int accountNo, String loanType);

    boolean applyLoan(int accNo, String name, String email,
                      String loanType, int age, double amount) throws Exception;


    boolean sendOtp(String email);

    boolean verifyOtpAndReset(String email, String otp, String newPassword);


}