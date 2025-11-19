package com.productmanagement.dataprovider;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Result of a data provider import operation.
 */
@Data
public class ImportResult {
    
    private int totalProducts;
    private int associatedProducts;
    private int notAssociatedProducts;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> errors = new ArrayList<>();
    
    public void incrementAssociated() {
        this.associatedProducts++;
    }
    
    public void incrementNotAssociated() {
        this.notAssociatedProducts++;
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("ImportResult[total=%d, associated=%d, unassociated=%d, errors=%d]",
                totalProducts, associatedProducts, notAssociatedProducts, errors.size());
    }
}

