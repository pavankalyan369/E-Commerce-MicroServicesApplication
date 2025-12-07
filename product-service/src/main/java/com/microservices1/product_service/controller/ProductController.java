package com.microservices1.product_service.controller;

import com.microservices1.product_service.dto.ProductDto;
import com.microservices1.product_service.entity.Product;
import com.microservices1.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    // ------------ CREATE ------------ //

    @PostMapping("/create")
    public Product createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    // ------------ READ ------------ //

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String q) {
        return productService.searchByName(q);
    }

    @GetMapping("/type/{type}")
    public List<Product> getProductsByType(@PathVariable String type) {
        return productService.getProductsByType(type);
    }

    // ------------ UPDATE (PUT) ------------ //

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return productService.updateProduct(id, productDto);
    }

    // ------------ DELETE ------------ //

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // ------------ STOCK PATCHES ------------ //

    // PATCH stock with delta (e.g. -5 when order is placed)
    @PatchMapping("/{id}/stock")
    public Product patchStockDelta(@PathVariable Long id, @RequestParam Long delta) {
        return productService.updateStockDelta(id, delta);
    }

    // PATCH stock absolute (set to exact value)
    @PatchMapping("/{id}/stock/absolute")
    public Product patchStockAbsolute(@PathVariable Long id, @RequestParam Long stock) {
        return productService.updateStockAbsolute(id, stock);
    }

    // ------------ SPECIFIC FIELD PATCHES ------------ //

    @PatchMapping("/{id}/name")
    public Product patchName(@PathVariable Long id, @RequestParam String name) {
        return productService.patchName(id, name);
    }

    @PatchMapping("/{id}/price")
    public Product patchPrice(@PathVariable Long id, @RequestParam Long price) {
        return productService.patchPrice(id, price);
    }

    @PatchMapping("/{id}/type")
    public Product patchType(@PathVariable Long id, @RequestParam String type) {
        return productService.patchType(id, type);
    }

    @PatchMapping("/{id}/weight")
    public Product patchWeight(@PathVariable Long id, @RequestParam Long weight) {
        return productService.patchWeight(id, weight);
    }
}
