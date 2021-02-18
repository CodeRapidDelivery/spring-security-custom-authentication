# To run
```
./mvnw spring-boot:run
```

#To test:
To get a jwt token:
```
curl -v -d "username=john" -d "password=password" -d "department=IT" http://localhost:8080/v1/api/login 
```

Call protected endpoint:
```
curl -v -H "Authorization: Bearer ${token}" http://localhost:8080/v1/api/hello
```
Example:
```
 curl -v -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiYXV0aCI6W10sImlhdCI6MTYxMzY1NzM5MCwiZXhwIjoxNjEzNjYwOTkwfQ.0yjSUqLb2Tm3lQakIovXiF0UOOJa07YIJQSXHYwobgM" http://localhost:8080/v1/api/hello
```

swagger:
```
http://localhost:8080/swagger-ui.html
```
H2 console:
```
http://localhost:8080/h2-console/
```