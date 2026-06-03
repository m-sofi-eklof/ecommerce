package se.sofiekl.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.sofiekl.productservice.client.FakeStoreClient;
import se.sofiekl.productservice.dto.ProductDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FakeStoreClient fakeStoreClient;

    public List<ProductDTO> getAllProducts() {
        return fakeStoreClient.getAllProducts();
    }

    public ProductDTO getProductById(Integer id) {
        return fakeStoreClient.getProductById(id);
    }

    public List<String> getCategories() {
        return fakeStoreClient.getCategories();
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        return fakeStoreClient.getProductsByCategory(category);
    }
}