@echo off
echo "Setting JAVA_HOME..."
set JAVA_HOME="C:\Program Files\Java\jdk-17"

echo "Running the application..."
call .\mvnw.cmd clean javafx:run

