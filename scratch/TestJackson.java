import com.fasterxml.jackson.databind.ObjectMapper;
public class TestJackson {
    public static void main(String[] args) throws Exception {
        String json = "{\"sku\":\"123\",\"name\":\"test\",\"costPrice\":10,\"sellingPrice\":20,\"stockQuantity\":5,\"reorderLevel\":1,\"reorderQuantity\":1,\"active_status\":\"Active\",\"category\":\"Cat\"}";
        ObjectMapper mapper = new ObjectMapper();
        com.example.indentory_management_system.dto.ProductRequestdto dto = mapper.readValue(json, com.example.indentory_management_system.dto.ProductRequestdto.class);
        System.out.println("Parsed dto: " + dto);
        System.out.println("Active status is: " + dto.getActive_status());
    }
}
