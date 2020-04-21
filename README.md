# To run
```
./mvnw spring-boot:run
```

#To test:
To login:
```
curl -v -c ./cookies -d "username=john" -d "password=password" -d "department=IT" http://localhost:8080/v1/api/login 
```

Protected endpoint:
```
curl -v -b ./cookies http://localhost:8080/v1/api/hello
```

To logout:
```
curl -v -b ./cookies http://localhost:8080/v1/api/logout
```

swagger:
```
http://localhost:8080/swagger-ui.html#/
```
H2 console:
```
http://localhost:8080/h2-console/
```