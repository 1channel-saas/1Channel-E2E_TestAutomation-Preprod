#!/bin/bash

echo "=========================================="
echo "    ALLURE REPORT GENERATOR"
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven first."
    exit 1
fi

echo "Choose an option:"
echo "1. Run tests and generate Allure report"
echo "2. Generate Allure report from existing results"
echo "3. Open Allure report in browser"
echo "4. Clean previous results and reports"
echo ""

read -p "Enter your choice (1-4): " choice

case $choice in
    1)
        echo ""
        echo "Running tests with Allure..."
        mvn clean test
        if [ $? -ne 0 ]; then
            echo "Tests failed, but generating report anyway..."
        fi
        echo ""
        echo "Generating Allure report..."
        mvn allure:report
        echo ""
        echo "Opening Allure report..."
        mvn allure:serve
        ;;
    2)
        echo ""
        echo "Generating Allure report from existing results..."
        mvn allure:report
        echo ""
        echo "Report generated at: target/allure-report"
        echo "Opening report in browser..."
        if [[ "$OSTYPE" == "linux-gnu"* ]]; then
            xdg-open target/allure-report/index.html
        elif [[ "$OSTYPE" == "darwin"* ]]; then
            open target/allure-report/index.html
        elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "win32" ]]; then
            start target/allure-report/index.html
        fi
        ;;
    3)
        echo ""
        echo "Opening Allure report server..."
        mvn allure:serve
        ;;
    4)
        echo ""
        echo "Cleaning previous results and reports..."
        rm -rf target/allure-results
        rm -rf target/allure-report
        echo "Clean completed!"
        ;;
    *)
        echo "Invalid choice!"
        ;;
esac

echo ""
read -p "Press enter to continue..."