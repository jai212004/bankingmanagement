import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.*;

public class Bank {
    private ArrayList<Branch> Branches;
    private static ArrayList<Customer> Customers;

    public static boolean findCustomer(String name) {
        int count = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareCall("Select * from Customers where name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = count + 1;
            }
        } catch (SQLException e) {

        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Bank() {
        Branches = new ArrayList<>();
        this.Customers = new ArrayList<>();
    }

    public static void main(String[] args) {

    }


    public static void removeCust(String name) {
        String email = null;
        if (findCustomer(name)) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("insert into past_customers (removed_on , name , gender , date_of_birth , account_type , acc_no , nominee , branch , email_id , phone_number) select current_date , name , gender , dob , acc_type , acc_no , nominee , branch , email_id , phone_number from customers where name = ?")
            ) {
                ps.setString(1, name);
                int rows = ps.executeUpdate();
                System.out.println("removed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            email = Bank.getEmail(name);
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("Delete from customers where name = ?")
            ) {
                ps.setString(1, name);
                int rows = ps.executeUpdate();
                System.out.println("removed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             Statement statement = con.createStatement();
        ){
            statement.executeUpdate("drop table " + name);
        }catch (SQLException e){
            e.printStackTrace();
        }

        String Subject = "Account update";
        String text = "Your Account has been closed with us , it was great having you , if this info is incorrect , contact branch";
        Bank.createMessage(email , Subject , text);
    }

    public static boolean findBranch(String name) {
        int count = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareCall("Select * from branches where name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = count + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void removeBranch(String name) {
        if (findBranch(name)) {

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("insert into past_branches (removed_on , name , state , city , pincode , branchid) select current_date , name , state , city , pincode , branchid from branches where name = ?")) {
                ps.setString(1, name);
                int res = ps.executeUpdate();
            } catch (SQLException e) {

            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("Delete from branches where name = ?")
            ) {
                ps.setString(1, name);
                int rows = ps.executeUpdate();
                System.out.println("branch " + name + " removed");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("Delete from Customers where branch = ?")
            ) {
                ps.setString(1, name);
                int rows = ps.executeUpdate();
                System.out.println("customers from " + name + " branch also removed");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void getcustbybranch(String branchname) {
        if (findBranch(branchname)) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("Select * from customers where branch = ?")
            ) {
                ps.setString(1, branchname);
                ResultSet res = ps.executeQuery();
                while (res.next()) {
                    String name = res.getString("name");
                    String Gender = res.getString("Gender");
                    String date = res.getString("Dob");
                    String Accounttype = res.getString("acc_no");
                    Long accno = res.getLong("Acc_no");
                    String nominee = res.getString("nominee");
                    String branch = res.getString("branch");
                    String email = res.getString(8);
                    String number = res.getString(9);
                    System.out.println(
                            "Name = " + name +
                                    ", Gender = " + Gender +
                                    ", Date_Of_Birth = " + date +
                                    ", Account_Type = " + Accounttype +
                                    ", Account_Number = " + accno +
                                    ", Nominee = " + nominee +
                                    ", Branch = " + branch +
                                    ", Email = " + email +
                                    ", Phone = " + number
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getbranches() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("Select * from branches")
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String State = rs.getString("state");
                String City = rs.getString("city");
                long Pincode = rs.getLong("pincode");
                String Branch_Id = rs.getString("branchid");
                System.out.println(
                        "name = " + name +
                                ", State = " + State +
                                ", City = " + City +
                                ", Pincode = " + Pincode +
                                ", Branch_Id = " + Branch_Id);
            }
        } catch (SQLException e) {

        }
    }

    public static void getpastbranches() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("Select * from past_branches")
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("removed_on");
                String name = rs.getString("name");
                String State = rs.getString("state");
                String City = rs.getString("city");
                long Pincode = rs.getLong("pincode");
                String Branch_Id = rs.getString("branchid");
                System.out.println(
                        "removed on = " + date +
                                ", name = " + name +
                                ", State = " + State +
                                ", City = " + City +
                                ", Pincode = " + Pincode +
                                ", Branch_Id = " + Branch_Id);
            }
        } catch (SQLException e) {

        }
    }

    public static void getcustdetails(String iname) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("Select * from customers where name = ?")
        ) {
            ps.setString(1, iname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String date = rs.getString("DOB");
                String Acc_type = rs.getString("acc_type");
                Long Acc_no = rs.getLong("acc_no");
                String nominee = rs.getNString("nominee");
                String branch = rs.getNString("branch");
                String email = rs.getString("email_id");
                Long phone = rs.getLong("phone_number");
                System.out.println(
                        "Name = " + name +
                                ", Gender = " + gender +
                                ", Date_Of_Birth = " + date +
                                ", Account_Type = " + Acc_type +
                                ", Account_Number = " + Acc_no +
                                ", Nominee = " + nominee +
                                ", branch = " + branch +
                                ", Email = " + email +
                                ", phone = " + phone);
            }
        } catch (SQLException e) {

        }
    }

