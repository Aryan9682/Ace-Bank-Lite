package com.acebank.lite.dao;

import com.acebank.lite.models.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BankUserDao {

    String getPasswordHash(int accountNo) throws SQLException;

    boolean login(int accountNo, String password) throws SQLException;

    boolean signUp(User user, int accountNo) throws SQLException;

    LoginResult getUserDetails(int accountNo) throws SQLException;

    boolean deposit(int accountNo, BigDecimal amount) throws SQLException;

    boolean withdraw(int accountNo, BigDecimal amount) throws SQLException;

    BigDecimal getDailyWithdrawalTotal(int accountNo) throws SQLException;

    boolean transfer(int fromAccount, int toAccount, BigDecimal amount) throws SQLException;

    List<Transaction> getStatement(int accountNo) throws SQLException;

    boolean changePassword(int accountNo, String oldPw, String newPw) throws SQLException;

    Optional<AccountRecoveryDTO> getRecoveryDetails(String email) throws SQLException;

    boolean accountExists(int accountNo) throws SQLException;

    BigDecimal getBalance(int accountNo) throws SQLException;

    boolean saveResetToken(String email,String token) throws SQLException;

    boolean updatePasswordByToken(String token,String newPassword) throws SQLException;

    boolean updatePasswordDirect(int accountNo, String newHash) throws SQLException;

    boolean saveLoanRequest(int accountNo, String loanType) throws SQLException;

    boolean saveLoanRequest(int accNo, String name, String loanType, int age, double amount) throws SQLException;

    boolean saveOtp(String email, String otp) throws SQLException;
    Optional<String> getOtpByEmail(String email) throws SQLException;
    boolean incrementOtpAttempts(String email) throws SQLException;
    boolean resetOtp(String email) throws SQLException;
    boolean updatePasswordByEmail(String email, String newHash) throws SQLException;

    String getUserMobile(int accountNo);
}