# BeatManager

API REST de gerenciamento de eventos construída com Spring Boot 4.1.0, Java 17 e PostgreSQL.

## Pré-requisitos

- Java 17+
- Maven 3.9+
- PostgreSQL 14+
- Conta no [Cloudinary](https://cloudinary.com) (para upload de imagens)

## Configuração

### 1. Banco de dados

Crie o banco de dados no PostgreSQL:

```sql
CREATE DATABASE beatmanager;
```

As tabelas são criadas automaticamente pelo Flyway na primeira execução.

### 2. Variáveis de ambiente

Copie o arquivo de exemplo e configure:

```bash
cp .env.example .env
```

Edite o `.env` com suas credenciais:

```env
# Banco de dados
DB_URL=jdbc:postgresql://localhost:5432/beatmanager
DB_USERNAME=postgres
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua_chave_secreta_base64
JWT_EXPIRATION=86400000

# Cloudinary (https://cloudinary.com/console)
CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=sua_api_secret
```

## Executar

```bash
# Modo desenvolvimento (padrão)
./mvnw spring-boot:run

# Modo produção
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
```

A API estará disponível em `http://localhost:8081`.

## Endpoints

### Autenticação

| Método | Autenticado | Descrição |
|--------|:-----------:|-----------|
| `POST /auth/cadastro` | Não | Cadastrar administrador |
| `POST /auth/login` | Não | Login e receber token JWT |
| `GET /auth/me` | Sim | Retornar dados do usuário logado |

### Administradores

| Método | Descrição |
|--------|-----------|
| `GET /administradores` | Listar todos |
| `GET /administradores/{id}` | Buscar por ID |
| `GET /administradores/email/{email}` | Buscar por e-mail |
| `POST /administradores` | Criar |
| `PUT /administradores/{id}` | Atualizar |
| `DELETE /administradores/{id}` | Deletar |

### Eventos

| Método | Descrição |
|--------|-----------|
| `GET /eventos` | Listar todos (paginado) |
| `GET /eventos/{id}` | Buscar por ID |
| `GET /eventos/administrador/{id}` | Listar por administrador (paginado) |
| `POST /eventos` | Criar (dono extraído do token) |
| `PUT /eventos/{id}` | Atualizar data/local (apenas dono) |
| `DELETE /eventos/{id}` | Deletar (apenas dono) |

### Upload de imagens

| Método | Descrição |
|--------|-----------|
| `POST /upload` | Upload de imagem (multipart/form-data, campo `file`) |
| `DELETE /upload?imageUrl=...` | Deletar imagem pelo URL |

**Upload:**
```bash
curl -X POST http://localhost:8081/upload \
  -H "Authorization: Bearer <token>" \
  -F "file=@imagem.jpg"
```

**Response:**
```json
{ "url": "https://res.cloudinary.com/.../image/upload/..." }
```

### Paginação

```bash
GET /eventos?page=0&size=10&sort=data,desc
```

- `page` — número da página (começa em 0)
- `size` — itens por página (padrão: 10)
- `sort` — ordenar por campo + direção

## Documentação Swagger

Acesse `http://localhost:8081/swagger-ui/index.html` para ver a documentação interativa da API.

No Swagger, clique em **Authorize** e insira o token JWT recebido no login para testar endpoints autenticados.

## Rodar testes

```bash
./mvnw test
```

## Stack

- Java 17
- Spring Boot 4.1.0
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Flyway
- Cloudinary (upload de imagens)
- SpringDoc OpenAPI (Swagger)
- Lombok
- JUnit 5 + Mockito