    public static void getbranchdetails(String iname) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("Select * from branches where name = ?")
        ) {
            ps.setString(1, iname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String state = rs.getString("state");
                String city = rs.getString("city");
                Long pincode = rs.getLong("pincode");
                String branchid = rs.getString(5);
                System.out.println(
                        "Name = " + name +
                                ", State = " + state +
                                ", City = " + city +
                                ", Pincode = " + pincode +
                                ", BranchId = " + branchid);
            }
        } catch (SQLException e) {

        }
    }

    public static void addTrans(String customername, int amount, String type) {
        String email = null;
        int balance = 0;
        UUID uid = UUID.randomUUID();
        String id = uid.toString();
        String fin = id.substring(0, 4);
        if (findCustomer(customername)) {
            if (type.toUpperCase().equals("CREDIT")) {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                     PreparedStatement ps = con.prepareStatement("select balance from " + customername + " order by transactiontime desc limit 1;")
                ) {
                    ResultSet rs = ps.executeQuery();
                    int bal = 0;
                    while (rs.next()) {
                        bal = rs.getInt("balance");
                    }
                    System.out.println(bal);
                    if (amount > 0) {
                        try (Connection cond = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                             PreparedStatement psd = cond.prepareStatement("insert into " + customername + " (TransactionType , TransactionAmount , TransactionDate , TransactionTime , TransactionId , balance) values (? , ? , Current_Date , Current_Time , ? , ?)")
                        ) {
                            psd.setString(1, type);
                            psd.setInt(2, amount);
                            psd.setString(3, fin);
                            psd.setInt(4, bal + amount);
                            psd.executeUpdate();
                            System.out.println("done");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        email = Bank.getEmail(customername);
                        balance = Bank.getBalance(customername);
                        String Subject = "Account Update";
                        String Text = "your account has been " + type +"ed with " + amount + " and your current balance is " + balance;
                        Bank.createMessage(email , Subject , Text);
                    } else {
                        System.out.println("invalid ammount");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (type.toUpperCase().equals("DEBIT")) {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                     PreparedStatement ps = con.prepareStatement("select balance from " + customername + " order by transactiontime desc limit 1;")
                ) {
                    ResultSet rs = ps.executeQuery();
                    int bal = 0;
                    while (rs.next()) {
                        bal = rs.getInt("balance");
                    }
                    System.out.println(bal);
                    if (bal > amount) {
                        try (Connection cond = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                             PreparedStatement psd = cond.prepareStatement("insert into " + customername + " (TransactionType , TransactionAmount , TransactionDate , TransactionTime , TransactionId , balance) values (? , ? , Current_Date , Current_Time , ? , ?)")
                        ) {
                            psd.setString(1, type);
                            psd.setInt(2, amount);
                            psd.setString(3, fin);
                            psd.setInt(4, bal - amount);
                            psd.executeUpdate();
                            System.out.println("done");
                            email = Bank.getEmail(customername);
                            balance = Bank.getBalance(customername);
                            String Subject = "Account Update";
                            String Text = "your account has been debited with " + amount + " and your current balance is " + balance;
                            Bank.createMessage(email , Subject , Text);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("insufficient balance");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getBal(String name) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("select balance from " + name + " order by transactiontime desc limit 1")
        ) {
            ResultSet rs = ps.executeQuery();
            int bal = 0;
            while (rs.next()) {
                bal = rs.getInt("balance");
            }
            System.out.println(bal);
        } catch (SQLException e) {

        }
    }

    public static void getLasttrans(String name , int nooftrans){
        if(findCustomer(name)){
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
                 PreparedStatement ps = con.prepareStatement("select * from " + name + " order by transactiontime desc limit ?")
            ){
                ps.setInt(1 , nooftrans);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String type = rs.getString(1);
                    int amount = rs.getInt(2);
                    String date = rs.getString(3);
                    String time = rs.getString(4);
                    String id = rs.getString(5);
                    int bal = rs.getInt(6);
                    System.out.println("TransactionType -> " + type + " , TransactionAmount -> " + amount + " , TransactionDate -> " + date + " , TransactionTime -> " + time + " , TransactionId -> " + id + " , Balance -> " + bal);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
    public static void createMessage(String To , String subject , String text){
        Properties properties = new Properties();
        properties.put("mail.smtp.host" , "smtp.gmail.com");
        properties.put("mail.smtp.port" , 465);
        properties.put("mail.smtp.auth" , "true");
        properties.put("mail.smtp.ssl.enable" , "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jaianmol84@gmail.com" , "rtmj hcyo woyq jtcn");
            }
        });

        try{
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO , new InternetAddress(To));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public static String getEmail(String name){
        String email = null;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("select email_id from customers where name = ?")
        ){
            ps.setString(1 , name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                email = rs.getString(1);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return email;
    }
    public static int getBalance(String name){
        int balance = 0;
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt", "root", "12345");
             PreparedStatement ps = con.prepareStatement("select balance from " + name + " order by transactiontime desc limit 1")
        ){
            //ps.setString(1 , customername);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                balance  = rs.getInt(1);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }
}


//    public static Customer findCustomers(String customer){
//        for (int i = 0 ; i < Customers.size() ; i++){
//            if (Customers.get(i).getName().equals(customer)){
//                return Customers.get(i);
//            }
//        }
//        return null;
//    }
//
//    public static boolean addCustomer(Customer customer){
//        if (findCustomers(customer.getName()) != null){
//            return false;
//        }else{
//            Customers.add(customer);
//            return true;
//        }
//    }
//
//    public static boolean removeCustomer(Customer customer){
//        if (findCustomers(customer.getName()) == null){
//            Customers.remove(customer);
//            return true;
//        }else{
//            Customers.add(customer);
//            return false;
//        }
//    }
//
//    public void getlist(){
//        for (int i = 0 ; i < Customers.size() ; i++){
//            System.out.println("name -> " + Customers.get(i).getName() + " Gender -> " + Customers.get(i).getGender() + " dob -> " + Customers.get(i).getDate_Of_Birth() + " AccountType -> " + Customers.get(i).getAccount_Type() + " AccountNo -> " + Customers.get(i).getAccount_Number() + " Nominee -> " + Customers.get(i).getNominee() + " Branch -> " + Customers.get(i).getBranch());
//        }
//    }
