# Script de Prueba para Endpoints de Imágenes
# Usar PowerShell

Write-Host "=== PRUEBAS DE ENDPOINTS DE IMÁGENES ===" -ForegroundColor Green

# 1. Obtener todos los productos
Write-Host "`n1. Listando todos los productos..." -ForegroundColor Cyan
$productos = Invoke-RestMethod -Uri "http://localhost:8082/api/productos" -Method Get
$productos | Format-Table id, nombre, imagenUrl

# 2. Obtener un producto específico
$productoId = $productos[0].id
Write-Host "`n2. Obteniendo producto con ID $productoId..." -ForegroundColor Cyan
$producto = Invoke-RestMethod -Uri "http://localhost:8082/api/productos/$productoId" -Method Get
$producto | Format-List

# 3. Subir una imagen (necesitas tener una imagen de prueba)
Write-Host "`n3. Para subir una imagen, usa este comando:" -ForegroundColor Yellow
Write-Host @"
`$imagePath = 'C:\ruta\a\tu\imagen.jpg'
`$uri = 'http://localhost:8082/api/productos/$productoId/imagen'
`$fileBytes = [System.IO.File]::ReadAllBytes(`$imagePath)
`$boundary = [System.Guid]::NewGuid().ToString()
`$LF = "`r`n"

`$bodyLines = (
    "--`$boundary",
    "Content-Disposition: form-data; name=`"file`"; filename=`"imagen.jpg`"",
    "Content-Type: image/jpeg`$LF",
    [System.Text.Encoding]::GetEncoding('iso-8859-1').GetString(`$fileBytes),
    "--`$boundary--`$LF"
) -join `$LF

Invoke-RestMethod -Uri `$uri -Method Post -ContentType "multipart/form-data; boundary=`$boundary" -Body `$bodyLines
"@ -ForegroundColor Gray

# 4. Ver imagen en el navegador
Write-Host "`n4. Para ver la imagen en el navegador:" -ForegroundColor Yellow
if ($producto.imagenUrl) {
    $imageUrl = "http://localhost:8082/api/productos/imagen/$($producto.imagenUrl)"
    Write-Host "Abre esta URL en tu navegador: $imageUrl" -ForegroundColor Green
} else {
    Write-Host "El producto aún no tiene imagen" -ForegroundColor Red
}

Write-Host "`n=== FIN DE PRUEBAS ===" -ForegroundColor Green

