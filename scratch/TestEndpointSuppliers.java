import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestEndpointSuppliers {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        // Login as admin
        String loginJson = "{\"email\":\"admin@ims.com\", \"password\":\"Admin@123\"}";
        HttpRequest loginRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/api/v1/auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(loginJson))
            .build();
            
        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        
        // Try manager instead if admin fails
        if (loginResponse.statusCode() != 200) {
            loginJson = "{\"email\":\"manager@ims.com\", \"password\":\"Manager@123\"}";
            loginRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8082/api/v1/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginJson))
                .build();
            loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        }
        
        String token = "";
        Matcher m = Pattern.compile("\"token\":\"([^\"]+)\"").matcher(loginResponse.body());
        if (m.find()) {
            token = m.group(1);
        } else {
            System.out.println("Login failed: " + loginResponse.body());
            return;
        }
        
        // Fetch ALL suppliers
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/api/v1/suppliers/getallsuppliers"))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
            
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }
}
