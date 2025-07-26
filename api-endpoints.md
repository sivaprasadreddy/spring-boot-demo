# API Endpoints

This document provides a summary of all the API endpoints available in the blog application.

## Auth API

Base Path: `/api`

| Method | Endpoint | Request Body   | Response Body   | Description                              |
|--------|----------|----------------|-----------------|------------------------------------------|
| POST   | `/login` | `LoginRequest` | `LoginResponse` | Authenticate a user and get a JWT token. |

**cURL Request:**

```bash
curl -X POST \
  http://localhost:8080/api/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "admin@gmail.com",
    "password": "admin"
  }'
```

## Users API

Base Path: `/api/users`

| Method | Endpoint | Request Body          | Response Body          | Description          |
|--------|----------|-----------------------|------------------------|----------------------|
| POST   | `/`      | `RegistrationRequest` | `RegistrationResponse` | Register a new user. |

**cURL Request:**

```bash
curl -X POST \
  http://localhost:8080/api/users \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Siva",
    "email": "siva@gmail.com",
    "password": "siva"
  }'
```

## Posts API

Base Path: `/api/posts`

| Method | Endpoint           | Request Body           | Response Body          | Description                      |
|--------|--------------------|------------------------|------------------------|----------------------------------|
| GET    | `/`                |                        | `PagedResult<PostDto>` | Get a paginated list of posts.   |
| GET    | `/{slug}`          |                        | `PostDto`              | Get a post by its slug.          |
| GET    | `/{slug}/comments` |                        | `List<CommentDto>`     | Get all comments for a post.     |
| POST   | `/{slug}/comments` | `CreateCommentPayload` |                        | Create a new comment for a post. |
| POST   | `/`                | `PostPayload`          |                        | Create a new post.               |
| PUT    | `/{slug}`          | `PostPayload`          |                        | Update an existing post.         |

**cURL Requests:**

*   **Get all posts:**

    ```bash
    curl -X GET http://localhost:8080/api/posts
    ```

*   **Get a post by its slug:**

    ```bash
    curl -X GET http://localhost:8080/api/posts/spring-boot-3-1-is-out
    ```

*   **Get all comments for a post:**

    ```bash
    curl -X GET http://localhost:8080/api/posts/spring-boot-3-1-is-out/comments
    ```

*   **Create a new comment for a post:**

    ```bash
    curl -X POST \
      http://localhost:8080/api/posts/spring-boot-3-1-is-out/comments \
      -H 'Content-Type: application/json' \
      -d '{
        "name": "Siva",
        "email": "siva@gmail.com",
        "content": "This is a great post!"
      }'
    ```

*   **Create a new post:**

    ```bash
    curl -X POST \
      http://localhost:8080/api/posts \
      -H 'Content-Type: application/json' \
      -H 'Authorization: Bearer <jwt_token>' \
      -d '{
        "title": "My New Post",
        "slug": "my-new-post",
        "content": "This is my new post content."
      }'
    ```

*   **Update an existing post:**

    ```bash
    curl -X PUT \
      http://localhost:8080/api/posts/my-new-post \
      -H 'Content-Type: application/json' \
      -H 'Authorization: Bearer <jwt_token>' \
      -d '{
        "title": "My Updated Post",
        "slug": "my-updated-post",
        "content": "This is my updated post content."
      }'
    ```

## DTOs

### LoginRequest
```json
{
  "email": "user@example.com",
  "password": "password"
}
```

### LoginResponse
```json
{
  "token": "jwt-token",
  "expiresAt": "2025-07-26T12:00:00Z",
  "name": "User",
  "email": "user@example.com",
  "role": "ROLE_USER"
}
```

### RegistrationRequest
```json
{
  "name": "New User",
  "email": "new.user@example.com",
  "password": "password"
}
```

### RegistrationResponse
```json
{
  "name": "New User",
  "email": "new.user@example.com",
  "role": "ROLE_USER"
}
```

### PagedResult<T>
```json
{
  "data": [],
  "currentPageNo": 1,
  "totalPages": 3,
  "totalElements": 36,
  "hasNextPage": true,
  "hasPreviousPage": false
}
```

### PostDto
```json
{
  "id": 1,
  "slug": "hello-world",
  "title": "Hello World",
  "content": "This is my first post.",
  "createdAt": "2025-07-26T10:00:00Z",
  "updatedAt": "2025-07-26T10:00:00Z"
}
```

### CommentDto
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "content": "This is a great post!",
  "createdAt": "2025-07-26T11:00:00Z"
}
```

### CreateCommentPayload
```json
{
  "name": "Jane Doe",
  "email": "jane.doe@example.com",
  "content": "I agree!"
}
```

### PostPayload
```json
{
  "title": "Updated Post Title",
  "slug": "updated-post-slug",
  "content": "This is the updated content of the post."
}
```