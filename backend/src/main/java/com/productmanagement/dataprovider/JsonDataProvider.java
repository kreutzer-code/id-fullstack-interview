package com.productmanagement.dataprovider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanagement.domain.DataProviderAttribute;
import com.productmanagement.domain.DataProviderProduct;
import com.productmanagement.domain.InternalProduct;
import com.productmanagement.domain.InternalProductAttribute;
import com.productmanagement.service.DataProviderProductService;
import com.productmanagement.service.InternalProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Data provider for importing products from JSON files.
 * Handles parsing, association, and persistence in a single workflow.
 */
@Component
@Slf4j
public class JsonDataProvider {

    private static final String PROVIDER_ID = "JsonDataProvider";
    private static final String SAMPLE_DATA_PATH = "sample-data/products-100.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InternalProductService productService;
    private final DataProviderProductService dataProviderProductService;
    private final List<AssociationStrategy> associationStrategies;

    public JsonDataProvider(
            InternalProductService productService,
            DataProviderProductService dataProviderProductService,
            GlobalTradeIdAssociationStrategy globalTradeIdAssociationStrategy) {
        this.productService = productService;
        this.dataProviderProductService = dataProviderProductService;
        this.associationStrategies = List.of(globalTradeIdAssociationStrategy);
    }

    /**
     * Import products from a JSON file.
     * @return import statistics
     */
    @Transactional
    public ImportResult importProducts() {
        log.info("Starting import from classpath resource: {}", SAMPLE_DATA_PATH);

        ImportResult result = new ImportResult();
        result.setStartTime(LocalDateTime.now());

        try {
            ClassPathResource resource = new ClassPathResource(SAMPLE_DATA_PATH);

            if (!resource.exists()) {
                throw new IllegalArgumentException("Sample data file not found: " + SAMPLE_DATA_PATH);
            }

            try (InputStream inputStream = resource.getInputStream()) {
                JsonNode rootNode = objectMapper.readTree(inputStream);

                if (!rootNode.isArray()) {
                    throw new IllegalArgumentException("Expected JSON array at root level");
                }

                int success = 0;
                for (JsonNode productNode : rootNode) {
                    try {
                        processProduct(productNode, result);
                        success++;
                    } catch (Exception e) {
                        log.error("Error processing product: {}", e.getMessage(), e);
                        result.addError("Product processing failed: " + e.getMessage());
                    }
                }

                result.setTotalProducts(success);
            }

            result.setEndTime(LocalDateTime.now());
            log.info("Import completed: {}", result);

        } catch (Exception e) {
            log.error("Import failed: {}", e.getMessage(), e);
            result.addError("Import failed: " + e.getMessage());
            result.setEndTime(LocalDateTime.now());
        }

        return result;
    }

    /**
     * Process a single product: parse, associate, and save.
     */
    private void processProduct(JsonNode productNode, ImportResult result) {
        try{
            String externalId = productNode.has("internalId") ? productNode.get("internalId").asText() : null;
            String gtin = productNode.has("globalTradeIdentifier") ? productNode.get("globalTradeIdentifier").asText() : null;

            Set<DataProviderAttribute> attributes = new HashSet<>();
            if (productNode.has("attributes") && productNode.get("attributes").isArray()) {
                for (JsonNode attrNode : productNode.get("attributes")) {
                    String name = attrNode.get("name").asText();
                    String value = attrNode.get("value").asText();
                    attributes.add(DataProviderAttribute.of(name, value));
                }
            }

            DataProviderProduct tempProduct = DataProviderProduct.builder()
                    .externalId(externalId)
                    .globalTradeIdentifier(gtin)
                    .attributes(attributes)
                    .build();

            AssociationResult associationResult = tryAssociate(tempProduct);

            if (associationResult.getInternalProduct().isPresent()) {
                InternalProduct internalProduct = associationResult.getInternalProduct().get();
                String strategyUsed = associationResult.getStrategyName();
                result.incrementAssociated();
                log.debug("Associated {} with existing product {} using strategy {}",
                         externalId, internalProduct.getInternalId(), strategyUsed);

                mapCategoryToInternalProduct(attributes, internalProduct);
                saveInternalProduct(internalProduct);

                saveDataProviderProduct(externalId, gtin, attributes, internalProduct, strategyUsed);
            }else{
                result.incrementNotAssociated();
            }

        }catch (Exception e){
            result.addError("Product processing failed: " + e.getMessage());
            log.error("Error processing product: {}", e.getMessage(), e);
        }
    }

