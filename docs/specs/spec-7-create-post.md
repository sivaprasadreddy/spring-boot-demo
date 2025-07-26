# Usecase Specification: Create a New Post

- **Usecase:** Create a New Post
- **Primary Actor:** Registered User
- **Prerequisites:** The user must be logged in.
- **Expected Outcomes:**
    - **Success:** A new blog post is created.
    - **Failure:** An error message is returned if the input is invalid.
- **Authentication:** Required (Bearer Token).

### cURL Request

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

### Sample Response

HTTP Status: 201
Header: Location: http://localhost:8080/api/posts/{slug}
