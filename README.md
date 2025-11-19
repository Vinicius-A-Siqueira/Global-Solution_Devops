# Global Solution 

![image](https://github.com/user-attachments/assets/6335eded-1ce5-41f1-8fbd-7921804f3f67)

## 👥 Integrantes

- **Gabriel Camargo** – RM557879  
- **Kauan Felipe** – RM557954  
- **Vinicius Alves** – RM551939  

---

# WellMind API

## 📋 Descrição do Projeto

**WellMind** é uma aplicação backend desenvolvida em **Java com Spring Boot** que oferece uma plataforma completa de monitoramento de bem-estar e saúde mental corporativa. A solução integra gestão de usuários, empresas, profissionais de saúde, registros de bem-estar e recomendações personalizadas em um ecossistema robusto e seguro.

---

## 🎯 Objetivo

O WellMind foi concebido para:

- **Monitorar e promover saúde mental** nos ambientes corporativos através de registros de bem-estar dos colaboradores.
- **Conectar usuários com profissionais de saúde** especializados em saúde mental e bem-estar.
- **Oferecer recomendações personalizadas** baseadas em categorias e histórico de bem-estar.
- **Facilitar a gestão** de múltiplas empresas e seus colaboradores em uma única plataforma.
- **Garantir segurança e privacidade** dos dados sensíveis através de autenticação JWT e controle de acesso.

---

## 🏗️ Arquitetura Geral

```
┌─────────────────────────────────────────────────────┐
│         Frontend (Angular/React)                    │
│    http://localhost:3000 ou localhost:4200         │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│      WellMind API (Spring Boot)                     │
│         localhost:8080                              │
├─────────────────────────────────────────────────────┤
│ • Segurança: JWT + Spring Security                 │
│ • Cache: Simple Cache (desenvolvimento)             │
│ • Banco: Oracle Database                            │
│ • Documentação: Swagger/OpenAPI                     │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│      Oracle Database (ORCL)                         │
│   oracle.fiap.com.br:1521:ORCL                     │
└─────────────────────────────────────────────────────┘
```

---

## 🚀 Tecnologias Utilizadas

### Backend
- **Java 17+**
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - ORM com Hibernate
- **Spring Cache** - Gerenciamento de cache (simples em dev, Redis em prod)
- **JWT (JSON Web Tokens)** - Autenticação sem estado
- **Oracle JDBC** - Conectividade ao banco Oracle

### Ferramentas & Bibliotecas
- **Lombok** - Redução de código boilerplate
- **Jackson** - Serialização/Desserialização JSON
- **Springdoc OpenAPI** - Geração automática de documentação Swagger
- **HikariCP** - Pool de conexões de alta performance

### Banco de Dados
- **Oracle Database** - Armazenamento de dados
- **Hibernate** - Mapeamento Objeto-Relacional

### Desenvolvimento
- **Maven** - Gerenciador de dependências
- **Git** - Controle de versão
- **Postman/Swagger UI** - Testes de API
- **IntelliJ IDEA / Visual Studio Code** - IDEs recomendadas

---

## 📦 Estrutura do Projeto

```
wellmind-app/
├── src/
│   ├── main/
│   │   ├── java/com/wellmind/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java          # Configuração de segurança
│   │   │   │   ├── CacheConfig.java             # Configuração de cache
│   │   │   │   └── ...
│   │   │   ├── controller/
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── EmpresaController.java
│   │   │   │   ├── ProfissionalSaudeController.java
│   │   │   │   └── ...
│   │   │   ├── service/
│   │   │   │   ├── UsuarioService.java
│   │   │   │   ├── EmpresaService.java
│   │   │   │   └── ...
│   │   │   ├── repository/
│   │   │   │   ├── UsuarioRepository.java
│   │   │   │   ├── EmpresaRepository.java
│   │   │   │   └── ...
│   │   │   ├── dto/
│   │   │   │   ├── usuario/
│   │   │   │   ├── empresa/
│   │   │   │   └── ...
│   │   │   ├── entity/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Empresa.java
│   │   │   │   └── ...
│   │   │   ├── exception/
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── ...
│   │   │   └── WellMindApplication.java
│   │   └── resources/
│   │       ├── application.yml                  # Configuração principal
│   │       └── application-prod.yml             # Configuração produção
│   └── test/
│       └── java/com/wellmind/
├── pom.xml                                      # Dependências Maven
├── README.md
└── .gitignore
```

---

## 🔑 Funcionalidades Principais

### 1. **Autenticação e Autorização**
- Registro de novos usuários
- Login com geração de JWT
- Refresh token para renovação de sessão
- Controle de acesso baseado em roles (USER, ADMIN)

### 2. **Gestão de Usuários**
- CRUD completo de usuários
- Perfis de usuário (colaborador, gestor, profissional)
- Histórico de bem-estar pessoal

### 3. **Gestão de Empresas**
- Registro e gerenciamento de empresas
- Associação de colaboradores a empresas
- Dados de contato e localização

### 4. **Profissionais de Saúde**
- Catálogo de profissionais especializados
- Filtro por disponibilidade e especialidade
- Agendamento de consultas

### 5. **Registros de Bem-Estar**
- Criação de registros pessoais de bem-estar
- Categorização por tipo (físico, mental, emocional, etc.)
- Histórico e análise de tendências

### 6. **Recomendações Personalizadas**
- Sugestões baseadas em histórico de bem-estar
- Categorias de recomendação
- Priorização e relevância

---

## ⚙️ Configuração e Instalação

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- Oracle Database (ou acesso remoto configurado)
- Git

### Passos de Instalação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/wellmind-app.git
   cd wellmind-app
   ```

2. **Configure as variáveis de ambiente:**
   ```bash
   export DB_USER=seu_usuario_oracle
   export DB_PASSWORD=sua_senha_oracle
   export JWT_SECRET=sua_chave_secreta_jwt
   export SERVER_PORT=8080
   ```

3. **Instale as dependências e construa:**
   ```bash
   mvn clean install
   ```

4. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

5. **Acesse a API:**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs: `http://localhost:8080/api-docs`

---

## 🔒 Segurança

### Configuração de Segurança (`SecurityConfig`)
- CSRF desabilitado para APIs REST
- CORS configurado para origens permitidas
- Sessões stateless com JWT
- Filtro customizado de autenticação JWT
- BCrypt com 12 rounds para criptografia de senhas

### Autenticação JWT
- Token gerado no login válido por 24 horas
- Refresh token válido por 7 dias
- Payload contém identificação e roles do usuário

### Controle de Acesso
- Endpoints públicos: Swagger, health checks, login/registro
- Endpoints protegidos: Requerem autenticação JWT
- Role-based access control (RBAC) para operações sensíveis

---

## 💾 Banco de Dados

### Tabelas Principais
- **USUARIO** - Dados de usuários
- **EMPRESA** - Dados de empresas
- **PROFISSIONAL_SAUDE** - Profissionais de saúde
- **REGISTRO_BEMESTAR** - Registros pessoais
- **RECOMENDACAO** - Recomendações personalizadas
- **CATEGORIA_RECOMENDACAO** - Categorias de recomendação

### Connection Pool
- **HikariCP** com até 20 conexões ativas
- Timeout de 30 segundos para novas conexões
- Validação automática de conexões inativas

---

## 🧪 Testes e Documentação

### Swagger UI
Acesse `http://localhost:8080/swagger-ui.html` para visualizar e testar todos os endpoints interativamente.

### Exemplo de Requisição (POST /api/v1/usuario)
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123",
  "tipoUsuario": "COLABORADOR"
}
```

### Exemplo de Resposta (200 OK)
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "COLABORADOR",
  "dataCadastro": "2025-11-18T08:00:00",
  "statusAtivo": "S"
}
```

