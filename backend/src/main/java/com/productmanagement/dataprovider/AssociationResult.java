package com.productmanagement.dataprovider;

import com.productmanagement.domain.InternalProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Result of an association attempt between a DataProviderProduct and an InternalProduct.
 * Contains the matched InternalProduct (if found) and the name of the strategy that found the match.
 */
@Getter
@AllArgsConstructor
public class AssociationResult {
    

    private final Optional<InternalProduct> internalProduct;
    

    private final String strategyName;
    

    public boolean hasMatch() {
        return internalProduct.isPresent();
    }
}

