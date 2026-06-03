package se.sofiekl.userorderservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import se.sofiekl.userorderservice.DTO.ProductResponse;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {

    private final RestClient restClient;

    @Value("${services.product.url}")
    private String productServiceUrl;

    public ProductResponse getProductById(Integer productId, String authHeader) {
        return restClient.get()
                .uri(productServiceUrl + "/api/products/" + productId)
                .header("Authorization", authHeader)
                .retrieve()
                .body(ProductResponse.class);
    }
}