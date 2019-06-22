package apartmentmanagementsystem.nilaram.ams;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

class ConnectionClass {

    private Connection con;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

    @SuppressLint("NewApi")
    private Connection ConnectDataBase() {

        String driverClass = "com.mysql.jdbc.Driver";

        String ip = "192.168.1.4";
        String db = "ams2";
        String un = "root";
        String password = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL;

        try {
            Class.forName(driverClass);
            ConnURL = "jdbc:mysql://" + ip + "/" + db;
            conn = DriverManager.getConnection(ConnURL, un, password);
        } catch (SQLException se) {
            Log.e("ERROR: ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR: ", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR: ", e.getMessage());
        }

        return conn;
    }

    boolean CheckLogin(Context context, String mailID, String userPassword) {
        boolean loginSuccess = false;
        String hashPassword;

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select PassWord " +
                        "from user " +
                        "where Email_Id= '" + mailID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    hashPassword = rs.getString("PassWord");
                    loginSuccess = BCrypt.checkpw(userPassword, hashPassword);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return loginSuccess;
    }

    void UpdateToken(Context context, String userID, String token){
        boolean updateSuccess;
        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "insert into device_tokens (Tokens, User_Id) " +
                        "values (?, ?)";

                PreparedStatement preparedStmt = con.prepareStatement(query);

                preparedStmt.setString(1, token);
                preparedStmt.setString(2, userID);

                int count = preparedStmt.executeUpdate();
                updateSuccess = count > 0;

                if (updateSuccess)
                    Toast.makeText(context, "Token submitted successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void RemoveToken(Context context, String token){
        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "delete " +
                        "from device_tokens " +
                        "where Tokens = '" + token + "'";

                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    List<String> GetUserType(Context context, String mailID) {
        List<String> userInfo = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select User_Id, Type_Id " +
                        "from user " +
                        "where Email_Id= '" + mailID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    userInfo.add(rs.getString("User_Id"));
                    userInfo.add(rs.getString("Type_Id"));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return userInfo;
    }

    List<String> ViewProfile(Context context, String userID, String userType) {
        List<String> profile = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                if (userType.equals("TY002")) {
                    String query = "select U.First_Name, U.Last_Name, U.Address, U.Email_Id, U.Contact_Number, H.House_Name " +
                            "from user U " +
                            "left join house H on U.User_Id = H.Owner_Id " +
                            "where U.User_Id = '" + userID + "'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        profile.add(rs.getString("First_Name"));
                        profile.add(rs.getString("Last_Name"));
                        profile.add(rs.getString("Address"));
                        profile.add(rs.getString("House_Name"));
                        profile.add(rs.getString("Email_Id"));
                        profile.add(rs.getString("Contact_Number"));
                    }
                } else if (userType.equals("TY003")) {
                    String query = "select U.First_Name, U.Last_Name, U.Address, U.Email_Id, U.Contact_Number, J.Job, S.Amount " +
                            "from user U " +
                            "left join (SELECT EJ.Emp_Id, ET.Job " +
                            "FROM employee_job EJ " +
                            "LEFT JOIN employee_type ET ON EJ.Emtype_Id = ET.Emtype_Id) J ON U.User_Id = J.Emp_Id " +
                            "left join salary S ON U.User_Id = S.Emp_Id " +
                            "where U.User_Id = '" + userID + "'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    if (rs.next()) {
                        profile.add(rs.getString("First_Name"));
                        profile.add(rs.getString("Last_Name"));
                        profile.add(rs.getString("Address"));
                        profile.add(rs.getString("Job"));
                        profile.add(String.valueOf(rs.getFloat("Amount")));
                        profile.add(rs.getString("Email_Id"));
                        profile.add(rs.getString("Contact_Number"));
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return profile;
    }

    boolean UpdateProfile(Context context, String userID, List<String> newProfile) {
        boolean updateSuccess = false;
        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "update user " +
                        "set First_Name=?, Last_Name=?, Address=?, Email_Id=?, Contact_Number=? " +
                        "where User_Id= '" + userID + "'";

                try {
                    PreparedStatement preparedStmt = con.prepareStatement(query);

                    preparedStmt.setString(1, newProfile.get(0));
                    preparedStmt.setString(2, newProfile.get(1));
                    preparedStmt.setString(3, newProfile.get(2));
                    preparedStmt.setString(4, newProfile.get(3));
                    preparedStmt.setInt(5, Integer.valueOf(newProfile.get(4)));

                    int count = preparedStmt.executeUpdate();
                    updateSuccess = count > 0;
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (updateSuccess)
                    Toast.makeText(context, "Data updated successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return updateSuccess;
    }

    HashMap<String, String> GetNotifications(Context context, String userType) {
        HashMap<String, String> notifications = new LinkedHashMap<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "";

                if(userType.equals("TY002"))
                    query = "select Notification_Id, Subject " +
                            "from noticeboard " +
                            "where Notice_Type = 'Common' or Notice_Type = 'ForOwners' " +
                            "order by Notification_ID desc";
                else if(userType.equals("TY003"))
                    query = "select Notification_Id, Subject " +
                            "from noticeboard " +
                            "where Notice_Type = 'Common' or Notice_Type = 'ForEmployees' " +
                            "order by Notification_ID desc";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String notificationID = rs.getString("Notification_Id");
                    String subject = rs.getString("Subject");
                    notifications.put(notificationID, subject);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return notifications;
    }

    List<String> ViewNotifications(Context context, String notificationID) {
        List<String> notification = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select Subject, Content, Date " +
                        "from noticeboard " +
                        "where Notification_Id = '" + notificationID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    notification.add(rs.getString("Subject"));
                    notification.add(rs.getString("Content"));
                    notification.add(dateFormat.format(rs.getDate("Date")));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return notification;
    }

    HashMap<String, String> GetComplaints(Context context, String userID) {
        HashMap<String, String> complaints = new LinkedHashMap<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select Complaint_ID, Subject " +
                        "from complaint " +
                        "where User_Id= '" + userID + "' " +
                        "order by Complaint_ID desc";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String complaintID = rs.getString("Complaint_ID");
                    String subject = rs.getString("Subject");
                    complaints.put(complaintID, subject);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return complaints;
    }

    List<String> ViewComplaints(Context context, String complaintID) {
        List<String> complaint = new ArrayList<>();
        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * " +
                        "from complaint " +
                        "where Complaint_ID= '" + complaintID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    complaint.add(rs.getString("Subject"));
                    complaint.add(rs.getString("Description"));
                    complaint.add(dateFormat.format(rs.getDate("Date")));
                    complaint.add(rs.getString("Remark"));
                    complaint.add(rs.getString("Response"));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return complaint;
    }

    boolean NewComplaint(Context context, String[] newComplaint) {
        boolean updateSuccess = false;
        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);

                String query = "insert into complaint (Subject, Description, User_ID, Date, Remark, Response) " +
                        "values (?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = con.prepareStatement(query);

                preparedStmt.setString(1, newComplaint[0]);
                preparedStmt.setString(2, newComplaint[1]);
                preparedStmt.setString(3, newComplaint[2]);
                preparedStmt.setDate(4, date);
                preparedStmt.setString(5, "Not solve");
                preparedStmt.setString(6, "No response yet.");

                int count = preparedStmt.executeUpdate();
                updateSuccess = count > 0;

                if (updateSuccess)
                    Toast.makeText(context, "Complaint submitted successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return updateSuccess;
    }

    HashMap<String, String> GetEvents(Context context) {
        HashMap<String, String> events = new LinkedHashMap<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select Event_ID, Title " +
                        "from events " +
                        "order by Event_ID desc";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String eventID = String.valueOf(rs.getInt("Event_ID"));
                    String subject = rs.getString("Title");
                    events.put(eventID, subject);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return events;
    }

    List<String> ViewEvents(Context context, int eventID) {
        List<String> event = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * " +
                        "from events " +
                        "where Event_ID = '" + eventID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    event.add(rs.getString("Title"));
                    event.add(rs.getString("Content"));
                    event.add(dateFormat.format(rs.getDate("Date")));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return event;
    }

    List<HashMap<String, String>> GetEmployees(Context context) {
        List<HashMap<String, String>> employees = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select U.First_Name, U.Last_Name, U.Address, J.Job, U.Email_Id, U.Contact_Number, S.Amount " +
                        "from user U " +
                        "left join (SELECT EJ.Emp_Id, ET.Job " +
                        "FROM employee_job EJ " +
                        "LEFT JOIN employee_type ET ON EJ.Emtype_Id = ET.Emtype_Id) J ON U.User_Id = J.Emp_Id " +
                        "left join salary S ON U.User_Id = S.Emp_Id " +
                        "where U.Type_Id = 'TY003'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String fName = rs.getString("First_Name");
                    String lName = rs.getString("Last_Name");
                    String address = rs.getString("Address");
                    String job = rs.getString("Job");
                    int contact = rs.getInt("Contact_Number");
                    float salary = rs.getFloat("Amount");

                    HashMap<String, String> employee = new HashMap<>();
                    employee.put("fName", fName);
                    employee.put("lName", lName);
                    employee.put("address", address);
                    employee.put("job", job);
                    employee.put("contact", String.valueOf(contact));
                    employee.put("salary", "Salary: " + String.valueOf(salary));

                    employees.add(employee);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return employees;
    }

    List<HashMap<String, String>> GetHouseOwners(Context context, String userID) {
        List<HashMap<String, String>> houseOwners = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select U.First_Name, U.Last_Name, U.Address, U.Email_Id, U.Contact_Number, H.House_Name " +
                        "from user U " +
                        "left join house H on U.User_Id = H.Owner_Id " +
                        "where U.Type_Id = 'TY002' and U.User_Id != '" + userID + "'";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String fName = rs.getString("First_Name");
                    String lName = rs.getString("Last_Name");
                    String address = rs.getString("Address");
                    int contact = rs.getInt("Contact_Number");
                    String houseId = rs.getString("House_Name");

                    HashMap<String, String> houseOwner = new HashMap<>();
                    houseOwner.put("fName", fName);
                    houseOwner.put("lName", lName);
                    houseOwner.put("address", address);
                    houseOwner.put("contact", String.valueOf(contact));
                    houseOwner.put("houseId", "House: " + houseId);

                    houseOwners.add(houseOwner);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return houseOwners;
    }

    List<HashMap<String, String>> GetMaintenance(Context context, String userID) {
        List<HashMap<String, String>> maintenanceList = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * " +
                        "from owner_utility " +
                        "where Owner_Id = '" + userID + "' " +
                        "order by Cost_Id desc";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String month = rs.getString("Month");
                    String year = rs.getString("Year");
                    String gasAmount = String.valueOf(rs.getFloat("Gas_Amount"));
                    String waterAmount = String.valueOf(rs.getFloat("Water_Amount"));
                    String electAmount = String.valueOf(rs.getFloat("Electricity_Amount"));
                    String securityAmount = String.valueOf(rs.getFloat("Security_Charge"));
                    String otherExpensive = String.valueOf(rs.getFloat("Other_Expensive"));
                    String totalAmount = String.valueOf(rs.getFloat("Total_Amount"));

                    HashMap<String, String> maintenance = new HashMap<>();
                    maintenance.put("month", month);
                    maintenance.put("year", year);
                    maintenance.put("gasAmount", gasAmount);
                    maintenance.put("waterAmount", waterAmount);
                    maintenance.put("electAmount", electAmount);
                    maintenance.put("securityAmount", securityAmount);
                    maintenance.put("otherExpensive", otherExpensive);
                    maintenance.put("totalAmount", totalAmount);

                    maintenanceList.add(maintenance);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return maintenanceList;
    }

    List<HashMap<String, String>> GetVisitors(Context context, String userID) {
        List<HashMap<String, String>> visitors = new ArrayList<>();

        con = ConnectDataBase();

        try {
            if (con == null) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            } else {
                String query = "select * " +
                        "from visitor " +
                        "where User_Id = '" + userID + "' " +
                        "order by Visitor_Id desc";

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String visitorName = rs.getString("Full_Name");
                    String visitorAddress = rs.getString("Address");
                    String visitorContact = String.valueOf(rs.getInt("Mobile_No"));
                    String inDate = rs.getString("InDate");
                    String outDate = rs.getString("OutDate");
                    String inTime = rs.getString("InTime");
                    String outTime = rs.getString("OutTime");

                    HashMap<String, String> visitor = new HashMap<>();
                    visitor.put("visitorName", visitorName);
                    visitor.put("visitorAddress", visitorAddress);
                    visitor.put("visitorContact", visitorContact);
                    visitor.put("in", "In: " + inDate + " " + inTime);
                    visitor.put("out", "Out: " + outDate + " " + outTime);

                    visitors.add(visitor);
                }
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return visitors;
    }
}