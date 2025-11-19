package com.productmanagement.domain;

import org.springframework.data.neo4j.core.schema.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a product from an external data provider.
 * This entity stores the original product data from the provider
 * and maintains a relationship to the associated InternalProduct.
 */
@Node("DataProviderProduct")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"dataProviderId", "externalId"})
public class DataProviderProduct {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Identifier of the data provider (e.g., "JsonDataProvider", "BmecatProvider")
     */
    private String dataProviderId;

    /**
     * External ID from the data provider's system
     */
    private String externalId;

    /**
     * Global Trade Identifier (GTIN/EAN) if provided
     */
    private String globalTradeIdentifier;

    /**
     * Timestamp when this product was imported
     */
    private LocalDateTime importedAt;

    /**
     * Timestamp when this product was last updated
     */
    private LocalDateTime lastUpdatedAt;

    /**
     * Strategy used for association (e.g., "GTIN", "INTERNAL_ID", "ATTRIBUTES")
     */
    private String associationStrategy;

    /**
     * Attributes from the data provider
     */
    @Relationship(type = "HAS_DATAPROVIDERATTRIBUTE", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<DataProviderAttribute> attributes = new HashSet<>();

    /**
     * Relationship to the associated InternalProduct
     */
    @Relationship(type = "ASSOCIATED_WITH", direction = Relationship.Direction.OUTGOING)
    private InternalProduct associatedProduct;

    public void addAttribute(DataProviderAttribute attribute) {
        this.attributes.add(attribute);
    }

    public void removeAttribute(DataProviderAttribute attribute) {
        this.attributes.remove(attribute);
    }

    public boolean isAssociated() {
        return associatedProduct != null;
    }
}

