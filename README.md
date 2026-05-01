# Receitas de Croche API

Estou desenvolvendo esse projeto para minha namorada e esse é o instagram de artesanato dela -> [Lailly](https://www.instagram.com/laillycrafts?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==) 

Sistema para publicacao e gerenciamento de receitas de croche com Spring Boot, MongoDB e Docker.

---

## Funcionalidades

- Publicar receitas com nome, descricao, autor, materiais e tags
- Dividir receita em partes (ex: Cabeca, Corpo, Bracos)
- Escrever cada parte por rounds (voltas), com numero e descricao
- Associar imagem por parte e capa da receita (Base64)
- CRUD completo de receitas
- Pesquisa por palavra-chave e filtros por autor e tags
- Swagger UI para explorar e testar endpoints

---

## Tecnologias

| Tecnologia | Versao | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.3 | API REST |
| Spring Data MongoDB | 3.x | Persistencia |
| Spring Security | 7.0.3 | Autenticacao e autorizacao |
| JJWT | 0.12.3 | JWT (JSON Web Tokens) |
| MongoDB | 7.x | Banco de dados |
| Springdoc OpenAPI | 2.8.6 | Swagger UI |
| Docker / Docker Compose | - | Execucao local |

---

##  Melhorias recentes

- ✅ **Autenticação JWT** — Sistema seguro com Spring Security e JJWT
- ✅ **Endpoints de Registro e Login** — Criar usuário e autenticar
- ✅ **Paginação** — Listar receitas com suporte a paginas, tamanho e ordenacao
- ✅ **DTOs como Records** — Imutabilidade garantida, menos boilerplate
- ✅ **Mapper dividido** — `RecipeRequestMapper` e `RecipeResponseMapper` com responsabilidades bem definidas
- ✅ **Estrutura organizada** — DTOs separados em `request/`, `response/` e `error/`
- ✅ **Models na pasta `embedded/`** — Melhor organização de componentes
- ✅ **Exceptions limpas** — Removidas classes não utilizadas
- ✅ **Testes atualizados** — Compatíveis com Records e novos mappers

---

## Como executar

### Pre-requisitos

- Docker e Docker Compose
- Java 21
- Maven 3.9+ (se for rodar fora do Docker)

### Opcao 1 - Docker Compose completo

```bash
docker compose up --build
```

### Opcao 2 - Dev local (app local + Mongo em Docker)

```bash
docker compose -f docker-compose.dev.yml up -d
mvn spring-boot:run
```

---

## Endpoints da API

| Metodo | Endpoint | Descricao |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Registrar novo usuário |
| `POST` | `/api/v1/auth/login` | Fazer login |
| `GET` | `/api/v1/recipes` | Listar receitas (resumo) |
| `GET` | `/api/v1/recipes/paginated?page=0&size=10` | Listar com paginacao |
| `GET` | `/api/v1/recipes/{id}` | Buscar receita por ID |
| `POST` | `/api/v1/recipes` | Criar receita (autenticado) |
| `PUT` | `/api/v1/recipes/{id}` | Atualizar receita (autenticado) |
| `DELETE` | `/api/v1/recipes/{id}` | Deletar receita (autenticado) |
| `GET` | `/api/v1/recipes/search?keyword=...` | Pesquisar por texto |
| `GET` | `/api/v1/recipes/author/{authorName}` | Filtrar por autor |
| `GET` | `/api/v1/recipes/tags?tags=a,b` | Filtrar por tags |

---

## 🔐 Autenticacao com JWT

### Registro de Novo Usuário

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "seu-email@example.com",
    "password": "senha123",
    "passwordConfirm": "senha123"
  }'
```

**Response (201):**
```json
{
  "success": true,
  "message": "Usuário registrado com sucesso!",
  "timestamp": "2026-04-30T22:30:00",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "email": "seu-email@example.com",
    "type": "Bearer"
  }
}
```

### Fazer Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "seu-email@example.com",
    "password": "senha123"
  }'
```

### Usar Token em Requisições

