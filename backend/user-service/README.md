# test-service

Private Spring Boot 3.5.7 microservice secured by JWT.

- Runs on port **8082**
- All endpoints (like `/test/hello`) are **protected**
- Expects a valid JWT (from `auth-service`) in `Authorization: Bearer <token>` header
- Shares the same `security.jwt.secret-key` as `auth-service`

## Run

```bash
mvn clean install
mvn spring-boot:run
```

Then call via gateway (once configured) or directly:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8082/test/hello
```
