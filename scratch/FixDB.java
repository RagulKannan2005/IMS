import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FixDB {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/IMS", "root", "1234");
            Statement stmt = conn.createStatement();
            
            // Re-assign Supplier 1 back to User 4 (supplier@ims.com)
            int rows = stmt.executeUpdate("UPDATE supplier SET user_id = 4 WHERE id = 1");
            System.out.println("Re-assigned " + rows + " supplier record(s) to user 4.");
            
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
