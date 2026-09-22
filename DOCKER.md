# Postgres
```docker
docker run --name postgres-rental-store -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres:17
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
