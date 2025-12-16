# Script de Prueba - Productos con Imágenes

# Asegúrate de tener el servidor corriendo en http://localhost:8080
# y de tener una imagen de prueba en la ruta especificada

Write-Host "=== PRUEBAS DE PRODUCTOS CON IMAGENES ===" -ForegroundColor Cyan
Write-Host ""

# Variables de configuración
$baseUrl = "http://localhost:8080/api/productos"
$imagenTest = "C:\Users\alvar\Desktop\test-imagen.jpg"

# Función para mostrar respuestas
function Show-Response {
    param($response)
    Write-Host "Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Response:" -ForegroundColor Yellow
    $response.Content | ConvertFrom-Json | ConvertTo-Json -Depth 10
    Write-Host ""
}

# Test 1: Crear producto SIN imagen
Write-Host "Test 1: Crear producto SIN imagen (usando JSON)" -ForegroundColor Magenta
$body = @{
    nombre = "Producto Test Sin Imagen"
    descripcion = "Este producto no tiene imagen"
    precio = 19.99
    stock = 50
} | ConvertTo-Json

try {
    $response = Invoke-WebRequest -Uri "$baseUrl/json" `
        -Method POST `
        -Body $body `
        -ContentType "application/json" `
        -UseBasicParsing
    Show-Response $response
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host "----------------------------------------" -ForegroundColor Gray
Write-Host ""

# Test 2: Crear producto CON imagen (si existe la imagen)
if (Test-Path $imagenTest) {
    Write-Host "Test 2: Crear producto CON imagen" -ForegroundColor Magenta

    # Nota: PowerShell 5.1 tiene limitaciones con multipart/form-data
    # Este es un ejemplo usando curl si está disponible
    Write-Host "Usando curl para crear producto con imagen..." -ForegroundColor Yellow

    $curlCmd = @"
curl -X POST $baseUrl ``
  -F "nombre=Producto Con Imagen" ``
  -F "descripcion=Este producto tiene imagen de prueba" ``
  -F "precio=29.99" ``
  -F "stock=100" ``
  -F "imagen=@$imagenTest"
"@

    Write-Host $curlCmd -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Ejecutando..." -ForegroundColor Yellow

    try {
        $result = Invoke-Expression "curl -X POST $baseUrl -F `"nombre=Producto Con Imagen`" -F `"descripcion=Este producto tiene imagen de prueba`" -F `"precio=29.99`" -F `"stock=100`" -F `"imagen=@$imagenTest`""
        Write-Host $result -ForegroundColor Green
    } catch {
        Write-Host "Error: $_" -ForegroundColor Red
        Write-Host "Nota: Si no tienes curl instalado, puedes usar Postman o crear la imagen de prueba." -ForegroundColor Yellow
    }
} else {
    Write-Host "Test 2: OMITIDO - No se encontró imagen de prueba en: $imagenTest" -ForegroundColor Yellow
    Write-Host "Para probar este endpoint, crea una imagen en esa ruta o usa Postman." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "----------------------------------------" -ForegroundColor Gray
Write-Host ""

# Test 3: Listar todos los productos
Write-Host "Test 3: Obtener todos los productos" -ForegroundColor Magenta
try {
    $response = Invoke-WebRequest -Uri $baseUrl -Method GET -UseBasicParsing
    Show-Response $response
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "PRUEBAS COMPLETADAS" -ForegroundColor Green
Write-Host ""
Write-Host "Endpoints disponibles:" -ForegroundColor Cyan
Write-Host "  POST   $baseUrl              - Crear con imagen (multipart)" -ForegroundColor White
Write-Host "  POST   $baseUrl/json         - Crear sin imagen (JSON)" -ForegroundColor White
Write-Host "  PUT    $baseUrl/{id}         - Actualizar con imagen (multipart)" -ForegroundColor White
Write-Host "  PUT    $baseUrl/{id}/json    - Actualizar sin imagen (JSON)" -ForegroundColor White
Write-Host "  POST   $baseUrl/{id}/imagen  - Subir imagen separada" -ForegroundColor White
Write-Host "  GET    $baseUrl              - Listar todos" -ForegroundColor White
Write-Host "  GET    $baseUrl/{id}         - Obtener uno" -ForegroundColor White
Write-Host "  GET    $baseUrl/imagen/{file} - Obtener imagen" -ForegroundColor White
Write-Host ""
Write-Host "Documentación completa: GUIA_PRODUCTOS_IMAGENES.md" -ForegroundColor Yellow

