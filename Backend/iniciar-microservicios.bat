@echo off
rem Abre una ventana por cada microservicio y lo arranca con Maven Wrapper.
rem Requisito: MySQL de XAMPP encendido. Para detener un microservicio: Ctrl + C en su ventana.
rem Uso: doble clic en este archivo, o en la terminal:  Backend\iniciar-microservicios.bat

rem %~dp0 = carpeta donde esta este archivo (Backend\)
set BACKEND=%~dp0

call :iniciar ms-productos
call :iniciar ms-programas
call :iniciar ms-colaboraciones

echo.
echo Listo. Espera a que cada ventana diga "Started ...Application".
echo Puertos: productos 8081, programas 8082, colaboraciones 8083.
pause
exit /b

rem Abre una ventana nueva con el microservicio, si su carpeta existe.
:iniciar
if exist "%BACKEND%%1\mvnw.cmd" (
    echo Iniciando %1...
    start "%1" cmd /k "cd /d %BACKEND%%1 && mvnw.cmd spring-boot:run"
) else (
    echo No se encontro %1, se omite.
)
exit /b
