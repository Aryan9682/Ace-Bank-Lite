package com.acebank.lite.dao;

import com.acebank.lite.models.*;
import com.acebank.lite.util.ConnectionManager;
import com.acebank.lite.util.QueryLoader;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
public class BankUserDaoImpl implements BankUserDao {

    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    // ================= PASSWORD HASH =================

    @Override
    public String getPasswordHash(int accountNo) throws SQLException {

        String sql = QueryLoader.get("user.get_password_by_acc");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("PASSWORD_HASH");
            }
        }
        return null;
    }

    @Override
    public boolean login(int accountNo, String password) throws SQLException {
        return false;
    }

    // ================= LOGIN USER DETAILS =================

    @Override
    public LoginResult getUserDetails(int accountNo) throws SQLException {

        String sql = QueryLoader.get("user.get_details");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LoginResult(
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getString("EMAIL"),
                        rs.getBigDecimal("BALANCE"),
                        rs.getInt("ACCOUNT_NO")
                );
            }
        }

        throw new SQLException("User not found");
    }

    @Override
    public boolean deposit(int accountNo, BigDecimal amount) throws SQLException {

        String sql = QueryLoader.get("account.deposit");

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setBigDecimal(1, amount);
            ps.setInt(2, accountNo);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }

    @Override
    public boolean withdraw(int accountNo, BigDecimal amount) throws SQLException {

        String sql = QueryLoader.get("account.withdraw");

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setBigDecimal(1, amount);
            ps.setInt(2, accountNo);
            ps.setBigDecimal(3, amount);

            int rows = ps.executeUpdate();

            if(rows > 0){

                PreparedStatement logPs = con.prepareStatement(
                        QueryLoader.get("transaction.log_withdrawal"));

                logPs.setInt(1, accountNo);
                logPs.setInt(2, accountNo);
                logPs.setBigDecimal(3, amount);
                logPs.setString(4, "WITHDRAWAL");
                logPs.setString(5, "ATM Withdrawal");

                logPs.executeUpdate();
            }

            return rows > 0;
        }
    }

    @Override
    public BigDecimal getDailyWithdrawalTotal(int accountNo) throws SQLException {

        String sql = QueryLoader.get("transaction.get_daily_withdrawal_total");

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, accountNo);

            ResultSet rs = ps.executeQuery();

            if(rs.next() && rs.getBigDecimal(1)!=null){
                return rs.getBigDecimal(1);
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public boolean transfer(int fromAccount, int toAccount, BigDecimal amount) throws SQLException {

        Connection con = getConnection();

        try{
            con.setAutoCommit(false);

            // debit sender
            PreparedStatement withdrawPs = con.prepareStatement(
                    QueryLoader.get("account.withdraw"));

            withdrawPs.setBigDecimal(1, amount);
            withdrawPs.setInt(2, fromAccount);
            withdrawPs.setBigDecimal(3, amount);

            int debited = withdrawPs.executeUpdate();

            if(debited == 0){
                con.rollback();
                return false;
            }

            // credit receiver
            PreparedStatement depositPs = con.prepareStatement(
                    QueryLoader.get("account.deposit"));

            depositPs.setBigDecimal(1, amount);
            depositPs.setInt(2, toAccount);
            depositPs.executeUpdate();

            // transaction log
            PreparedStatement logPs = con.prepareStatement(
                    QueryLoader.get("transaction.log"));

            logPs.setInt(1, fromAccount);
            logPs.setInt(2, toAccount);
            logPs.setBigDecimal(3, amount);
            logPs.setString(4, "TRANSFER");
            logPs.setString(5, "Money Transfer");

            logPs.executeUpdate();

            con.commit();
            return true;

        }catch(Exception e){
            con.rollback();
            e.printStackTrace();
            return false;
        }finally{
            con.setAutoCommit(true);
            con.close();
        }
    }

    // ================= SIGNUP =================

    @Override
    public boolean signUp(User user, int accountNo) throws SQLException {

        try (Connection conn = getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement ps1 = conn.prepareStatement(
                    QueryLoader.get("user.signup"),
                    Statement.RETURN_GENERATED_KEYS
            );

            ps1.setString(1, user.firstName());
            ps1.setString(2, user.lastName());
            ps1.setString(3, user.aadhaarNo());
            ps1.setString(4, user.email());
            ps1.setString(5, user.passwordHash());
            ps1.setString(6, user.mobile());
            ps1.executeUpdate();

            ResultSet rs = ps1.getGeneratedKeys();

            if (rs.next()) {
                PreparedStatement ps2 = conn.prepareStatement(QueryLoader.get("account.create"));
                ps2.setInt(1, accountNo);
                ps2.setInt(2, rs.getInt(1));
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            log.severe("Signup failed: " + e.getMessage());
            return false;
        }
    }

    // ================= CHANGE PASSWORD =================

    @Override
    public boolean changePassword(int accountNo, String oldPw, String newPw) throws SQLException {

        String sqlGet = QueryLoader.get("user.get_password_by_acc");
        String sqlUpdate = QueryLoader.get("user.update_password_by_acc");

        try (Connection conn = getConnection();
             PreparedStatement psGet = conn.prepareStatement(sqlGet)) {

            psGet.setInt(1, accountNo);
            ResultSet rs = psGet.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("PASSWORD_HASH");

                // 🔐 compare old password
                if (org.mindrot.jbcrypt.BCrypt.checkpw(oldPw, storedHash)) {

                    // 🔥 HASH NEW PASSWORD
                    String newHash = org.mindrot.jbcrypt.BCrypt.hashpw(newPw,
                            org.mindrot.jbcrypt.BCrypt.gensalt());

                    PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, newHash);
                    psUpdate.setInt(2, accountNo);

                    int rows = psUpdate.executeUpdate();

                    System.out.println("PASSWORD UPDATED ROWS: " + rows);

                    return rows > 0;
                } else {
                    System.out.println("OLD PASSWORD WRONG");
                }
            }
        }

        return false;
    }

    @Override
    public Optional<AccountRecoveryDTO> getRecoveryDetails(String email) throws SQLException {

        String sql = QueryLoader.get("user.recover_details");

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return Optional.of(new AccountRecoveryDTO(
                        rs.getString("FIRST_NAME"),
                        rs.getString("LAST_NAME"),
                        rs.getInt("ACCOUNT_NO")
                ));
            }
        }

        return Optional.empty();
    }

    // ================= BALANCE =================

    @Override
    public BigDecimal getBalance(int accountNo) throws SQLException {

        String sql = QueryLoader.get("account.get_balance");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("BALANCE");
            }
        }

        return BigDecimal.ZERO;
    }

    // ================= TRANSACTION HISTORY =================

    @Override
    public List<Transaction> getStatement(int accountNo) throws SQLException {

        List<Transaction> list = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(QueryLoader.get("transaction.statement"))) {

            ps.setInt(1, accountNo);
            ps.setInt(2, accountNo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Transaction(
                        rs.getInt("ID"),
                        rs.getInt("SENDER_ACCOUNT"),
                        rs.getInt("RECEIVER_ACCOUNT"),
                        rs.getBigDecimal("AMOUNT"),
                        rs.getString("TX_TYPE"),
                        rs.getString("REMARK"),
                        rs.getTimestamp("CREATED_AT").toLocalDateTime()
                ));
            }
        }

        return list;
    }

    // ================= RESET TOKEN =================

    @Override
    public boolean saveResetToken(String email, String token) throws SQLException {

        String sql = """
                UPDATE USERS 
                SET RESET_TOKEN=?, TOKEN_EXPIRY=DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE EMAIL=?
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, token);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePasswordByToken(String token, String newPassword) throws SQLException {

        String sql = """
                UPDATE USERS 
                SET PASSWORD_HASH=?, RESET_TOKEN=NULL, TOKEN_EXPIRY=NULL
                WHERE RESET_TOKEN=? AND TOKEN_EXPIRY > NOW()
                """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, token);

            return ps.executeUpdate() > 0;
        }
    }

    // ================= ACCOUNT EXISTS =================

    @Override
    public boolean accountExists(int accountNo) throws SQLException {

        String sql = "SELECT 1 FROM ACCOUNTS WHERE ACCOUNT_NO=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        }
    }

    @Override
    public boolean updatePasswordDirect(int accountNo, String newHash) throws SQLException {

        String sql = QueryLoader.get("user.update_password_by_acc");

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, newHash);
            ps.setInt(2, accountNo);

            int rows = ps.executeUpdate();

            System.out.println("Rows updated: " + rows);

            return rows > 0;
        }
    }

    @Override
    public boolean saveLoanRequest(int accountNo, String loanType) throws SQLException {

        String sql = """
        INSERT INTO LOAN_REQUESTS (ACCOUNT_NO, LOAN_TYPE)
        VALUES (?, ?)
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountNo);
            ps.setString(2, loanType);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean saveLoanRequest(int accNo, String name, String loanType, int age, double amount) throws SQLException {

        String sql = QueryLoader.get("loan.save_request");

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accNo);
            ps.setString(2, name);
            ps.setString(3, loanType);
            ps.setInt(4, age);
            ps.setDouble(5, amount);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean saveOtp(String email, String otp) throws SQLException {

        String sql = """
        UPDATE USERS
        SET OTP_CODE=?, 
            OTP_EXPIRY=DATE_ADD(NOW(), INTERVAL 5 MINUTE),
            OTP_ATTEMPTS=0
        WHERE EMAIL=?
        """;

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, otp);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<String> getOtpByEmail(String email) throws SQLException {

        String sql = """
        SELECT OTP_CODE 
        FROM USERS 
        WHERE EMAIL=? 
          AND OTP_EXPIRY > NOW()
          AND OTP_ATTEMPTS < 3
        """;

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return Optional.of(rs.getString("OTP_CODE"));
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean incrementOtpAttempts(String email) throws SQLException {

        String sql = "UPDATE USERS SET OTP_ATTEMPTS = OTP_ATTEMPTS + 1 WHERE EMAIL=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean resetOtp(String email) throws SQLException {

        String sql = """
        UPDATE USERS 
        SET OTP_CODE=NULL,
            OTP_EXPIRY=NULL,
            OTP_ATTEMPTS=0
        WHERE EMAIL=?
        """;

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newHash) throws SQLException {

        String sql = "UPDATE USERS SET PASSWORD_HASH=? WHERE EMAIL=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, newHash);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public String getUserMobile(int accountNo) {
        return "";
    }
}