```bash
curl -X GET http://localhost:8080/api/v1/recipes \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

### Validacoes de Autenticacao

- ✅ Email válido (formato correto)
- ✅ Senha mínimo 6 caracteres
- ✅ Confirmação de senha (devem ser iguais)
- ✅ Email duplicado (erro 409 CONFLICT)
- ✅ Credenciais inválidas (erro 401)

---

## 📄 Paginacao de Receitas

### Listar com Paginacao

```bash
GET /api/v1/recipes/paginated?page=0&size=20&sort=createdAt,desc
```

**Response:**
```json
{
  "success": true,
  "message": "Página 1 de 5",
  "timestamp": "2026-04-30T...",
  "data": {
    "content": [
      { "id": "...", "name": "Urso Amigurumi", ... }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": { ... }
    },
    "totalElements": 100,
    "totalPages": 5,
    "last": false,
    "first": true
  }
}
```

### Parametros de Paginacao

| Parametro | Descricao | Exemplo |
|-----------|-----------|---------|
| `page` | Numero da pagina (comeca em 0) | `page=0` |
| `size` | Quantidade de itens por pagina | `size=20` |
| `sort` | Campo e direcao de ordenacao | `sort=createdAt,desc` |

### Endpoints com Paginacao

- `GET /api/v1/recipes/paginated` - Listar todas
- `GET /api/v1/recipes/search/paginated?keyword=...` - Pesquisa
- `GET /api/v1/recipes/author/{authorName}/paginated` - Por autor
- `GET /api/v1/recipes/tags/paginated?tags=a,b` - Por tags

---



Todos os DTOs do projeto utilizam **Java Records** (desde Java 21) para garantir:
- ✅ Imutabilidade automática
- ✅ Menos boilerplate (sem Lombok necessário para DTOs)
- ✅ Melhor performance
- ✅ Thread-safe por padrão

**Exemplo:**
```java
public record RecipeRequestDTO(
    @NotBlank String name,
    @NotBlank String description,
    @NotBlank String authorName,
    @NotEmpty @Valid List<MaterialDTO> materials,
    @NotEmpty @Valid List<RecipePartDTO> parts,
    String coverImageBase64,
    String coverImageContentType,
    List<String> tags
) {}
```

---

## Modelo JSON (com rounds)

### Request - `POST /api/v1/recipes`

```json
{
  "name": "Urso Amigurumi",
  "description": "Urso fofo para presente",
  "authorName": "Lailly",
  "tags": ["urso", "amigurumi", "brinquedo"],
  "coverImageBase64": "data:image/jpeg;base64,/9j/4AAQ...",
  "coverImageContentType": "image/jpeg",
  "materials": [
    {
      "name": "La acrilica",
      "quantity": "100g",
      "color": "Bege",
      "notes": "Fio numero 4"
    }
  ],
  "parts": [
    {
      "order": 1,
      "title": "Cabeca",
      "rounds": [
        {
          "roundNumber": 1,
          "description": "Join to first ch with a SC, 15SC (16)"
        },
        {
          "roundNumber": 2,
          "description": "SC in each stitch around (16)"
        },
        {
          "roundNumber": 3,
          "description": "SC in each (16), ch2 at the end of round"
        }
      ],
      "imageBase64": "data:image/png;base64,iVBORw0...",
      "imageContentType": "image/png"
    }
  ]
}
```

### Response - `201 Created`

```json
{
  "success": true,
  "message": "Receita criada com sucesso!",
  "timestamp": "2026-04-25T10:30:00",
  "data": {
    "id": "65f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Urso Amigurumi",
    "description": "Urso fofo para presente",
    "authorName": "Lailly",
    "materials": [],
    "parts": [
      {
        "order": 1,
        "title": "Cabeca",
        "rounds": [
          {
            "roundNumber": 1,
            "description": "Join to first ch with a SC, 15SC (16)"
          }
        ]
      }
    ],
    "tags": ["urso", "amigurumi"],
    "createdAt": "2026-04-25T10:30:00",
    "updatedAt": "2026-04-25T10:30:00"
  }
}
```

---

## Regras importantes do modelo

- `parts[].order` deve ser maior que 0
- `parts[].rounds` deve ter pelo menos 1 item
- `rounds[].roundNumber` deve ser maior que 0
- `rounds[].description` e obrigatoria

---

## Estrutura do projeto

```text
crochet-recipes/
|- .github/workflows/ci.yml
|- docker-compose.yml
|- docker-compose.dev.yml
|- pom.xml
|- src/main/java/com/crochet/recipes/
|  |- CrochetRecipesApplication.java
|  |- config/
|  |  |- MongoConfig.java
|  |  |- MongoInjectionValidator.java
|  |  |- NoMongoInjection.java
|  |  |- OpenApiConfig.java
|  |  |- SecurityConfig.java (autenticacao)
|  |  `- WebConfig.java
|  |- controller/
|  |  |- RecipeController.java
|  |  `- AuthenticationController.java (nova)
|  |- dto/
|  |  |- request/
|  |  |  |- RecipeRequestDTO.java (Record)
|  |  |  |- MaterialDTO.java (Record)
|  |  |  |- RecipePartDTO.java (Record)
|  |  |  |- RoundDTO.java (Record)
|  |  |  |- LoginRequestDTO.java (nova)
|  |  |  `- RegisterRequestDTO.java (nova)
|  |  |- response/
|  |  |  |- RecipeResponseDTO.java (Record)
|  |  |  |- RecipeSummaryDTO.java (Record)
|  |  |  |- ApiResponseDTO.java (Record)
|  |  |  `- LoginResponseDTO.java (nova)
|  |  `- error/
|  |     |- ErrorDetailsDTO.java (Record)
|  |     `- ValidationErrorDTO.java (Record)
|  |- exception/
|  |  |- CrochetException.java
|  |  |- GlobalExceptionHandler.java
|  |  |- RecipeNotFoundException.java
|  |  |- JwtAuthenticationException.java (nova)
|  |  `- UserNotFoundException.java (nova)
|  |- mapper/
|  |  |- RecipeRequestMapper.java
|  |  `- RecipeResponseMapper.java
|  |- model/
|  |  |- Recipe.java
|  |  |- User.java (nova)
|  |  `- embedded/
|  |     |- Material.java
|  |     |- RecipePart.java
|  |     `- Round.java
|  |- repository/
|  |  |- RecipeRepository.java
|  |  `- UserRepository.java (nova)
|  |- security/ (nova)
|  |  |- JwtProvider.java
|  |  |- JwtAuthenticationFilter.java
|  |  `- CustomUserDetailsService.java
|  `- service/
|     |- RecipeService.java
|     `- AuthenticationService.java (nova)
`- src/test/java/com/crochet/recipes/
   `- RecipeServiceTest.java
