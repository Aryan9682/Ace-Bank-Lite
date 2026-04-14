package com.acebank.lite.service;

import com.acebank.lite.util.SmsUtil;
import com.acebank.lite.dao.BankUserDao;
import com.acebank.lite.dao.BankUserDaoImpl;

import com.acebank.lite.models.*;
import com.acebank.lite.util.MailUtil;
import com.acebank.lite.util.PasswordUtil;
import lombok.extern.java.Log;

import java.math.BigDecimal;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;



@Log
public class BankServiceImpl implements BankService {

    private final BankUserDao userDao = new BankUserDaoImpl(); // Or get via Singleton
    private static final BigDecimal DAILY_LIMIT = new BigDecimal("2000.00");


    @Override
    public Optional<LoginResult> authenticate(int accountNo, String plainPassword) {
        try {
            String storedHash = userDao.getPasswordHash(accountNo);

            System.out.println("Entered password: " + plainPassword);
            System.out.println("Stored hash: " + storedHash);

            boolean match = PasswordUtil.checkPassword(plainPassword, storedHash);
            System.out.println("Password match: " + match);

            if (match) {
                return Optional.of(userDao.getUserDetails(accountNo));
            }

        } catch (SQLException e) {
            log.severe("Login DB error: " + e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public boolean changePassword(int accountNo, String oldPlain, String newPlain) {

        try {
            // 🔐 get stored hash
            String storedHash = userDao.getPasswordHash(accountNo);

            if (storedHash == null) {
                System.out.println("User not found");
                return false;
            }

            // 🔐 check old password
            if (!PasswordUtil.checkPassword(oldPlain, storedHash)) {
                System.out.println("OLD PASSWORD WRONG");
                return false;
            }

            // 🔐 hash new password
            String newHash = PasswordUtil.hashPassword(newPlain);

            // 🔥 UPDATE PASSWORD IN DB
            boolean updated = userDao.updatePasswordDirect(accountNo, newHash);

            if(updated){
                System.out.println("PASSWORD UPDATED SUCCESSFULLY");
            }else{
                System.out.println("PASSWORD UPDATE FAILED");
            }

            return updated;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean processDeposit(int accountNo, BigDecimal amount) {

        // Rule: amount valid hona chahiye
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        try {
            boolean success = userDao.deposit(accountNo, amount);

            if (success) {

                // 1️⃣ Updated balance nikalo
                BigDecimal newBalance = userDao.getBalance(accountNo);

                // 2️⃣ User email nikalo
                String email = userDao.getUserDetails(accountNo).email();

                // 3️⃣ Email message banao
                String message = String.format("""
                    Dear Customer,

                    ₹%s has been CREDITED to your account.
                    Available Balance: ₹%s

                    Thank you for banking with AceBank.
                    """, amount, newBalance);

                // 4️⃣ Async email send
                MailUtil.sendMailAsync(email, "Deposit Alert - AceBank", message);

                // 📱 SEND SMS ALSO
                String mobile = userDao.getUserMobile(accountNo);

                if(mobile != null){
                    String sms = "AceBank: Rs "+amount+" credited. Bal: Rs "+newBalance;
                    SmsUtil.sendSMS(mobile, sms);
                }
            }

            return success;

        } catch (SQLException e) {
            log.severe("Deposit Error for " + accountNo + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public String withdraw(int accountNo, BigDecimal amount) {
        try {
            // Rule 1: No negative or zero amounts
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return "Invalid amount.";
            }

            // Rule 2: Daily Limit Check
            BigDecimal alreadyWithdrawn = userDao.getDailyWithdrawalTotal(accountNo);
            BigDecimal projectedTotal = alreadyWithdrawn.add(amount);

            if (projectedTotal.compareTo(DAILY_LIMIT) > 0) {
                BigDecimal remaining = DAILY_LIMIT.subtract(alreadyWithdrawn);
                return "Limit exceeded. You can only withdraw ₹" + remaining + " more today.";
            }

            // Rule 3: Process withdrawal in DB
            boolean success = userDao.withdraw(accountNo, amount);

            if (success) {

                // ✅ STEP 4: Get updated balance
                BigDecimal newBalance = userDao.getBalance(accountNo);

                // ✅ STEP 5: Get user email
                String email = userDao.getUserDetails(accountNo).email();

                // ✅ STEP 6: Prepare email message
                String message = String.format("""
                    Dear Customer,

                    ₹%s has been DEBITED from your account.
                    Available Balance: ₹%s

                    If this was not you, contact support immediately.

                    Thank you for banking with AceBank.
                    """, amount, newBalance);

                // ✅ STEP 7: Send email asynchronously
                MailUtil.sendMailAsync(email, "Withdrawal Alert - AceBank", message);

                // 📱 SEND SMS ALSO
                String mobile = userDao.getUserMobile(accountNo);

                if(mobile != null){
                    String sms = "AceBank: Rs "+amount+" debited. Bal: Rs "+newBalance;
                    SmsUtil.sendSMS(mobile, sms);
                }

                return "SUCCESS";
            } else {
                return "Insufficient balance or account error.";
            }

        } catch (SQLException e) {
            return "System error. Please try later.";
        }
    }


    @Override
    public Optional<LoginResult> registerUser(User user) {
        // 1. Generate a unique account number
        int accountNumber = ThreadLocalRandom.current().nextInt(10000000, 99999999);
        // Hash before saving to DB
        String secureHash = PasswordUtil.hashPassword(user.passwordHash());

        // Create a new version of the record with the hash
        User secureUser = new User(
                user.userId(), user.firstName(), user.lastName(),
                user.aadhaarNo(), user.email(), secureHash, user.mobile(), user.createdAt()
        );
        try {
            // 2. Save to Database via DAO
            boolean isSaved = userDao.signUp(secureUser, accountNumber);

            if (isSaved) {
                // 3. Send Welcome Email (Asynchronous is better, but this works for now)
                sendWelcomeEmail(user, accountNumber);

                // 4. Return the details to be used for the session
                return Optional.of(new LoginResult(
                        user.firstName(),
                        user.lastName(),
                        user.email(),
                        BigDecimal.ZERO,
                        accountNumber
                ));
            }
        } catch (Exception e) {
            log.severe("Signup Error: " + e.getMessage());
        }
        return Optional.empty();
    }


    private void sendWelcomeEmail(User user, int accNo) {
        String subject = "Welcome to AceBank";
        String msg = String.format("Dear %s,\n\nWelcome! Your account number is: %d.\nKeep it safe!",
                user.firstName(), accNo);
        try {
            MailUtil.sendMail(user.email(), subject, msg);
        } catch (Exception e) {
            log.warning("Email failed to send, but account was created.");
        }
    }

    @Override
    public BigDecimal getBalance(int accountNo) {
        try {
            return userDao.getBalance(accountNo);
        } catch (SQLException e) {
            log.severe("Could not fetch balance for: " + accountNo);
            return BigDecimal.ZERO; // Default fallback
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(int accountNo) {
        try {
            return userDao.getStatement(accountNo);
        } catch (SQLException e) {
            log.severe("Could not fetch transactions for: " + accountNo);
            return List.of(); // Return empty list to avoid NullPointer in JSP
        }
    }

    @Override
    public ServiceResponse processTransfer(int fromAcc, int toAcc, BigDecimal amount) {
        // 1. Validation: Self-transfer
        if (fromAcc == toAcc) {
            return new ServiceResponse(false, "You cannot transfer money to your own account.");
        }

        // 2. Validation: Positive amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new ServiceResponse(false, "Please enter a valid amount greater than zero.");
        }

        try {
            // 3. Check if Recipient exists
            // This avoids triggering foreign key constraint errors in the DB
            if (!userDao.accountExists(toAcc)) {
                return new ServiceResponse(false, "Recipient account number " + toAcc + " not found.");
            }

            // 4. Check Sender's Balance
            // We do this in the Service to provide a friendly message before hitting the DB
            BigDecimal currentBalance = userDao.getBalance(fromAcc);
            if (currentBalance.compareTo(amount) < 0) {
                return new ServiceResponse(false, "Insufficient balance. Your current balance is ₹" + currentBalance);
            }

            // 5. Atomic Transaction
            // We pass the amount to the DAO which handles Debit, Credit, and Logging
            boolean success = userDao.transfer(fromAcc, toAcc, amount);

            if (success) {
                // 1️⃣ Get Updated Balances
                BigDecimal senderBalance = userDao.getBalance(fromAcc);
                BigDecimal receiverBalance = userDao.getBalance(toAcc);

                // 2️⃣ Get Email IDs
                String senderEmail = userDao.getUserDetails(fromAcc).email();
                String receiverEmail = userDao.getUserDetails(toAcc).email();

                // 3️⃣ Prepare Messages
                String debitMessage = String.format("""
            Dear Customer,

            ₹%s has been DEBITED from your account.
            Transferred To: %d
            Available Balance: ₹%s

            Thank you for banking with AceBank.
            """, amount, toAcc, senderBalance);

                String creditMessage = String.format("""
            Dear Customer,

            ₹%s has been CREDITED to your account.
            Received From: %d
            Available Balance: ₹%s

            Thank you for banking with AceBank.
            """, amount, fromAcc, receiverBalance);

                // 4️⃣ Send Email Asynchronously
                MailUtil.sendMailAsync(senderEmail, "Debit Alert - AceBank", debitMessage);
                MailUtil.sendMailAsync(receiverEmail, "Credit Alert - AceBank", creditMessage);
                return new ServiceResponse(true, "Transfer Successful!");
            } else {
                return new ServiceResponse(false, "Transfer could not be processed. Please try again.");
            }

        } catch (SQLException e) {
            log.severe("SQL Error during transfer: " + e.getMessage());
            return new ServiceResponse(false, "Database connection error. Please contact support.");
        } catch (Exception e) {
            log.severe("General Error during transfer: " + e.getMessage());
            return new ServiceResponse(false, "An unexpected error occurred.");
        }
    }

    @Override
    public boolean recoverAccount(String email) {
        try {
            // 1. Get the joined details from DAO
            Optional<AccountRecoveryDTO> detailsOpt = userDao.getRecoveryDetails(email);

            if (detailsOpt.isPresent()) {
                AccountRecoveryDTO details = detailsOpt.get();

                // 2. Compose Email
                String subject = "AceBank - Account Recovery";
                String msg = "Hi " + details.firstName() + " " + details.lastName() + ",\n\n"
                        + "We found your account details associated with this email:\n"
                        + "- Account Number: " + details.accountNo() + "\n\n"
                        + "Note: For security reasons, we cannot email your password. "
                        + "Please use the 'Change Password' feature if you've forgotten it.\n\n"
                        + "Best regards,\nAceBank Team";

                MailUtil.sendMail(email, subject, msg);
                return true;
            }
        } catch (Exception e) {
            log.severe("Failed recovery for: " + email + " Error: " + e.getMessage());
        }
        return false;
    }


    @Override
    public boolean applyForLoan(String firstName, String email, String loanType) {
        String subject = "Loan Application Received - AceBank";
        String body = String.format(
                """
                        Dear %s,
                        
                        Thank you for applying for a %s loan with AceBank.
                        We have received your request and our team will review it shortly.
                        
                        We will be in touch with you as soon as a decision is made.
                        
                        Sincerely,
                        The AceBank Team""",
                firstName, loanType
        );

        try {
            MailUtil.sendMail(email, subject, body);
            return true;
        } catch (Exception e) {
            log.severe("Failed to send loan confirmation email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendResetLink(String email) {
        try {
            Optional<AccountRecoveryDTO> userOpt = userDao.getRecoveryDetails(email);

            if (userOpt.isEmpty()) {
                return false;
            }

            // 1️⃣ Generate unique token
            String token = java.util.UUID.randomUUID().toString();

            // 2️⃣ Save token in DB
            userDao.saveResetToken(email, token);

            // 3️⃣ Create reset link
            String resetLink = "http://localhost:8080/ace-bank-lite/reset-password?token=" + token;

            // 4️⃣ Send email
            MailUtil.sendMail(email,
                    "Password Reset - AceBank",
                    "Click the link below to reset your password:\n\n" + resetLink +
                            "\n\nThis link will expire in 15 minutes.");

            return true;

        } catch (Exception e) {
            log.severe("Error sending reset link: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        try {
            // 🔐 HASH PASSWORD FIRST
            String hashedPassword = PasswordUtil.hashPassword(newPassword);

            // Save hashed password in DB
            return userDao.updatePasswordByToken(token, hashedPassword);

        } catch (Exception e) {
            log.severe("Reset password failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean applyLoan(int accountNo, String loanType) {

        try {
            // 1️⃣ DB me save
            boolean saved = userDao.saveLoanRequest(accountNo, loanType);

            if (!saved) return false;

            // 2️⃣ user email nikalo
            String email = userDao.getUserDetails(accountNo).email();
            String name = userDao.getUserDetails(accountNo).firstName();

            // 3️⃣ mail content
            String subject = "Loan Request Received - AceBank";
            String msg = """
                Dear %s,
                
                Your %s loan request has been received successfully.
                Our team will contact you shortly.
                
                Thank you for choosing AceBank.
                """.formatted(name, loanType);

            // 4️⃣ send mail
            MailUtil.sendMailAsync(email, subject, msg);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean applyLoan(int accNo, String name, String email,
                             String loanType, int age, double amount) throws Exception {

        // ✅ 1. Pre-approved limit check
        double preApprovedLimit = 500000;

        if(amount > preApprovedLimit){
            return false;
        }

        // ✅ 2. Save in DB
        boolean saved = userDao.saveLoanRequest(accNo, name, loanType, age, amount);

        if(!saved) return false;

        // ✅ 3. Send Professional Email
        String subject = "Loan Application Request - AceBank";

        String message = "Dear " + name + ",\n\n"
                + "Greetings from AceBank!\n\n"
                + "We have received your loan application request. Below are the details:\n\n"
                + "--------------------------------------\n"
                + "Customer Name : " + name + "\n"
                + "Account Number: " + accNo + "\n"
                + "Loan Type     : " + loanType + "\n"
                + "Age           : " + age + " years\n"
                + "Pre-Approved Loan Limit : ₹5,00,000\n"
                + "Requested Loan Amount   : ₹" + amount + "\n"
                + "--------------------------------------\n\n"
                + "Our loan department will review your request within 24-48 hours.\n\n"
                + "Thank you for banking with AceBank.\n\n"
                + "Regards,\n"
                + "Loan Department\n"
                + "AceBank Pvt Ltd";

        MailUtil.sendMail(email, subject, message);

        return true;
    }




    @Override
    public boolean verifyOtpAndReset(String email, String otp, String newPassword){

        try{
            Optional<String> storedOtp = userDao.getOtpByEmail(email);

            if(storedOtp.isEmpty()){
                return false;
            }

            if(!storedOtp.get().equals(otp)){
                userDao.incrementOtpAttempts(email);
                return false;
            }

            // 🔐 HASH PASSWORD (MOST IMPORTANT)
            String hash = PasswordUtil.hashPassword(newPassword);

            // update hashed password
            userDao.updatePasswordByEmail(email, hash);

            // reset otp
            userDao.resetOtp(email);

            System.out.println("PASSWORD RESET SUCCESS");

            return true;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean sendOtp(String email) {

        try{
            Optional<AccountRecoveryDTO> userOpt = userDao.getRecoveryDetails(email);

            if(userOpt.isEmpty()){
                return false;
            }

            // 6 digit OTP
            String otp = String.valueOf((int)(Math.random()*900000)+100000);

            // save OTP DB me
            userDao.saveOtp(email, otp);

            // mail send
            MailUtil.sendMail(
                    email,
                    "AceBank OTP Verification",
                    "Your OTP is: " + otp + "\nValid for 5 minutes."
            );

            return true;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

}