@echo off
REM ============================================================
REM run.bat — Runs the Login Authentication System
REM ============================================================
REM Prerequisites:
REM   - Run compile.bat first to build the project
REM   - MySQL server running with auth_system database created
REM ============================================================

echo.

REM Set classpath to include compiled classes and all JARs
set CLASSPATH=out;lib\*

REM Run the main application
java -cp %CLASSPATH% com.auth.app.MainApp
