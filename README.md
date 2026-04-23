# 🧶 Receitas de Crochê API

Sistema completo para publicação e gerenciamento de receitas de crochê, desenvolvido com **Spring Boot**, **MongoDB** e **Docker**.

---

## 📋 Funcionalidades

- ✅ Publicar receitas com nome, descrição e materiais
- ✅ Dividir a receita em **partes nomeadas** (ex: "Parte 1 - Cabeça do Urso")
- ✅ Cada parte possui **título**, **instruções em texto** e **imagem própria** (Base64)
- ✅ **Imagem de capa** para a receita
- ✅ Filtros por **autor**, **dificuldade** e **tags**
- ✅ **Pesquisa** por palavra-chave (nome, descrição, tags)
- ✅ CRUD completo
- ✅ Swagger UI para documentação e testes
- ✅ Mongo Express para visualizar o banco via browser

---

## 🛠 Tecnologias

| Tecnologia       | Versão | Uso                          |
|------------------|--------|------------------------------|
| Java             | 21      | Linguagem principal          |
| Spring Boot      | 4.0.3  | Framework Web                |
| Spring Data MongoDB | 3.x    | Persistência                 |
| MongoDB          | 7.0    | Banco de dados               |
| Docker           | -      | Containerização              |
| Docker Compose   | -      | Orquestração local           |
| Springdoc OpenAPI| 2.8.6 | Swagger UI                   |
| Lombok           | -      | Redução de boilerplate       |

---

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados
- (Opcional para dev) Java 17 e Maven 3.9+

---

### ▶️ Opção 1 — Docker Compose completo (recomendado)

Sobe a API + MongoDB + Mongo Express tudo junto:

```bash
docker-compose up --build
```

Aguarde o build (~2 min na primeira vez). Acesse:

| Serviço        | URL                                      |
|----------------|------------------------------------------|
| API            | http://localhost:8080                    |
| Swagger UI     | http://localhost:8080/swagger-ui.html    |
| Mongo Express  | http://localhost:8081 (admin / admin123) |

---

### ▶️ Opção 2 — Desenvolvimento local (Maven + Docker)

Sobe só o MongoDB e Mongo Express via Docker, roda a API na sua máquina:

```bash
# 1. Sobe o banco
docker-compose -f docker-compose.dev.yml up -d

# 2. Roda a aplicação
./mvnw spring-boot:run

# Ou com Maven instalado:
mvn spring-boot:run
```

---

### ▶️ Opção 3 — Somente o JAR

```bash
mvn clean package -DskipTests
java -jar target/recipes-1.0.0.jar
```

---

## 📡 Endpoints da API

### Receitas

| Método   | Endpoint                              | Descrição                        |
|----------|---------------------------------------|----------------------------------|
| `POST`   | `/api/v1/recipes`                     | Criar nova receita               |
| `GET`    | `/api/v1/recipes`                     | Listar todas (resumo)            |
| `GET`    | `/api/v1/recipes/{id}`                | Buscar por ID (completo)         |
| `PUT`    | `/api/v1/recipes/{id}`               | Atualizar receita                |
| `DELETE` | `/api/v1/recipes/{id}`               | Deletar receita                  |
| `GET`    | `/api/v1/recipes/search?keyword=xxx`  | Pesquisar por palavra-chave      |
| `GET`    | `/api/v1/recipes/author/{nome}`       | Buscar por autor                 |
| `GET`    | `/api/v1/recipes/difficulty/{nivel}`  | Filtrar por dificuldade          |
| `GET`    | `/api/v1/recipes/tags?tags=a,b`       | Filtrar por tags                 |

---

## 📦 Estrutura do JSON

### Criar Receita — `POST /api/v1/recipes`

