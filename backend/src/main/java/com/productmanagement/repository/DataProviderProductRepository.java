package com.productmanagement.repository;

import com.productmanagement.domain.DataProviderProduct;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataProviderProductRepository extends Neo4jRepository<DataProviderProduct, Long> {

    /**
     * Find a data provider product by provider ID and external ID
     */
    @Query("MATCH (dp:DataProviderProduct) " +
           "WHERE dp.dataProviderId = $providerId AND dp.externalId = $externalId " +
           "RETURN dp")
    Optional<DataProviderProduct> findByDataProviderIdAndExternalId(
            @Param("providerId") String dataProviderId,
            @Param("externalId") String externalId);

    /**
     * Find all products from a specific data provider
     */
    @Query("MATCH (dp:DataProviderProduct) " +
           "WHERE dp.dataProviderId = $providerId " +
           "RETURN dp")
    List<DataProviderProduct> findByDataProviderId(@Param("providerId") String dataProviderId);

    /**
     * Find all associated data provider products for an internal product
     */
    @Query("MATCH (dp:DataProviderProduct)-[:ASSOCIATED_WITH]->(p:InternalProduct) " +
           "WHERE p.internalId = $internalId " +
           "RETURN dp")
    List<DataProviderProduct> findByAssociatedProductId(@Param("internalId") String internalId);

    /**
     * Find unassociated data provider products
     */
    @Query("MATCH (dp:DataProviderProduct) " +
           "WHERE NOT (dp)-[:ASSOCIATED_WITH]->(:InternalProduct) " +
           "RETURN dp")
    List<DataProviderProduct> findUnassociated();

    /**
     * Find data provider products by GTIN
     */
    @Query("MATCH (dp:DataProviderProduct) " +
           "WHERE dp.globalTradeIdentifier = $gtin " +
           "RETURN dp")
    List<DataProviderProduct> findByGlobalTradeIdentifier(@Param("gtin") String gtin);
}