    /**
     * Try to associate using available strategies.
     */
    private AssociationResult tryAssociate(DataProviderProduct externalProduct) {
        for (AssociationStrategy strategy : associationStrategies) {
            Optional<InternalProduct> match = strategy.findMatch(externalProduct);
            if (match.isPresent()) {
                log.debug("Match found using strategy: {}", strategy.getName());
                return new AssociationResult(match, strategy.getName());
            }
        }
        return new AssociationResult(Optional.empty(), null);
    }

    /**
     * Map Category attribute from DataProviderProduct to InternalProduct.
     * Adds or updates the Category attribute on the InternalProduct.
     */
    private void mapCategoryToInternalProduct(Set<DataProviderAttribute> dpAttributes, InternalProduct internalProduct) {
        Optional<DataProviderAttribute> categoryAttr = dpAttributes.stream()
                .filter(attr -> "Category".equalsIgnoreCase(attr.getName()))
                .findFirst();

        if (categoryAttr.isPresent()) {
            String categoryValue = categoryAttr.get().getValue();

            Optional<InternalProductAttribute> existingCategory = internalProduct.getAttributes().stream()
                    .filter(attr -> "Category".equalsIgnoreCase(attr.getName()))
                    .findFirst();

            if (existingCategory.isPresent()) {
                if (!categoryValue.equals(existingCategory.get().getValue())) {
                    log.debug("Updating Category for product {} from '{}' to '{}'",
                            internalProduct.getInternalId(),
                            existingCategory.get().getValue(),
                            categoryValue);
                    internalProduct.removeAttribute(existingCategory.get());
                    internalProduct.addAttribute(InternalProductAttribute.of("Category", categoryValue));
                }
            } else {
                log.debug("Adding Category '{}' to product {}",
                        categoryValue, internalProduct.getInternalId());
                internalProduct.addAttribute(InternalProductAttribute.of("Category", categoryValue));
            }
        }
    }

    /**
     * Save InternalProduct with updated attributes.
     */
    private void saveInternalProduct(InternalProduct internalProduct) {
        productService.save(internalProduct);
        log.debug("Saved InternalProduct {} with {} attributes",
                internalProduct.getInternalId(), internalProduct.getAttributes().size());
    }

    /**
     * Save DataProviderProduct with association to InternalProduct.
     */
    private void saveDataProviderProduct(String externalId, String gtin,
                                        Set<DataProviderAttribute> attributes,
                                        InternalProduct internalProduct,
                                        String strategyUsed) {

        Optional<DataProviderProduct> existing = dataProviderProductService
                .findByDataProviderIdAndExternalId(PROVIDER_ID, externalId);

        DataProviderProduct dpProduct;
        if (existing.isPresent()) {
            dpProduct = existing.get();
            dpProduct.setLastUpdatedAt(LocalDateTime.now());
        } else {
            dpProduct = DataProviderProduct.builder()
                    .dataProviderId(PROVIDER_ID)
                    .externalId(externalId)
                    .globalTradeIdentifier(gtin)
                    .importedAt(LocalDateTime.now())
                    .lastUpdatedAt(LocalDateTime.now())
                    .build();
        }

        dpProduct.setAssociatedProduct(internalProduct);
        dpProduct.setAssociationStrategy(strategyUsed);
        dpProduct.getAttributes().clear();
        dpProduct.getAttributes().addAll(attributes);

        dataProviderProductService.save(dpProduct);
        log.debug("Saved DataProviderProduct {} with {} attributes",
                externalId, dpProduct.getAttributes().size());
    }
}

