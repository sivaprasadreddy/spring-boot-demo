# Usecase Specification: Get Comments for a Post

- **Usecase:** Get Comments for a Post
- **Primary Actor:** Any User (Guest or Registered)
- **Prerequisites:** The post with the given slug must exist.
- **Expected Outcomes:**
    - **Success:** A list of all comments for the specified post is returned.
- **Authentication:** Not required.

### cURL Request

```bash
curl -X GET http://localhost:8080/api/posts/hello-world/comments
```

### Sample Response

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "content": "This is a great post!",
    "createdAt": "2025-07-26T11:00:00Z"
  }
]
```