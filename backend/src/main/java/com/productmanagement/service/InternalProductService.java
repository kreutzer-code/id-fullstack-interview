package com.productmanagement.service;

import com.productmanagement.domain.InternalProduct;
import com.productmanagement.repository.InternalProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InternalProductService {
    
    private final InternalProductRepository productRepository;
    
    public List<InternalProduct> findAll() {
        log.debug("Finding all products");
        return productRepository.findAll();
    }
    
    public Optional<InternalProduct> findById(String internalId) {
        log.debug("Finding product by ID: {}", internalId);
        return productRepository.findById(internalId);
    }


    public InternalProduct save(InternalProduct product) {
        log.debug("Saving product: {}", product.getInternalId());
        return productRepository.save(product);
    }

    public Optional<InternalProduct> findByGlobalTradeIdentifier(String gtin) {
        log.debug("Finding product by GTIN: {}", gtin);
        return productRepository.findByGlobalTradeIdentifier(gtin);
    }

}