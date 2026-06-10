import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.CompletableFuture;

public class TestEndpoint {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest optionsRequest = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8082/api/v1/suppliers/my-orders"))
            .header("Origin", "http://localhost:4200")
            .header("Access-Control-Request-Method", "GET")
            .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
            .build();
            
        System.out.println("Sending OPTIONS request...");
        HttpResponse<String> res = client.send(optionsRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("OPTIONS Status: " + res.statusCode());
        System.out.println("OPTIONS Headers: " + res.headers().map());
    }
}
