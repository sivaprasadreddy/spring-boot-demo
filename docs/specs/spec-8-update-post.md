# Usecase Specification: Update an Existing Post

- **Usecase:** Update an Existing Post
- **Primary Actor:** Registered User (Author of the post)
- **Prerequisites:** The user must be logged in and be the author of the post. The post with the given slug must exist.
- **Expected Outcomes:**
    - **Success:** The specified blog post is updated.
    - **Failure:** An error message is returned if the input is invalid, the post does not exist, or the user is not authorized to update the post.
- **Authentication:** Required (Bearer Token).

### cURL Request

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

### Sample Response

HTTP Status: 200
Header: Location: http://localhost:8080/api/posts/{slug}
