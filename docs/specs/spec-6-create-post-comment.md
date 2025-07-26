# Usecase Specification: Create a Comment on a Post

- **Usecase:** Create a Comment on a Post
- **Primary Actor:** Any User (Guest or Registered)
- **Prerequisites:** The post with the given slug must exist.
- **Expected Outcomes:**
    - **Success:** A new comment is added to the specified post.
    - **Failure:** An error message is returned if the input is invalid or the post does not exist.
- **Authentication:** Not required.

### cURL Request

```bash
curl -X POST \
  http://localhost:8080/api/posts/hello-world/comments \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Jane Doe",
    "email": "jane.doe@example.com",
    "content": "I agree!"
  }'
```

### Sample Response

HTTP Status: 201