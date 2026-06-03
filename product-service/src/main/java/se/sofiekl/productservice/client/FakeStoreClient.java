package se.sofiekl.productservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import se.sofiekl.productservice.dto.ProductDTO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FakeStoreClient {

    private final RestClient restClient;

    @Value("${fakestore.api.url}")
    private String fakeStoreUrl;

    public List<ProductDTO> getAllProducts() {
        return restClient.get()
                .uri(fakeStoreUrl + "/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductDTO>>() {});
    }

    public ProductDTO getProductById(Integer id) {
        return restClient.get()
                .uri(fakeStoreUrl + "/products/" + id)
                .retrieve()
                .body(ProductDTO.class);
    }

    public List<String> getCategories() {
        return restClient.get()
                .uri(fakeStoreUrl + "/products/categories")
                .retrieve()
                .body(new ParameterizedTypeReference<List<String>>() {});
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        return restClient.get()
                .uri(fakeStoreUrl + "/products/category/" + category)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductDTO>>() {});
    }
}