```json
{
  "name": "Urso de Crochê Amigurumi",
  "description": "Um urso fofo e charmoso, perfeito para presentear!",
  "authorName": "Maria Silva",
  "difficulty": "INICIANTE",
  "tags": ["urso", "amigurumi", "brinquedo"],
  "coverImageBase64": "data:image/jpeg;base64,/9j/4AAQ...",
  "coverImageContentType": "image/jpeg",
  "materials": [
    {
      "name": "Lã Acrílica",
      "quantity": "100g",
      "color": "Bege",
      "notes": "Fio número 4"
    },
    {
      "name": "Agulha de Crochê",
      "quantity": "1 unidade",
      "notes": "Tamanho 3.5mm"
    },
    {
      "name": "Olhos de Segurança",
      "quantity": "2 unidades",
      "notes": "9mm"
    }
  ],
  "parts": [
    {
      "order": 1,
      "title": "Parte 1 - Cabeça do Urso",
      "instructions": "Anel mágico com 6 pontos.\nVolta 1: Aumente 6x = 12 pts\nVolta 2: (1 pb, 1 aum) x 6 = 18 pts\nVolta 3: (2 pb, 1 aum) x 6 = 24 pts\n...",
      "imageBase64": "data:image/jpeg;base64,/9j/4AAQ...",
      "imageContentType": "image/jpeg"
    },
    {
      "order": 2,
      "title": "Parte 2 - Corpo do Urso",
      "instructions": "Anel mágico com 6 pontos.\nVolta 1: Aumente 6x = 12 pts\n...",
      "imageBase64": null,
      "imageContentType": null
    },
    {
      "order": 3,
      "title": "Parte 3 - Perna do Urso",
      "instructions": "Faça 2 unidades.\nAnel mágico com 6 pontos...",
      "imageBase64": "data:image/png;base64,iVBORw0...",
      "imageContentType": "image/png"
    }
  ]
}
```

### Resposta — `201 Created`

```json
{
  "success": true,
  "message": "Receita criada com sucesso!",
  "timestamp": "2024-03-15T10:30:00",
  "data": {
    "id": "65f1a2b3c4d5e6f7a8b9c0d1",
    "name": "Urso de Crochê Amigurumi",
    "authorName": "Maria Silva",
    "difficulty": "INICIANTE",
    "parts": [ ... ],
    "materials": [ ... ],
    "createdAt": "2024-03-15T10:30:00",
    "updatedAt": "2024-03-15T10:30:00"
  }
}
```

---

## 🗂 Estrutura do Projeto

```
crochet-recipes/
├── Dockerfile
├── docker-compose.yml          # Produção (API + Mongo + Mongo Express)
├── docker-compose.dev.yml      # Dev (só Mongo + Mongo Express)
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/crochet/recipes/
    │   │   ├── CrochetRecipesApplication.java
    │   │   ├── config/
    │   │   │   ├── MongoConfig.java
    │   │   │   └── OpenApiConfig.java
    │   │   ├── controller/
    │   │   │   └── RecipeController.java
    │   │   ├── dto/
    │   │   │   ├── ApiResponseDTO.java
    │   │   │   ├── MaterialDTO.java
    │   │   │   ├── RecipePartDTO.java
    │   │   │   ├── RecipeRequestDTO.java
    │   │   │   ├── RecipeResponseDTO.java
    │   │   │   └── RecipeSummaryDTO.java
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── RecipeNotFoundException.java
    │   │   ├── model/
    │   │   │   ├── Material.java
    │   │   │   ├── Recipe.java
    │   │   │   └── RecipePart.java
    │   │   ├── repository/
    │   │   │   └── RecipeRepository.java
    │   │   └── service/
    │   │       ├── RecipeMapper.java
    │   │       └── RecipeService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/crochet/recipes/
            └── RecipeServiceTest.java
```

---

## 🔧 Variáveis de Ambiente

| Variável       | Padrão                                      | Descrição              |
|----------------|---------------------------------------------|------------------------|
| `MONGODB_URI`  | `mongodb://localhost:27017/crochet_recipes` | URI do MongoDB         |
| `SERVER_PORT`  | `8080`                                      | Porta da aplicação     |
| `JAVA_OPTS`    | `-Xms256m -Xmx512m`                         | Opções da JVM          |

---

## 🧪 Executar Testes

```bash
mvn test
```

---

## 📝 Níveis de Dificuldade

| Valor          | Descrição              |
|----------------|------------------------|
| `INICIANTE`    | Pontos básicos         |
| `INTERMEDIARIO`| Técnicas intermediárias|
| `AVANCADO`     | Técnicas avançadas     |

---

## 💡 Dicas sobre Imagens

As imagens são armazenadas em **Base64** diretamente no MongoDB, o que simplifica o deploy.

Para converter uma imagem para Base64:

```bash
# Linux/Mac
base64 -i imagem.jpg | tr -d '\n'

# Ou em JavaScript
const toBase64 = file => new Promise(resolve => {
  const reader = new FileReader();
  reader.onload = () => resolve(reader.result);
  reader.readAsDataURL(file);
});
```

> ⚠️ Para imagens muito grandes (>5MB), considere usar um serviço de storage como AWS S3 ou Cloudinary e armazenar apenas a URL.
