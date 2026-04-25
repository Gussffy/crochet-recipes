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
| MongoDB | 7.x | Banco de dados |
| Springdoc OpenAPI | 2.8.6 | Swagger UI |
| Docker / Docker Compose | - | Execucao local |

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
| `POST` | `/api/v1/recipes` | Criar receita |
| `GET` | `/api/v1/recipes` | Listar receitas (resumo) |
| `GET` | `/api/v1/recipes/{id}` | Buscar receita por ID |
| `PUT` | `/api/v1/recipes/{id}` | Atualizar receita |
| `DELETE` | `/api/v1/recipes/{id}` | Deletar receita |
| `GET` | `/api/v1/recipes/search?keyword=...` | Pesquisar por texto |
| `GET` | `/api/v1/recipes/author/{authorName}` | Filtrar por autor |
| `GET` | `/api/v1/recipes/tags?tags=a,b` | Filtrar por tags |

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
|  |- controller/RecipeController.java
|  |- dto/
|  |  |- RecipeRequestDTO.java
|  |  |- RecipeResponseDTO.java
|  |  |- RecipeSummaryDTO.java
|  |  |- RecipePartDTO.java
|  |  `- RoundDTO.java
|  |- model/
|  |  |- Recipe.java
|  |  |- RecipePart.java
|  |  `- Round.java
|  |- repository/RecipeRepository.java
|  `- service/
|     |- RecipeService.java
|     `- RecipeMapper.java
`- src/test/java/com/crochet/recipes/RecipeServiceTest.java
```

---

## CI (GitHub Actions)

Workflow em `.github/workflows/ci.yml` rodando em push e pull_request para `main` e `develop`:

- setup Java 21
- cache Maven
- `mvn -B clean verify --no-transfer-progress`

---

## Executar testes

```bash
mvn test
```

