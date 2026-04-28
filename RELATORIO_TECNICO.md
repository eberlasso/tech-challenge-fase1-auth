# RELATÓRIO TÉCNICO - TECH CHALLENGE FASE 1

**ESTUDANTE:** MAURICIO BORGES FLORENCIO  
**PROJETO:** USER SERVICE - AUTH  
**CURSO:** POS TECH - FIAP

---

## 1. Descrição Detalhada da Arquitetura da Aplicação

A aplicação **User Service** é um microsserviço de backend desenvolvido com **Spring Boot 4.0.5** e **Java 21**. A arquitetura foi desenhada seguindo os princípios de camadas e separação de interesses (SoC), garantindo um código modular, testável e de fácil manutenção.

### Estrutura de Pastas (Project Layout)
A organização do projeto segue a convenção Maven e as melhores práticas do Spring:
```text
tech-challenge-fase1-auth/
├── src/main/java/br/com/user/service/auth/
│   ├── config/             # Configurações de Segurança (JWT, SecurityFilterChain)
│   ├── controller/         # Adaptadores de Entrada (REST Controllers)
│   │   ├── auth/           # Endpoints de Login e Autenticação
│   │   └── user/           # Endpoints de Gestão de Usuários
│   ├── domain/             # Objetos de domínio e respostas genéricas
│   ├── dto/                # Data Transfer Objects (Requests e Responses)
│   ├── entities/           # Entidades JPA (Mapeamento de Banco de Dados)
│   ├── exceptions/         # Handlers globais e exceções customizadas (RFC 7807)
│   ├── mapper/             # Interfaces MapStruct para conversão DTO <-> Entity
│   ├── repository/         # Interfaces JpaRepository (Acesso a dados)
│   ├── service/            # Camada de Regras de Negócio
│   └── utils/              # Constantes e utilitários do sistema
├── src/main/resources/
│   ├── db/migration/       # Scripts Flyway para versionamento do banco
│   ├── static/             # Documentação estática (openapi.yaml)
│   └── application.yaml    # Configurações de ambiente e banco
├── Dockerfile              # Definição da imagem Docker da aplicação
├── docker-compose.yaml     # Orquestração da Aplicação + Banco PostgreSQL
└── collection.json         # Coleção Postman para testes da API
```

### Tecnologias e Frameworks
- **Spring Security + JWT:** Gerenciamento de autenticação e autorização stateless.
- **Spring Data JPA:** Abstração da camada de dados para PostgreSQL.
- **Flyway:** Evolução controlada do esquema do banco de dados.
- **Problem Detail (RFC 7807):** Padronização das respostas de erro para facilitar a integração com o frontend.
- **MapStruct:** Mapeamento performático entre entidades e DTOs.

## 2. Modelagem das Entidades e Relacionamentos

A modelagem de dados foi estruturada para garantir a unicidade de usuários e a organização das informações de endereço.

### Entidade `User` (Tabela `users`)
Representa o núcleo do sistema, contendo as credenciais e o perfil do usuário.
- **Atributos:**
    - `id` (PK): Identificador autoincremental.
    - `name` (full_name): Nome completo do usuário.
    - `email`: Endereço de e-mail com restrição de **Unique Index** no banco.
    - `login` (login_handle): Identificador de acesso.
    - `password` (password_hash): Senha armazenada com hash seguro.
    - `type` (user_role): Define se o usuário é `CLIENT` ou `RESTAURANT_OWNER`.
    - `address_id` (FK): Chave estrangeira para a tabela de endereços.
    - `lastUpdateDate`: Timestamp de auditoria para a última alteração.
    - `deleted`: Flag para **Soft Delete**.

### Entidade `Address` (Tabela `addresses`)
- **Atributos:** `street`, `number`, `city`, `zipCode`.

### Relacionamento e Integridade
O relacionamento entre `User` e `Address` é **1:1 (One-to-One)**.
- **Cascata:** A entidade `User` é a dona do relacionamento. Operações de persistência e exclusão em `User` são propagadas para `Address` (CascadeType.ALL).
- **Relacionamento no Banco:** A tabela `users` possui uma coluna `address_id` que aponta para o `id` da tabela `addresses`.

## 3. Descrição dos Endpoints e Exemplos de Uso

A API utiliza versionamento `/v1` e segue os verbos HTTP corretamente.

### **Autenticação**
- **POST `/v1/auth/login`**
  - **Request Body:**
    ```json
    { "login": "mauricio.user", "password": "minhasenha" }
    ```
  - **Response:** `{ "token": "eyJhbGciOi..." }`

### **Gestão de Usuários**
- **POST `/v1/users` (Cadastro)**
  - Cria um novo usuário. O campo `email` é validado para garantir que não haja duplicidade.
- **PUT `/v1/users/{id}` (Atualização de Dados)**
  - Permite alterar Nome, E-mail, Tipo e Endereço. Este endpoint **não** altera a senha.
- **PATCH `/v1/users/{id}/password` (Troca de Senha)**
  - Endpoint exclusivo para segurança. Exige a senha antiga e a nova.
    ```json
    { "oldPassword": "123", "newPassword": "456" }
    ```
- **GET `/v1/users/search?name=Mauricio` (Busca)**
  - Retorna usuários filtrando por nome parcial.
- **DELETE `/v1/users/{id}` (Remoção)**
  - Executa a deleção lógica. Restrito a usuários com perfil de Dono de Restaurante.

## 4. Documentação Swagger e OpenAPI

A documentação está disponível de duas formas:
1. **Swagger UI (Interativo):** Acessível em `http://localhost:8080/swagger-ui.html` após subir o Docker.
2. **OpenAPI (Estático):** Arquivo `src/main/resources/static/openapi.yaml` contendo a definição técnica dos contratos.

## 5. Coleção Postman

A coleção `collection.json` na raiz do projeto automatiza os testes de:
- Cadastro Válido e Inválido (E-mail duplicado).
- Validação de Login (Obrigatório).
- Troca de senha e atualização de perfil.
- Busca por nome.

## 6. Estrutura do Banco de Dados (Tabelas)

O esquema é criado automaticamente pelo Flyway via scripts SQL.
- **Tabela `users`**: Gerencia autenticação e roles.
- **Tabela `addresses`**: Gerencia dados de localização.
- **Auditoria**: O campo `last_modification_date` (lastUpdateDate) garante o rastreio da última alteração conforme exigido pelo requisito.

## 7. Passo a passo para Execução com Docker Compose

1. **Configurar Segredos**: Crie o arquivo `./secrets/db_password.txt` com a senha do banco.
2. **Executar**:
   ```bash
   docker-compose up --build -d
   ```
3. **Verificar**: A aplicação estará disponível em `http://localhost:8080`.

---
**Mauricio Borges Florencio - Tech Challenge Fase 1**
