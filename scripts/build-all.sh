#!/bin/bash

echo "=== Building all services ==="

# Build API Gateway
echo "Building API Gateway..."
cd api-gateway
./gradlew clean build
cd ..

# Build Wellness Service
echo "Building Wellness Service..."
cd wellness-service
./gradlew clean build
cd ..

# Build Goal Tracking Service
echo "Building Goal Tracking Service..."
cd goal-tracking-service
./gradlew clean build
cd ..

# Build Event Service
echo "Building Event Service..."
cd event-service
./gradlew clean build
cd ..

echo "=== All services built successfully ==="