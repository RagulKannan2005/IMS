import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckPOs {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/IMS";
        String user = "root";
        String password = "password"; 

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("\n--- Purchase Orders ---");
            ResultSet rs = stmt.executeQuery("SELECT id, po_number, supplier_id, created_by, total_amount, status FROM purchase_orders");
            while (rs.next()) {
                System.out.println("PO ID: " + rs.getLong("id") + 
                                   ", PO Number: " + rs.getString("po_number") +
                                   ", Supplier ID: " + rs.getLong("supplier_id") +
                                   ", User ID: " + rs.getLong("created_by") +
                                   ", Amount: " + rs.getBigDecimal("total_amount") +
                                   ", Status: " + rs.getString("status"));
            }

            System.out.println("\n--- Suppliers ---");
            ResultSet rs2 = stmt.executeQuery("SELECT id, supplier_email, user_id FROM supplier");
            while (rs2.next()) {
                System.out.println("Supplier ID: " + rs2.getLong("id") + 
                                   ", Email: " + rs2.getString("supplier_email") +
                                   ", User ID: " + rs2.getLong("user_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
