@echo off
REM Quick Setup Script for Hotel Management System (Windows)
REM This script automates the setup process

echo.
echo ================================
echo Hotel Management System Setup
echo ================================
echo.

REM Step 1: Database Setup
echo [1] Creating MySQL database...
echo Please ensure MySQL is installed and running.
echo.
echo Do you want to create the database now? (This requires MySQL to be in PATH)
echo Press Y to continue or N to skip database setup...
set /p db_create="Choice (Y/N): "

if /i "%db_create%"=="Y" (
    mysql -u root -p < database_setup.sql
    if %errorlevel% equ 0 (
        echo.
        echo ^✓ Database created successfully!
    ) else (
        echo.
        echo ^✗ Database creation failed. Please manually run: mysql -u root -p ^< database_setup.sql
    )
) else (
    echo Skipping database creation. Please run: mysql -u root -p ^< database_setup.sql manually.
)

echo.
echo [2] Building the project...
echo.

REM Build the project
call mvnw.cmd clean package

if %errorlevel% equ 0 (
    echo.
    echo ^✓ Project built successfully!
) else (
    echo.
    echo ^✗ Build failed. Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo [3] Starting the application...
echo Application will start on http://localhost:8080
echo.
echo Press Ctrl+C to stop the application.
echo.

REM Run the application
call mvnw.cmd spring-boot:run

echo.
echo Application shutdown complete!
echo ================================
pause

