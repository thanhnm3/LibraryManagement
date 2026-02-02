# Script chạy get_test_accounts.sql qua Docker (PostgreSQL)
# Chạy từ thư mục backend: .\scripts\run_get_test_accounts.ps1

$containerName = "demo-postgres"
$dbName = "demo_db"
$dbUser = "demo_user"
$scriptPath = Join-Path $PSScriptRoot "get_test_accounts.sql"

if (-not (Test-Path $scriptPath)) {
  Write-Error "Không tìm thấy file: $scriptPath"
  exit 1
}

Write-Host "=== Tai khoan test (password: password123) ===" -ForegroundColor Cyan
Get-Content -Path $scriptPath -Raw | docker exec -i $containerName psql -U $dbUser -d $dbName -f -
