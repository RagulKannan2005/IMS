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
            ResultSet rs = stmt.executeQuery("SELECT id, username, email FROM users");
            while (rs.next()) {
                System.out.println("User ID: " + rs.getLong("id") + 
                                   ", Username: " + rs.getString("username") + 
                                   ", Email: " + rs.getString("email"));
            }
            rs.close();
            
            System.out.println("--- SUPPLIERS ---");
            ResultSet rs2 = stmt.executeQuery("SELECT id, supplier_email, supplier_phone, user_id FROM supplier");
            while (rs2.next()) {
                System.out.println("Supplier ID: " + rs2.getLong("id") + 
                                   ", Email: " + rs2.getString("supplier_email") + 
                                   ", Phone: " + rs2.getString("supplier_phone") + 
                                   ", User_ID: " + rs2.getLong("user_id"));
            }
            rs2.close();
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
