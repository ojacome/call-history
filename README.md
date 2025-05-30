# Tenpo Call History API

API REST para guardar y consultar el historial de llamadas a endpoints, desarrollada con **Spring Boot 3**, **Java 21**, **PostgreSQL**, y **Docker**.

---

## 🧰 Requisitos mínimos

Asegúrate de tener lo siguiente instalado:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## 🚀 Cómo ejecutar el proyecto

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/ojacome/call-history.git
   cd call-history
   ```


2. **Levanta la app con Docker** 
   ```bash
   docker-compose up --build
    ```

3. **Documentacion** 

Swagger UI (si configurado): http://localhost:8080/swagger-ui.html




## ⚙️ Variables de entorno (ya configuradas en docker-compose.yml) 

| Variable                        | Valor                                      |
|---------------------------------|--------------------------------------------|
| SPRING\_DATASOURCE\_URL         | jdbc\:postgresql://postgres:5432/db\_tenpo |
| SPRING\_DATASOURCE\_USERNAME    | ojacome                                    |
| SPRING\_DATASOURCE\_PASSWORD    | 12345678                                   |
| CACHE_PERCENTAGE_EXPIRE_MINUTES | 30                                         |
| EXTERNAL_SERVICE_URL            | https://b228a110-1857-4572-9f1a-7eb59c1e2058.mock.pstmn.io/api/percentage                                           |

## 🧪 Pruebas
```bash
   ./gradlew test
```

- Cálculo con porcentaje dinámico

![alt text](image-1.png)

- Historial de llamadas

![alt text](image-2.png)