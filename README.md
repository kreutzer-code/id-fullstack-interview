# Product Management System - Fullstack Interview Project

## Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 21
- Maven 3.8+
- Node.js 18+ & npm

### Repository Structure

```
id-fullstack-interview/
├── backend/                           # Spring Boot backend application
│   ├── src/main/java/com/productmanagement/
│   │   ├── controller/                # REST API endpoints
│   │   ├── service/                   # Business logic layer
│   │   ├── repository/                # Neo4j data access layer
│   │   ├── domain/                    # Entity classes (nodes & relationships)
│   │   ├── dataprovider/              # Data import logic (JSON, XML)
│   │   └── ProductManagementApplication.java
│   ├── src/main/resources/
│   │   ├── application.yml            # Spring Boot configuration
│   │   └── sample-data/               # Sample JSON/XML files
│   └── pom.xml                        # Maven dependencies
│
├── frontend/                          # Angular 17 frontend application
│   ├── src/app/
│   │   ├── components/                # UI components
│   │   │   └── product-overview/      # Main dashboard component
│   │   ├── services/                  # HTTP services for API calls
│   │   ├── models/                    # TypeScript interfaces
│   │   ├── app.component.ts           # Root component
│   │   └── app.routes.ts              # Route configuration
│   ├── package.json                   # npm dependencies
│   └── angular.json                   # Angular configuration
│
├── infrastructure/
│   ├── docker-compose.yml             # Neo4j container configuration
│   └── neo4j/
│       ├── init/                      # Database initialization scripts
│       │   └── 01-constraints.cypher  # Constraints and indexes
│       └── reset/                     # Database reset utilities
│           ├── reset-database.sh      # Shell script to reset DB
│           └── reset-database.cypher  # Cypher script with sample data
│
└── README.md                          # This file
```

### 1. Start Neo4j Database
```bash
# Navigate to infrastructure folder
cd infrastructure

# Start Neo4j container
docker-compose up

# Wait for Neo4j to be ready (check http://localhost:7474)
# Default credentials: neo4j / password123
```

### 3. Start Backend
```bash
cd backend
mvn spring-boot:run
```
Backend will be available at `http://localhost:8080`

### 4. Start Frontend
```bash
cd frontend
npm install
npm start
```
Frontend will be available at `http://localhost:4200`


---

## Core Principles & Architecture of the app

### Technology Stack
- **Database**: Neo4j 5.15 (Graph Database)
- **Backend**: Spring Boot 3.2.0 with Spring Data Neo4j
- **Frontend**: Angular 17 (Standalone Components)
- **UI Framework**: Angular Material

### Domain Model

The system manages two types of products:

#### 1. **InternalProduct**
- Represents products in the internal system
- Has a unique `internalId` and `globalTradeIdentifier` 
- Contains attributes (name/value pairs) via `InternalProductAttribute` nodes

#### 2. **DataProviderProduct**
- Represents products from external data providers
- Has `dataProviderId` and `externalId`
- Contains attributes via `DataProviderAttribute` nodes
- Can be associated with an `InternalProduct` via `ASSOCIATED_WITH` relationship

### Graph Database Structure
```
(InternalProduct)-[:HAS_ATTRIBUTE]->(InternalProductAttribute)
(DataProviderProduct)-[:HAS_DATAPROVIDERATTRIBUTE]->(DataProviderAttribute)
(DataProviderProduct)-[:ASSOCIATED_WITH]->(InternalProduct)
```

### Architecture Layers

**Backend (Spring Boot)**:
- **Controllers**: REST API endpoints (`/api/products`, `/api/dataprovider-products`, `/api/dataprovider/import/json`)
- **Services**: Business logic layer (ProductService, DataProviderProductService, InternalProductService)
- **Repositories**: Data access layer using Spring Data Neo4j
- **Domain**: Entity classes with Neo4j annotations
- **Data Providers**: Import logic for external data sources (JSON, XML)
- **Association Strategies**: Strategy pattern for matching DataProviderProducts to InternalProducts

**Frontend (Angular)**:
- **Components**: Standalone components with Material UI
- **Services**: HTTP services for API communication
- **Models**: TypeScript interfaces for type safety
- **Routing**: Lazy-loaded routes

### Key Features

1. **Product Overview Dashboard**
   - Tab 1: View all Internal Products
   - Tab 2: View all Data Provider Products
   - Tab 3: Product Comparison (select product and view details side-by-side)

2. **JSON DataProvider**
   - Import products from JSON file
   - Automatic association using GlobalTradeId matching strategy
   - Category attribute mapping from DataProviderProduct to InternalProduct

3. **Database Management**
   - Reset script to clear and reinitialize database
   - Sample data with 100+ products

---

## Coding Challenge Tasks
The coding challenge shall help us to get to know your coding style better. 
It is also a first introduction for yourself to what kind of problems you might face in this position.
The challenge is intended to take you about 3h.

### Task 1: Understand the Project
Understand the project's architecture and functionality. Get the code running locally.
Create a feature branch for yourself (feat/\<firstname>-\<lastname>).

### Task 2: Implement XML Data Provider
Implement a second type of data provider that can import `backend/src/main/resources/sample-data/products-150.xml`.
Be aware that data might be corrupt.
Add a similar button as the "Import JSON" button in the frontend to trigger the import.

### Task 3: Attribute Mapping
In the product comparison tab, add a simple functionality that lets the user map data provider attributes to internal product attributes.

### Documentation
Please document your thoughts about the problem/code whenever needed into the code and/or in a separate file.

### Use feature branch
Please commit all your changes to the feature branch created in Task 1 (feat/\<firstname>-\<lastname>). Push only to this branch.

---

## Useful Commands
# Reset database
./infrastructure/neo4j/reset/reset-database.sh

