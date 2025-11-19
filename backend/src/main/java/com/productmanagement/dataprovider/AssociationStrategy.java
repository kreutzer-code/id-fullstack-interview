package com.productmanagement.dataprovider;

import com.productmanagement.domain.DataProviderProduct;
import com.productmanagement.domain.InternalProduct;

import java.util.Map;
import java.util.Optional;

/**
 * Strategy for associating external product data with internal products.
 */
public interface AssociationStrategy {
    

    Optional<InternalProduct> findMatch(DataProviderProduct externalProduct);
    

    String getName();
}

