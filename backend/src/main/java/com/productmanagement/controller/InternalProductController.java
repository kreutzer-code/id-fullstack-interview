package com.productmanagement.controller;

import com.productmanagement.domain.InternalProduct;
import com.productmanagement.service.InternalProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class InternalProductController {
    
    private final InternalProductService productService;
    
    @GetMapping
    public ResponseEntity<List<InternalProduct>> getAllProducts() {
        log.info("GET /api/products - Getting all products");
        List<InternalProduct> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InternalProduct> getProduct(@PathVariable String id) {
        log.info("GET /api/products/{} - Getting product by ID", id);
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }
}