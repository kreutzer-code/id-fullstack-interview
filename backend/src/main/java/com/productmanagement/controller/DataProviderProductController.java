package com.productmanagement.controller;

import com.productmanagement.domain.DataProviderProduct;
import com.productmanagement.service.DataProviderProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dataprovider-products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class DataProviderProductController {
    
    private final DataProviderProductService dataProviderProductService;
    
    @GetMapping
    public ResponseEntity<List<DataProviderProduct>> getAllDataProviderProducts() {
        log.info("GET /api/dataprovider-products - Getting all data provider products");
        List<DataProviderProduct> products = dataProviderProductService.findAll();
        log.info("Found {} data provider products", products.size());
        return ResponseEntity.ok(products);
    }
}

