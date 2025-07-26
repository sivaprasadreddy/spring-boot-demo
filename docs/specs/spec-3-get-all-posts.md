# Usecase Specification: Get All Posts

- **Usecase:** Get All Posts
- **Primary Actor:** Any User (Guest or Registered)
- **Prerequisites:** None
- **Expected Outcomes:**
    - **Success:** A paginated list of all blog posts is returned.
- **Authentication:** Not required.

### cURL Request

```bash
curl -X GET http://localhost:8080/api/posts
```

### Sample Response

```json
{
  "data": [
    {
      "id": 1,
      "slug": "hello-world",
      "title": "Hello World",
      "content": "This is my first post.",
      "createdAt": "2025-07-26T10:00:00Z",
      "updatedAt": "2025-07-26T10:00:00Z"
    }
  ],
  "currentPageNo": 1,
  "totalPages": 1,
  "totalElements": 1,
  "hasNextPage": false,
  "hasPreviousPage": false
}
```