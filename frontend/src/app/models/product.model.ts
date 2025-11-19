export interface Product {
  internalId: string;
  globalTradeIdentifier?: string;
  attributes?: ProductAttribute[];
}

export interface ProductAttribute {
  id?: number;
  name: string;
  value: string;
}

export interface ProductFilter {
  globalTradeIdentifier?: string;
  attributeFilters?: AttributeFilter[];
}

export interface AttributeFilter {
  attributeName: string;
  attributeValue: string;
  operator: FilterOperator;
}

export enum FilterOperator {
  EQ = 'EQ',
  CONTAINS = 'CONTAINS',
  GT = 'GT',
  LT = 'LT',
  EXISTS = 'EXISTS'
}

export interface DataProviderProduct {
  id?: number;
  dataProviderId: string;
  externalId: string;
  globalTradeIdentifier?: string;
  importedAt?: string;
  lastUpdatedAt?: string;
  associationStrategy?: string;
  attributes?: DataProviderAttribute[];
  associatedProduct?: Product;
}

export interface DataProviderAttribute {
  id?: number;
  name: string;
  value: string;
}

export interface ImportResult {
  totalProducts: number;
  associatedProducts: number;
  notAssociatedProducts: number;
  errors: string[];
  startTime: string;
  endTime: string;
}