# Usecase Specification: Get a Single Post by Slug

- **Usecase:** Get a Single Post by Slug
- **Primary Actor:** Any User (Guest or Registered)
- **Prerequisites:** The post with the given slug must exist.
- **Expected Outcomes:**
    - **Success:** The details of the specified post are returned.
    - **Failure:** A "Not Found" error is returned if the post does not exist.
- **Authentication:** Not required.

### cURL Request

```bash
curl -X GET http://localhost:8080/api/posts/hello-world
```

### Sample Response

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