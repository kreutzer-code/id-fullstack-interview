package com.productmanagement.dataprovider;

import com.productmanagement.domain.DataProviderProduct;
import com.productmanagement.domain.InternalProduct;
import com.productmanagement.service.InternalProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Association strategy that matches by Global Trade Identifier (GTIN).
 * Highest priority strategy.
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class GlobalTradeIdAssociationStrategy implements AssociationStrategy {

    private final InternalProductService productService;

    @Override
    public Optional<InternalProduct> findMatch(DataProviderProduct externalProduct) {
        String gtin = externalProduct.getGlobalTradeIdentifier();

        if (gtin == null || gtin.trim().isEmpty()) {
            return Optional.empty();
        }

        log.debug("Trying GTIN match: {}", gtin);
        return productService.findByGlobalTradeIdentifier(gtin);
    }

    @Override
    public String getName() {
        return "GlobalTradeId";
    }
}

