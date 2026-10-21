@echo off
setlocal
cd /d "%~dp0"

echo ========================================================
echo   SafeHer: Launching directly from Antigravity IDE
echo ========================================================

set "PATH=%LOCALAPPDATA%\Android\Sdk\platform-tools;%PATH%"

adb devices | findstr /R "device$" >nul 2>&1
if errorlevel 1 (
    echo [1/3] Starting Pixel_9_2 Android Emulator...
    start "" "%LOCALAPPDATA%\Android\Sdk\emulator\emulator.exe" -avd Pixel_9_2
    echo Waiting for emulator to initialize...
    adb wait-for-device
) else (
    echo [1/3] Connected Android emulator/device detected!
)

echo [2/3] Building and installing SafeHer...
call .\gradlew.bat installDebug

echo [3/3] Launching SafeHer on screen...
for /f "tokens=1" %%d in ('adb devices ^| findstr /R "device$"') do (
    echo Opening SafeHer on %%d...
    adb -s %%d shell am start -n com.aistudio.safeher.womensec/com.example.MainActivity
)

echo ========================================================
echo   SafeHer is now running on your Android emulator!
echo ========================================================
