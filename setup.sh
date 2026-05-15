#!/bin/bash
# Quick Setup Script for Hotel Management System
# This script automates the setup process

echo "================================"
echo "Hotel Management System Setup"
echo "================================"
echo ""

# Step 1: Database Setup
echo "[1] Creating MySQL database..."
echo "Please ensure MySQL is running. Press Enter to continue..."
read

# Run the SQL script
mysql -u root -p < database_setup.sql

if [ $? -eq 0 ]; then
    echo "✓ Database created successfully!"
else
    echo "✗ Database creation failed. Please check your MySQL credentials."
    exit 1
fi

echo ""
echo "[2] Building the project..."

# Build the project
./mvnw clean package

if [ $? -eq 0 ]; then
    echo "✓ Project built successfully!"
else
    echo "✗ Build failed. Please check the error messages above."
    exit 1
fi

echo ""
echo "[3] Starting the application..."
echo "Application will start on http://localhost:8080"
echo ""

# Run the application
./mvnw spring-boot:run

echo ""
echo "Application startup complete!"
echo "================================"

