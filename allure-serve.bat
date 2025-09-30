@echo off
echo ==========================================
echo    ALLURE REPORT GENERATOR
echo ==========================================
echo.

REM Check if Maven is installed
mvn -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven first.
    pause
    exit /b 1
)

echo Choose an option:
echo 1. Run tests and generate Allure report
echo 2. Generate Allure report from existing results
echo 3. Open Allure report in browser
echo 4. Clean previous results and reports
echo.

set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" (
    echo.
    echo Running tests with Allure...
    call mvn clean test
    if %errorlevel% neq 0 (
        echo Tests failed, but generating report anyway...
    )
    echo.
    echo Generating Allure report...
    call mvn allure:report
    echo.
    echo Opening Allure report...
    call mvn allure:serve
) else if "%choice%"=="2" (
    echo.
    echo Generating Allure report from existing results...
    call mvn allure:report
    echo.
    echo Report generated at: target\allure-report
    echo Opening report in browser...
    start "" "target\allure-report\index.html"
) else if "%choice%"=="3" (
    echo.
    echo Opening Allure report server...
    call mvn allure:serve
) else if "%choice%"=="4" (
    echo.
    echo Cleaning previous results and reports...
    if exist "target\allure-results" rmdir /s /q "target\allure-results"
    if exist "target\allure-report" rmdir /s /q "target\allure-report"
    echo Clean completed!
) else (
    echo Invalid choice!
)

echo.
pause