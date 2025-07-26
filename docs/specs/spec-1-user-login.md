# Usecase Specification: User Login

- **Usecase:** User Login
- **Primary Actor:** Registered User
- **Prerequisites:** User must be registered with the application.
- **Expected Outcomes:**
    - **Success:** The user is successfully authenticated and a JWT token is returned.
    - **Failure:** An error message is returned indicating invalid credentials.
- **Authentication:** Not required.

### cURL Request

```bash
curl -X POST \
  http://localhost:8080/api/login \
  -H 'Content-Type: application/json' \
  -d '{
    "email": "admin@gmail.com",
    "password": "admin"
  }'
```

### Sample Response

```json
{
  "token": "<jwt_token>",
  "expiresAt": "2025-07-26T12:00:00Z",
  "name": "Admin",
  "email": "admin@gmail.com",
  "role": "ROLE_ADMIN"
}
```