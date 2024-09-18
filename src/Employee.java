import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Employee {
    private String Name;
    private String Gender;
    private String Date_Of_Birth;
    private String EmpId;
    private String HireDate;
    private String branch;



    public String getName() {
        return Name;
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

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getHireDate() {
        return HireDate;
    }

    public void setHireDate(String hireDate) {
        HireDate = hireDate;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setName(String name) {
        Name = name;
    }

    public String idCreater(){
        UUID uniq = UUID.randomUUID();
        String rand = uniq.toString();
        String fin = rand.substring(0 , 5);
        return fin;
    }
    public Employee(String Name , String Gender , String Date_Of_Birth , String HireDate , String branchname){
        String id = idCreater();
        this.Date_Of_Birth = Date_Of_Birth;
        this.Name = Name;
        this.Gender = Gender;
        this.HireDate = HireDate;
        this.EmpId = id;
        this.branch = branchname;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt" , "root" , "12345");
             PreparedStatement ps = con.prepareStatement("Insert into Employees (Name , Gender , Date_Of_Birth , Empid , HireDate , Branch) values (? , ? , ? , ? , ? , ?)")){
            ps.setString(1 , Name);
            ps.setString(2 , Gender);
            ps.setDate(3 , java.sql.Date.valueOf(Date_Of_Birth));
            ps.setString(4 , id);
            ps.setDate(5 , java.sql.Date.valueOf(HireDate));
            ps.setString(6 , branch);
            int rows = ps.executeUpdate();
            System.out.println("Employee added");
        }catch (java.sql.SQLIntegrityConstraintViolationException e){

        }catch (SQLException e){
            System.out.println("probably syntax issue");
        }
    }
}
//Session session = Session.getInstance(props , Authenticator(){
//protected PasswordAuthentication getPasswordAuthentication(){
//    return new PasswordAuthentication("jaianmol84@gmail.com" , "")
//}
//        })