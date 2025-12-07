package com.microservices1.product_service.service;

import com.microservices1.product_service.dto.ProductDto;
import com.microservices1.product_service.entity.Product;
import com.microservices1.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    // ------------ CREATE ------------ //

    public Product createProduct(ProductDto dto) {
        Product product = Product.builder()
                .id(dto.getId())        // manual id if you want
                .name(dto.getName())
                .price(dto.getPrice())
                .type(dto.getType())
                .weight(dto.getWeight())
                .stock(dto.getStock() == null ? 0L : dto.getStock())
                .build();

        return productRepo.save(product);
    }

    // ------------ READ ------------ //

    public ProductDto getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToDto(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public List<Product> searchByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getProductsByType(String type) {
        return productRepo.findByType(type);
    }

    // ------------ UPDATE (PUT) ------------ //

    public Product updateProduct(Long id, ProductDto dto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setType(dto.getType());
        existing.setWeight(dto.getWeight());
        if (dto.getStock() != null) {
            existing.setStock(dto.getStock());
        }

        return productRepo.save(existing);
    }

    // ------------ DELETE ------------ //

    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }

    // ------------ STOCK MANAGEMENT ------------ //

    // delta-based stock update (can be positive or negative)
    public Product updateStockDelta(Long id, Long delta) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        long current = product.getStock() == null ? 0L : product.getStock();
        long newStock = current + delta;

        if (newStock < 0) {
            throw new RuntimeException("Insufficient stock for product id: " + id);
        }

        product.setStock(newStock);
        return productRepo.save(product);
    }

    // absolute stock set
    public Product updateStockAbsolute(Long id, Long stock) {
        if (stock < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setStock(stock);
        return productRepo.save(product);
    }

    // ------------ SPECIFIC PATCH HELPERS ------------ //

    public Product patchName(Long id, String name) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setName(name);
        return productRepo.save(product);
    }

    public Product patchPrice(Long id, Long price) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setPrice(price);
        return productRepo.save(product);
    }

    public Product patchType(Long id, String type) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setType(type);
        return productRepo.save(product);
    }

    public Product patchWeight(Long id, Long weight) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setWeight(weight);
        return productRepo.save(product);
    }

    // ------------ MAPPER ------------ //

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setType(product.getType());
        dto.setWeight(product.getWeight());
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
