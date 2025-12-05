#!/bin/bash

echo "=== Testing COMP3095 Assignment 2 ==="

# Wait for services to be ready
echo "Waiting for services to start..."
sleep 30

# Get Keycloak token for staff user
echo "Getting staff token from Keycloak..."
STAFF_TOKEN=$(curl -s -X POST \
  http://localhost:8080/realms/gbc-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=staff1&password=password&grant_type=password&client_id=api-gateway&client_secret=gateway-secret" \
  | jq -r '.access_token')

echo "Staff Token: $STAFF_TOKEN"

# Get Keycloak token for student user
echo "Getting student token from Keycloak..."
STUDENT_TOKEN=$(curl -s -X POST \
  http://localhost:8080/realms/gbc-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=student1&password=password&grant_type=password&client_id=api-gateway&client_secret=gateway-secret" \
  | jq -r '.access_token')

echo "Student Token: $STUDENT_TOKEN"

# Test API Gateway routes
echo "Testing API Gateway routes..."

# Test wellness service through gateway
echo "1. Testing wellness service..."
curl -s -H "Authorization: Bearer $STAFF_TOKEN" \
  http://localhost:8082/api/resources | jq '.[0:2]'

# Test goal service through gateway
echo "2. Testing goal service..."
curl -s -H "Authorization: Bearer $STUDENT_TOKEN" \
  http://localhost:8082/api/goals | jq '.[0:2]'

# Test event service through gateway
echo "3. Testing event service..."
curl -s -H "Authorization: Bearer $STUDENT_TOKEN" \
  http://localhost:8082/api/events | jq '.[0:2]'

echo "=== Test Complete ==="