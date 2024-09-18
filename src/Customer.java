import java.sql.*;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Customer {
    private String Name;
    private String Gender;
    private String Date_Of_Birth;
    private String Account_Type;
    private long Account_Number;
    private String Nominee;
    private String branch;
    private int balance;
    private String emailid;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    private String number;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDate_Of_Birth() {
        return Date_Of_Birth;
    }

    public void setDate_Of_Birth(String date_Of_Birth) {
        Date_Of_Birth = date_Of_Birth;
    }

    public String getAccount_Type() {
        return Account_Type;
    }

    public void setAccount_Type(String account_Type) {
        Account_Type = account_Type;
    }

    public long getAccount_Number() {
        return Account_Number;
    }

    public void setAccount_Number(int account_Number) {
        Account_Number = account_Number;
    }

    public String getNominee() {
        return Nominee;
    }

    public void setNominee(String nominee) {
        Nominee = nominee;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "Name='" + Name + '\'' +
                ", Gender='" + Gender + '\'' +
                ", Date_Of_Birth='" + Date_Of_Birth + '\'' +
                ", Account_Type='" + Account_Type + '\'' +
                ", Account_Number=" + Account_Number +
                ", Nominee='" + Nominee + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }

    public Customer(String name, String gender, String date_Of_Birth, String account_Type, String nominee, String branch , String emailid , String number) {
        UUID uid = UUID.randomUUID();
        String id = uid.toString();
        String fin = id.substring(0 , 4);
        Random rand = new Random();
        long lowerBound = 10000L;
        long upperBound = 99999L;
        long ACC = lowerBound + (long) (rand.nextDouble() * (upperBound - lowerBound + 1));
        Name = name;
        Gender = gender;
        Date_Of_Birth = date_Of_Birth;
        Account_Type = account_Type;
        Account_Number = ACC;
        Nominee = nominee;
        this.balance = 1000;
        this.emailid = emailid;
        this.number = number;
        if (Bank.findBranch(branch)) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("insert into Customers (name , gender , dob , acc_type , acc_no , nominee , branch , joined_on , email_id , phone_number) values (? , ? , ? , ? , ? , ? , ? , Current_Date , ? , ?)")
            ) {
                ps.setString(1, name);
                ps.setString(2, gender);
                ps.setString(3, date_Of_Birth);
                ps.setString(4, account_Type);
                ps.setLong(5, ACC);
                ps.setString(6, nominee);
                ps.setString(7, branch);
                ps.setString(8 , emailid);
                ps.setString(9 , number);
                int rows = ps.executeUpdate();
            } catch (SQLException e) {

            }
            String str = "create table " + name + " (TransactionType varchar(23) , TransactionAmount int , TransactionDate date , TransactionTime time , TransactionId varchar(23) , Balance int)";
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 Statement statement = con.createStatement();
            ) {
                statement.executeUpdate(str);
            } catch (SQLException e) {

            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("insert into " + name + "(transactiontype , transactionamount , transactiondate , transactiontime , transactionid , balance) values (? , 1000 , current_date , current_time , ? , 1000)")
            ) {
                ps.setString(1, "credit");
                ps.setString(2, fin);
                int rows = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("branch does not exist , add branch first");
        }
        String subject = "Account creation";
        String text = "Your account is created with us , thanks for joining us in this financial journey your details stored with us are as follows :- "+ " Name -> " + name + ", Gender -> " + gender + ", DOB -> " + date_Of_Birth + ", Acc_No " + Account_Number + ", Acc_Type " + account_Type + ", Nominee -> " + nominee + ", Branch -> " + branch + ", Email-Id" + ", Number -> " + number + "" +
                "please contact Branch if there is an issue with details";
        Bank.createMessage(emailid , subject , text);
    }
}