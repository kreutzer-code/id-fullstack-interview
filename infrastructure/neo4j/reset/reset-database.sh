#!/bin/bash

# ========================================
# Neo4j Database Reset Script
# ========================================
# This script resets the Neo4j database to its initial state
# by deleting all data and recreating constraints and sample data.
#
# Usage:
#   ./reset-database.sh
#
# Prerequisites:
#   - Neo4j must be running (docker-compose up)
#   - cypher-shell must be available in the Neo4j container
# ========================================

set -e

echo "========================================="
echo "Neo4j Database Reset"
echo "========================================="
echo ""

# Configuration
NEO4J_USER="neo4j"
NEO4J_PASSWORD="password123"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CYPHER_SCRIPT="$SCRIPT_DIR/reset-database.cypher"

# Find Neo4j container (try common names)
NEO4J_CONTAINER=$(docker ps --format "{{.Names}}" | grep -i neo4j | head -n 1)

# Check if Neo4j container is running
if [ -z "$NEO4J_CONTAINER" ]; then
    echo "❌ Error: Neo4j container is not running"
    echo "   Please start it with: docker-compose up -d"
    exit 1
fi

echo "✓ Neo4j container is running: $NEO4J_CONTAINER"
echo ""

# Check if cypher script exists
if [ ! -f "$CYPHER_SCRIPT" ]; then
    echo "❌ Error: Cypher script not found at $CYPHER_SCRIPT"
    exit 1
fi

echo "✓ Found reset script: $CYPHER_SCRIPT"
echo ""

# Execute the reset script
echo "🔄 Resetting database..."
echo ""

docker exec -i "$NEO4J_CONTAINER" cypher-shell -u "$NEO4J_USER" -p "$NEO4J_PASSWORD" < "$CYPHER_SCRIPT"

echo ""
echo "========================================="
echo "✅ Database reset complete!"
echo "========================================="
echo ""
echo "The database has been reset to its initial state with:"
echo "  - All data cleared"
echo "  - Constraints recreated"
echo "  - Sample data loaded"
echo ""
echo "You can verify by opening Neo4j Browser at:"
echo "  http://localhost:7474"
echo ""

