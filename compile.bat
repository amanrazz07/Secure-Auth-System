@echo off
REM ============================================================
REM compile.bat — Compiles the Login Authentication System
REM ============================================================
REM Prerequisites:
REM   - Java JDK 17+ installed and 'javac' on PATH
REM   - jbcrypt-0.4.jar and mysql-connector-j-8.x.jar in lib/
REM ============================================================

echo.
echo ========================================
echo   Compiling Web Authentication System
echo ========================================
echo.

REM Set classpath to include all JARs in lib/
set CLASSPATH=lib\*

REM Compile all Java source files
javac -cp %CLASSPATH% -d out src\com\auth\model\User.java src\com\auth\model\AuthResult.java src\com\auth\db\DatabaseConnection.java src\com\auth\util\PasswordUtil.java src\com\auth\util\InputValidator.java src\com\auth\util\AuthLogger.java src\com\auth\service\AuthService.java src\com\auth\web\StaticFileHandler.java src\com\auth\web\ApiHandler.java src\com\auth\app\MainApp.java

IF %ERRORLEVEL% EQU 0 (
    echo   [SUCCESS] Compilation successful!
    echo   Output directory: out\
    echo.
) ELSE (
    echo   [ERROR] Compilation failed. Check errors above.
    echo.
)

pause
