import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckDB {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMS", "root", "1234");
            Statement stmt = conn.createStatement();
            
            System.out.println("--- USERS ---");
            ResultSet rs = stmt.executeQuery("SELECT id, username, email, role, phone_number, created_by_id, created_at, updated_at FROM users");
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id") + 
                                   ", Username: " + rs.getString("username") + 
                                   ", Email: " + rs.getString("email") +
                                   ", Role: " + rs.getString("role") +
                                   ", Phone: " + rs.getString("phone_number") +
                                   ", CreatedBy: " + rs.getObject("created_by_id") +
                                   ", CreatedAt: " + rs.getString("created_at") +
                                   ", UpdatedAt: " + rs.getString("updated_at"));
            }
            rs.close();
            
            System.out.println("--- SUPPLIERS ---");
            ResultSet rs2 = stmt.executeQuery("SELECT id, supplier_name, contact_person, supplier_email, supplier_phone, status, user_id FROM supplier");
            while (rs2.next()) {
                System.out.println("ID: " + rs2.getLong("id") + 
                                   ", Name: " + rs2.getString("supplier_name") +
                                   ", Contact: " + rs2.getString("contact_person") +
                                   ", Email: " + rs2.getString("supplier_email") + 
                                   ", Phone: " + rs2.getString("supplier_phone") + 
                                   ", Status: " + rs2.getBoolean("status") +
                                   ", User_ID: " + rs2.getObject("user_id"));
            }
            rs2.close();
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
