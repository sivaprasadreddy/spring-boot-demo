# Usecase Specification: User Registration

- **Usecase:** User Registration
- **Primary Actor:** Guest User
- **Prerequisites:** None
- **Expected Outcomes:**
    - **Success:** A new user account is created.
    - **Failure:** An error message is returned if the email is already in use or the input is invalid.
- **Authentication:** Not required.

### cURL Request

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

### Sample Response

```json
{
  "name": "Siva",
  "email": "siva@gmail.com",
  "role": "ROLE_USER"
}
```