```

---

## Arquitetura dos Mappers

O projeto utiliza **dois mappers especializados** para melhor separação de responsabilidades:

### `RecipeRequestMapper`
Converte `RecipeRequestDTO` (request) → `Recipe` (model)
- Converte DTOs em entities
- Ordena partes por `order` e rounds por `roundNumber`
- Atualiza models existentes

### `RecipeResponseMapper`
Converte `Recipe` (model) → DTOs de resposta
- Converte models em `RecipeResponseDTO` (resposta detalhada)
- Converte models em `RecipeSummaryDTO` (resposta resumida)
- Calcula totais (partes, materiais)

---

## 🔒 Configuracao de Seguranca

### Environment Variables

Para produção, configure as variáveis de ambiente:

```bash
JWT_SECRET=sua-chave-secreta-muito-longa-com-mais-de-32-caracteres
JWT_EXPIRATION_MS=86400000  # 24 horas em ms
```

**Valor padrão (dev):**
```properties
jwt.secret=your-secret-key-change-this-in-production-environment
jwt.expiration.ms=86400000
```

### Regras de Acesso

| Endpoint | Acesso | Requer Token |
|----------|--------|--------------|
| GET /recipes | Público | ❌ |
| GET /recipes/{id} | Público | ❌ |
| GET /recipes/search | Público | ❌ |
| GET /recipes/paginated | Público | ❌ |
| POST /recipes | Autenticado | ✅ |
| PUT /recipes/{id} | Autenticado | ✅ |
| DELETE /recipes/{id} | Autenticado | ✅ |
| POST /auth/login | Público | ❌ |
| POST /auth/register | Público | ❌ |

---



Workflow em `.github/workflows/ci.yml` rodando em push e pull_request para `main` e `develop`:

- setup Java 21
- cache Maven
- `mvn -B clean verify --no-transfer-progress`

---

## Executar testes

```bash
mvn test
```

Testes incluem:
- ✅ Criação de receitas
- ✅ Busca por ID, autor, tags e palavra-chave
- ✅ Atualização e deleção
- ✅ Validação de erros e exceções

---

