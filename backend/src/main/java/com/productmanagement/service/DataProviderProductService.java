package com.productmanagement.service;

import com.productmanagement.domain.DataProviderProduct;
import com.productmanagement.repository.DataProviderProductRepository;
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
public class DataProviderProductService {

    private final DataProviderProductRepository dataProviderProductRepository;

    public List<DataProviderProduct> findAll() {
        log.debug("Finding all DataProviderProducts");
        return dataProviderProductRepository.findAll();
    }

    public Optional<DataProviderProduct> findByDataProviderIdAndExternalId(String dataProviderId, String externalId) {
        log.debug("Finding DataProviderProduct by providerId: {} and externalId: {}", dataProviderId, externalId);
        return dataProviderProductRepository.findByDataProviderIdAndExternalId(dataProviderId, externalId);
    }

    public DataProviderProduct save(DataProviderProduct product) {
        log.debug("Saving DataProviderProduct: {}", product.getExternalId());
        return dataProviderProductRepository.save(product);
    }

}

