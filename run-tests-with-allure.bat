@echo off
echo Running tests and generating Allure report...

REM Run tests (continue even if they fail)
mvn clean test

REM Store the test exit code
set TEST_RESULT=%ERRORLEVEL%

REM Always generate Allure report
echo Generating Allure report...
mvn allure:report

REM Open the report
if exist target\allure-report\index.html (
    echo Opening Allure report...
    start "" "target\allure-report\index.html"
)

REM Exit with the original test result code
exit /b %TEST_RESULT%