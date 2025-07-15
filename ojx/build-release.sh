#!/bin/bash

# OJX Build and Package Script
echo "=== Building OJX Application ==="

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Compile and package with dependencies
echo "Compiling and packaging application..."
mvn package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "=== Build Successful! ==="
    echo ""
    echo "Generated JAR files:"
    ls -la target/*.jar
    echo ""
    echo "Main executable JAR: target/ojx-1.0.jar"
    echo ""
    echo "To run the application:"
    echo "java -jar target/ojx-1.0.jar"
    echo ""
    echo "Distribution package ready!"
else
    echo ""
    echo "=== Build Failed! ==="
    echo "Please check the error messages above."
    exit 1
fi
