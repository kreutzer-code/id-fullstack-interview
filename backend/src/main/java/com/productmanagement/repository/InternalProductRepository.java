package com.productmanagement.repository;

import com.productmanagement.domain.InternalProduct;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternalProductRepository extends Neo4jRepository<InternalProduct, String> {

    @Query("MATCH (p:InternalProduct) " +
           "WHERE p.globalTradeId = $gtin " +
           "OPTIONAL MATCH (p)-[r:HAS_ATTRIBUTE]->(a:InternalProductAttribute) " +
           "RETURN p, collect(r), collect(a)")
    Optional<InternalProduct> findByGlobalTradeIdentifier(@Param("gtin") String gtin);

    @Query("MATCH (p:InternalProduct)-[:HAS_ATTRIBUTE]->(a:Attribute) " +
           "WHERE a.name = $attributeName AND a.value = $attributeValue " +
           "RETURN p")
    List<InternalProduct> findByAttributeNameAndValue(@Param("attributeName") String attributeName,
                                                      @Param("attributeValue") String attributeValue);

    @Query("MATCH (p:InternalProduct) " +
           "WHERE p.internalId CONTAINS $searchTerm OR p.globalTradeId CONTAINS $searchTerm " +
           "RETURN p")
    List<InternalProduct> searchProducts(@Param("searchTerm") String searchTerm);
}
