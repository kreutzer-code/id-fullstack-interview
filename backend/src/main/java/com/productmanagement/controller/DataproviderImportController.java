package com.productmanagement.controller;

import com.productmanagement.dataprovider.ImportResult;
import com.productmanagement.dataprovider.JsonDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for importing products from data providers.
 */
@RestController
@RequestMapping("/api/dataprovider/import")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class DataproviderImportController {

    private final JsonDataProvider jsonDataProvider;

    /**
     * Import products from a JSON file.
     * @return import result
     */
    @PostMapping("/json")
    public ResponseEntity<ImportResult> importJson() {
        log.info("Import request received for JSON data provider");

        try {
            ImportResult result = jsonDataProvider.importProducts();

            if (result.hasErrors()) {
                log.warn("Import completed with errors: {}", result);
                return ResponseEntity.status(207).body(result);
            }

            log.info("Import successful: {}", result);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Import failed", e);
            ImportResult errorResult = new ImportResult();
            errorResult.addError("Import failed: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResult);
        }
    }
}