---

## 📝 Configuração do `application.yml`

```yaml
spring:
  application:
    name: wellmind-api
    version: 1.0.0

  datasource:
    url: jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
    username: ${DB_USER:rm551939}
    password: ${DB_PASSWORD:270399}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

  cache:
    type: simple  # 'simple' para dev, 'redis' para prod

server:
  port: ${SERVER_PORT:8080}

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000  # 24 horas
    refresh-expiration: 604800000  # 7 dias
```

---

## 🐛 Troubleshooting

### Erro: "exceeded simultaneous SESSIONS_PER_USER limit"
- **Causa:** Muitas conexões simultâneas ao banco Oracle
- **Solução:** Reduza o tamanho do pool em `application.yml` ou libere sessões antigas no banco

### Erro: "Could not resolve subtype... missing type id property '@class'"
- **Causa:** Jackson exigindo tipagem polimórfica no JSON
- **Solução:** Comente `activateDefaultTyping` no `ObjectMapper` ou inclua `@class` no JSON enviado

### Erro: "Unable to connect to Redis"
- **Causa:** Redis configurado mas não disponível
- **Solução:** Mude `cache.type` para `simple` em desenvolvimento

---

## 🚢 Deployment em Produção

### Azure App Service
```bash
# Build da aplicação
mvn clean package -DskipTests

# Deploy para Azure
az webapp up --name wellmind-api --runtime "java|17-java17"
```

### Configuração Produção
- Ativar Redis para cache distribuído
- Configurar HTTPS/SSL
- Aumentar pool de conexões do banco
- Usar variáveis de ambiente sensíveis

---

## 👥 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está licenciado sob a MIT License - veja o arquivo LICENSE para detalhes.

---

## 📞 Contato e Suporte

- **Autor:** Vinícius Siqueira
- **Email:** vinicius@fiap.com.br
- **GitHub:** [@ViníciusSiqueira](https://github.com/seu-usuario)
- **Status:** Em desenvolvimento ativo

---

## 📚 Referências e Documentação

- [Spring Boot Official Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/gs/securing-web/)
- [JWT.io - JSON Web Tokens](https://jwt.io/)
- [Oracle JDBC Driver](https://www.oracle.com/database/technologies/appdev/jdbc.html)
- [Springdoc OpenAPI](https://springdoc.org/)

---

**Última atualização:** 18 de novembro de 2025  
**Versão:** 1.0.0
