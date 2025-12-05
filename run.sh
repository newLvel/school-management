#!/bin/bash
echo "Setting JAVA_HOME..."
export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64" # Example path, change if needed

echo "Running the application..."
./mvnw clean javafx:run
