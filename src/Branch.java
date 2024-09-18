import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Branch{
    private String name;
    private String State;
    private String City;
    private long Pincode;
    private String Branch_Id;

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public long getPincode() {
        return Pincode;
    }

    public void setPincode(long pincode) {
        Pincode = pincode;
    }

    public String getBranch_Id() {
        return Branch_Id;
    }

    public void setBranch_Id(String branch_Id) {
        Branch_Id = branch_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch(String name , String state, String city, long pincode) {
        this.name = name;
        String branchId = getBranchId();
        State = state;
        City = city;
        Pincode = pincode;
        Branch_Id = branchId;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankingmgmt" , "root" , "12345");
            PreparedStatement ps = con.prepareStatement("Insert into Branches (Name , state , city , pincode , branchid) values (? , ? , ? , ? , ?)")
        ) {
            ps.setString(1 , name);
            ps.setString(2 , state);
            ps.setString(3 , city);
            ps.setLong(4 , pincode);
            ps.setString(5 , branchId);
            int res = ps.executeUpdate();
        }catch (SQLException e){

        }
    }

    public String getBranchId(){
        UUID uuid = UUID.randomUUID();
        String idbank = uuid.toString();
        String finid = idbank.substring(0 , 4);
        return finid;
    }





}
