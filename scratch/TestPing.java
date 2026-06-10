import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TestPing {
    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8082/api/v1/suppliers/my-orders"))
                .GET()
                .build();
                
            System.out.println("Sending request...");
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
