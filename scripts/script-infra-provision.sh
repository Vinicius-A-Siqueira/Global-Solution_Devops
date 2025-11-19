#!/bin/bash

# ============================================
# Script de Provisionamento - Azure CLI
# Projeto: WellMind API - Global Solution 2025
# Grupo: Intelli Solution
# ============================================

# Variáveis de Configuração
RESOURCE_GROUP="rg-wellmind-prod"
LOCATION="brazilsouth"
APP_SERVICE_PLAN="plan-wellmind-prod"
WEB_APP_NAME="app-wellmind-api"
SQL_SERVER_NAME="sql-wellmind-server"
SQL_DB_NAME="db-wellmind"
SQL_ADMIN_USER="wellmindadmin"
SQL_ADMIN_PASSWORD="Wm@2025SecurePass!"  

echo "🚀 Iniciando provisionamento de recursos Azure..."

# 1. Criar Resource Group
echo "📦 Criando Resource Group: rg-wellmind-prod"
az group create \
  --name rg-wellmind-prod \
  --location brazilsouth

# 2. Criar Azure SQL Server
echo "🗄️ Criando Azure SQL Server: sql-wellmind-server"
az sql server create \
  --name sql-wellmind-server \
  --resource-group rg-wellmind-prod \
  --location brazilsouth \
  --admin-user wellmindadmin \
  --admin-password Wm@2025SecurePass!

# 3. Configurar Firewall do SQL Server (permitir Azure Services)
echo "🔓 Configurando regras de firewall..."
az sql server firewall-rule create \
  --resource-group rg-wellmind-prod \
  --server sql-wellmind-server \
  --name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

# 4. Criar Azure SQL Database
echo "💾 Criando Azure SQL Database: db-wellmind"
az sql db create \
  --resource-group rg-wellmind-prod \
  --server sql-wellmind-server \
  --name db-wellmind \
  --service-objective Basic \
  --backup-storage-redundancy Local

# 5. Criar App Service Plan (Linux, Java 17)
echo "📋 Criando App Service Plan: plan-wellmind-prod"
az appservice plan create \
  --name plan-wellmind-prod \
  --resource-group rg-wellmind-prod \
  --location brazilsouth \
  --sku B1 \
  --is-linux

# 6. Criar Web App
echo "🌐 Criando Web App: app-wellmind-api"
az webapp create \
  --name app-wellmind-api \
  --resource-group rg-wellmind-prod \
  --plan plan-wellmind-prod \
  --runtime "JAVA:17-java17"

# 7. Configurar variáveis de ambiente (Connection String)
echo "🔧 Configurando variáveis de ambiente..."
CONNECTION_STRING="jdbc:sqlserver://sql-wellmind-server.database.windows.net:1433;database=db-wellmind;user=wellmindadmin@sql-wellmind-server;password=Wm@2025SecurePass!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

az webapp config appsettings set \
  --name app-wellmind-api \
  --resource-group rg-wellmind-prod \
  --settings \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://sql-wellmind-server.database.windows.net:1433;database=db-wellmind;user=wellmindadmin@sql-wellmind-server;password=Wm@2025SecurePass!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
    SPRING_DATASOURCE_USERNAME="wellmindadmin" \
    SPRING_DATASOURCE_PASSWORD="Wm@2025SecurePass!" \
    SPRING_JPA_HIBERNATE_DDL_AUTO="update" \
    SPRING_JPA_SHOW_SQL="true"

echo "✅ Provisionamento concluído com sucesso!"
echo "📌 Resource Group: rg-wellmind-prod"
echo "📌 Web App URL: https://app-wellmind-api.azurewebsites.net"
echo "📌 SQL Server: sql-wellmind-server.database.windows.net"
echo "📌 Database: db-wellmind"
