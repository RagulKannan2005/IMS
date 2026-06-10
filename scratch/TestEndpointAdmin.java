import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestEndpointAdmin {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        // Login as admin
        String loginJson = "{\"email\":\"manager@ims.com\", \"password\":\"Manager@123\"}";
        HttpRequest loginRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/api/v1/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(loginJson))
            .build();
            
        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        
        String token = "";
        Matcher m = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(loginResponse.body());
        if (m.find()) {
            token = m.group(1);
        } else {
            System.out.println("Login failed: " + loginResponse.body());
            return;
        }
        
        // Fetch ALL orders
        HttpRequest ordersRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/api/v1/purchase-orders/findallpurchaseorders"))
            .header("Authorization", "Bearer " + token)
            .POST(HttpRequest.BodyPublishers.noBody()) // findallpurchaseorders is a POST endpoint per the controller
            .build();
            
        HttpResponse<String> ordersResponse = client.send(ordersRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + ordersResponse.statusCode());
        System.out.println("Response: " + ordersResponse.body());
    }
}
