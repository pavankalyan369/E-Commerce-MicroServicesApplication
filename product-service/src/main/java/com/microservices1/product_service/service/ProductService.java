package com.microservices1.product_service.service;

import com.microservices1.product_service.dto.ProductDto;
import com.microservices1.product_service.entity.Product;
import com.microservices1.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public void addProduct(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setType(productDto.getType());
        product.setPrice(productDto.getPrice());
        product.setWeight(productDto.getWeight());
        productRepo.save(product);

    }
    public ProductDto getProductById(Long id){
        Optional<Product> p = productRepo.findById(id);
        Product product = p.get();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setType(product.getType());
        productDto.setWeight(product.getWeight());
        return productDto;

    }
    public List<Product> getAllProducts(){
        return productRepo.findAll();

    }

}
