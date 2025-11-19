// InternalProduct constraints and indexes
CREATE CONSTRAINT internal_product_id IF NOT EXISTS FOR (p:InternalProduct) REQUIRE p.internalId IS UNIQUE;
CREATE CONSTRAINT internal_product_gtin IF NOT EXISTS FOR (p:InternalProduct) REQUIRE p.globalTradeId IS UNIQUE;

// DataProviderProduct constraints and indexes
CREATE CONSTRAINT dataprovider_product_id IF NOT EXISTS FOR (dp:DataProviderProduct) REQUIRE dp.dataproviderId IS UNIQUE;
CREATE INDEX dataprovider_key IF NOT EXISTS FOR (dp:DataProviderProduct) ON (dp.dataproviderKey);
CREATE INDEX dataprovider_gtin IF NOT EXISTS FOR (dp:DataProviderProduct) ON (dp.globalTradeId);

// InternalProductAttribute indexes
CREATE INDEX attribute_name IF NOT EXISTS FOR (ipa:InternalProductAttribute) ON (ipa.name);

// DataproviderAttribute indexes
CREATE INDEX dataprovider_attribute_name IF NOT EXISTS FOR (dpa:DataproviderAttribute) ON (dpa.name);
